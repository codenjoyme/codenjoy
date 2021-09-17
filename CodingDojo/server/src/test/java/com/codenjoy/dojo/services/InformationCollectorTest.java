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
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InformationCollectorTest {

    @AllArgsConstructor
    public class Event {
        Object data;

        @Override
        public String toString() {
            return String.format("Event[%s]", data)
                    .replace("\"", "'");
        }
    }
    
    private Scores scores;
    private InformationCollector info;

    public static class Scores implements PlayerScores {

        public static final String SCORE = "score";
        private Object score;

        public Scores(boolean isJson) {
            if (isJson) {
                score = new JSONObject();
                set(0);
            } else {
                score = 0;
            }
        }
        @Override
        public int clear() {
            if (score != null) {
                set(0);
            }

            return 0;
        }

        @Override
        public Object getScore() {
            return  (score instanceof JSONObject)
                    ? new JSONObject(){{
                        put(SCORE, Scores.this.get());
                    }}
                    : score;
        }

        @Override
        public void event(Object event) {
            Object data = ((Event) event).data;
            if (score == null) {
                score = data;
                return;
            }

            int value = InformationCollector.parse(data);
            int result = get() + value;
            result = Math.max(0, result);
            set(result);
        }

        @Override
        public void update(Object score) {
            // do nothing
        }

        public void set(int input) {
            if (score instanceof JSONObject) {
                ((JSONObject)score).put(SCORE, input);
            } else {
                score = input;
            }
        }

        public int get() {
            return InformationCollector.parse(score);
        }
    }

    public void event(int score) {
        info.event(new Event(score));
    }

    private void event(String message) {
        info.event(new CustomMessage(message));
    }

    public void jsonEvent(int score) {
        info.event(new Event(json(score)));
    }

    private void levelChanged(int level) {
        info.levelChanged(new LevelProgress(level + 1, level, level - 1));
    }

    @Test
    public void shouldFifo_caseInteger() {
        // given
        scores = new Scores(false);
        info = new InformationCollector(scores);

        // when
        event(14);
        event(11);
        event(13);
        event(12);
        levelChanged(4);

        // then
        assertEquals("+14, +11, +13, +12, Level 4", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals(
                "[Event[14], " +
                "Event[11], " +
                "Event[13], " +
                "Event[12], " +
                "Level 4]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldFifo_caseJson() {
        // given
        scores = new Scores(true);
        info = new InformationCollector(scores);

        // when
        jsonEvent(14);
        jsonEvent(11);
        jsonEvent(13);
        jsonEvent(12);
        levelChanged(4);

        // then
        assertEquals("+14, +11, +13, +12, Level 4",
                info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals(
                "[Event[{'score':14}], " +
                "Event[{'score':11}], " +
                "Event[{'score':13}], " +
                "Event[{'score':12}], " +
                "Level 4]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    private static JSONObject json(int score) {
        return new JSONObject(String.format("{'score':%s}", score));
    }

    @Test
    public void shouldFifo_butLevelChangesInfoAtEnd_caseInteger() {
        // given
        scores = new Scores(false);
        info = new InformationCollector(scores);

        // when
        event(13);
        levelChanged(4);
        event(11);
        event(12);

        // then
        assertEquals("+13, +11, +12, Level 4", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals("[Event[13], Level 4, Event[11], Event[12]]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldFifo_butLevelChangesInfoAtEnd_caseJson() {
        // given
        scores = new Scores(true);
        info = new InformationCollector(scores);

        // when
        jsonEvent(13);
        levelChanged(4);
        jsonEvent(11);
        jsonEvent(12);

        // then
        assertEquals("+13, +11, +12, Level 4", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals(
                "[Event[{'score':13}], " +
                "Level 4, " +
                "Event[{'score':11}], " +
                "Event[{'score':12}]]", info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldIgnoreZero_caseJson() {
        // given
        scores = new Scores(true);
        info = new InformationCollector(scores);
        scores.set(11);

        // when
        jsonEvent(0);

        // then
        assertEquals(null, info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals("[Event[{'score':0}]]", info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldIgnoreZero_caseInteger() {
        // given
        scores = new Scores(false);
        info = new InformationCollector(scores);
        scores.set(11);

        // when
        event(0);

        // then
        assertEquals(null, info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals("[Event[0]]", info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldNotLessThanZero_caseJson() {
        // given
        scores = new Scores(true);
        info = new InformationCollector(scores);
        scores.set(13);

        // when
        jsonEvent(-10);
        jsonEvent(-10);

        // then
        assertEquals("-10, -3", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals(
                "[Event[{'score':-10}], " +
                "Event[{'score':-10}]]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldNotLessThanZero_caseInteger() {
        // given
        scores = new Scores(false);
        info = new InformationCollector(scores);
        scores.set(13);

        // when
        event(-11);
        event(-11);

        // then
        assertEquals("-11, -2", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals(
                "[Event[-11], " +
                "Event[-11]]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }

    @Test
    public void shouldPrintCustomMessage() {
        // given
        scores = new Scores(false);
        info = new InformationCollector(scores);

        // when
        event("3");
        event("2");
        event("1");
        event("Fight!!!");

        // then
        assertEquals("3, 2, 1, Fight!!!", info.getMessage());
        assertEquals(null, info.getMessage());

        assertEquals("[[3], [2], [1], [Fight!!!]]",
                info.getAllMessages());
        assertEquals("[]", info.getAllMessages());
    }
}