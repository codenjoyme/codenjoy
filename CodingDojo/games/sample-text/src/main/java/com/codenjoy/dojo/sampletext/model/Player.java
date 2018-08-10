package com.codenjoy.dojo.sampletext.model;

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


import com.codenjoy.dojo.sampletext.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки.
 * Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player extends GamePlayer<Hero, Field> {

    private Field field;
    private List<QuestionAnswer> history;
    private int questionIndex;
    Hero hero;

    public Player(EventListener listener) {
        super(listener);
        history = new LinkedList<>();
    }

    public void clearScore() {
        if (history != null) {
            history.clear();
        }
        questionIndex = 0;
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(Field field) {
        hero = new Hero();
        this.field = field;
        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    public String getNextQuestion() { // TODO test me
        if (field.isLastQuestion(questionIndex)) {
            return "You win!";
        }
        return field.getQuestion(questionIndex);
    }

    public List<QuestionAnswer> getHistory() {
        List<QuestionAnswer> result = new LinkedList<>();
        result.addAll(history);
        return result;
    }

    public void checkAnswer() {
        hero.tick();

        String answer = hero.popAnswer();
        if (answer != null && !field.isLastQuestion(questionIndex)) {
            String question = field.getQuestion(questionIndex);
            String validAnswer = field.getAnswer(questionIndex);
            if (validAnswer.equals(answer)) {
                logSuccess(question, answer);
                event(Events.WIN);
                questionIndex++;
            } else {
                logFailure(question, answer);
                event(Events.LOOSE);
                questionIndex = 0;
            }
        }
    }

    private void logSuccess(String question, String answer) {
        log(question, answer, true);
    }

    private void logFailure(String question, String answer) {
        log(question, answer, false);
    }

    private void log(String question, String answer, boolean valid) {
        QuestionAnswer qa = new QuestionAnswer(question, answer);
        qa.setValid(valid);
        history.add(qa);
    }
}
