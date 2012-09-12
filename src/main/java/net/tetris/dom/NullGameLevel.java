package net.tetris.dom;

public class NullGameLevel implements GameLevel {
    @Override
    public boolean accept(GlassEvent event) {
        return false;
    }

    @Override
    public void apply() {
    }
}
