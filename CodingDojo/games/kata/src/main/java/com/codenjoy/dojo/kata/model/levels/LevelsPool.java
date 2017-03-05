package com.codenjoy.dojo.kata.model.levels;

import java.util.List;

/**
 * Created by indigo on 2017-03-05.
 */
public interface LevelsPool {

    int getQuestionIndex();

    List<String> getQuestions();

    List<String> getAnswers();

    void nextQuestion();

    int getLevelIndex();

    boolean isLastQuestion();

    void firstLevel();

    String getDescription();
}
