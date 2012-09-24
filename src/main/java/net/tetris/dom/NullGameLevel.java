package net.tetris.dom;

public class NullGameLevel implements GameLevel {
    @Override
    public boolean accept(GlassEvent event) {
        return false;
    }

    @Override
    public void apply() {
    }

    @Override
    public String getNextLevelIngoingCriteria() {
        return GlassEvent.THIS_IS_LAST_LEVEL;
    }
}
