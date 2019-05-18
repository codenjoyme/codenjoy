package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.*;

public class Battlecity implements Field {

    private Dice dice;
    private LinkedList<Pacman> aiTanks;
    private int aiCount;

    private int size;
    private List<Construction> constructions;
    private List<Border> borders;

    private List<Player> players = new LinkedList<Player>();
    private final List<Food> foods = new LinkedList<>();

    public Battlecity(int size, Dice dice, List<Construction> constructions, Pacman... aiTanks) {
        this(size, dice, constructions, new DefaultBorders(size).get(), aiTanks);
    }

    public Battlecity(int size, Dice dice, List<Construction> constructions,
                      List<Border> borders, Pacman... aiTanks) {
        aiCount = 6;
        this.dice = dice;
        this.size = size;
        this.aiTanks = new LinkedList<>();
        this.constructions = new LinkedList<>(constructions);
        this.borders = new LinkedList<>(borders);

        for (Pacman pacman : aiTanks) {
            addAI(pacman);
        }
        generateBullets();
    }

    @Override
    public void clearScore() {
        players.forEach(Player::reset);
        constructions.forEach(Construction::reset);
        getTanks().forEach(Pacman::reset);
    }

    @Override
    public void tick() {
        removeDeadTanks();

        newAI();

        List<Pacman> pacmen = getTanks();

        for (Pacman pacman : pacmen) {
            pacman.tick();
        }

        for (int i = 0; i < pacmen.size(); i++) {
            if (pacmen.get(i).isAlive()) {
                pacmen.get(i).move();
            }
        }

        for (int i = 0; i < pacmen.size(); i++) {
            if (pacmen.get(i).isAlive()) {
                for (int j = 0; j != i  && j < pacmen.size(); j++) {
                    if (pacmen.get(j).isAlive()) {
                        if (!(pacmen.get(i).isGhost() && pacmen.get(j).isGhost())) {
                            if (pacmen.get(i).getPreviousPosition().itsMe(pacmen.get(j).getPosition())) {
                                if (pacmen.get(i).getDirection() == pacmen.get(j).getDirection().inverted()) {
                                    pacmen.get(i).kill(null);
                                    pacmen.get(j).kill(null);
                                    break;
                                } else if (pacmen.get(i).getDirection() != pacmen.get(j).getDirection()){
                                    if (pacmen.get(i).getPosition().itsMe(pacmen.get(i).getPreviousPosition())) {
                                        pacmen.get(i).kill(null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Pacman pacman : pacmen) {
            if (pacman.isAlive()) {
                List<Food> foods = getFoods();
                int index = foods.indexOf(pacman);
                if (index != -1) {
                    Food food = foods.get(index);
                    affect(food);
                }
            }
        }

        for (Construction construction : constructions) {
            if (!pacmen.contains(construction) && !getFoods().contains(construction)) {
                construction.tick();
            }
        }
    }

    private boolean isSame(Direction d, Point p1, Point p2) {
        Point p = new PointImpl(p1);
        switch (d) {
            case UP: p.setY(p1.getY() + 1); break;
            case DOWN: p.setY(p1.getY() - 1); break;
            case RIGHT: p.setY(p1.getX() + 1); break;
            case LEFT: p.setY(p1.getX() - 1); break;
        }
        return p.itsMe(p2);
    }

    private void newAI() {
        for (int count = aiTanks.size(); count < aiCount; count++) {
            int y = size - 2;
            int x;
            int c = 0;
            do {
                x = dice.next(size);
            } while (isBarrier(x, y) && c++ < size);

            if (!isBarrier(x, y)) {
                addAI(new Ghost(x, y, dice, Direction.DOWN));
            }
        }
    }

    private void generateBullets() {
        generateBullet();
    }

    private void generateBullet() {
        int x, y;

        for (int i = 0; i < 25; i++) {
            do {
                x = dice.next(this.size());
                y = dice.next(this.size());
            } while (isBarrier(x, y));
            foods.add(new Food(this, null, new PointImpl(x, y), null, null));
        }
    }

    private void removeDeadTanks() {
        for (Pacman pacman : getTanks()) {
            if (!pacman.isAlive()) {
                aiTanks.remove(pacman);
            }
        }
        for (Player player : players.toArray(new Player[0])) {
            if (!player.getHero().isAlive()) {
                players.remove(player);
            }
        }
    }

    void addAI(Pacman pacman) {
        pacman.init(this);
        aiTanks.add(pacman);
    }

    @Override
    public void affect(Food food) {
        if (borders.contains(food)) {
            food.onDestroy();
            return;
        }

        if (getTanks().contains(food)) {
            int index = getTanks().indexOf(food);
            Pacman pacman = getTanks().get(index);
//            if (pacman == food.getOwner()) {
//                return;
//            }

            scoresForKill(food, pacman);

//            pacman.kill(food);
            food.onDestroy();  // TODO заимплементить взрыв
            foods.remove(food);
            if(foods.size() < 20)
                generateBullet();
            return;
        }

        for (Food food2 : getFoods().toArray(new Food[0])) {
            if (food != food2 && food.equals(food2)) {
                food.boom();
                food2.boom();
                return;
            }
        }

        if (constructions.contains(food)) {
            Construction construction = getConstructionAt(food);

            if (!construction.destroyed()) {
                construction.destroyFrom(food.getDirection());
                food.onDestroy();  // TODO заимплементить взрыв
            }

            return;
        }
    }

    private Construction getConstructionAt(Food food) {
        int index = constructions.indexOf(food);
        return constructions.get(index);
    }

    private void scoresForKill(Food killedFood, Pacman diedPacman) {
        Player died = null;
//        boolean aiDied = aiTanks.contains(diedPacman);
//        if (!aiDied) {
       try {
           died = getPlayer(diedPacman);
       } catch (Exception e){

       }

//        }

//        Pacman killerTank = killedFood.getOwner();
//        Player killer = null;
//        if (!aiTanks.contains(killerTank)) {
//            killer = getPlayer(killerTank);
//        }
//
//        if (killer != null) {
//            if (aiDied) {
//                killer.event(Events.KILL_OTHER_AI_TANK);
//            } else {
//                killer.killHero();
//                killer.event(Events.KILL_OTHER_HERO_TANK.apply(killer.score()));
//            }
//        }
        if (died != null) {
//            died.event(Events.KILL_YOUR_TANK);
            died.event(Events.KILL_OTHER_HERO_TANK.apply(1));
        }
    }

    private Player getPlayer(Pacman pacman) {
        for (Player player : players) {
            if (player.getHero().equals(pacman)) {
                return player;
            }
        }

        throw new RuntimeException("Танк игрока не найден!");
    }

    @Override
    public boolean isBarrier(int x, int y) {
        for (Construction construction : constructions) {
            if (construction.itsMe(x, y) && !construction.destroyed()) {
                return true;
            }
        }
        for (Point border : borders) {
            if (border.itsMe(x, y)) {
                return true;
            }
        }
//        for (Pacman tank : getTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
//            if (tank.itsMe(x, y)) {
//                return true;
//            }
//        }
        return outOfField(x, y);
    }

    @Override
    public boolean outOfField(int x, int y) { // TODO заменить все есть в point
        return x < 0 || y < 0 || y > size - 1 || x > size - 1;
    }

    private List<Food> getFoods() {
        //        for (Pacman tank : getTanks()) {
//            for (Food bullet : tank.getFoods()) {
//                result.add(bullet);
//            }
//        }
        return foods;
    }

    @Override
    public List<Pacman> getTanks() {
        LinkedList<Pacman> result = new LinkedList<>(aiTanks);
        for (Player player : players) {
//            if (player.getTank().isAlive()) { // TODO разремарить с тестом
            result.add(player.getHero());
//            }
        }
        return result;
    }

    @Override
    public void remove(Player player) {   // TODO test me
        players.remove(player);
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Battlecity.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Battlecity.this.getBorders());
                    addAll(Battlecity.this.getTanks());
                    addAll(Battlecity.this.getConstructions());
                    addAll(Battlecity.this.getFoods());
                }};
            }
        };
    }

    @Override
    public List<Construction> getConstructions() {
        List<Construction> result = new LinkedList<>();
        for (Construction construction : constructions) {
            if (!construction.destroyed()) {
                result.add(construction);
            }
        }
        return result;
    }

    @Override
    public List<Border> getBorders() {
        return borders;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

}
