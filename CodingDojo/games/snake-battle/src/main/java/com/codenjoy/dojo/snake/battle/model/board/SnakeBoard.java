package com.codenjoy.dojo.snake.battle.model.board;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.battle.model.Player;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.Level;
import com.codenjoy.dojo.snake.battle.model.objects.*;
import com.codenjoy.dojo.snake.battle.services.Events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {SnakeBoard#tick()}
 */
public class SnakeBoard implements Tickable, Field {

    public boolean debugMode = false;
    private static final int pause = 5;
    private List<Wall> walls;
    private List<StartFloor> starts;
    private List<Apple> apples;
    private List<Stone> stones;
    private List<FlyingPill> flyingPills;
    private List<FuryPill> furyPills;
    private List<Gold> gold;

    private List<Player> players;
    private int startCounter;

    private final int size;
    private Dice dice;

    public SnakeBoard(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        starts = level.getStartPoints();
        apples = level.getApples();
        stones = level.getStones();
        flyingPills = level.getFlyingPills();
        furyPills = level.getFuryPills();
        gold = level.getGold();
        size = level.getSize();
        players = new LinkedList<>();
        startCounter = pause;
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        // отсчёт "секунд" до старта
        if (startCounter >= 0) {
            setStartCounter(startCounter - 1);
        }
        int aliveBefore = countAliveHeroes(); // количество живых с прошлого хода

        // победа последнего игрока и рестарт игры
        if (players.size() > 1 && aliveBefore < 2 && startCounter < 0){
            fireWinEventAndRestartGame();
            return;
        }
        // Для тестового режима, если только один игрок, можно ползать пока не умираешь.
        if (aliveBefore < 1 && startCounter < 0) {
            setStartCounter(pause);
            return;
        }

        snakesMove(); // продвижение живых змеек
        snakesCollisionDetection(); // реакция на столкновения змей друг с другом
        int aliveAfter = countAliveHeroes(); // сколько осталось живо после хода
        fireDieEvents();
        fireAliveEvents(aliveBefore - aliveAfter); // отправляем живым сообщения, когда кто-то умер
        setNewObjects();
    }

    private void setNewObjects() {
        int playersVar = (players.size() / 2) + 1;
        int randVal = dice.next(50);
        if (randVal == 42 && furyPills.size() < playersVar)
            setFuryPill(getFreeRandom());
        if (randVal == 32 && flyingPills.size() < playersVar)
            setFlyingPill(getFreeRandom());
        if (randVal == 11 || stones.size() < 1)
            setStone(getFreeRandom());
        if (randVal < 5 || apples.size() < playersVar)
            setApple(getFreeRandom());
    }

    private void fireDieEvents() {
        for (Player player : players) {
            if (player.isActive() && !player.isAlive()) {
                player.event(Events.DIE);
            }
        }
    }

    private int countAliveHeroes() {
        int counter = 0;
        for (Player player : players) {
            if (!player.isAlive())
                continue;
            counter++;
        }
        return counter;
    }

    private void snakesMove() {
        for (Player player : players) {
            if (startCounter == 0)
                player.event(Events.START);
            if (!player.isActive())
                continue;
            Hero hero = player.getHero();
            Point head = hero.getNextPoint();
            hero.tick();

            if (apples.contains(head)) {
                apples.remove(head);
                player.event(Events.APPLE);
            }
            if (stones.contains(head) && !hero.isFlying()) {
                stones.remove(head);
                player.event(Events.STONE);
            }
            if (gold.contains(head)) {
                gold.remove(head);
                player.event(Events.GOLD);
            }
            if (flyingPills.contains(head)) {
                flyingPills.remove(head);
            }
            if (furyPills.contains(head)) {
                furyPills.remove(head);
            }
        }
    }

    private void snakesCollisionDetection() {
        for (Player player : players) {
            if (!player.isActive())
                continue;
            Hero hero = player.getHero();
            if (hero.isFlying())
                continue;
            Hero enemy = checkHeadByHeadCollision(hero);
            if (enemy != null) {
                if (enemy.isFlying())
                    continue;
                if (hero.isFury() && !enemy.isFury()) {
                    enemy.die();
                } else if (!hero.isFury() && enemy.isFury()) {
                    hero.die();
                } else {
                    int hSize = hero.size();
                    hero.reduce(enemy.size());
                    enemy.reduce(hSize);
                }
            } else if (isAnotherHero(hero)) {
                if (hero.isFury()) {
                    Hero reducedEnemy = getAnotherHero(hero);
                    reducedEnemy.reduceFromPoint(hero.getHead());
                } else
                    hero.die();
            }
        }
    }

    private void fireAliveEvents(int died) {
        for (Player player : players)
            if (player.isAlive())
                for (int i = 0; i < died; i++)
                    player.event(Events.ALIVE);
    }

    private void fireWinEventAndRestartGame() {
        for (Player player : players)
            if (player.isAlive()) {
                player.event(Events.WIN);
                newGame(player);
            }
        setStartCounter(pause);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(Point p) {
        return p.isOutOf(size) || walls.contains(p) || starts.contains(p);
    }

    @Override
    public Point getFreeRandom() {
        int rndX;
        int rndY;
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
    public Point getFreeStart() {
        for (StartFloor start : starts)
            if (freeOfHero(start))
                return start;
        return PointImpl.pt(0, 0);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        return isFree(pt);
    }

    public boolean isFree(Point pt) {
        return isFreeOfObjects(pt) && freeOfHero(pt);
    }

    public boolean isFreeOfObjects(Point pt) {
        return !(apples.contains(pt) ||
                stones.contains(pt) ||
                walls.contains(pt) ||
                starts.contains(pt) ||
                flyingPills.contains(pt) ||
                furyPills.contains(pt) ||
                gold.contains(pt));
    }

    private boolean freeOfHero(Point pt) {
        for (Hero h : getHeroes()) {
            if (h != null && h.getBody().contains(pt) &&
                    !pt.equals(h.getTailPoint()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isApple(Point p) {
        return apples.contains(p);
    }

    @Override
    public boolean isStone(Point p) {
        return stones.contains(p);
    }

    @Override
    public boolean isFlyingPill(Point p) {
        return flyingPills.contains(p);
    }

    @Override
    public boolean isFuryPill(Point p) {
        return furyPills.contains(p);
    }

    @Override
    public boolean isGold(Point p) {
        return gold.contains(p);
    }

    @Override
    public boolean isAnotherHero(Hero h) {
        return getAnotherHero(h) != null;
    }

    @Override
    public Hero getAnotherHero(Hero h) {
        for (Player anotherPlayer : players) {
            Hero enemy = anotherPlayer.getHero();
            if (enemy.equals(h) ||
                    !enemy.isAlive() ||
                    enemy.isFlying())
                continue;
            if (enemy.getBody().contains(h.getHead()))
                return enemy;
        }
        return null;
    }

    private Hero checkHeadByHeadCollision(Hero h) {
        for (Player anotherPlayer : players) {
            Hero enemy = anotherPlayer.getHero();
            if (enemy.equals(h))
                continue;
            if (!enemy.isAlive())
                continue;
            if (enemy.getHead().equals(h.getHead()) ||
                    enemy.getNeck().equals(h.getHead()) && h.getNeck().equals(enemy.getHead()))
                return anotherPlayer.getHero();
        }
        return null;
    }

    void addToPoint(Point p) {
        if (p instanceof Apple)
            setApple(p);
        else if (p instanceof Stone)
            setStone(p);
        else if (p instanceof FlyingPill)
            setFlyingPill(p);
        else if (p instanceof FuryPill)
            setFuryPill(p);
        else if (p instanceof Gold)
            setGold(p);
        else
            fail("Невозможно добавить на поле объект типа " + p.getClass());
    }

    private void setApple(Point p) {
        if (isFree(p))
            apples.add(new Apple(p));
    }

    @Override
    public boolean setStone(Point p) {
        if (isFree(p)) {
            stones.add(new Stone(p));
            return true;
        }
        return false;
    }

    @Override
    public void setFlyingPill(Point p) {
        if (isFree(p))
            flyingPills.add(new FlyingPill(p));
    }

    @Override
    public void setFuryPill(Point p) {
        if (isFree(p))
            furyPills.add(new FuryPill(p));
    }

    @Override
    public void setGold(Point p) {
        if (isFree(p))
            gold.add(new Gold(p));
    }

    public List<Apple> getApples() {
        return apples;
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<>(players.size());
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

    public List<StartFloor> getStarts() {
        return starts;
    }

    public List<FlyingPill> getFlyingPills() {
        return flyingPills;
    }

    public List<FuryPill> getFuryPills() {
        return furyPills;
    }

    public List<Gold> getGold() {
        return gold;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = SnakeBoard.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<>();
                List<Hero> heroes = SnakeBoard.this.getHeroes();
                for (Hero hero : heroes)
                    result.addAll(hero.getBody());
                result.addAll(SnakeBoard.this.getWalls());
                result.addAll(SnakeBoard.this.getApples());
                result.addAll(SnakeBoard.this.getStones());
                result.addAll(SnakeBoard.this.getFlyingPills());
                result.addAll(SnakeBoard.this.getFuryPills());
                result.addAll(SnakeBoard.this.getGold());
                result.addAll(SnakeBoard.this.getStarts());
                for (int i = 0; i < result.size(); i++) {
                    Point p = result.get(i);
                    if (p.isOutOf(SnakeBoard.this.size())) { // TODO могут ли существовать объекты за границей поля? (выползать из-за края змея)
                        result.remove(p);
                    }
                }
                return result;
            }
        };
    }

    public void setStartCounter(int newValue) {
        if (!debugMode)
            this.startCounter = newValue;
    }

    private void fail(String message) {
        throw new RuntimeException(message);
    }

    Point getObjOn(Point additionObject) {
        if (apples.contains(additionObject))
            return new Apple(additionObject);
        if (stones.contains(additionObject))
            return new Stone(additionObject);
        if (flyingPills.contains(additionObject))
            return new FlyingPill(additionObject);
        if (furyPills.contains(additionObject))
            return new FuryPill(additionObject);
        if (gold.contains(additionObject))
            return new Gold(additionObject);
        if (starts.contains(additionObject))
            return new StartFloor(additionObject);
        if (walls.contains(additionObject))
            return new Wall(additionObject);
        for (Player player : players)
            if (player.getHero().getBody().contains(additionObject))
                return player.getHero().getNeck(); // это просто любой объект типа Tail
        return null;
    }
}
