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


import java.util.List;

public interface LevelsPool {

    int getQuestionIndex();

    int getTotalQuestions();

    List<String> getQuestions();

    List<String> getAnswers();

    void nextLevel();

    void nextQuestion();

    /**
     * @return true - if last question answered
     *         false - if there are some unanswered questions
     */
    boolean isLevelFinished();

    int getLevelIndex();

    boolean isLastQuestion();

    void firstLevel();

    String getDescription();

    int getComplexity();

    void waitNext();

    boolean isWaitNext();
}
