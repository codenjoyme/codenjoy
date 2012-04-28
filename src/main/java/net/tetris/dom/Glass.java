package net.tetris.dom;

public interface Glass {
    boolean accept(Figure figure, int x, int y);

    void drop(Figure figure, int x, int y);

    void empty();
}
