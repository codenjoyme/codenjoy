package com.codenjoy.dojo.kata.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import java.util.Arrays;
import java.util.List;

public class LevelsPoolImpl implements LevelsPool {

    private List<Level> levels;
    private int levelIndex;
    private int questionIndex;
    private Level level;

    public LevelsPoolImpl(List<Level> levels) {
        this.levels = levels;
    }
     
    @Override
    public void nextLevel() {
        firstQuestion();
        if (levelIndex < levels.size() - 1) {
            levelIndex++;
            level = levels.get(levelIndex);
        } else {
            levelIndex++;
            level = new NullLevel();
        }
    }

    @Override
    public boolean isLevelFinished() {
        return questionIndex >= level.size() - 1;
    }
    
    @Override
    public void nextQuestion() {
        if (!isLevelFinished()) {
            questionIndex++;
        }
    }

    @Override
    public void firstLevel() {
        levelIndex = -1;
        nextLevel();
    }

    @Override
    public String getDescription() {
        return level.description();
    }

    @Override
    public int getComplexity() {
        return level.complexity();
    }

    @Override
    public void waitNext() {
        level = new WaitLevel();
    }
    
    @Override
    public boolean isWaitNext() {
        return level instanceof WaitLevel; 
    }

    private void firstQuestion() {
        questionIndex = 0;
    }

    private List<String> getSubList(List<String> list) {
        if (list.isEmpty()) {
            return Arrays.asList();
        } else if (questionIndex < list.size() - 1) {
            return list.subList(0, questionIndex + 1);
        } else if (questionIndex == list.size() - 1) {
            return list;
        } else { // questionIndex >= list.size()
            throw new IllegalStateException("questionIndex >= list.size()");
        }
    }

    @Override
    public int getLevelIndex() {
        return levelIndex;
    }

    @Override
    public int getQuestionIndex() {
        return questionIndex;
    }

    @Override
    public int getTotalQuestions() {
        return level.size();
    }

    @Override
    public List<String> getQuestions() {
        return getSubList(level.getQuestions());
    }

    @Override
    public List<String> getAnswers() {
        return getSubList(level.getAnswers());
    }

    @Override
    public boolean isLastQuestion() {
        return level.size() == 0;
    }

}
