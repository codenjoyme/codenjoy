package com.epam.dojo.expansion.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.PlayerScores;
import org.json.JSONArray;
import org.json.JSONObject;

public class Scores implements PlayerScores {

    private final static String SCORE = "score";
    public static final String ROUNDS = "rounds";

    private volatile int current;
    private JSONObject log;

    public Scores(Object startScore) {
        if (startScore instanceof Integer) {
            this.current = (Integer)startScore;
        } else if (startScore instanceof String) {
            JSONObject object = new JSONObject((String) startScore);
            this.current = object.getInt(SCORE);
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
        return current = 0;
    }

    @Override
    public JSONObject getScore() {
        log.put(SCORE, current);
        return log;
    }
    @Override
    public void event(Object input) {
        Events events = (Events)input;

        int score = events.getScore();
        rounds().put(score);

        if (events.getType() == Events.Type.WIN) {
            current += score;
        } else if (events.getType() == Events.Type.LOOSE) {
            current -= score;
        }
        current = Math.max(0, current);
    }

    private JSONArray rounds() {
        return log.getJSONArray(ROUNDS);
    }
}
