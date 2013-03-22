package com.codenjoy.dojo.transport.http;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import net.tetris.dom.Figure;
import net.tetris.services.Plot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * User: serhiy.zelenin
 * Date: 3/22/13
 * Time: 5:44 PM
 */
@ContextConfiguration(locations = "classpath:/com/codenjoy/dojo/transport/http/httpTransportContext.xml")

@RunWith(SpringJUnit4ClassRunner.class)
public class HttpPlayerTransportTest {

    private FakeHttpServer server;

    @Autowired
    @Qualifier("httpPlayerTransport")
    private HttpPlayerTransport transport;
    private HttpPlayerTransportTest.MockPlayerResponseHandler responseHandler;
    private String id = "test";
    private String callbackUrl = "http://localhost:1111/";

    @Before
    public void setUp() throws Exception {
        server = new FakeHttpServer(1111);
        server.start();
        responseHandler = new MockPlayerResponseHandler();
        transport.registerPlayerEndpoint(id, responseHandler, callbackUrl);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldSendGameState() throws InterruptedException, IOException {
        transport.sendState(id, new MockGameState("someState"));
        server.setResponse("response");
        server.waitForRequest();
        Thread.sleep(100);

        assertEquals("response", responseHandler.responseContent);
    }

    private static class MockGameState implements GameState {
        private String state;

        public MockGameState(String state) {
            this.state = state;
        }

        @Override
        public String asString() {
            return state;
        }
    }

    private class MockPlayerResponseHandler implements PlayerResponseHandler<HttpResponseContext> {
        private String responseContent;

        @Override
        public void onResponseComplete(String responseContent, HttpResponseContext httpResponseContext) {
            this.responseContent = responseContent;
        }

        @Override
        public void onError(TransportErrorType type, HttpResponseContext httpResponseContext) {
        }
    }
}
