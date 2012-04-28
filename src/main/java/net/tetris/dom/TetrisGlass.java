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
        int[] alignedRows = alignFigureRowCoordinatesWithGlass(figure, x);
        for (int i = 0; i < alignedRows.length; i++) {
            occupied[position + alignedRows.length - i - 1] |= alignedRows[i];
        }
    }

    private int findAvailableYPosition(Figure figure, int x, int y) {
        int position = y;
        while (accept(figure, x, --position)){}
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
