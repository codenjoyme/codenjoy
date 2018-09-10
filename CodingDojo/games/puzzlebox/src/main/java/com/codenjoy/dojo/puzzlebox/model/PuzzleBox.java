package com.codenjoy.dojo.puzzlebox.model;

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


import com.codenjoy.dojo.puzzlebox.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import static com.codenjoy.dojo.services.PointImpl.*;

public class PuzzleBox implements Field {

    private final Level level;
    private List<Player> players;

    public final int size;
    private Dice dice;
    private List<Wall> walls;
    private List<Target> targets;

    private int clicks;

    public PuzzleBox(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        targets = level.getTargets();
        this.level = level;
        size = level.getSize();
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            if (player.getHero().isWin()){
                return;
            }
            for (Box box: player.getHero().getBoxes()) {
                box.tick();
            }
        }
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getBoxes().contains(pt);
    }

    @Override
    public boolean isTarget(int x, int y) {
        boolean result = targets.contains(pt(x, y));
        if (result) {
            players.get(0).event(Events.FILL);
        }
        return result;
    }

    public int size() {
        return size;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
        player.getHero().setBoxes(level.getBoxes(), this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = PuzzleBox.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    addAll(getBoxes());
                    addAll(walls);
                    addAll(targets);
                }};
            }
        };
    }

    public List<Box> getBoxes() {
        List<Box> boxes = new LinkedList<>();
        for (Player player : players) {
            boxes.addAll(player.getHero().getBoxes());
        }
        return boxes;
    }
}
















