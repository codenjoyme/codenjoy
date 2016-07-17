package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.*;
import net.tetris.services.PlayerFigures;
import net.tetris.services.levels.LevelsFactory;

public class Levels implements GlassEventListener {
    private GameLevel[] levels;
    private int currentLevel;
    private ChangeLevelListener changeLevelListener;
    private int totalRemovedLines;

    public Levels(GameLevel ... levels) {
        if (levels.length == 0) {
            throw new IllegalArgumentException("Should provide initial level");
        }
        this.levels = levels;
        currentLevel = 0;
        totalRemovedLines = 0;
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

    protected void onLevelChanged() {
        if (changeLevelListener != null) {
            changeLevelListener.levelChanged(getCurrentLevelNumber(), getCurrentLevel());
        }
    }

    public int getTotalRemovedLines() {
        return totalRemovedLines;
    }

    public class LevelsReader {
        public int getCurrentLevel() {
            return Levels.this.currentLevel;
        }

        public int getTotalRemovedLines() {
            return Levels.this.totalRemovedLines;
        }

        public String getLevelsName() {
            return Levels.this.getClass().getSimpleName();
        }
    }

    public static class LevelsBuilder {

        private LevelsFactory levelsFactory = new LevelsFactory();

        private int totalRemovedLines;
        private int currentLevel;
        private String name;
        private PlayerFigures queue;
        private Levels levels;

        public void setCurrentLevel(int currentLevel) {
            this.currentLevel = currentLevel;
        }

        public void setTotalRemovedLines(int totalRemovedLines) {
            this.totalRemovedLines = totalRemovedLines;
        }

        public void setLevelsName(String name) {
            this.name = name;
        }

        public FigureQueue getFigureQueue() {
            return queue;
        }

        public Levels getLevels() {
            if (levels == null) {
                queue = new PlayerFigures();
                levels = levelsFactory.getGameLevels(queue, name);
                levels.totalRemovedLines = totalRemovedLines;
                levels.currentLevel = currentLevel;
                levels.getCurrentLevel().apply();
            }
            return levels;
        }

        public void setLevels(Levels levels) {
            this.levels = levels;
        }
    }
}
