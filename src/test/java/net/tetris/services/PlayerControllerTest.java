package net.tetris.services;

import net.tetris.dom.Figure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public class PlayerControllerTest {

    private FakeHttpServer server;
    private PlayerController controller;

    @Before
    public void setUp() throws Exception {
        server = new FakeHttpServer(1111);
        server.start();
        controller = new PlayerController();
        controller.setTimeout(30);
        controller.afterPropertiesSet();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldSendRequestControlCommands() throws IOException, InterruptedException {
        controller.requestControl(new Player("vasya", "http://localhost:1111/"), Figure.Type.T, 4, 19);
        server.waitForRequest();
        assertEquals("T", server.getRequestParameter("figure"));
        assertEquals("4", server.getRequestParameter("x"));
        assertEquals("19", server.getRequestParameter("y"));
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        try {
            controller.requestControl(new Player("vasya", "http://localhost:1111"), Figure.Type.T, 1, 1);
        } catch (NumberFormatException e) {
            fail();
        }
    }
}
