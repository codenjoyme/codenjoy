package net.tetris.dom;

public interface ScoreBoard {
    void glassOverflown();

    void linesRemoved(int amount);

    void figureDropped(Figure figure);
}
