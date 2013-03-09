package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Information;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.ScreenSender;
import com.codenjoy.dojo.snake.model.SnakePlayerScores;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.snake.services.playerdata.PlotColor;
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

        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.HEAD)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "head", 1, 2);
    }

    @Test
    public void shouldCompleteResponseWhenExceptionOnWrite() throws IOException {
        response.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.HEAD)).asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendOtherUpdatesWhenExceptionOnWrite() throws UnsupportedEncodingException {
        MockHttpServletResponse exceptionResponse = new MockHttpServletResponse();
        exceptionResponse.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        MockAsyncContext exceptionContext = new MockAsyncContext(exceptionResponse);
        sender.scheduleUpdate(new UpdateRequest(exceptionContext, "exception"));
        sender.scheduleUpdate(updateRequestFor("petya"));


        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.HEAD))
                        .addScreenFor("exception", 123, plot(1, 3, PlotColor.HEAD)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "head", 2, 3);
        assertTrue(exceptionContext.isComplete());
    }

    @Test
    public void shouldRemoveRequestWhenProcessed() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));
        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.HEAD)).asMap());
        response.setWriterAccessAllowed(false);

        try {
            sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.HEAD)).asMap());
        } catch (Exception e) {
            fail("Should send only once");
        }
    }

    private void assertContainsPlayerCoordinates(String responseContent, String playerName, String color, int expectedX, int expectedY) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedX, jsonPath.getInt(playerName + ".plots." + color + "[0][0]"));
        assertEquals(expectedY, jsonPath.getInt(playerName + ".plots." + color + "[0][1]"));
    }

    @Test
    public void shouldSendToRequestedPlayersOnly() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.HEAD))
                        .addScreenFor("vasya", 123, plot(1, 3, PlotColor.HEAD)).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertNull("Should contain only requested user screens", jsonPath.get("petya"));
    }

    @Test
    public void shouldIgnoreNonRequestedPlayerData() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.HEAD)).asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendUpdateForSeveralRequestedPlayers() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya", "petya"));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, PlotColor.HEAD)).
                        addScreenFor("vasya", 123, plot(1, 2, PlotColor.STONE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "stone", 1, 2);
        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "head", 3, 4);
    }

    @Test
    public void shouldSendUpdateForAllPlayersWhenRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(new UpdateRequest(asyncContext, true, null));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, PlotColor.HEAD)).
                        addScreenFor("vasya", 123, plot(1, 2, PlotColor.STONE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "stone", 1, 2);
        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "head", 3, 4);
    }

    @Test
    public void shouldSendScores() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", 345).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertEquals(345, jsonPath.getInt("vasya.score"));
    }

    private Plot plot(int x, int y, PlotColor color) {
        return new Plot(x, y, color);
    }

    private UpdateRequest updateRequestFor(String... playerName) {
        return new UpdateRequest(asyncContext, false, new HashSet<String>(Arrays.asList(playerName)));
    }

    private Screen screenFor(String playerName, Plot... plots) {
        return new Screen(playerName, 123, plots);
    }

    private Screen screenFor(String playerName, int score, Plot... plots) {
        return new Screen(playerName, score, plots);
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

        public Screen(String playerName, int score, Plot... plots) {
            this.map = new HashMap<Player, PlayerData>();
            addScreenFor(playerName, score, plots);
        }

        public Screen addScreenFor(String playerName, int score, Plot... plots) {
            Information info = mock(Information.class);
            map.put(new Player(playerName, "", new SnakePlayerScores(0), info),
                    new PlayerData(
                            10, Arrays.asList(plots), score, 8, 7, 9, "info")); // 8 & 7 & 10 & "info" - dummy values
            return this;
        }

        public Map<Player, PlayerData> asMap() {
            return map;
        }

    }
}
