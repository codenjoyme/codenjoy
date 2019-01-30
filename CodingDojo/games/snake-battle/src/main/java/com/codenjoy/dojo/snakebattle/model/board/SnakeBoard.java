package com.codenjoy.dojo.snakebattle.model.board;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.level.Level;
import com.codenjoy.dojo.snakebattle.model.objects.*;
import com.codenjoy.dojo.snakebattle.services.Events;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class SnakeBoard implements Field {

    private List<Wall> walls;
    private List<StartFloor> starts;
    private List<Apple> apples;
    private List<Stone> stones;
    private List<FlyingPill> flyingPills;
    private List<FuryPill> furyPills;
    private List<Gold> gold;

    private List<Player> players;
    private List<Player> theWalkingDead;

    private Timer timer;
    private Parameter<Integer> roundsPerMatch;
    private int round;

    private int size;
    private Dice dice;

    public SnakeBoard(Level level, Dice dice, Timer timer, Parameter<Integer> roundsPerMatch) {
        this.dice = dice;
        this.roundsPerMatch = roundsPerMatch;
        round = 0;
        walls = level.getWalls();
        starts = level.getStartPoints();
        apples = level.getApples();
        stones = level.getStones();
        flyingPills = level.getFlyingPills();
        furyPills = level.getFuryPills();
        gold = level.getGold();
        size = level.getSize();
        players = new LinkedList<>();
        theWalkingDead = new LinkedList<>();

        this.timer = timer.reset();
    }

    @Override
    public void tick() {
        snakesClear();

        timer.tick(this::sendTimerStatus);

        if (timer.justFinished()) {
            round++;
            players.forEach(p -> p.start(round));
        }

        if (!timer.done()) {
            return;
        }

        restartIfLast();
        snakesMove();
        snakesFight();
        fireWinEvents();
        setNewObjects();
    }

    private void sendTimerStatus() {
        String pad = StringUtils.leftPad("", timer.time(), '.');
        String message = pad + timer.time() + pad;
        players.forEach(player -> player.printMessage(message));
    }

    private void snakesClear() {
        players.stream()
                .filter(p -> p.isActive() && !p.isAlive())
                .forEach(p -> p.getHero().clear());
    }

    @Override
    public void clearScore() {
        round = 0;

        players.forEach(p -> newGame(p));
    }

    private void setNewObjects() {
        int playersVar = (players.size() / 2) + 1;
        int randVal = dice.next(50);
        if (randVal == 42 && furyPills.size() < playersVar)
            setFuryPill(getFreeRandom());
        if (randVal == 32 && flyingPills.size() < playersVar)
            setFlyingPill(getFreeRandom());
        if (randVal == 21 && gold.size() < playersVar)
            setGold(getFreeRandom());
        if ((randVal == 11 && stones.size() < size / 2) || stones.size() < 1)
            setStone(getFreeRandom());
        if (randVal < 5 || apples.size() < playersVar)
            setApple(getFreeRandom());
    }

    private void fireWinEvents() {
        if (theWalkingDead.isEmpty()) {
            return;
        }
        theWalkingDead.clear();

        List<Player> alive = aliveActive();
        if (alive.size() == 1) {
            alive.forEach(p -> p.event(Events.WIN));
        }
    }

    private List<Player> aliveActive() {
        return players.stream()
                .filter(p -> p.isAlive() && p.isActive())
                .collect(toList());
    }

    private void snakesMove() {
        for (Player player : aliveActive()) {
            Hero hero = player.getHero();
            Point head = hero.getNextPoint();
            hero.tick();

            if (apples.contains(head)) {
                apples.remove(head);
                player.event(Events.APPLE);
            }
            if (stones.contains(head) && !hero.isFlying()) {
                stones.remove(head);
                if (player.isAlive()) {
                    player.event(Events.STONE);
                }
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

    private void snakesFight() {
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

    private void restartIfLast() {
        if (timer.unlimited()) {
            return;
        }

        List<Player> players = aliveActive();
        if (players.size() == 1) {
            newGame(players.get(0));
        }

        // если остался один игрок или вообще никого -
        // перезапускаем таймер. Когда время выйдет -
        // змейки пустятся в пляс
        if (players.size() <= 1) {
            timer.reset();
        }
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
        int x;
        int y;
        int c = 0;
        do {
            x = dice.next(size);
            y = dice.next(size);
        } while (!isFree(x, y) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);
        }

        return pt(x, y);
    }

    @Override
    public Point getFreeStart() {
        for (int i = 0; i < 10 && !starts.isEmpty(); i++) {
            StartFloor start = starts.get(dice.next(starts.size()));
            if (freeOfHero(start))
                return start;
        }
        for (StartFloor start : starts)
            if (freeOfHero(start))
                return start;
        return pt(0, 0);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);
        return isFree(pt);
    }

    public boolean isFree(Point pt) {
        return isFreeOfObjects(pt) && freeOfHero(pt);
    }

    public boolean isFreeForStone(Point pt) {
        Point leftSide = pt.copy();
        leftSide.change(Direction.LEFT);
        return isFree(pt) && !starts.contains(leftSide);
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

    @Override
    public void oneMoreDead(Player player) {
        player.die(round == roundsPerMatch.getValue());
        theWalkingDead.add(player);
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

    public void addToPoint(Point p) {
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
        if (isFreeForStone(p)) {
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
                return new LinkedList<Point>(){{
                    SnakeBoard.this.getHeroes().forEach(hero -> addAll(hero.getBody()));
                    addAll(SnakeBoard.this.getWalls());
                    addAll(SnakeBoard.this.getApples());
                    addAll(SnakeBoard.this.getStones());
                    addAll(SnakeBoard.this.getFlyingPills());
                    addAll(SnakeBoard.this.getFuryPills());
                    addAll(SnakeBoard.this.getGold());
                    addAll(SnakeBoard.this.getStarts());
                    for (int i = 0; i < size(); i++) {
                        Point p = get(i);
                        if (p.isOutOf(SnakeBoard.this.size())) { // TODO могут ли существовать объекты за границей поля? (выползать из-за края змея)
                            remove(p);
                        }
                    }
                }};
            }
        };
    }

    private void fail(String message) {
        throw new RuntimeException(message);
    }

    public Point getObjOn(Point additionObject) {
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
