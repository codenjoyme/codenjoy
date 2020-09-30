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


import com.codenjoy.dojo.battlecity.model.items.*;
import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Battlecity implements Field {

    private int size;

    private PrizeGenerator prizeGen;
    private AiGenerator aiGen;

    private List<Player> players = new LinkedList<>();

    private List<Wall> walls;
    private List<Border> borders;
    private List<Tree> trees;
    private List<Ice> ice;
    private List<River> rivers;
    private List<Prize> prizes;
    private List<Tank> ais;

    public Battlecity(int size, Dice dice,
                      Parameter<Integer> whichSpawnWithPrize,
                      Parameter<Integer> damagesBeforeAiDeath)
    {
        this.size = size;
        ais = new LinkedList<>();
        prizes = new LinkedList<>();
        walls = new LinkedList<>();
        borders = new LinkedList<>();
        trees = new LinkedList<>();
        ice = new LinkedList<>();
        rivers = new LinkedList<>();

        prizeGen = new PrizeGenerator(this, dice);

        aiGen = new AiGenerator(this, dice, whichSpawnWithPrize, damagesBeforeAiDeath);
    }

    public void addAiTanks(List<? extends Point> tanks) {
        aiGen.dropAll(tanks);
    }

    @Override
    public void clearScore() {
        players.forEach(Player::reset);
        walls.forEach(Wall::reset);
        allTanks().forEach(Tank::reset);
    }

    @Override
    public void tick() {
        removeDeadTanks();

        aiGen.dropAll();

        List<Tank> tanks = allTanks();

        for (Tank tank : tanks) {
            tank.tick();
        }

        for (Bullet bullet : bullets()) {
            if (bullet.destroyed()) {
                bullet.onDestroy();
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.tryFire();
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.move();

                List<Bullet> bullets = bullets();
                int index = bullets.indexOf(tank);
                if (index != -1) {
                    Bullet bullet = bullets.get(index);
                    affect(bullet);
                }
            }
        }

        for (Bullet bullet : bullets()) {
            bullet.move();
        }

        for (Wall wall : walls) {
            if (!tanks.contains(wall) && !bullets().contains(wall)) {
                wall.tick();
            }
        }
    }

    private void removeDeadTanks() {
        for (Tank tank : allTanks()) {
            if (!tank.isAlive()) {
                ais.remove(tank);
                if (tank.isTankPrize()) {
                    prizeGen.drop();
                }
            }
        }

        for (Player player : players.toArray(new Player[0])) {
            if (!player.getHero().isAlive()) {
                players.remove(player);
            }
        }
    }

    @Override
    public void affect(Bullet bullet) {
        if (borders.contains(bullet)) {
            bullet.onDestroy();
            return;
        }

        if (allTanks().contains(bullet)) {
            int index = allTanks().indexOf(bullet);
            Tank tank = allTanks().get(index);
            if (tank == bullet.getOwner()) {
                return;
            }

            scoresForKill(bullet, tank);

            tank.kill(bullet);
            bullet.onDestroy();  // TODO заимплементить взрыв
            return;
        }

        for (Bullet bullet2 : bullets().toArray(new Bullet[0])) {
            if (bullet != bullet2 && bullet.equals(bullet2)) {
                bullet.boom();
                bullet2.boom();
                return;
            }
        }

        if (walls.contains(bullet)) {
            Wall wall = getWallAt(bullet);

            if (!wall.destroyed()) {
                wall.destroyFrom(bullet.getDirection());
                bullet.onDestroy();  // TODO заимплементить взрыв
            }

            return;
        }
    }

    @Override
    public boolean isRiver(Point pt) {
        return rivers.stream().anyMatch(river -> river.itsMe(pt));
    }

    @Override
    public boolean isIce(Point pt) {
        return ice.stream().anyMatch(ice -> ice.itsMe(pt));
    }

    @Override
    public void addPrize(Prize prize) {
        prizes.add(prize);
    }

    @Override
    public void addAi(Tank tank) {
        ais.add(tank);
    }

    private Wall getWallAt(Bullet bullet) {
        int index = walls.indexOf(bullet);
        return walls.get(index);
    }

    private void scoresForKill(Bullet killedBullet, Tank diedTank) {
        Player died = null;
        boolean aiDied = ais.contains(diedTank);
        if (!aiDied) {
             died = getPlayer(diedTank);
        }

        Tank killerTank = killedBullet.getOwner();
        Player killer = null;
        if (!ais.contains(killerTank)) {
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
        for (Wall wall : this.walls) {
            if (wall.itsMe(pt) && !wall.destroyed()) {
                return true;
            }
        }

        if (isRiver(pt)) {
            return true;
        }

        for (Point border : borders) {
            if (border.itsMe(pt)) {
                return true;
            }
        }

        for (Tank tank : allTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.itsMe(pt)) {
                return true;
            }
        }

        return pt.isOutOf(size);
    }

    private List<Bullet> bullets() {
        return allTanks().stream()
                .flatMap(tank -> tank.getBullets().stream())
                .collect(toList());
    }

    @Override
    public List<Tank> aiTanks() {
        return ais;
    }

    public List<Tank> allTanks() {
        List<Tank> result = new LinkedList<>(ais);
        for (Player player : players) {
//            if (player.getTank().isAlive()) { // TODO разремарить с тестом
                result.add(player.getHero());
//            }
        }
        return result;
    }

    public List<Tank> tanks() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public List<Prize> getPrizes() {
        return prizes;
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
                    addAll(Battlecity.this.allTanks());
                    addAll(Battlecity.this.getWalls());
                    addAll(Battlecity.this.bullets());
                    addAll(Battlecity.this.getPrizes());
                    addAll(Battlecity.this.getTrees());
                    addAll(Battlecity.this.getIce());
                    addAll(Battlecity.this.getRivers());
                }};
            }
        };
    }

    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();
        for (Wall wall : walls) {
            if (!wall.destroyed()) {
                result.add(wall);
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

    public List<Border> getBorders() {
        return borders;
    }


    public void addBorder(List<Border> borders) {
        this.borders.addAll(borders);
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public void addBorder(Border border) {
        borders.add(border);
    }

    public void addIce(Ice ice) {
        this.ice.add(ice);
    }

    public void addRiver(River river) {
        rivers.add(river);
    }

    public AiGenerator getAiGenerator() {
        return aiGen;
    }

    public void addWall(List<Wall> walls) {
        this.walls.addAll(walls);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public void addRiver(List<River> rivers) {
        this.rivers.addAll(rivers);
    }

    public void addTree(List<Tree> trees) {
        this.trees.addAll(trees);
    }

    public void addIce(List<Ice> ice) {
        this.ice.addAll(ice);
    }
}
