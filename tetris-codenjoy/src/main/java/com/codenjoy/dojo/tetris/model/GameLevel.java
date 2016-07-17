package com.codenjoy.dojo.tetris.model;

public interface GameLevel {
    boolean accept(GlassEvent event);

    void apply();

    String getNextLevelIngoingCriteria();

    FigureQueue getFigureQueue();

    int getFigureTypesToOpenCount();
}
