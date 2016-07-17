package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.GameLevel;
import com.codenjoy.dojo.tetris.model.GlassEvent;

public class NullGameLevel implements GameLevel {

    public static final String THIS_IS_LAST_LEVEL = "This is last level";
    public static final boolean NO_ACCEPT = false;

    @Override
    public boolean accept(GlassEvent event) {
        return NO_ACCEPT;
    }

    @Override
    public void apply() {
    }

    @Override
    public String getNextLevelIngoingCriteria() {
        return THIS_IS_LAST_LEVEL;
    }

    @Override
    public FigureQueue getFigureQueue() {
        return null;
    }

    @Override
    public int getFigureTypesToOpenCount() {
        return 0;
    }
}
