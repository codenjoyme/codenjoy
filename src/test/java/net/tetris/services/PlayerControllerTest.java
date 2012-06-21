package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

    private FakeHttpServer server;
    private PlayerController controller;

//    @Mock
//    private Joystick joystick;
    private MockJoystick joystick;
    @Captor
    private ArgumentCaptor<Integer> leftCaptor;
    @Captor
    private ArgumentCaptor<Integer> rightCaptor;
    @Captor
    private ArgumentCaptor<Integer> rotateCaptor;
    private Player vasya;

    @Before
    public void setUp() throws Exception {
        joystick = new MockJoystick();
        server = new FakeHttpServer(1111);
        server.start();
        controller = new PlayerController();
        controller.setTimeout(30);
        controller.init();
        vasya = new Player("vasya", "http://localhost:1111/");
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldSendRequestControlCommands() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick);
        server.waitForRequest();
        assertEquals("T", server.getRequestParameter("figure"));
        assertEquals("4", server.getRequestParameter("x"));
        assertEquals("19", server.getRequestParameter("y"));
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        try {
            controller.requestControl(new Player("vasya", "http://localhost:1111"), Figure.Type.T, 1, 1, joystick);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void shouldMoveJoystick() throws IOException, InterruptedException {
        server.setResponse("left=1,right=2,rotate=3,drop");

        waitForPlayerResponse();

        assertEquals("left=1,right=2,rotate=3,drop", joystick.toString());

    }

    @Test
    public void shouldMoveJoystick2() throws IOException, InterruptedException {
        server.setResponse("DrOP/?.,,ROTATE=2,LeFt=1");

        waitForPlayerResponse();

        assertEquals("drop,rotate=2,left=1", joystick.toString());
    }

    private void waitForPlayerResponse() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.I, 123, 123, joystick);
        server.waitForRequest();
        Thread.sleep(100);
    }

    private static class MockJoystick implements Joystick {
        private List<String> calls = new ArrayList<>();

        @Override
        public void moveLeft(int delta) {
            calls.add("left=" + delta);
        }

        @Override
        public void moveRight(int delta) {
            calls.add("right=" + delta);
        }

        @Override
        public void drop() {
            calls.add("drop");
        }

        @Override
        public void rotate(int times) {
            calls.add("rotate=" + times);
        }

        @Override
        public String toString() {
            return StringUtils.join(calls, ",");
        }
    }
}
