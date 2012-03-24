package net.tetris.dom;

public class TetrisFigure implements Figure {
    private int centerX;
    private int centerY;
    private String[] rows = new String[]{"#"};

    TetrisFigure() {
    }

    TetrisFigure(int centerX, int centerY, String... rows) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.rows = rows;
    }

    public int getLeft() {
        return centerX;
    }

    public int getRight() {
        return rows[0].length() - centerX - 1;
    }

    public int getTop() {
        return centerY;
    }

    public int getBottom() {
        return rows.length - centerY - 1;
    }
}
