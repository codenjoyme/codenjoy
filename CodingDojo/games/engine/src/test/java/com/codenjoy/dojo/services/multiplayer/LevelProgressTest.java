package com.codenjoy.dojo.services.multiplayer;

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
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':1,'lastPassed':0}}");

        // when
        JSONObject updated = LevelProgress.nextLevel(json);

        // then
        String same = "{\"levelProgress\":{\"total\":3,\"current\":1,\"lastPassed\":0}}";
        assertEquals(same, json.toString());
        assertEquals(same, updated.toString());
    }

    @Test
    public void nextLevel_ok_currentCanBeEaualsTotal() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':3,'current':2,'lastPassed':2}}");

        // when
        JSONObject updated = LevelProgress.nextLevel(json);

        // then
        assertEquals("{\"levelProgress\":{\"total\":3,\"current\":2,\"lastPassed\":2}}", json.toString());
        assertEquals("{\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}", updated.toString());
    }

    @Test
    public void nextLevel_skip_currentMoreThanTotal() {
        // given
        JSONObject json = new JSONObject("{'levelProgress':{'total':2,'current':2,'lastPassed':2}}");

        // when
        JSONObject updated = LevelProgress.nextLevel(json);

        // then
        String same = "{\"levelProgress\":{\"total\":2,\"current\":2,\"lastPassed\":2}}";
        assertEquals(same, json.toString());
        assertEquals(same, updated.toString());
    }

    @Test
    public void nextLevel_ok() {
        // given
        JSONObject json = new JSONObject("{'a':'data','levelProgress':{'total':5,'current':2,'lastPassed':3}}");

        // when
        JSONObject updated = LevelProgress.nextLevel(json);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":5,\"current\":2,\"lastPassed\":3}}", json.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":5,\"current\":3,\"lastPassed\":3}}", updated.toString());

        // when
        JSONObject updated2 = LevelProgress.nextLevel(updated);

        // then
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":5,\"current\":3,\"lastPassed\":3}}", updated.toString());
        assertEquals("{\"a\":\"data\",\"levelProgress\":{\"total\":5,\"current\":4,\"lastPassed\":3}}", updated2.toString());

        // when
        JSONObject updated3 = LevelProgress.nextLevel(updated2);

        // then
        String same = "{\"a\":\"data\",\"levelProgress\":{\"total\":5,\"current\":4,\"lastPassed\":3}}";
        assertEquals(same, updated2.toString());
        assertEquals(same, updated3.toString());
    }
}