package com.codenjoy.dojo.kata.model;

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


import com.codenjoy.dojo.kata.model.levels.LevelsPool;
import com.codenjoy.dojo.kata.services.Events;
import com.codenjoy.dojo.services.EventListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player {

    private EventListener listener;
    private LevelsPool level;
    private Field field;
    private List<QuestionAnswers> history = new LinkedList<>();
    Hero hero;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты которые касаются конкретного пользователя сормить ему.
     */
    public Player(EventListener listener, LevelsPool level) {
        this.listener = listener;
        this.level = level;
        clearScore();
    }

    private void increaseScore() {
        level.nextQuestion();
    }

    public int getMaxScore() {
        return 0;
    }

    public int getScore() {
        return level.getLevelIndex();
    }

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     *
     * @param event тип ивента
     */
    public void event(Events event) {
        switch (event) {
            case LOOSE:
                gameOver();
                break;
            case WIN:
                increaseScore();
                break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        // do nothing
    }

    public void clearScore() {
        history.clear();
        level.firstLevel();
    }

    public Hero getHero() {
        return hero;
    }

    /**
     * Когда создается новая игра для пользователя, кто-то должен создать героя
     *
     * @param field борда
     */
    public void newHero(Field field) {
        hero = new Hero();
        this.field = field;
        hero.init(field);
    }

    public List<String> getNextQuestion() {
        if (level.isLastQuestion()) {
            return Arrays.asList("Congratulations!! Mo more questions!");
        }
        return level.getQuestions();
    }

    public List<QuestionAnswer> getLastHistory() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1).getQuestionAnswers();
    }

    public List<QuestionAnswers> getHistory() {
        List<QuestionAnswers> result = new LinkedList<>();
        result.addAll(history);
        return result;
    }

    public void checkAnswer() {
        hero.tick();

        List<String> actualAnswers = hero.popAnswers();
        if (actualAnswers.isEmpty()) {
            return;
        }

        logNextAttempt();

        if (level.isLastQuestion()) {
            return;
        }

        List<String> questions = level.getQuestions();
        List<String> expectedAnswers = level.getAnswers();
        boolean isWin = true;
        for (int index = 0; index < questions.size(); index++) {
            String question = questions.get(index);
            String expectedAnswer = expectedAnswers.get(index);
            String actualAnswer = "Unanswered! You should answer this question!!";
            if (index < actualAnswers.size()) {
                actualAnswer = actualAnswers.get(index);
            }

            if (expectedAnswer.equals(actualAnswer)) {
                logSuccess(question, actualAnswer);
            } else {
                logFailure(question, actualAnswer);
                isWin = false;
            }
        }

        if (isWin) {
            event(Events.WIN);
        } else {
            event(Events.LOOSE);
        }
    }

    private void logSuccess(String question, String answer) {
        log(question, answer, true);
    }

    private void logFailure(String question, String answer) {
        log(question, answer, false);
    }

    private void logNextAttempt() {
        history.add(new QuestionAnswers());
    }

    private void log(String question, String answer, boolean valid) {
        QuestionAnswer qa = new QuestionAnswer(question, answer);
        qa.setValid(valid);
        history.get(history.size() - 1).add(qa);
    }

    public String getDescription() {
        return level.getDescription();
    }
}