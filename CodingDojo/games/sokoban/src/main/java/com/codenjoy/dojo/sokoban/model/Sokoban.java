package com.codenjoy.dojo.sokoban.model;

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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.sokoban.model.items.*;
import com.codenjoy.dojo.sokoban.model.levels.Level;
import com.codenjoy.dojo.sokoban.services.Events;
import com.codenjoy.dojo.sokoban.services.GameSettings;
import com.codenjoy.dojo.sokoban.services.Player;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Sokoban implements Field {

    public static final int MAX = 100;
    static int realMarksToWin;

    private int marksToWin;
    private int size;
    private List<BoxOnTheMark> boxesOnTheMarks;
    private List<Wall> walls;
    private List<Box> boxes;
    private List<Mark> marks;
    private boolean boxesBlocked;
    private boolean isWon;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    public Sokoban(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        size = level.getSize();
        walls = level.getWalls();
        boxes = level.getBoxes();
        marks = level.getMarks();
//      boxesOnTheMarks = new LinkedList<>();
        boxesOnTheMarks = level.getBoxesOnTheMarks();
        this.settings = settings;
        marks.stream().forEach(mark -> mark.init(this));
        this.marksToWin = level.getMarksToWin();
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        realMarksToWin = 0;
        Player player = players.get(0);
        Hero hero = player.getHero();
        hero.tick();

        if (!hero.isAlive() || boxesBlocked) {
            player.event(Events.LOSE);
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

        if (marksToWin == realMarksToWin) {
            isWon = true;
            player.event(Events.WIN);
        }

    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(Point pt) {
        return pt.isOutOf(size)
                || walls.contains(pt)
                || boxes.contains(pt)
                || boxesOnTheMarks.contains(pt)
                || getHeroes().contains(pt);
    }

    public boolean isWall(Point pt) {
        return walls.contains(pt);
    }

    public boolean isAdjacentToWall(Point pt) {
        return isBarrier(LEFT.change(pt))
                || isWall(RIGHT.change(pt))
                || isWall(DOWN.change(pt))
                || isWall(UP.change(pt));
    }

    @Override
    public Point getFreeRandom() {
        int c = 0;
        Point pt;
        do {
            pt = PointImpl.random(dice, size);
        } while (!isFree(pt) && c++ < MAX);

        if (c >= MAX) {
            return pt(0, 0);
        }

        return pt;
    }

    @Override
    public boolean isFree(Point pt) {
        return !(walls.contains(pt)
                || boxes.contains(pt)
                || getHeroes().contains(pt));
    }

    @Override
    public boolean isBox(Point pt) {
        return boxes.contains(pt);
    }

    @Override
    public boolean isBoxOnTheMark(Point pt) {
        return boxesOnTheMarks.contains(pt);
    }

    public void moveBox(Point pt, Point newPt) {
        boxes.remove(pt);
        boxes.add(new Box(newPt));
    }

    public void setBox(Point pt) {
        boxes.add(new Box(pt));
    }

    @Override
    public boolean isMark(Point pt) {
        return marks.contains(pt);
    }

    @Override
    public void setMark(Point pt) {
        if (!marks.contains(pt)) {
            marks.add(new Mark(pt));
        }
    }

    @Override
    public void setBoxOnTheMark(Point pt) {
        if (!boxesOnTheMarks.contains(pt)) {
            boxesOnTheMarks.add(new BoxOnTheMark(pt));
        }
    }

    @Override
    public void removeBoxOnTheMark(Point pt) {
        boxesOnTheMarks.remove(pt);
    }

    @Override
    public void removeBox(Point pt) {
        boxes.remove(pt);
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

    @Override
    public GameSettings settings() {
        return settings;
    }

    public List<Wall> getWalls() {
        return walls;
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
        return new BoardReader<Player>() {
            private int size = Sokoban.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements(Player player) {
                LinkedList<Point> result = new LinkedList<>();
                result.addAll(getWalls());
                result.addAll(getHeroes());
                result.addAll(getBoxesOnTheMarks());
                result.addAll(getBoxes());
                result.addAll(getMarks());
                return result;
            }
        };
    }

    public int getRealMarksToWin() {
        return realMarksToWin;
    }

    public int getMarksToWin() {
        return marksToWin;
    }

    public boolean isWon() {
        return isWon;
    }
}