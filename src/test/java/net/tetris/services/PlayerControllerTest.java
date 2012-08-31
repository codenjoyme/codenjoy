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
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static net.tetris.dom.TetrisGame.*;
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
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Collections.<Plot>emptyList());
        server.waitForRequest();
        assertEquals("T", server.getRequestParameter("figure"));
        assertEquals("4", server.getRequestParameter("x"));
        assertEquals("19", server.getRequestParameter("y"));
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        try {
            controller.requestControl(new Player("vasya", "http://localhost:1111"), Figure.Type.T, 1, 1, joystick, Collections.<Plot>emptyList());
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

    @Test
    public void shouldSendGlassState() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Arrays.asList(plot(0, 0)));
        server.waitForRequest();

        int times = 10 - 1;
        assertEquals("*" + spaces(times) + spaces(GLASS_WIDTH * (GLASS_HEIGHT - 1)),
                server.getRequestParameter("glass"));
    }

    private String spaces(int times) {
        return StringUtils.repeat(" ", times);
    }

    @Test
    public void shouldSendGlassStateWhenSeveralDropped() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick,
                Arrays.asList(plot(5, 0), plot(6, 1)));
        server.waitForRequest();

        assertEquals(spaces(5) + "*" + spaces(GLASS_WIDTH - 5 - 1) +
                spaces(6) + "*" + spaces(GLASS_WIDTH - 6 - 1) +
                spaces(GLASS_WIDTH * (GLASS_HEIGHT - 2)),
                server.getRequestParameter("glass"));
    }

    private Plot plot(int x, int y) {
        return new Plot(x, y, PlotColor.CYAN);
    }

    private void waitForPlayerResponse() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.I, 123, 123, joystick, Collections.<Plot>emptyList());
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
