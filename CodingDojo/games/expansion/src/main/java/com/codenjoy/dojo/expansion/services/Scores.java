package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.services.PlayerScores;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Scores implements PlayerScores {

    private static final Logger log = LoggerFactory.getLogger(Hero.class);

    public static final String SCORE = "score";
    public static final String ROUNDS = "rounds";
    public static final String DETAILS = "details";
    public static final double QUARTER = 25.0;

    private volatile int score;
    private JSONObject scores;
    private DecimalFormat format;

    public Scores(Object startScore) {
        if (startScore instanceof Integer) {
            this.score = (Integer)startScore;
        } else if (startScore instanceof String) {
            JSONObject object = new JSONObject((String) startScore);
            this.score = object.getInt(SCORE);
        }
        scores = new JSONObject();
        format = new DecimalFormat("0.00");
        clearRounds();
    }

    private void clearRounds() {
        scores.put(ROUNDS, new JSONArray());
    }

    @Override
    public int clear() {
        clearRounds();
        return score = 0;
    }

    // TODO есть ли случай, когда надо вот это вот все?
    @Override
    public void update(Object data) {
        if (data instanceof String) {
            try {
                data = new JSONObject((String)data);
            } catch (Exception e) {
                try {
                    data = Integer.valueOf((String)data);
                } catch (Exception e2) {
                    // do nothing, we don't know how to parse this format
                }
            }
        }

        if (data instanceof Integer) {
            score = Integer.parseInt(data.toString());
            scores.put(SCORE, score);
        } else if (data instanceof JSONObject) {
            JSONObject json = (JSONObject) data;
            score = json.getInt(SCORE);
            scores.put(SCORE, score);
            scores.put(ROUNDS, json.getJSONArray(ROUNDS));
        } else {
            // do nothing, we don't know how to parse this format
        }
    }

    @Override
    public JSONObject getScore() {
        scores.put(SCORE, score);
        scores.put(DETAILS, details());
        return scores;
    }

    private String details() {
        List<Integer> list = (List)rounds().toList();
        int count = list.size();

        double average = twoDecimalPlaces(QUARTER *
                list.stream()
                        .mapToInt(it -> it)
                        .average().orElse(0.0));

        Collections.reverse(list);

        String rounds = list.stream()
                .map(Object::toString)
                .collect(joining(""));

        return String.format("%s%%(∑%s)[i%s]%s",
                format.format(average),
                score,
                count,
                rounds);
    }

    private double twoDecimalPlaces(double average) {
        return 1.0D * Math.round(average * 100) / 100;
    }

    @Override
    public void event(Object input) {
        Event events = (Event)input;

        int score = events.value();
        rounds().put(score);

        if (events.type() == Event.Type.WIN) {
            this.score += score;
        } else if (events.type() == Event.Type.LOSE) {
            this.score -= score;
        }
        this.score = Math.max(0, this.score);

        if (log.isDebugEnabled()) {
            log.debug("Scores after event {} is {}", input, getScore());
        }
    }

    private JSONArray rounds() {
        return scores.getJSONArray(ROUNDS);
    }
}
