package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Information;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.snake.model.*;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Point;
import com.codenjoy.dojo.snake.model.artifacts.Stone;
import com.codenjoy.dojo.snake.services.playerdata.Plot;
import com.codenjoy.dojo.snake.services.playerdata.PlotColor;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:30 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

    private FakeHttpServer server;
    private PlayerController controller;
    private MockJoystick joystick;
    private Player vasya;

    private static final int BOARD_SIZE = 10;
    private BoardImpl board;
    private Snake snake;

    @Before
    public void setUp() throws Exception {
        board = mock(BoardImpl.class);
        snake = mock(Snake.class);
        when(board.getSnake()).thenReturn(snake);
        when(board.getSize()).thenReturn(BOARD_SIZE);

        joystick = new MockJoystick();

        server = new FakeHttpServer(1111);
        server.start();

        controller = new PlayerController();
        controller.setTimeout(30);
        controller.init();

        Information info = mock(Information.class);
        vasya = new Player("vasya", "http://localhost:1111/", new SnakePlayerScores(0), info);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

//    @Test
//    public void shouldSendRequestControlCommands() throws IOException, InterruptedException {
//        snakeAt(4, 6);
//        controller.requestControl(vasya, joystick, board);
//        server.waitForRequest();
//        assertEquals("4", server.getRequestParameter("x"));
//        assertEquals("6", server.getRequestParameter("y"));
//    }

    private void snakeAt(int x, int y) {
        snakeAt(new Point(x, y));
    }

    private void snakeAt(Point... points) {
        int x = points[0].getX();
        int y = points[0].getY();

        when(snake.getX()).thenReturn(x);
        when(snake.getY()).thenReturn(y);
        when(snake.getHead()).thenReturn(new Point(x, y));
        when(snake.getDirection()).thenReturn(Direction.UP);


        when(snake.iterator()).thenReturn(Arrays.asList(points).iterator());
    }

    @Test
    public void shouldSendRequestControlCommandsNoTailSlash() throws IOException, InterruptedException {
        snakeAt(2, 2);
        try {
            controller.requestControl(vasya, joystick, board);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickLeft() throws IOException, InterruptedException {
        server.willResponse("LEFT"); // TODO без этих двух строк тест слетает :(
        waitForPlayerResponse();     // наверное сразу чето не подхватывется сразу после старта системы

        server.willResponse("LEFT");
        waitForPlayerResponse();
        assertTrue(joystick.toString().contains("left"));
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickRight() throws IOException, InterruptedException {
        server.willResponse("right");
        waitForPlayerResponse();
        assertEquals("right", joystick.toString());
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickUp() throws IOException, InterruptedException {
        server.willResponse("Up");
        waitForPlayerResponse();
        assertEquals("up", joystick.toString());
    }

    @Test(timeout = 2000)
    public void shouldMoveJoystickDown() throws IOException, InterruptedException {
        server.willResponse("DowN/?.");
        waitForPlayerResponse();
        assertEquals("down", joystick.toString());
    }

    @Test
    public void shouldSendBoardState() throws IOException, InterruptedException {
        appleAt(0, 0);
        stoneAt(5, 5);
        snakeAt(new Point(6, 9), new Point(6, 8), new Point(6, 7));
        wallAt(6, 4);

        controller.requestControl(vasya, joystick, board);
        server.waitForRequest();

        assertEquals(
                "      ▲   " +
                "      ○   " +
                "      ○   " +
                "          " +
                "     ☻    " +
                "      ☼   " +
                "          " +
                "          " +
                "          " +
                "☺         ",
                server.getRequestParameter("board"));
    }

    private void wallAt(int x, int y) {
        Walls walls = mock(Walls.class);
        when(board.getWalls()).thenReturn(walls);

        when(walls.iterator()).thenReturn(Arrays.asList(new Point(x, y)).iterator());
    }

    private void stoneAt(int x, int y) {
        Stone stone = mock(Stone.class);
        when(board.getStone()).thenReturn(stone);
        when(stone.getX()).thenReturn(x);
        when(stone.getY()).thenReturn(x);
    }

    private void appleAt(int x, int y) {
        Apple apple = mock(Apple.class);
        when(board.getApple()).thenReturn(apple);
        when(apple.getX()).thenReturn(x);
        when(apple.getY()).thenReturn(x);
    }

    private Plot plot(int x, int y, PlotColor color) {
        return new Plot(x, y, color);
    }

    private void waitForPlayerResponse() throws IOException, InterruptedException {
        snakeAt(2, 2);
        controller.requestControl(vasya, joystick, board);
        server.waitForRequest();
        Thread.sleep(100);
    }

    private static class MockJoystick implements Joystick {
        private List<String> calls = new ArrayList<String>();

        @Override
        public void turnLeft() {
            calls.add("left");
        }

        @Override
        public void turnRight() {
            calls.add("right");
        }

        @Override
        public void turnUp() {
            calls.add("up");
        }

        @Override
        public void turnDown() {
            calls.add("down");
        }

        @Override
        public String toString() {
            return StringUtils.join(calls, ",");
        }
    }
}