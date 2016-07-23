package net.tetris.online.service;

import com.jayway.restassured.path.json.JsonPath;
import net.tetris.services.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 7:48 PM
 */
public class RestDataSenderTest {

    private RestDataSender sender;
    private MockHttpServletResponse response;
    private MockAsyncContext asyncContext;
    private ScheduledExecutorService restSenderExecutorService = new ScheduledThreadPoolExecutor(10);
    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        sender = new RestDataSender(restSenderExecutorService);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        asyncContext = new MockAsyncContext(request, response);
    }

    @Test
    public void shouldSendUpdateWhenOnePlayerRequested() throws UnsupportedEncodingException, InterruptedException {
        sender.scheduleGameProgressRequest(progressRequest("123", "petya"));

        sendProgressAndDown("petya", "123", 50);

        assertProgress(response.getContentAsString(), 50);
    }

    @Test
    public void shouldCompleteResponseWhenExceptionOnWrite() throws IOException, InterruptedException {
        response.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        sender.scheduleGameProgressRequest(progressRequest("345", "petya"));

        sendProgressAndDown("petya", "345", 22);

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSkipSendingWhenAnotherPlayer() throws UnsupportedEncodingException, InterruptedException {
        sender.scheduleGameProgressRequest(progressRequest("123", "vasya"));

        sendProgressAndDown("petya", "123", 11);

        assertFalse(asyncContext.isComplete());
        assertTrue(StringUtils.isBlank(response.getContentAsString()));
    }

    @Test
    public void shouldSkipSendingWhenAnotherTimestamp() throws UnsupportedEncodingException, InterruptedException {
        sender.scheduleGameProgressRequest(progressRequest("123", "vasya"));

        sendProgressAndDown("vasya", "321", 11);

        assertFalse(asyncContext.isComplete());
        assertTrue(StringUtils.isBlank(response.getContentAsString()));
    }

    @Test
    public void shouldRemoveRequestWhenResponseSent() throws UnsupportedEncodingException, InterruptedException {
        sender.scheduleGameProgressRequest(progressRequest("123", "vasya"));
        sender.sendProgressFor("vasya", "123", 11);

        sendProgressAndDown("vasya", "123", 11);
        assertNull(asyncContext.getError());
    }

    @Test
    public void shouldNotClearRequestsForOtherTimestamps() throws UnsupportedEncodingException, InterruptedException {
        sender.scheduleGameProgressRequest(progressRequest("321", "vasya"));
        sender.sendProgressFor("vasya", "123", 11);

        sendProgressAndDown("vasya", "321", 11);

        assertTrue(asyncContext.isComplete());
    }

    private void sendProgressAndDown(String playerName, String timestamp, int progress) throws InterruptedException {
        sender.sendProgressFor(playerName, timestamp, progress);
        shutDownExecutor();
    }

    @Test
    public void shouldHandleRuntimeExceptionsWhenSending() throws InterruptedException {
        response.setWriterAccessAllowed(false);
        sender.scheduleGameProgressRequest(progressRequest("345", "petya"));

        sendProgressAndDown("petya", "345", 22);
        assertTrue(asyncContext.isComplete());

    }

    private void shutDownExecutor() throws InterruptedException {
        restSenderExecutorService.shutdown();
        assertTrue(restSenderExecutorService.awaitTermination(1, TimeUnit.SECONDS));
    }

    private void loginUser(String playerName) {
        request.setAttribute(SecurityFilter.LOGGED_USER, playerName);
    }

    private ProgressRequest progressRequest(String timestamp, String playerName) {
        return new ProgressRequest(asyncContext, playerName, timestamp);
    }

    private void assertProgress(String responseContent, int expectedProgress) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedProgress, jsonPath.getInt("progress"));
    }


/*
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
        sender.scheduleUpdate(new PlayerScreenUpdateRequest(exceptionContext, "exception"));
        sender.scheduleUpdate(updateRequestFor("petya"));


        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.BLUE))
                        .addScreenFor("exception", 123, plot(1, 3, PlotColor.BLUE)).asMap());

        assertProgress(response.getContentAsString(), "petya", "blue", 2, 3);
        assertTrue(exceptionContext.isComplete());
    }

    @Test
    public void shouldRemoveRequestWhenProcessed() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));
        sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.BLUE)).asMap());
        response.setWriterAccessAllowed(false);

        try {
            sender.sendUpdates(screenFor("vasya", plot(1, 2, PlotColor.BLUE)).asMap());
        } catch (Exception e) {
            fail("Should send only once");
        }
    }

    private void assertProgress(String responseContent, String playerName, String color, int expectedX, int expectedY) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedX, jsonPath.getInt(playerName + ".plots." + color + "[0][0]"));
        assertEquals(expectedY, jsonPath.getInt(playerName + ".plots." + color + "[0][1]"));
    }

    @Test
    public void shouldSendToRequestedPlayersOnly() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.BLUE))
                        .addScreenFor("vasya", 123, plot(1, 3, PlotColor.BLUE)).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertNull("Should contain only requested user screens", jsonPath.get("petya"));
    }

    @Test
    public void shouldIgnoreNonRequestedPlayerData() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, PlotColor.BLUE)).asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendUpdateForSeveralRequestedPlayers() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya", "petya"));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, PlotColor.CYAN)).
                        addScreenFor("vasya", 123, plot(1, 2, PlotColor.RED)).asMap());

        assertProgress(response.getContentAsString(), "vasya", "red", 1, 2);
        assertProgress(response.getContentAsString(), "petya", "cyan", 3, 4);
    }

    @Test
    public void shouldSendUpdateForAllPlayersWhenRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, true, null));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, PlotColor.CYAN)).
                        addScreenFor("vasya", 123, plot(1, 2, PlotColor.RED)).asMap());

        assertProgress(response.getContentAsString(), "vasya", "red", 1, 2);
        assertProgress(response.getContentAsString(), "petya", "cyan", 3, 4);
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

    private PlayerScreenUpdateRequest updateRequestFor(String... playerName) {
        return new PlayerScreenUpdateRequest(asyncContext, false, new HashSet<>(Arrays.asList(playerName)));
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
        private Map<Player, PlayerData> map;

        public Screen(String playerName, int score, Plot... plots) {
            this.map = new HashMap<>();
            addScreenFor(playerName, score, plots);
        }

        public Screen addScreenFor(String playerName, int score, Plot... plots) {
            map.put(new Player(playerName, "", new TetrisPlayerScores(0), TestUtils.emptyLevels()),
                    new PlayerData(Arrays.asList(plots), score,
                            345, "", 7)); // dummy values
            return this;
        }

        public Map<Player, PlayerData> asMap() {
            return map;
        }

    }
*/
}
