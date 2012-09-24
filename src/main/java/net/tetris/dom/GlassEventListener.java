package net.tetris.dom;

public interface GlassEventListener {
    void glassOverflown();

    void linesRemoved(int total, int amount);

    void figureDropped(Figure figure);
}
