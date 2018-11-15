package com.codenjoy.dojo.tetris.client.ai;

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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.model.*;

import java.util.List;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver extends AbstractJsonSolver<Board> {

    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
        String glassString = board.getGlass().getLayersString().get(0);
        int size = board.getGlass().size();
        GlassImpl glass = new GlassImpl(size, size, () -> 0);

        Elements current = board.getCurrentFigureType();
        Figure figure = Type.getByIndex(current.index()).create();

        Point point = board.getCurrentFigurePoint();

        Level level = new LevelImpl(glassString);
        List<Plot> plots = level.plots();

        removeCurrentFigure(glass, figure, point, plots);

        Tetris.setPlots(glass, plots);

        Point to = getPointToDrop(size, glass, figure);

        if (to == null) {
            System.out.println(); // не должно случиться
        }

        int dx = to.getX() - point.getX();
        Direction direction;
        if (dx > 0) {
            direction = Direction.RIGHT;
        } else if (dx < 0) {
            direction = Direction.LEFT;
        } else {
            return Direction.DOWN.toString();
        }

        final String[] result = {""};
        IntStream.rangeClosed(1, Math.abs(dx))
                .forEach(i -> result[0] += (((i > 1)?",":"") + direction.toString()));
        return result[0];
    }

    private void removeCurrentFigure(GlassImpl glass, Figure figure, Point point, List<Plot> plots) {
        glass.isAt(figure, point.getX(), point.getY());
        List<Plot> toRemove = glass.currentFigure();
        plots.removeAll(toRemove);
        glass.isAt(null, 0, 0);
    }

    private Point getPointToDrop(int size, GlassImpl glass, Figure figure) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (glass.accept(figure, x, y)) {
                    return pt(x, y);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://127.0.0.1:8080/codenjoy-contest/board/player/apofig@gmail.com?code=20010765231070354251",
                new AISolver(new RandomDice()),
                new Board());
    }
}
