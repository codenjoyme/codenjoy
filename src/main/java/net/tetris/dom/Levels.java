package net.tetris.dom;

public class Levels implements GlassEventListener {
    private GameLevel[] levels;
    private int currentLevel;
    private ChangeLevelListener changeLevelListener;

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

    public GameLevel getNextLevel() {
        if (currentLevel + 1 >= levels.length) {
            return new NullGameLevel();
        }
        return levels[currentLevel + 1];
    }

    private boolean applyLevelIfAccepted(GlassEvent event) {
        if (getNextLevel().accept(event)){
            getNextLevel().apply();
            currentLevel++;
            changeLevelListener.levelChanged(getCurrentLevel());
            return true;
        }
        return false;
    }

    public FigureQueue getCurrntLevelQueue() {
        return getCurrentLevel().getFigureQueue();
    }

    @Override
    public void linesRemoved(int total, int amount) {
        boolean accepted = applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, total));
        if (accepted) return;
        applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, amount));
    }

    @Override
    public void figureDropped(Figure figure) {
        applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.FIGURE_DROPPED, figure));
    }

    public GameLevel getCurrentLevel() {
        return levels[currentLevel];
    }

    public int getCurrentLevelNumber() {
        return currentLevel;
    }

    public void setChangeLevelListener(ChangeLevelListener changeLevelListener) {
        this.changeLevelListener = changeLevelListener;
    }
}
