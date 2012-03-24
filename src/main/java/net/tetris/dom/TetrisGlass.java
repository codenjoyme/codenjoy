package net.tetris.dom;

public class TetrisGlass implements Glass {
    private int width;
    private int height;
    private boolean occupied[][];
    private int occupied2[];
    
    public TetrisGlass(int width, int height) {
        this.width = width;
        this.height = height;
        occupied = new boolean[width][height];
        occupied2 = new int[height];
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
        return (occupied2[y] & figure.getRowCodes()[0] << (width - x - figure.getRight())) == 0;
    }

    public void drop(Figure figure, int x, int y) {
        occupied[x][0] = true;

        occupied2[0] |= figure.getRowCodes()[0] << (width - x - figure.getRight());
    }

    public void empty() {

    }
}
