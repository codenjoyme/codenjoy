package net.tetris.dom;

public class Levels implements GlassEventListener {
    private GameLevel[] levels;
    private int currentLevel;
    private ChangeLevelListener changeLevelListener;
    private int totalRemovedLines;
    private int removedLinesWithoutOverflown;

    public Levels(GameLevel ... levels) {
        if (levels.length == 0) {
            throw new IllegalArgumentException("Should provide initial level");
        }
        this.levels = levels;
        currentLevel = 0;
        totalRemovedLines = 0;
        removedLinesWithoutOverflown = 0;
        levels[0].apply();
    }

    @Override
    public void glassOverflown() {
        removedLinesWithoutOverflown = 0;
        applyLevelIfAccepted(new GlassEvent<Void>(GlassEvent.Type.GLASS_OVERFLOW, null));
    }

    public GameLevel getNextLevel() {
        if (currentLevel + 1 >= levels.length) {
            return new NullGameLevel();
        }
        return levels[currentLevel + 1];
    }

    private void applyLevelIfAccepted(GlassEvent event) {
        if (getNextLevel().accept(event)){
            getNextLevel().apply();
            currentLevel++;
            onLevelChanged();
        }
    }

    @Override
    public void linesRemoved(int amount) {
        totalRemovedLines += amount;
        removedLinesWithoutOverflown += amount;
        applyLevelIfAccepted(nextLevelAcceptedCriteriaOnLinesRemovedEvent(amount));
    }

    protected GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, amount);
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
        onLevelChanged();
    }

    private void onLevelChanged() {
        removedLinesWithoutOverflown = 0;
        changeLevelListener.levelChanged(getCurrentLevel());
    }

    public int getTotalRemovedLines() {
        return totalRemovedLines;
    }

    public int getRemovedLinesWithoutOverflown() {
        return removedLinesWithoutOverflown;
    }
}
