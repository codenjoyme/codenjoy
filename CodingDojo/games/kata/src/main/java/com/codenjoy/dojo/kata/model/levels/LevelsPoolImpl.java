package com.codenjoy.dojo.kata.model.levels;

import java.util.Arrays;
import java.util.List;

/**
 * Created by indigo on 2017-03-04.
 */
public class LevelsPoolImpl implements LevelsPool {

    private List<Level> levels;
    private int levelIndex;
    private int questionIndex;
    private Level level;

    public LevelsPoolImpl(List<Level> levels) {
        this.levels = levels;
    }

    private void nextLevel() {
        if (levelIndex < levels.size() - 1) {
            levelIndex++;
            level = levels.get(levelIndex);
            firstQuestion();
        } else {
            levelIndex++;
            level = new NullLevel();
        }
    }

    @Override
    public void nextQuestion() {
        if (questionIndex < level.size() - 1) {
            questionIndex++;
        } else {
            questionIndex = 0;
            nextLevel();
        }
    }

    @Override
    public void firstLevel() {
        levelIndex = -1;
        nextLevel();
    }

    private void firstQuestion() {
        questionIndex = -1;
        nextQuestion();
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
