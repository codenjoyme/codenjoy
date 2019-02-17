package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.quake2d.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Quake2D implements Field {
    public static final int IS_DEAD = 1;
    public static final int IS_ALIVE = 0;
    public static final int ABILITY_TIME_EXIST = 5;
    public static int counterOfAbility;

    private List<Wall> walls;
    private List<Ability> abilities;
    private List<Bullet> bullets;

    private List<Player> players;

    private final int size;
    private Dice dice;
    private List<Robot> robots;
    private List<Robot> robotsNew;


    public Quake2D(Level level, Dice dice) {
        counterOfAbility = ABILITY_TIME_EXIST;
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        abilities = new LinkedList<Ability>();
        players = new LinkedList<Player>();
        bullets = new LinkedList<Bullet>();
        robotsNew = new LinkedList<Robot>();
        robots = level.getRobots(this);
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
//        robots.addAll(robotsNew);
//        robotsNew.clear();

        takeAbility();
        checkBulletDirection();
        checkLifeCycleHero();
//        for (Robot robot : robots) {
//            robot.tick();
//        }

        if (counterOfAbility != 0){
            counterOfAbility--;
        }
        if (abilities.isEmpty() && counterOfAbility == 0){
            Point pos = getFreeRandom();
            abilities.add(new Ability(pos.getX(), pos.getY(), dice));
        }
    }

    private void takeAbility() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();

            if (abilities.contains(hero)) {
                int indexAbility = abilities.indexOf(hero);
                hero.setAbility(abilities.get(indexAbility));
                abilities.remove(hero);
                counterOfAbility = ABILITY_TIME_EXIST;
//                player.event(Events.WIN);
//                Point pos = getFreeRandom();
//                gold.add(new Ability(pos.getX(), pos.getY()));
            }
        }
    }

    private void checkLifeCycleHero() {
        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive() && hero.getDeathTimeCounter() == IS_ALIVE) {
                player.event(Events.KILL);
                hero.setDeathTimeCounter(IS_DEAD);
            } else if (!hero.isAlive() && hero.getDeathTimeCounter() == IS_DEAD){
                hero.setDeathTimeCounter(IS_ALIVE);
                hero.setAlive(true);
                player.newHero(this);
            } else {
                //do nothing
            }
        }
    }

    private void checkBulletDirection() {
        for (Bullet elemBullet : bullets.toArray(new Bullet[bullets.size()])){

            if (isBulletHitHero(elemBullet.getDirection().changeX(elemBullet.getX()), elemBullet.getDirection().inverted().changeY(elemBullet.getY()))){
                int heroIndex = getHeroes().indexOf(pt(elemBullet.getDirection().changeX(elemBullet.getX()),
                        elemBullet.getDirection().inverted().changeY(elemBullet.getY())));
                Hero tmpHero = getHeroes().get(heroIndex);
                tmpHero.setDamage(elemBullet.getDamage());

                if (tmpHero.getHealth() == 0) {
                    tmpHero.setAlive(false);
                }
                bullets.remove(elemBullet);
            } else if (!elemBullet.getField().isBarrier(elemBullet.getDirection().changeX(elemBullet.getX()),
                    elemBullet.getDirection().inverted().changeY(elemBullet.getY()))){
                elemBullet.tick();
            } else {
                bullets.remove(elemBullet);
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        return BoardUtils.getFreeRandom(size, dice, pt -> isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        return  !abilities.contains(pt) &&
                !bullets.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public boolean isBulletHitHero(int x, int y) {
        return getHeroes().contains(pt(x, y));
    }

//    @Override
//    public void setBomb(int x, int y) {
//        Point pt = pt(x, y);
//        if (!bullets.contains(pt)) {
//            bullets.add(new Bullet(x, y));
//        }
//    }

//    @Override
//    public void removeBomb(int x, int y) {
//        bullets.remove(pt(x, y));
//    }

    @Override
    public void fireBullet(int x, int y, Direction direction, Field field, Hero hero) {
        if (direction == null) return;

//        int newX = direction.changeX(x);
//        int newY = direction.inverted().changeY(y);
//        bullets.add(new Bullet(newX, newY, direction, field, hero));
        bullets.add(new Bullet(x, y, direction, field, hero));
    }

    @Override
    public void newRobot(int x, int y) {
        robotsNew.add(new Robot(this, x, y));
    }

//    public List<Ability> getGold() {
//        return gold;
//    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<Hero>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Quake2D.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    addAll(Quake2D.this.getWalls());
                    addAll(Quake2D.this.getHeroes());
                    addAll(Quake2D.this.getAbilities());
                    addAll(Quake2D.this.getBullets());
                }};
            }
        };
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public boolean catchAbility(int x, int y) {
        return getAbilities().contains(pt(x, y));
    }
}
