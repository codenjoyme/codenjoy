package net.tetris.dom;

public class NullGameLevel implements GameLevel {

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
        return GlassEvent.THIS_IS_LAST_LEVEL;
    }

    @Override
    public FigureQueue getFigureQueue() {
        return null;
    }
}
