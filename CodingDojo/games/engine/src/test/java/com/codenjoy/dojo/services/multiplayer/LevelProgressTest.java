package com.codenjoy.dojo.services.multiplayer;

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

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class LevelProgressTest {

    @Test
    public void constructFromInts_valid() {
        // given
        int total = 5;
        int current = 2;
        int passed = 1;

        // when
        LevelProgress progress = new LevelProgress(total, current, passed);

        // then
        assertEquals("{'current':2,'passed':1,'total':5,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromInts_invalid_totalLessThanCurrent() {
        // given
        int total = 2; // BUG
        int current = 5;
        int passed = 1;

        // when
        LevelProgress progress = new LevelProgress(total, current, passed);

        // then
        assertEquals("{'current':5,'passed':1,'total':2,'valid':false}",
                progress.toString());
    }

    @Test
    public void constructFromInts_invalid_currentMoreThanPassedPlus1() {
        // given
        int total = 5;
        int current = 2;
        int passed = 0; // BUG

        // when
        LevelProgress progress = new LevelProgress(total, current, passed);

        // then
        assertEquals("{'current':2,'passed':0,'total':5,'valid':false}",
                progress.toString());
    }

    @Test
    public void constructFromInts_invalid_passedMoreThanTotal() {
        // given
        int total = 5;
        int current = 2;
        int passed = 6; // BUG

        // when
        LevelProgress progress = new LevelProgress(total, current, passed);

        // then
        assertEquals("{'current':2,'passed':6,'total':5,'valid':false}",
                progress.toString());
    }

    @Test
    public void constructFromInts_valid_totalCanBeEqualsWithCurrent_thisIsLasMultipleLevelForTraining() {
        // given
        int total = 2;
        int current = total;
        int passed = 1;

        // when
        LevelProgress progress = new LevelProgress(total, current, passed);

        // then
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromType_anyOtherType() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        // when
        LevelProgress progress = new LevelProgress(type);

        // then
        assertEquals("{'current':0,'passed':-1,'total':1,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromType_trainingType() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(5);

        // when
        LevelProgress progress = new LevelProgress(type);

        // then
        assertEquals("{'current':0,'passed':-1,'total':5,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromJSON_validType() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':1,'lastPassed':0}}");

        // when
        LevelProgress progress = new LevelProgress(json);

        // then
        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromJSON_invalidJsonData() {
        // given
        JSONObject json = new JSONObject("{'bad':'data'}");

        // when
        try {
            new LevelProgress(json);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("Bad progress json format: " +
                    "JSONObject[\"levelProgress\"] not found.: " +
                    "{\"bad\":\"data\"}", e.getMessage());
        }
    }

    @Test
    public void constructFromJSON_invalidValue() {
        // given
        int bug = 150;
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':" + bug + ",'lastPassed':0}}");

        // when
        try {
            new LevelProgress(json);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("Progress is invalid: {'current':150,'passed':0,'total':3,'valid':false}", e.getMessage());
        }
    }

    @Test
    public void constructFromJSONString_validType() {
        // given
        String json = "{'levelProgress':{'total':3,'current':1,'lastPassed':0}}";

        // when
        LevelProgress progress = LevelProgress.parse(json);

        // then
        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                progress.toString());
    }

    @Test
    public void constructFromJSONString_invalidJson() {
        // given
        String json = "bad_data";

        // when
        try {
            LevelProgress.parse(json);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("Bad progress json format: " +
                    "A JSONObject text must begin with '{' at 1 [character 2 line 1]: " +
                    "bad_data", e.getMessage());
        }
    }

    @Test
    public void constructFromJSONString_invalidJsonData() {
        // given
        String json = "{'bad':'data'}";

        // when
        try {
            LevelProgress.parse(json);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("Bad progress json format: " +
                    "JSONObject[\"levelProgress\"] not found.: " +
                    "{\"bad\":\"data\"}", e.getMessage());
        }
    }

    @Test
    public void constructFromJSONString_invalidValue() {
        // given
        int bug = 150;
        String json = "{'levelProgress':{'total':3,'current':" + bug + ",'lastPassed':0}}";

        // when
        try {
            LevelProgress.parse(json);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("Progress is invalid: {'current':150,'passed':0,'total':3,'valid':false}", e.getMessage());
        }
    }

    @Test
    public void saveToJSON() {
        // given
        LevelProgress progress = new LevelProgress(5, 2, 1);
        JSONObject json = new JSONObject("{'some':'data'}");

        // when
        json = progress.saveTo(json);

        // then
        assertEquals("{\"some\":\"data\",\"levelProgress\":{\"total\":5,\"current\":2,\"lastPassed\":1}}",
                json.toString());
    }

    @Test
    public void change_levelAndPassed() {
        // given
        LevelProgress progress = new LevelProgress(6, 2, 1);

        // when
        progress.change(4, 5);

        // then
        assertEquals("{'current':4,'passed':5,'total':6,'valid':true}",
                progress.toString());
    }

    @Test
    public void change_levelAndPassed_withoutValidation() {
        // given
        LevelProgress progress = new LevelProgress(6, 2, 1);

        // when
        progress.change(40, 50); // bug

        // then
        assertEquals("{'current':40,'passed':50,'total':6,'valid':false}",
                progress.toString());
    }

    @Test
    public void change_onlyLevel() {
        // given
        LevelProgress progress = new LevelProgress(5, 1, 4);

        // when
        progress.change(3);
        assertEquals(true, progress.canChange(3));

        // then
        assertEquals("{'current':3,'passed':4,'total':5,'valid':true}",
                progress.toString());
    }

    @Test
    public void change_onlyLevel_withoutValidation() {
        // given
        LevelProgress progress = new LevelProgress(5, 1, 4);

        // when
        assertEquals(false, progress.canChange(30));
        progress.change(30); // bug


        // then
        assertEquals("{'current':30,'passed':4,'total':5,'valid':false}",
                progress.toString());
    }

    @Test
    public void nextLevel_skip_currentWillBeMoreThanPassed() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':2,'lastPassed':0}}");

        // when
        try {
            LevelProgress.winLevel(json);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Progress is invalid: {'current':2,'passed':0,'total':3,'valid':false}", e.getMessage());
        }
    }

    @Test
    public void nextLevel_ok_currentCanBeEqualsTotal() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':2,'lastPassed':2}}");

        // when
        JSONObject updated = LevelProgress.winLevel(json);

        // then
        assertEquals("{\"levelProgress\":{\"total\":3,\"current\":2,\"lastPassed\":2}}", json.toString());
        assertEquals("{\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}", updated.toString());
    }

    @Test
    public void nextLevel_skip_currentMoreThanTotal() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':2,'current':2,'lastPassed':2}}");

        // when
        JSONObject updated = LevelProgress.winLevel(json);

        // then
        assertEquals("{\"levelProgress\":{\"total\":2,\"current\":2,\"lastPassed\":2}}", json.toString());
        assertEquals(null, updated);
    }

    @Test
    public void nextLevel_ok_dontChangeLastPassed() {
        // given
        JSONObject json = new JSONObject("{'a':'data','levelProgress':{'total':4,'current':2,'lastPassed':3}}");

        // when
        JSONObject updated = LevelProgress.winLevel(json);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":2,\"lastPassed\":3}}", json.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":3,\"lastPassed\":3}}", updated.toString());

        // when
        JSONObject updated2 = LevelProgress.winLevel(updated);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":3,\"lastPassed\":3}}", updated.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":4,\"lastPassed\":3}}", updated2.toString());

        // when
        JSONObject updated3 = LevelProgress.winLevel(updated2);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":4,\"lastPassed\":3}}", updated2.toString());
        assertEquals(null, updated3);
    }

    @Test
    public void nextLevel_ok_increaseLassPassedWithCurrent() {
        // given
        JSONObject json = new JSONObject("{'a':'data','levelProgress':{'total':4,'current':2,'lastPassed':1}}");

        // when
        JSONObject updated = LevelProgress.winLevel(json);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":2,\"lastPassed\":1}}", json.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":3,\"lastPassed\":2}}", updated.toString());

        // when
        JSONObject updated2 = LevelProgress.winLevel(updated);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":3,\"lastPassed\":2}}", updated.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":4,\"lastPassed\":3}}", updated2.toString());

        // when
        JSONObject updated3 = LevelProgress.winLevel(updated2);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":4,\"current\":4,\"lastPassed\":3}}", updated2.toString());
        assertEquals(null, updated3);
    }
}
