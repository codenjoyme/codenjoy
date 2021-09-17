package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class InformationCollector implements EventListener, ChangeLevelListener, Information {

    private static final String LEVEL = "Level ";

    public static final Comparator<String> LEVEL_AT_LAST = (o1, o2) -> {
        if (o1.contains(LEVEL)) return 1;
        if (o2.contains(LEVEL)) return -1;
        return 0;
    };

    protected List<String> out = new LinkedList<>();
    private PlayerScores scores;
    private Collector all = new Collector();

    public InformationCollector(PlayerScores scores) {
        this.scores = scores;
    }

    @Override
    public void event(Object event) {
        all.put(event.toString());

        if (event instanceof CustomMessage) {
            out.add(((CustomMessage) event).getMessage());
        } else {
            Object before = scores.getScore();
            scores.event(event);
            add(before);
        }
    }

    private void add(Object before) {
        int delta = delta(scores.getScore(), before);
        if (delta != 0) {
            out.add(showSign(delta));
        }
    }

    private int delta(Object score, Object before) {
        return parse(score) - parse(before);
    }

    public static int parse(Object score) {
        if (score instanceof Integer) {
            return (Integer)score;
        } else if (score instanceof JSONObject) {
            return ((JSONObject)score).getInt("score");
        }
        throw new UnsupportedOperationException("Unknown type: " + score.getClass());
    }

    private String showSign(int integer) {
        if (integer > 0) {
            return "+" + integer;
        } else {
            return "" + integer;
        }
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

    public List<String> all() {
        return out;
    }

    @Override
    public void levelChanged(LevelProgress progress) {
        out.add(LEVEL + progress.getCurrent());
    }

    public void setInfo(String information) {
        out.clear();
        out.add(information);
    }

    public String popLastMessages() {
        return all.popAll();
    }
}
