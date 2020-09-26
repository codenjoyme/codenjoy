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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.PointImpl.random;

public class Battlecity implements Field {

    private Dice dice;
    private LinkedList<Tank> aiTanks;
    private LinkedList<Prize> prize;
    private int aiCount;
    private int spawnAiPrize;
    private int hitKillsAiPrize;
    private int aiTanksCount = 0;

    private int size;
    private List<Construction> constructions;
    private List<Border> borders;
    private List<Tree> trees;
    private List<Ice> ice;
    private List<River> rivers;

    private List<Player> players = new LinkedList<>();
    private final List<Elements> prizes = Arrays.asList(Elements.PRIZE_IMMORTALITY, Elements.PRIZE_BREAKING_WALLS, Elements.PRIZE_WALKING_ON_WATER);

    public Battlecity(int size, Dice dice, List<Construction> constructions,
                      Parameter<Integer> spawnAiPrize, Parameter<Integer> hitKillsAiPrize,
                      Tank... aiTanks) {
        this(size, dice, constructions, new DefaultBorders(size).get(), spawnAiPrize,
                hitKillsAiPrize, aiTanks);
    }

    public Battlecity(int size, Dice dice, List<Construction> constructions,
                      List<Border> borders, Parameter<Integer> spawnAiPrize,
                      Parameter<Integer> hitKillsAiPrize, Tank... aiTanks) {
        aiCount = aiTanks.length;
        this.dice = dice;
        this.size = size;
        this.aiTanks = new LinkedList<>();
        this.prize = new LinkedList<>();
        this.constructions = new LinkedList<>(constructions);
        this.borders = new LinkedList<>(borders);
        this.spawnAiPrize = spawnAiPrize.getValue();
        this.hitKillsAiPrize = hitKillsAiPrize.getValue();
        this.trees = new LinkedList<>();
        this.ice = new LinkedList<>();
        this.rivers = new LinkedList<>();

        for (Tank tank : aiTanks) {
            addAI(tank);
        }
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    public void setIce(List<Ice> ice) {
        this.ice = ice;
    }

    public void setRivers(List<River> rivers) {
        this.rivers = rivers;
    }

    @Override
    public void clearScore() {
        players.forEach(Player::reset);
        constructions.forEach(Construction::reset);
        getTanks().forEach(Tank::reset);
    }

    @Override
    public void tick() {
        removeDeadTanks();

        newAI();

        List<Tank> tanks = getTanks();

        for (Tank tank : tanks) {
            tank.tick();
        }

        for (Bullet bullet : getBullets()) {
            if (bullet.destroyed()) {
                bullet.onDestroy();
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.fire();
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.move();

                List<Bullet> bullets = getBullets();
                int index = bullets.indexOf(tank);
                if (index != -1) {
                    Bullet bullet = bullets.get(index);
                    affect(bullet);
                }
            }
        }

        for (Bullet bullet : getBullets()) {
            bullet.move();
        }

        for (Construction construction : constructions) {
            if (!tanks.contains(construction) && !getBullets().contains(construction)) {
                construction.tick();
            }
        }
    }

    private void newAI() {
        for (int count = aiTanks.size(); count < aiCount; count++) {
            Point pt = pt(0, size - 2);
            int c = 0;
            do {
                pt.setX(dice.next(size));
            } while (isBarrier(pt) && c++ < size);

            if (!isBarrier(pt)) {
                addAI(new AITank(pt, dice, Direction.DOWN));
            }
        }
    }

    private void removeDeadTanks() {
        for (Tank tank : getTanks()) {
            if (!tank.isAlive()) {
                aiTanks.remove(tank);
                if (tank.isTankPrize()) {
                    dropPrize();
                }
            }
        }

        for (Player player : players.toArray(new Player[0])) {
            if (!player.getHero().isAlive()) {
                players.remove(player);
            }
        }
    }

    private void dropPrize() {
        Point pt;
        int c = 0;
        do {
           pt = random(dice, size);
        } while (isBarrier(pt) && c++ < size);

        if (!isBarrier(pt)) {
            prize.add(new Prize(pt, prizes.get(dice.next(prizes.size()))));
        }
    }

    void addAI(Tank tank) {
        Tank resultTank = replaceAiOnAiPrize(tank);
        resultTank.init(this);
        aiTanks.add(resultTank);
        this.aiTanksCount++;
    }

    @Override
    public void affect(Bullet bullet) {
        if (borders.contains(bullet)) {
            bullet.onDestroy();
            return;
        }

        if (getTanks().contains(bullet)) {
            int index = getTanks().indexOf(bullet);
            Tank tank = getTanks().get(index);
            if (tank == bullet.getOwner()) {
                return;
            }

            scoresForKill(bullet, tank);

            tank.kill(bullet);
            bullet.onDestroy();  // TODO заимплементить взрыв
            return;
        }

        for (Bullet bullet2 : getBullets().toArray(new Bullet[0])) {
            if (bullet != bullet2 && bullet.equals(bullet2)) {
                bullet.boom();
                bullet2.boom();
                return;
            }
        }

        if (constructions.contains(bullet)) {
            Construction construction = getConstructionAt(bullet);

            if (!construction.destroyed()) {
                construction.destroyFrom(bullet.getDirection());
                bullet.onDestroy();  // TODO заимплементить взрыв
            }

            return;
        }
    }

    @Override
    public boolean isRiver(Point pt) {
        return rivers.stream().anyMatch(river -> river.itsMe(pt));
    }

    private Construction getConstructionAt(Bullet bullet) {
        int index = constructions.indexOf(bullet);
        return constructions.get(index);
    }

    private void scoresForKill(Bullet killedBullet, Tank diedTank) {
        Player died = null;
        boolean aiDied = aiTanks.contains(diedTank);
        if (!aiDied) {
             died = getPlayer(diedTank);
        }

        Tank killerTank = killedBullet.getOwner();
        Player killer = null;
        if (!aiTanks.contains(killerTank)) {
            killer = getPlayer(killerTank);
        }

        if (killer != null) {
            if (aiDied) {
                killer.event(Events.KILL_OTHER_AI_TANK);
            } else {
                killer.killHero();
                killer.event(Events.KILL_OTHER_HERO_TANK.apply(killer.score()));
            }
        }
        if (died != null) {
            died.event(Events.KILL_YOUR_TANK);
        }
    }

    private Player getPlayer(Tank tank) {
        for (Player player : players) {
            if (player.getHero().equals(tank)) {
                return player;
            }
        }

        throw new RuntimeException("Танк игрока не найден!");
    }

    @Override
    public boolean isBarrier(Point pt) {
        for (Construction construction : constructions) {
            if (construction.itsMe(pt) && !construction.destroyed()) {
                return true;
            }
        }
        for (Point border : borders) {
            if (border.itsMe(pt)) {
                return true;
            }
        }
        for (Tank tank : getTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.itsMe(pt)) {
                return true;
            }
        }
        return pt.isOutOf(size);
    }

    private List<Bullet> getBullets() {
        List<Bullet> result = new LinkedList<>();
        for (Tank tank : getTanks()) {
            for (Bullet bullet : tank.getBullets()) {
                result.add(bullet);
            }
        }
        return result;
    }

    @Override
    public List<Tank> getTanks() {
        LinkedList<Tank> result = new LinkedList<>(aiTanks);
        for (Player player : players) {
//            if (player.getTank().isAlive()) { // TODO разремарить с тестом
                result.add(player.getHero());
//            }
        }
        return result;
    }

    public List<Prize> getPrize() {
        return prize;
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
                    addAll(Battlecity.this.getBullets());
                    addAll(Battlecity.this.getPrize());
                    addAll(Battlecity.this.getTrees());
                    addAll(Battlecity.this.getIce());
                    addAll(Battlecity.this.getRivers());
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

    public List<Tree> getTrees() {
        return trees;
    }

    public List<Ice> getIce() {
        return ice;
    }

	public List<River> getRivers() {
		return rivers;
	}

    @Override
    public List<Border> getBorders() {
        return borders;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    private Tank replaceAiOnAiPrize(Tank tank) {
        if (aiTanksCount == spawnAiPrize) {
            this.aiTanksCount = 0;
        }

        if (spawnAiPrize > 1) {
            int indexAiPrize = spawnAiPrize - 2;
            if (this.aiTanksCount == indexAiPrize) {
                Point pt = pt(tank.getX(), tank.getY());
                return new AITankPrize(pt, dice, tank.getDirection(), hitKillsAiPrize);
            }
        }
        return tank;
    }
}
