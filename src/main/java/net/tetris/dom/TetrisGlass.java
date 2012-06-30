package net.tetris.dom;

import net.tetris.services.Plot;
import net.tetris.services.PlotColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TetrisGlass implements Glass {
    private int width;
    private int height;
    private GlassEventListener[] glassEventListeners;
    private int occupied[];
    private Figure currentFigure;
    private int currentX;
    private int currentY;

    public TetrisGlass(int width, int height, GlassEventListener... glassEventListeners) {
        this.width = width;
        this.height = height;
        this.glassEventListeners = glassEventListeners;
        occupied = new int[height];
    }

    public boolean accept(Figure figure, int x, int y) {
        if (isOutside(figure, x, y)) {
            return false;
        }

        int[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x);
        boolean isOccupied = false;
        for (int i = 0; i < alignedRows.length; i++) {
            int alignedRow = alignedRows[i];
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
        int position = findAvailableYPosition(figure, x, y) - figure.getBottom();
        if (position >= height) {
            return;
        }
        performDrop(figure, x, position);
        removeLines();
    }

    private void performDrop(Figure figure, int x, int position) {
        int[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x);
        for (int i = 0; i < alignedRows.length; i++) {
            occupied[position + alignedRows.length - i - 1] |= alignedRows[i];
        }

        for (GlassEventListener glassEventListener : glassEventListeners) {
            glassEventListener.figureDropped(figure);
        }
    }

    private void removeLines() {
        int removedLines = 0;
        for (int i = 0; i < occupied.length; i++) {
            while (occupied[i] == 0b11111111110) {
                System.arraycopy(occupied, 1, occupied, 0, occupied.length - 1);
                occupied[occupied.length - 1] = 0;
                removedLines++;
            }
        }
        if (removedLines > 0) {
            for (GlassEventListener glassEventListener : glassEventListeners) {
                glassEventListener.linesRemoved(removedLines);
            }
        }
    }

    private int findAvailableYPosition(Figure figure, int x, int y) {
        int position = y;
        while (accept(figure, x, --position)) {
        }
        position++;
        return position;
    }

    private int[] alignFigureRowCoordinatesWithGlass(Figure figure, int x) {
        int[] rows = figure.getRowCodes();
        int[] result = new int[figure.getRowCodes().length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i] << (width - x - figure.getRight());
        }
        return result;
    }

    public void empty() {
        Arrays.fill(occupied, 0);
        for (GlassEventListener glassEventListener : glassEventListeners) {
            glassEventListener.glassOverflown();
        }
    }

    @Override
    public void figureAt(Figure figure, int x, int y) {
        currentFigure = figure;
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public List<Plot> getPlots() {
        ArrayList<Plot> plots = new ArrayList<>();
        if (currentFigure != null) {
            plots.addAll(exportCurrentFigure());
        }
        plots.addAll(exportDropped());
        return plots;
    }

    private List<Plot> exportDropped() {
        LinkedList<Plot> plots = new LinkedList<>();
        for (int y = 0; y < occupied.length; y++) {
            for (int x = width; x >= 0; x--) {
                if (((occupied[y] >> x) & 0x1) == 0) {
                    continue;
                }
                plots.add(new Plot(0 - x + width, y, PlotColor.CYAN));
            }
        }
        return plots;
    }

    private List<Plot> exportCurrentFigure() {
        LinkedList<Plot> plots = new LinkedList<>();
        final int[] rowCodes = currentFigure.getRowCodes();
        for (int i = 0; i < rowCodes.length; i++) {
            for (int x = currentFigure.getWidth(); x >= 0; x--) {
                if (((rowCodes[i] >> x) & 0x1) == 0) {
                    continue;
                }
                int y = currentFigure.getTop() - i;
                plots.add(new Plot(currentX - x + currentFigure.getRight(), currentY + y, PlotColor.CYAN));
            }
        }
        return plots;
    }

    public boolean isEmpty() {
        for (int anOccupied : occupied) {
            if (anOccupied != 0) {
                return false;
            }
        }
        return true;
    }
}
