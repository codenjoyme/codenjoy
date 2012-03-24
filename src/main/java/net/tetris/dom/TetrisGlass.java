package net.tetris.dom;

public class TetrisGlass implements Glass {
    private int width;
    private int height;
    private int occupied[];
    
    public TetrisGlass(int width, int height) {
        this.width = width;
        this.height = height;
        occupied = new int[height];
    }

    public boolean accept(Figure figure, int x, int y) {
        if (x - figure.getLeft() < 0) {
            return false;
        }
        if (x + figure.getRight() > width) {
            return false;
        }
        if (y - figure.getBottom() < 0) {
            return false;
        }
        int[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x);
        boolean isOccupied = false;
        for (int i = 0; i < alignedRows.length; i++) {
            int alignedRow = alignedRows[i];
            isOccupied |= (occupied[y - i + figure.getTop()] & alignedRow) > 0;
        }
        return !isOccupied;
    }

    public void drop(Figure figure, int x, int y) {
        int[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x);
        for (int i = 0; i < alignedRows.length; i++) {
            occupied[alignedRows.length - i - 1] |= alignedRows[i];
        }
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

    }
}
