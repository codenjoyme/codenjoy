package net.tetris.dom;

import java.util.List;

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
        return !occupied[x][y];
    }

    public void drop(Figure figure, int x, int y) {
        occupied[x][0] = true;
    }

    public void empty() {

    }
}
