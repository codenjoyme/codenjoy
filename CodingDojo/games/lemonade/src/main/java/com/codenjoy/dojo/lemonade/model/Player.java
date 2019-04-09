package com.codenjoy.dojo.lemonade.model;

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


import com.codenjoy.dojo.lemonade.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки.
 * Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player extends GamePlayer<Hero, Field> {

    private Field field;
    private Queue<SalesResult> history;
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

    public JSONObject getNextQuestion() { // TODO test me
        if (field.isLastQuestion(questionIndex)) {
            return new JSONObject().put("messages", "You win!");
        }
        return hero.getNextQuestion().toJson();
    }

    public JSONArray getHistoryJson() {
        JSONArray historyJson = new JSONArray();
        history.forEach(sr -> historyJson.put(sr.toJSONObject()));
        return historyJson;
    }

    public void checkAnswer() {
        hero.tick();
        SalesResult salesResult = hero.popSalesResult();
        if (salesResult != null) {
            history.add(salesResult);
            while (history.size() > 10)
                history.remove();
            if (salesResult.isBunkrupt()) {
                event(Events.LOOSE);
            } else {
                event(Events.WIN);
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
    }
}
