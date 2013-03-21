package net.tetris.services;

import net.tetris.dom.*;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.*;
import static net.tetris.dom.TestUtils.*;
import static net.tetris.dom.TetrisGame.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpPlayerControllerTest {

    private FakeHttpServer server;
    private HttpPlayerController controller;

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
    private PlayerControllerListener listener;

    @Captor
    private ArgumentCaptor<String> requestCaptor;
    @Captor
    private ArgumentCaptor<String> responseCaptor;
    @Captor
    private ArgumentCaptor<Player> playerCaptor;

    @Before
    public void setUp() throws Exception {
        joystick = new MockJoystick();
        server = new FakeHttpServer(1111);
        server.start();
        controller = createController(1000);
        vasya = new Player("vasya", "http://localhost:1111/", new PlayerScores(0), emptyLevels(), null);
        listener = Mockito.mock(PlayerControllerListener.class);
    }

    private HttpPlayerController createController(int timeout) throws Exception {
        HttpPlayerController controller = new HttpPlayerController();
        controller.setTimeout(timeout);
        controller.init();
        return controller;
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldSendRequestControlCommands() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Collections.<Plot>emptyList(), Arrays.asList(Figure.Type.I, Figure.Type.J, Figure.Type.L, Figure.Type.O));
        server.waitForRequest();
        assertEquals("T", server.getRequestParameter("figure"));
        assertEquals("4", server.getRequestParameter("x"));
        assertEquals("19", server.getRequestParameter("y"));
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        try {
            controller.requestControl(new Player("vasya", "http://localhost:1111", new PlayerScores(0), emptyLevels(), null),
                    Figure.Type.T, 1, 1, joystick, Collections.<Plot>emptyList(), Arrays.asList(Figure.Type.I, Figure.Type.J, Figure.Type.L, Figure.Type.O));
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void shouldMoveJoystick() throws IOException, InterruptedException {
        controller.setSync(true);
        server.setResponse("left=1,right=2,rotate=3,drop");

        waitForPlayerResponse();

        assertEquals("left=1,right=2,rotate=3,drop", joystick.toString());
    }


    @Test
    public void shouldSendGlassState() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Arrays.asList(plot(0, 0)), Arrays.asList(Figure.Type.I, Figure.Type.J, Figure.Type.L, Figure.Type.O));
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
                Arrays.asList(plot(5, 0), plot(6, 1)), Arrays.asList(Figure.Type.I));
        server.waitForRequest();

        assertEquals(spaces(5) + "*" + spaces(GLASS_WIDTH - 5 - 1) +
                spaces(6) + "*" + spaces(GLASS_WIDTH - 6 - 1) +
                spaces(GLASS_WIDTH * (GLASS_HEIGHT - 2)),
                server.getRequestParameter("glass"));
    }

    @Test
    public void shouldLogHttpRequestResponse() throws IOException, InterruptedException {
        setupListener();
        server.setResponse("left=1,right=2,rotate=3,drop");

        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick,
                Arrays.asList(plot(0, 0)), Arrays.asList(Figure.Type.I));
        server.waitForRequest();

        int times = 1;
        verifyListenerCalled(times);
        assertTrue(requestCaptor.getValue().startsWith("/?figure=T&x=4&y=19&glass=*"));
        assertEquals("left=1,right=2,rotate=3,drop", responseCaptor.getValue());
        assertSame(vasya, playerCaptor.getValue());
    }

    @Test
    public void shouldLogHttpRequestResponseWhenException() throws IOException, InterruptedException {
        setupListener();
        server.setResponseException(new RuntimeException());

        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Collections.<Plot>emptyList(), Arrays.asList(Figure.Type.I));
        server.waitForRequest();

        verifyListenerCalled(1);
        assertEquals("ERROR:500", responseCaptor.getValue());
    }

    @Test
    public void shouldLogHttpRequestResponseWhenTimeout() throws Exception, InterruptedException {
        controller = createController(100);
        setupListener();
        server.setWaitTime(200);

        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Collections.<Plot>emptyList(), Arrays.asList(Figure.Type.I));
        server.waitForRequest();

        verifyListenerCalled(1);
        assertEquals("EXPIRED", responseCaptor.getValue());
    }

    @Test
    public void shouldSendFutureFigures() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.T, 4, 19, joystick, Arrays.asList(plot(0, 0)),
                Arrays.asList(Figure.Type.I, Figure.Type.J, Figure.Type.L, Figure.Type.O));
        server.waitForRequest();

        assertEquals("IJLO", server.getRequestParameter("next"));
    }

    private void setupListener() {
        controller.setListener(listener);
        controller.setSync(true);
    }

    private void verifyListenerCalled(int times) {
        verify(listener, times(times)).log(playerCaptor.capture(), requestCaptor.capture(), responseCaptor.capture());
    }


    private Plot plot(int x, int y) {
        return new Plot(x, y, PlotColor.CYAN);
    }

    private void waitForPlayerResponse() throws IOException, InterruptedException {
        controller.requestControl(vasya, Figure.Type.I, 123, 123, joystick, Collections.<Plot>emptyList(), Arrays.asList(Figure.Type.I));
        server.waitForRequest();
        Thread.sleep(100);
    }

}
