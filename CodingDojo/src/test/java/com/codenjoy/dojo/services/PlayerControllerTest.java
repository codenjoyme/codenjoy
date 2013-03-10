package com.codenjoy.dojo.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:30 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

    private FakeHttpServer server;
    private PlayerController controller;
    private Joystick joystick;
    private Player vasya;

    private static final int BOARD_SIZE = 10;

    @Before
    public void setUp() throws Exception {
        joystick = mock(Joystick.class);

        server = new FakeHttpServer(1111);
        server.start();

        controller = new PlayerController();
        controller.setTimeout(30);
        controller.init();

        Information info = mock(Information.class);
        vasya = new Player("vasya", "http://localhost:1111/", null, info);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        try {
            controller.requestControl(vasya, joystick, "");
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickLeft() throws IOException, InterruptedException {
        server.willResponse("LEFT");
        waitForPlayerResponse();

        verify(joystick, only()).left();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickRight() throws IOException, InterruptedException {
        server.willResponse("right");
        waitForPlayerResponse();

        verify(joystick, only()).right();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickUp() throws IOException, InterruptedException {
        server.willResponse("Up");
        waitForPlayerResponse();

        verify(joystick, only()).up();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickAct() throws IOException, InterruptedException {
        server.willResponse("aCt");
        waitForPlayerResponse();

        verify(joystick, only()).act();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickDown() throws IOException, InterruptedException {
        server.willResponse("DowN/?.");
        waitForPlayerResponse();

        verify(joystick, only()).down();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickRightAct() throws IOException, InterruptedException {
        server.willResponse("right,Act");
        waitForPlayerResponse();

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).act();
        inOrder.verifyNoMoreInteractions();
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickManyOperations() throws IOException, InterruptedException {
        server.willResponse("Act,right, left  ,,,,act");
        waitForPlayerResponse();

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act();
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).act();
        inOrder.verifyNoMoreInteractions();
    }

    @Test(timeout = 2000)
    public void shouldSendBoardState() throws IOException, InterruptedException {
        controller.requestControl(vasya, joystick, "board");
        server.waitForRequest();

        assertEquals("board", server.getRequestParameter("board"));
    }

    private void waitForPlayerResponse() throws IOException, InterruptedException {
        controller.requestControl(vasya, joystick, "board");
        server.waitForRequest();
        Thread.sleep(100);
    }
}