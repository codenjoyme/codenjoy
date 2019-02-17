package com.codenjoy.dojo.sokoban.model.game;

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

import com.codenjoy.dojo.sokoban.model.items.Field;
import com.codenjoy.dojo.sokoban.model.items.Level;
import com.codenjoy.dojo.sokoban.model.itemsImpl.*;
import com.codenjoy.dojo.sokoban.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.sokoban.services.Player;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sokoban#tick()}
 */
public class Sokoban implements Field {

    private List<BoxOnTheMark> boxesOnTheMarks;
    private List<Wall> walls;
    private List<Gold> gold;
    private List<Bomb> bombs;
    private List<Box> boxes;
    private List<Mark> marks;
    private boolean boxesBlocked;
    private boolean isWon;
    private List<Player> players;
    private final int expectedMarksToWin;
    private final int size;
    static int realMarksToWin;
    private Dice dice;

    public Sokoban(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        walls = level.getWalls();
        boxes = level.getBoxes();
        marks = level.getMarks();
//      boxesOnTheMarks = new LinkedList<>();
        boxesOnTheMarks = level.getBoxesOnTheMarks();
        marks.stream().forEach(mark -> mark.init(this));
        gold = level.getGold();
        this.expectedMarksToWin = level.getExpectedBoxesValuesInMarks();
        players = new LinkedList<>();
        bombs = new LinkedList<>();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        realMarksToWin = 0;
        Player player = players.get(0);
        Hero hero = player.getHero();
        hero.tick();

        heroCheckingReachGold(player, hero);

        if (!hero.isAlive() || boxesBlocked) {
            player.event(Events.LOOSE);
        }

        for (Mark mark : marks) {
            mark.tick();
        }

        for (Mark mark : marks) {
            if (mark.isFilled()) {
                realMarksToWin++;
            } else {
                realMarksToWin--;
            }
        }

        if (expectedMarksToWin == realMarksToWin) {
            isWon = true;
            player.event(Events.WIN);
        }

    }

    /**
     * cheking reached Gold if so - triggered Win
     *
     * @param player Player obj
     * @param hero   particular case of player = player.getHero()
     */
    private void heroCheckingReachGold(Player player, Hero hero) {
        if (gold.contains(hero)) {
            gold.remove(hero);
            player.event(Events.WIN);
            isWon = true;
            Point pos = getFreeRandom();
            gold.add(new Gold(pos));
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1
                || x < 0
                || y < 0
                || y > size - 1
                || walls.contains(pt)
                || boxes.contains(pt)
                || boxesOnTheMarks.contains(pt)
                || getHeroes().contains(pt);
    }

    public boolean isWall(int x, int y) {
        Point pt = pt(x, y);
        return walls.contains(pt);
    }

    public boolean isAdjacentToWall(int x, int y) {
        return isBarrier(x - 1, y)
                || isWall(x + 1, y)
                || isWall(x, y - 1)
                || isWall(x, y + 1);
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
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !(gold.contains(pt)
                || bombs.contains(pt)
                || walls.contains(pt)
                || boxes.contains(pt)
                || getHeroes().contains(pt));
    }

    @Override
    public boolean isBomb(int x, int y) {
        return bombs.contains(pt(x, y));
    }

    @Override
    public boolean isBox(int x, int y) {
        return boxes.contains(pt(x, y));
    }

    @Override
    public boolean isBoxOnTheMark(int x, int y) {
        return boxesOnTheMarks.contains(pt(x, y));
    }

    public void moveBox(int x, int y, int xNew, int yNew) {
        boxes.remove(pt(x, y));
        boxes.add(new Box(pt(xNew, yNew)));
    }

    public void setBox(int x, int y) {
        boxes.add(new Box(pt(x, y)));
    }

    @Override
    public boolean isMark(int x, int y) {
        return marks.contains(pt(x, y));
    }

    @Override
    public void setBomb(int x, int y) {
        Point pt = pt(x, y);
        if (!bombs.contains(pt)) {
            bombs.add(new Bomb(x, y));
        }
    }

    @Override
    public void setMark(int x, int y) {
        Point pt = pt(x, y);
        if (!marks.contains(pt)) {
            marks.add(new Mark(x, y));
        }
    }

    @Override
    public void removeBomb(int x, int y) {
        bombs.remove(pt(x, y));
    }

    @Override
    public void setBoxOnTheMark(int x, int y) {
        Point pt = pt(x, y);
        if (!boxesOnTheMarks.contains(pt)) {
            boxesOnTheMarks.add(new BoxOnTheMark(x, y));
        }
    }

    @Override
    public void removeBoxOnTheMark(int x, int y) {
        boxesOnTheMarks.remove(pt(x, y));
    }

    @Override
    public void removeBox(int x, int y) {
        boxes.remove(pt(x, y));
    }

    public List<Gold> getGold() {
        return gold;
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<BoxOnTheMark> getBoxesOnTheMarks() {
        return boxesOnTheMarks;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Sokoban.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                LinkedList<Point> result = new LinkedList<>();
                result.addAll(getWalls());
                result.addAll(getHeroes());
                result.addAll(getGold());
                result.addAll(getBoxesOnTheMarks());
                result.addAll(getBombs());
                result.addAll(getBoxes());
                result.addAll(getMarks());
                return result;
            }
        };
    }

    public int getRealMarksToWin() {
        return realMarksToWin;
    }

    public int getExpectedMarksToWin() {
        return expectedMarksToWin;
    }

    public boolean isWon() {
        return isWon;
    }
}
