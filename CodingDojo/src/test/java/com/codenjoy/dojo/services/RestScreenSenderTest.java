package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.web.controller.UpdateRequest;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 7:48 PM
 */
public class RestScreenSenderTest {

    private ScreenSender sender;
    private MockHttpServletResponse response;
    private RestScreenSenderTest.MockAsyncContext asyncContext;
    private ScheduledExecutorService restSenderExecutorService = new ScheduledThreadPoolExecutor(10);

    @Before
    public void setUp() throws Exception {
        sender = new RestScreenSender(restSenderExecutorService);
        response = new MockHttpServletResponse();
        asyncContext = new MockAsyncContext(response);
    }

    @Test
    public void shouldSendUpdateWhenOnePlayerRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", "vasyaboard").asMap());

        assertContainsPlayerBoard(response.getContentAsString(), "vasya", "vasyaboard");
    }

    @Test
    public void shouldCompleteResponseWhenExceptionOnWrite() throws IOException {
        response.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", "vasyaboard").asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendOtherUpdatesWhenExceptionOnWrite() throws UnsupportedEncodingException {
        MockHttpServletResponse exceptionResponse = new MockHttpServletResponse();
        exceptionResponse.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        MockAsyncContext exceptionContext = new MockAsyncContext(exceptionResponse);
        sender.scheduleUpdate(new UpdateRequest(exceptionContext, "exception"));
        sender.scheduleUpdate(updateRequestFor("petya"));


        sender.sendUpdates(screenFor("petya", "petyaboard")
                        .addScreenFor("exception", 123, "exceptionboard").asMap());

        assertContainsPlayerBoard(response.getContentAsString(), "petya", "petyaboard");
        assertTrue(exceptionContext.isComplete());
    }

    @Test
    public void shouldRemoveRequestWhenProcessed() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));
        sender.sendUpdates(screenFor("vasya", "vasyaboard").asMap());
        response.setWriterAccessAllowed(false);

        try {
            sender.sendUpdates(screenFor("vasya", "vasyaboard").asMap());
        } catch (Exception e) {
            fail("Should send only once");
        }
    }

    private void assertContainsPlayerBoard(String responseContent, String playerName, String expectedBoard) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedBoard, jsonPath.getString(playerName + ".board"));
    }

    @Test
    public void shouldSendToRequestedPlayersOnly() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("petya", "petyaboard")
                        .addScreenFor("vasya", 123, "vasyaboard").asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertNull("Should contain only requested user screens", jsonPath.get("petyaboard"));
    }

    @Test
    public void shouldIgnoreNonRequestedPlayerData() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("petya", "petyaboard").asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendUpdateForSeveralRequestedPlayers() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya", "petya"));

        sender.sendUpdates(screenFor("petya", "petyaboard").
                        addScreenFor("vasya", 123, "vasyaboard").asMap());

        assertContainsPlayerBoard(response.getContentAsString(), "vasya", "vasyaboard");
        assertContainsPlayerBoard(response.getContentAsString(), "petya", "petyaboard");
    }

    @Test
    public void shouldSendUpdateForAllPlayersWhenRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(new UpdateRequest(asyncContext, true, null));

        sender.sendUpdates(screenFor("petya", "petyaboard").
                        addScreenFor("vasya", 123, "vasyaboard").asMap());

        assertContainsPlayerBoard(response.getContentAsString(), "vasya", "vasyaboard");
        assertContainsPlayerBoard(response.getContentAsString(), "petya", "petyaboard");
    }

    @Test
    public void shouldSendScores() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", 345, "vasyaboard").asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertEquals(345, jsonPath.getInt("vasya.score"));
    }

    private Plot plot(int x, int y, String color) {
        return new Plot(x, y, color);
    }

    private UpdateRequest updateRequestFor(String... playerName) {
        return new UpdateRequest(asyncContext, false, new HashSet<String>(Arrays.asList(playerName)));
    }

    private Screen screenFor(String playerName, String board) {
        return new Screen(playerName, 123, board);
    }

    private Screen screenFor(String playerName, int score, String board) {
        return new Screen(playerName, score, board);
    }

    private class MockAsyncContext implements AsyncContext {
        private MockHttpServletResponse response;
        private boolean completed;

        public MockAsyncContext(MockHttpServletResponse response) {
            this.response = response;
        }

        @Override
        public ServletRequest getRequest() {
            return null;
        }

        @Override
        public ServletResponse getResponse() {
            return response;
        }

        @Override
        public boolean hasOriginalRequestAndResponse() {
            return false;  
        }

        @Override
        public void dispatch() {
            
        }

        @Override
        public void dispatch(String path) {
            
        }

        @Override
        public void dispatch(ServletContext context, String path) {
            
        }

        @Override
        public void complete() {
            completed = true;
        }

        @Override
        public void start(Runnable run) {
            
        }

        @Override
        public void addListener(AsyncListener listener) {
            
        }

        @Override
        public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
            
        }

        @Override
        public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
            return null;  
        }

        @Override
        public void setTimeout(long timeout) {
        }

        @Override
        public long getTimeout() {
            return 0;
        }

        public boolean isComplete() {
            return completed;
        }
    }

    private class Screen {
        private Map<Player, PlayerData> map;

        public Screen(String playerName, int score, String board) {
            this.map = new HashMap<Player, PlayerData>();
            addScreenFor(playerName, score, board);
        }

        public Screen addScreenFor(String playerName, int score, String board) {
            Information info = mock(Information.class);
            map.put(new Player(playerName, "", null, info, null),
                    new PlayerData(10, board, score, 8, 7, 9, "info")); // 8 & 7 & 10 & "info" - dummy values
            return this;
        }

        public Map<Player, PlayerData> asMap() {
            return map;
        }

    }
}
