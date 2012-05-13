package net.tetris.services;

import com.jayway.restassured.path.json.JsonPath;
import net.tetris.web.controller.UpdateRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 7:48 PM
 */
public class RestScreenSenderTest {

    private ScreenSender sender;
    private MockHttpServletResponse response;
    private RestScreenSenderTest.MockAsyncContext asyncContext;

    @Before
    public void setUp() throws Exception {
        sender = new RestScreenSender();
        response = new MockHttpServletResponse();
        asyncContext = new MockAsyncContext(response);
    }

    @Test
    public void shouldSendUpdateWhenOnePlayerRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.BLUE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "blue", 1, 2);
    }

    @Test
    public void shouldCompleteResponseWhenExceptionOnWrite() throws IOException {
        response.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.BLUE)).asMap());

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
                screenFor("petya", plot(2, 3, PlotColor.BLUE))
                        .addScreenFor("exception", plot(1, 3, PlotColor.BLUE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "blue", 2, 3);
        assertTrue(exceptionContext.isComplete());
    }

    private void assertContainsPlayerCoordinates(String responseContent, String playerName, String color, int expectedX, int expectedY) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedX, jsonPath.getInt(playerName + "." + color + "[0][0]"));
        assertEquals(expectedY, jsonPath.getInt(playerName + "." + color + "[0][1]"));
    }

    @Test
    public void shouldSendToRequestedPlayersOnly() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.BLUE))
                        .addScreenFor("vasya", plot(1, 3, PlotColor.BLUE)).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertNull("Should contain only requested user screens", jsonPath.get("petya"));
    }

    @Test
    public void shouldIgnoreNonRequestedPlayerData() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.BLUE)).asMap());

        assertFalse(asyncContext.isComplete());
    }

    @Test
    public void shouldSendUpdateForSeveralRequestedPlayers() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya", "petya"));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, PlotColor.CYAN)).
                        addScreenFor("vasya", plot(1, 2, PlotColor.RED)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "red", 1, 2);
        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "cyan", 3, 4);

    }

    private Plot plot(int x, int y, PlotColor color) {
        return new Plot(x, y, color);
    }

    private UpdateRequest updateRequestFor(String... playerName) {
        return new UpdateRequest(asyncContext, new HashSet<>(Arrays.asList(playerName)));
    }

    private Screen screenFor(String playerName, Plot... plots) {
        return new Screen(playerName, plots);
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
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void dispatch() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void dispatch(String path) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void dispatch(ServletContext context, String path) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void complete() {
            completed = true;
        }

        @Override
        public void start(Runnable run) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void addListener(AsyncListener listener) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        private Map<Player, List<Plot>> map;

        public Screen(String playerName, Plot... plots) {
            this.map = new HashMap<Player, List<Plot>>();
            addScreenFor(playerName, plots);
        }

        public Screen addScreenFor(String playerName, Plot... plots) {
            map.put(new Player(playerName, ""), Arrays.asList(plots));
            return this;
        }

        public Map<Player, List<Plot>> asMap() {
            return map;
        }

    }
}
