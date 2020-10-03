package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.gamelevel.NullGameLevel;

import java.util.Arrays;

public class Levels implements GlassEventListener {

    private GameLevel[] levels;
    private int currentLevel;
    private ChangeLevelListener listener;
    private int totalRemovedLines;
    protected boolean levelCompleted;

    public Levels(GameLevel ... levels) {
        if (levels.length == 0) {
            throw new IllegalArgumentException("Should provide initial level");
        }
        this.levels = levels;
        currentLevel = 0;
        totalRemovedLines = 0;
        levelCompleted = false;
        levels[0].apply();
    }

    public int count() {
        return levels.length;
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
            levelCompleted = true;
            onLevelCompleted();
        }
    }

    public void tryGoNextLevel() {
        if (levelCompleted) {
            levelCompleted = false;
            gotoLevel(currentLevel + 1);
        }
    }

    public void gotoLevel(int levelNumber) {
        currentLevel = levelNumber;
        levels[currentLevel].apply();
        onLevelChanged();
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
    public void figureDropped(Type figureType) {
        applyLevelIfAccepted(new GlassEvent<>(GlassEvent.Type.FIGURE_DROPPED, figureType));
    }

    public GameLevel getCurrentLevel() {
        return levels[currentLevel];
    }

    public int getCurrentLevelNumber() {
        return currentLevel;
    }

    public void onChangeLevel(ChangeLevelListener listener) {
        this.listener = listener;
        onLevelChanged();
    }

    protected void onLevelChanged() {
        if (listener != null) {
            listener.levelChanged(getCurrentLevelNumber(), getCurrentLevel());
        }
    }

    protected void onLevelCompleted() {
        if (listener != null) {
            listener.levelCompleted(getCurrentLevelNumber(), getCurrentLevel());
        }
    }

    public int totalRemovedLines() {
        return totalRemovedLines;
    }

    public void clearScore() {
        Arrays.stream(levels).forEach(it -> it.queue().clear());
        gotoLevel(0);
    }

    public boolean levelCompleted() {
        return levelCompleted;
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
        private FigureQueue queue;
        private Levels levels;
        private Dice dice;

        public LevelsBuilder(Dice dice) {
            this.dice = dice;
        }

        public void setCurrentLevel(int currentLevel) {
            this.currentLevel = currentLevel;
        }

        public void setTotalRemovedLines(int totalRemovedLines) {
            this.totalRemovedLines = totalRemovedLines;
        }

        public void setLevelsName(String name) {
            this.name = name;
        }

        public FigureQueue figureQueue() {
            return queue;
        }

        public Levels levels() {
            if (levels == null) {
                queue = new Figures();
                levels = levelsFactory.createLevels(name, dice, queue);
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
