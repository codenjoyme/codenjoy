package com.codenjoy.dojo.expansion.services;

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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.PlayerScores;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

public class Scores implements PlayerScores {

    private static Logger logger = DLoggerFactory.getLogger(Scores.class);

    public final static String SCORE = "score";
    public static final String ROUNDS = "rounds";

    private volatile int score;
    private JSONObject log;

    public Scores(Object startScore) {
        if (startScore instanceof Integer) {
            this.score = (Integer)startScore;
        } else if (startScore instanceof String) {
            JSONObject object = new JSONObject((String) startScore);
            this.score = object.getInt(SCORE);
        }
        log = new JSONObject();
        clearRounds();
    }

    private void clearRounds() {
        log.put(ROUNDS, new JSONArray());
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
            score = Integer.valueOf(data.toString());
            log.put(SCORE, score);
        } else if (data instanceof JSONObject) {
            JSONObject json = (JSONObject) data;
            score = Integer.valueOf(json.getInt(SCORE));
            log.put(SCORE, score);
            log.put(ROUNDS, json.getJSONArray(ROUNDS));
        } else {
            // do nothing, we don't know how to parse this format
        }
    }

    @Override
    public JSONObject getScore() {
        log.put(SCORE, score);
        return log;
    }
    @Override
    public void event(Object input) {
        Events events = (Events)input;

        int score = events.getScore();
        rounds().put(score);

        if (events.getType() == Events.Type.WIN) {
            this.score += score;
        } else if (events.getType() == Events.Type.LOOSE) {
            this.score -= score;
        }
        this.score = Math.max(0, this.score);

        if (logger.isDebugEnabled()) {
            logger.debug("Scores after event {} is {}", input, getScore());
        }
    }

    private JSONArray rounds() {
        return log.getJSONArray(ROUNDS);
    }
}
