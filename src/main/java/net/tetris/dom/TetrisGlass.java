package net.tetris.dom;

public class TetrisGlass implements Glass {
    private int width;
    private int height;
    private boolean occupied[][];
    
    public TetrisGlass(int width, int height) {
        this.width = width;
        this.height = height;
        occupied = new boolean[width][height];
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
        return !occupied[x][y];
    }

    public void drop(Figure figure, int x, int y) {
        occupied[x][0] = true;
    }

    public void empty() {

    }
}
