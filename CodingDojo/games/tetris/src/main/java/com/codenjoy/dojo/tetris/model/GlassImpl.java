package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.tetris.services.Events;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class GlassImpl implements Glass {

    public static final int BITS = 3; // per point
    public static final BigInteger Ob111 = new BigInteger("111", 2);

    private int width;
    private int height;
    private EventListener listener;
    private List<BigInteger> occupied = new ArrayList<>();
    private Figure figure;
    private int x;
    private int y;
    private Supplier<Integer> getLevel;

    public GlassImpl(int width, int height, Supplier<Integer> supplier) {
        this.width = width;
        this.height = height;
        this.getLevel = supplier;
        for (int i = 0; i < height; i++) {
            occupied.add(BigInteger.ZERO);
        }
    }

    public boolean accept(Figure figure, int x, int y) {
        if (isOutside(figure, x, y)) {
            return false;
        }

        BigInteger[] aligned = alignRowWithGlass(figure, x, true);
        boolean occupied = false;
        for (int i = 0; i < aligned.length; i++) {
            BigInteger figureLine = aligned[i];
            int pos = y - i + figure.top();
            if (pos >= height) {
                continue;
            }
            BigInteger line = this.occupied.get(pos);
            occupied |= (line.and(figureLine).compareTo(BigInteger.ZERO) == 1);
        }
        return !occupied;
    }

    private boolean isOutside(Figure figure, int x, int y) {
        if (isOutsideLeft(figure, x)) {
            return true;
        }
        if (isOutsideRight(figure, x)) {
            return true;
        }
        if (isOutsideBottom(figure, y)) {
            return true;
        }
        return false;
    }

    private boolean isOutsideBottom(Figure figure, int y) {
        return y - figure.bottom() < 0;
    }

    private boolean isOutsideLeft(Figure figure, int x) {
        return x - figure.left() < 0;
    }

    private boolean isOutsideRight(Figure figure, int x) {
        return x + figure.right() >= width;
    }

    public void drop(Figure figure, int x, int y) {
        if (isOutside(figure, x, y)) {
            return;
        }
        int available = findAvailableYPosition(figure, x, y);
        if (available >= height) {
            return;
        }
        performDrop(figure, x, available - figure.bottom());
        removeLines();
    }

    private void performDrop(Figure figure, int x, int position) {
        BigInteger[] aligned = alignRowWithGlass(figure, x, false);
        for (int i = 0; i < aligned.length; i++) {
            int row = position + aligned.length - i - 1;
            if (row >= occupied.size()) {
                continue;
            }
            BigInteger line = occupied.get(row);
            BigInteger figureLine = aligned[i];
            occupied.set(row, line.or(figureLine));
        }

        if (listener != null) {
            listener.event(Events.figuresDropped(getLevel.get(), figure.type().getColor().index()));
        }
    }

    private void removeLines() {
        int removed = 0;
        for (int i = 0; i < occupied.size(); i++) {
            while (wholeLine(i)) {
                occupied.remove(i);
                occupied.add(BigInteger.ZERO);
                removed++;
            }
        }
        if (removed > 0) {
            if (listener != null) {
                listener.event(Events.linesRemoved(getLevel.get(), removed));
            }
        }
    }

    private boolean wholeLine(int y) {
        for (int i = 0; i < width; i++) {
            BigInteger line = occupied.get(y);
            BigInteger atPos = Ob111.shiftLeft((i + 1)*BITS);
            if ((line.and(atPos).equals(BigInteger.ZERO))) {
                return false;
            }
        }
        return true;
    }

    private int findAvailableYPosition(Figure figure, int x, int y) {
        int yy = y;
        while (accept(figure, x, --yy)) {}
        yy++;
        return yy;
    }

    private BigInteger[] alignRowWithGlass(Figure figure, int x, boolean ignoreColors) {
        int[] rows = figure.rowCodes(ignoreColors);
        BigInteger[] result = new BigInteger[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = BigInteger.valueOf((long) rows[i])
                    .shiftLeft((width - x - figure.right()) * BITS);
        }
        return result;
    }

    public void empty() {
        for (int i = 0; i < occupied.size(); i++) {
            occupied.set(i, BigInteger.ZERO);
        }
        if (listener != null) {
            listener.event(Events.glassOverflown(getLevel.get()));
        }
    }

    @Override
    public void figureAt(Figure figure, int x, int y) {
        this.figure = figure;
        this.x = x;
        this.y = y;
    }

    @Override
    public List<Plot> dropped() {
        LinkedList<Plot> plots = new LinkedList<>();
        for (int y = 0; y < occupied.size(); y++) {
            for (int x = width; x >= 0; x--) {
                BigInteger line = occupied.get(y);
                int color = line.shiftRight(x * BITS).and(new BigInteger("111", 2)).intValue();
                if (color == 0) {
                    continue;
                }
                plots.add(new Plot(0 - x + width, y, findColor(color - 1)));
            }
        }
        return plots;
    }

    private Elements findColor(long colorNumber) {
        return Elements.values()[(int) colorNumber];
    }

    @Override
    public List<Plot> currentFigure() {
        LinkedList<Plot> plots = new LinkedList<>();
        if (figure == null) {
            return plots;
        }
        int[] rowCodes = figure.rowCodes(false);
        int rowWidth = figure.width();

        for (int i = 0; i < rowCodes.length; i++) {
            for (int x = rowWidth; x >= 0; x--) {
                int color = (rowCodes[i] >> (x * BITS)) & 0b111;
                if (color == 0) {
                    continue;
                }
                int y = figure.top() - i;
                plots.add(new Plot(this.x - x + figure.right(),
                        this.y + y, findColor(color - 1)));
            }
        }
        return plots;
    }

    @Override
    public boolean isEmpty() {
        for (BigInteger row : occupied) {
            if (!row.equals(BigInteger.ZERO)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public Glass clone() {
        GlassImpl result = new GlassImpl(width, height, getLevel);
        result.setListener(listener);
        result.figureAt(figure, x, y);
        result.occupied = new LinkedList<>(occupied);
        return result;
    }

}
