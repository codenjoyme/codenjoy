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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TetrisGlass implements Glass {

    public static final int BITS_PER_POINT = 3;
    private int width;
    private int height;
    private EventListener eventListener;
    private long occupied[];
    private Figure currentFigure;
    private int currentX;
    private int currentY;

    public TetrisGlass(int width, int height) {
        this.width = width;
        this.height = height;
        occupied = new long[height];
    }

    public boolean accept(Figure figure, int x, int y) {
        if (isOutside(figure, x, y)) {
            return false;
        }

        long[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x, true);
        boolean isOccupied = false;
        for (int i = 0; i < alignedRows.length; i++) {
            long alignedRow = alignedRows[i];
            int rowPosition = y - i + figure.getTop();
            if (rowPosition >= height) {
                continue;
            }
            isOccupied |= (occupied[rowPosition] & alignedRow) > 0;
        }
        return !isOccupied;
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
        return y - figure.getBottom() < 0;
    }

    private boolean isOutsideLeft(Figure figure, int x) {
        return x - figure.getLeft() < 0;
    }

    private boolean isOutsideRight(Figure figure, int x) {
        return x + figure.getRight() >= width;
    }

    public void drop(Figure figure, int x, int y) {
        if (isOutside(figure, x, y)) {
            return;
        }
        int availablePosition = findAvailableYPosition(figure, x, y);
        if (availablePosition >= height) {
            return;
        }
        performDrop(figure, x, availablePosition - figure.getBottom());
        removeLines();
    }

    private void performDrop(Figure figure, int x, int position) {
        long[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x, false);
        for (int i = 0; i < alignedRows.length; i++) {
            int rowPosition = position + alignedRows.length - i - 1;
            if (rowPosition >= occupied.length) {
                continue;
            }
            occupied[rowPosition] |= alignedRows[i];
        }

        if (eventListener != null) {
            eventListener.event(Events.figuresDropped(figure.getType().getColor().index()));
        }
    }

    private void removeLines() {
        int removedLines = 0;
        for (int i = 0; i < occupied.length; i++) {
            while (wholeLine(i)) {
                System.arraycopy(occupied, i + 1, occupied, i, occupied.length - i - 1);
                occupied[occupied.length - 1] = 0;
                removedLines++;
            }
        }
        if (removedLines > 0) {
            if (eventListener != null) {
                eventListener.event(Events.linesRemoved(removedLines));
            }
        }
    }

    private boolean wholeLine(int rowNum) {
        for (int i = 0; i < width; i++) {
            if ((occupied[rowNum] & (0b111 << ((i + 1) * BITS_PER_POINT))) == 0) {
                return false;
            }
        }
        return true;
    }

    private int findAvailableYPosition(Figure figure, int x, int y) {
        int myPosition = y;
        while (accept(figure, x, --myPosition)) {
        }
        myPosition++;
        return myPosition;
    }

    private long[] alignFigureRowCoordinatesWithGlass(Figure figure, int x, boolean ignoreColors) {
        int[] rows = figure.getRowCodes(ignoreColors);
        long[] result = new long[figure.getRowCodes(false).length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = ((long) rows[i]) << ((width - x - figure.getRight()) * BITS_PER_POINT);
        }
        return result;
    }

    public void empty() {
        Arrays.fill(occupied, 0);
        if (eventListener != null) {
            eventListener.event(Events.glassOverflown());
        }
    }

    @Override
    public void figureAt(Figure figure, int x, int y) {
        currentFigure = figure;
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public List<Plot> getDroppedPlots() {
        LinkedList<Plot> plots = new LinkedList<>();
        for (int y = 0; y < occupied.length; y++) {
            for (int x = width; x >= 0; x--) {
                long colorNumber = (occupied[y] >> (x * BITS_PER_POINT)) & 0b111;
                if (colorNumber == 0) {
                    continue;
                }
                plots.add(new Plot(0 - x + width, y, findColor(colorNumber - 1)));
            }
        }
        return plots;
    }

    private Elements findColor(long colorNumber) {
        return Elements.values()[(int) colorNumber];
    }

    @Override
    public List<Plot> getCurrentFigurePlots() {
        LinkedList<Plot> plots = new LinkedList<>();
        if (currentFigure == null) {
            return plots;
        }
        final int[] rowCodes = currentFigure.getRowCodes(false);
        int rowWidth = currentFigure.getWidth();

        for (int i = 0; i < rowCodes.length; i++) {
            for (int x = rowWidth; x >= 0; x--) {
                int colorNumber = (rowCodes[i] >> (x * BITS_PER_POINT)) & 0b111;
                if (colorNumber == 0) {
                    continue;
                }
                int y = currentFigure.getTop() - i;
                plots.add(new Plot(currentX - x + currentFigure.getRight(), currentY + y, findColor(colorNumber - 1)));
            }
        }
        return plots;
    }

    public boolean isEmpty() {
        for (long anOccupied : occupied) {
            if (anOccupied != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}
