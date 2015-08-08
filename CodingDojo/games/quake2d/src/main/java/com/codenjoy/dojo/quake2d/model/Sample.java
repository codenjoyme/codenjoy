package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Sample implements Tickable, Field {
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


    public Sample(Level level, Dice dice) {
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
                int heroIndex = getHeroes().indexOf(PointImpl.pt(elemBullet.getDirection().changeX(elemBullet.getX()),
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
        Point pt = PointImpl.pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return PointImpl.pt(0, 0);
        }

        return PointImpl.pt(rndX, rndY);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = PointImpl.pt(x, y);

        return  !abilities.contains(pt) &&
                !bullets.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public boolean isBulletHitHero(int x, int y) {
        return getHeroes().contains(PointImpl.pt(x, y));
    }

//    @Override
//    public void setBomb(int x, int y) {
//        Point pt = PointImpl.pt(x, y);
//        if (!bullets.contains(pt)) {
//            bullets.add(new Bullet(x, y));
//        }
//    }

//    @Override
//    public void removeBomb(int x, int y) {
//        bullets.remove(PointImpl.pt(x, y));
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
            private int size = Sample.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Sample.this.getWalls());
                result.addAll(Sample.this.getHeroes());
//                result.addAll(Sample.this.robots);
                result.addAll(Sample.this.getAbilities());
                result.addAll(Sample.this.getBullets());
                return result;
            }
        };
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public boolean catchAbility(int x, int y) {
        return getAbilities().contains(PointImpl.pt(x, y));
    }
}
