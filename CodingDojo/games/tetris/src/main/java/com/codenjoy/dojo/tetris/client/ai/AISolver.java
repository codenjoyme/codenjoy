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
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver extends AbstractJsonSolver<Board> {

    private static final int GAPS_FACTOR = 100;
    private static final int HEIGHT_FACTOR = 50;
    private Dice dice;
    private int size;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
        String glassString = board.getGlass().getLayersString().get(0);
        size = board.getGlass().size();
        Glass glass = new GlassImpl(size, size, () -> 0);

        Elements current = board.getCurrentFigureType();
        if (current == null) {
            return "";
        }
        Figure figure = Type.getByIndex(current.index()).create();

        Point point = board.getCurrentFigurePoint();

        Level level = new LevelImpl(glassString);
        List<Plot> plots = level.plots();

        removeCurrentFigure(glass, figure, point, plots);

        Tetris.setPlots(glass, plots);

        List<Combination> combos = getPointToDrop(size, glass, figure);

        Combination combo = findBest(combos);

        if (combo == null) {
            System.out.println(); // не должно случиться
        }
        Point to = combo.getPoint();

        int dx = to.getX() - point.getX();
        Direction direction;
        if (dx > 0) {
            direction = Direction.RIGHT;
        } else if (dx < 0) {
            direction = Direction.LEFT;
        } else {
            direction = null;
        }

        final String[] result = {""};
        if (direction != null) {
            IntStream.rangeClosed(1, Math.abs(dx))
                    .forEach(i -> result[0] += (((result[0].length() > 0) ? "," : "") + direction.toString()));
        }
        String rotate = (combo.getRotate() == 0) ? "" : String.format("ACT(%s)", combo.getRotate());
        String comma = (StringUtils.isEmpty(rotate) || StringUtils.isEmpty(result[0])) ? "" : ",";
        String part = rotate + comma + result[0];
        String comma2 = (StringUtils.isEmpty(part)) ? "" : ",";
        return part + comma2 + "DOWN";
    }

    private Combination findBest(List<Combination> combos) {
        return combos.stream()
                    .sorted(compareCombos())
                    .findFirst()
                    .get();
    }

    private Comparator<? super Combination> compareCombos() {
        return (o1, o2) -> Float.compare(getPenalty(o1), getPenalty(o2));
    }

    private int getPenalty(Combination o1) {
        return o1.getGaps() * GAPS_FACTOR +
                o1.getHeight() * HEIGHT_FACTOR;
    }

    private void removeCurrentFigure(Glass glass, Figure figure, Point point, List<Plot> plots) {
        glass.figureAt(figure, point.getX(), point.getY());
        List<Plot> toRemove = glass.currentFigure();
        plots.removeAll(toRemove);
        glass.figureAt(null, 0, 0);
    }

    static class Combination {
        private Point point;
        private int rotate;
        private int gaps;
        private int height;

        public Combination(int rotate, Point point) {
            this.rotate = rotate;
            this.point = point;
        }

        public void setScore(int gaps, int height) {
            this.gaps = gaps;
            this.height = height;
        }

        public Point getPoint() {
            return point;
        }

        public int getRotate() {
            return rotate;
        }

        public int getGaps() {
            return gaps;
        }

        public int getHeight() {
            return height;
        }
    }

    private List<Combination> getPointToDrop(int size, Glass glass, Figure figure) {
        List<Combination> result = new LinkedList<>();
        for (int r = 0; r <= 2; r++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (glass.accept(figure, x, y)) {
                        Glass clone = glass.clone();
                        clone.drop(figure, x, y);
                        Combination combo = new Combination(r, pt(x, y));
                        calculateScore(clone, combo);
                        result.add(combo);
                    }
                }
            }
            figure.rotate(1);
        }
        return result;
    }

    private void calculateScore(Glass glass, Combination combo) {
        List<Plot> dropped = glass.dropped();
        boolean[][] occupied = new boolean[size][size];
        for (int x = 0; x < size; x++) {
            occupied[x] = new boolean[size];
            for (int y = 0; y < size; y++) {
                occupied[x][y] = false;
            }
        }
        dropped.forEach(point -> occupied[point.getX()][point.getY()] = true);
        int gaps = 0;
        int height = 0;
        for (int x = 0; x < size; x++) {
            boolean start = false;
            int deep = 0;
            for (int y = size - 1; y >= 0; y--) {
                if (start) {
                    height++;
                    deep++;
                    if (!occupied[x][y]) {
                        gaps += deep;
                        deep = 0;
                    }
                } else {
                    if (occupied[x][y]) {
                        start = true;
                    }
                }
            }
        }
        combo.setScore(gaps, height);
    }
}
