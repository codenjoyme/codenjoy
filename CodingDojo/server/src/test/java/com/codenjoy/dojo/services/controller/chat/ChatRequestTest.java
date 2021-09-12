package com.codenjoy.dojo.services.controller.chat;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Test;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

public class ChatRequestTest {

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void testAllProperties() {
        ChatRequest request = new ChatRequest("{'command':'getAllTopic', " +
                "'data':{'id':12, 'room':'room2', 'count':3, " +
                "'afterId':4, 'beforeId':6, 'inclusive':true}}");

        assertEquals(3, request.count());
        assertEquals(4, request.afterId());
        assertEquals(6, request.beforeId());
        assertEquals(12, request.id());
        assertEquals("room2", request.room());
        assertEquals(true, request.inclusive());
        assertEquals("getAllTopic", request.method());
        assertEquals("Filter(room=room2, count=3, afterId=4, beforeId=6, inclusive=true)",
                request.filter().toString());
    }

    @Test
    public void testPropertiesNotSet() {
        ChatRequest request = new ChatRequest("{'command':'getAllTopic', " +
                "'data':{}}");

        assertEquals(null, request.count());
        assertEquals(null, request.afterId());
        assertEquals(null, request.beforeId());
        assertEquals(null, request.id());
        assertEquals(null, request.room());
        assertEquals(null, request.inclusive());
        assertEquals("getAllTopic", request.method());
        assertEquals("Filter(room=null, count=null, afterId=null, beforeId=null, inclusive=null)",
                request.filter().toString());
    }

    @Test
    public void testPropertiesIsNull() {
        ChatRequest request = new ChatRequest("{'command':'getAllTopic', " +
                "'data':{'id':null, 'room':null, 'count':null, " +
                "'afterId':null, 'beforeId':null, 'inclusive':null}}");

        assertEquals(null, request.count());
        assertEquals(null, request.afterId());
        assertEquals(null, request.beforeId());
        assertEquals(null, request.id());
        assertEquals(null, request.room());
        assertEquals(null, request.inclusive());
        assertEquals("getAllTopic", request.method());
        assertEquals("Filter(room=null, count=null, afterId=null, beforeId=null, inclusive=null)",
                request.filter().toString());
    }
}
