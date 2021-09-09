package com.codenjoy.dojo.services.controller.chat;

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
}