package net.tetris.dom;

public interface GlassEventListener {
    void glassOverflown();

    void linesRemoved(int amount);

    void figureDropped(Figure figure);
}
