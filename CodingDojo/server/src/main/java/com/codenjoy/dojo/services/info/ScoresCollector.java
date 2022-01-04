package com.codenjoy.dojo.services.info;

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


import com.codenjoy.dojo.services.CustomMessage;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import org.json.JSONObject;

import java.util.Comparator;

import static java.util.stream.Collectors.joining;

public class ScoresCollector extends EventsCollector {

    public static final Comparator<String> LEVEL_AT_LAST = (o1, o2) -> {
        if (o1.contains(LEVEL)) return 1;
        if (o2.contains(LEVEL)) return -1;
        return 0;
    };

    private PlayerScores scores;
    private Collector out = new Collector();

    public ScoresCollector(String playerId, PlayerScores scores) {
        super(playerId);
        this.scores = scores;
    }

    @Override
    public void event(Object event) {
        if (event instanceof CustomMessage) {
            put(((CustomMessage) event).getMessage());
        } else {
            Object before = scores.getScore();
            scores.event(event);
            Object after = scores.getScore();

            int delta = delta(after, before);
            String message = showSign(delta);
            super.put(event.toString() + " => " + message);

            if (delta != 0) {
                out.put(message);
            }
        }
    }

    private int delta(Object score, Object before) {
        return parse(score) - parse(before);
    }

    public static int parse(Object score) {
        if (score instanceof Integer) {
            return (Integer)score;
        }

        if (score instanceof JSONObject) {
            return ((JSONObject)score).getInt("score");
        }

        throw new UnsupportedOperationException("Unknown type: " + score.getClass());
    }

    private String showSign(int value) {
        String sign = value > 0 ? "+" : "";
        return sign + value;
    }

    @Override
    public String getMessage() {
        if (out.isEmpty()) {
            return null;
        }
        String result = out.stream()
                .sorted(LEVEL_AT_LAST)
                .collect(joining(", "));
        out.clear();
        return result;
    }

    @Override
    public String toString() {
        return out.toString();
    }

    @Override
    public void levelChanged(LevelProgress progress) {
        put(LEVEL + progress.getCurrent());
    }

    @Override
    public void put(String message) {
        super.put(message);
        out.put(message);
    }
}