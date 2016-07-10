package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.FigureQueue;

public interface GameLevel {
    boolean accept(GlassEvent event);

    void apply();

    String getNextLevelIngoingCriteria();

    FigureQueue getFigureQueue();

    int getFigureTypesToOpenCount();
}
