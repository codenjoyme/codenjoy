package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.Figure;

public interface GlassEventListener {
    void glassOverflown();

    void linesRemoved(int amount);

    void figureDropped(Figure figure);
}
