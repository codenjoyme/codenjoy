package net.tetris.dom;

public class Levels implements GlassEventListener {
    private GameLevel[] levels;
    private int currentLevel;


    public Levels(GameLevel ... levels) {
        if (levels.length == 0) {
            throw new IllegalArgumentException("Should provide initial level");
        }
        this.levels = levels;
        currentLevel = 0;
        levels[0].apply();
    }

    @Override
    public void glassOverflown() {
        applyLevelIfAccepted(new GlassEvent<Void>(GlassEvent.Type.GLASS_OVERFLOW, null));
    }

    private GameLevel getNextLevel() {
        if (currentLevel + 1 >= levels.length) {
            return new NullGameLevel();
        }
        return levels[currentLevel + 1];
    }

    private void applyLevelIfAccepted(GlassEvent event) {
        if (getNextLevel().accept(event)){
            getNextLevel().apply();
            currentLevel++;
        }
    }

    @Override
    public void linesRemoved(int amount) {
        applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, amount));
    }

    @Override
    public void figureDropped(Figure figure) {
        applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.FIGURE_DROPPED, figure));
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

}
