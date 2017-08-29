package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.DoubleDirection;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class ForcesTest {

    @Test
    public void testParse() {
        JSONObject json = new JSONObject("{region:{x:3, y:4}, direction:'left_down', count:2}");

        // when
        Forces forces = new Forces(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(DoubleDirection.LEFT_DOWN, forces.getDirection());
        assertEquals("[3,4]", forces.getRegion().toString());
    }

    @Test
    public void testParse_withoutDirection() {
        JSONObject json = new JSONObject("{region:{x:-1, y:-5}, count:2}");

        // when
        Forces forces = new Forces(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(DoubleDirection.NONE, forces.getDirection());
        assertEquals("[-1,-5]", forces.getRegion().toString());
    }

    @Test
    public void testParse_upperCaseDirection() {
        JSONObject json = new JSONObject("{region:{x:3, y:4}, direction:'LEFT_DOWN', count:2}");

        // when
        Forces forces = new Forces(json);

        // then
        assertEquals(2, forces.getCount());
        assertEquals(DoubleDirection.LEFT_DOWN, forces.getDirection());
        assertEquals("[3,4]", forces.getRegion().toString());
    }

}