package com.codenjoy.dojo.tetris.model;

public interface GlassEventListener {
    void glassOverflown();

    void linesRemoved(int amount);

    void figureDropped(Figure figure);
}
