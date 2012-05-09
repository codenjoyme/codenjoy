package net.tetris.services;

import com.jayway.restassured.path.json.JsonPath;
import net.tetris.web.controller.UpdateRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 7:48 PM
 */
public class ScreenSenderTest {

    private ScreenSender sender;

    @Before
    public void setUp() throws Exception {
        sender = new ScreenSender();
    }

    @Test
    public void shouldSendUpdateWhenOnePlayerRequested() throws UnsupportedEncodingException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        sender.scheduleUpdate(new UpdateRequest(new MockAsyncContext(response), Collections.singleton("vasya")));
        HashMap<Player, List<Plot>> playersScreens = new HashMap<>();
        playersScreens.put(new Player("vasya", ""), Arrays.asList(new Plot(1, 2, PlotColor.BLUE)));

        sender.sendUpdates(playersScreens);

        JsonPath jsonPath = from(response.getContentAsString());
        assertEquals(1, jsonPath.getInt("vasya.blue[0][0]"));
        assertEquals(2, jsonPath.getInt("vasya.blue[0][1]"));
    }

    @Test
    @Ignore
    public void shouldCompleteResponseWhenExceptionOnWrite() {

    }

    @Test
    @Ignore
    public void shouldSendOtherUpdatesWhenExceptionOnWrite() {

    }

    @Test
    @Ignore
    public void shouldCompleteResponseWhenDone() {

    }

    private class MockAsyncContext implements AsyncContext {
        private MockHttpServletResponse response;

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
            //To change body of implemented methods use File | Settings | File Templates.
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
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getTimeout() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
