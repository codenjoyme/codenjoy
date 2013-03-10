package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.snake.model.artifacts.EmptySpace;
import com.codenjoy.dojo.snake.model.artifacts.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 3/10/13
 * Time: 2:51 PM
 */
public class SnakeDirectionTest {

    private Board board;
    private Snake snake;

    @Before
    public void setup() {
        board = mock(Board.class);
        when(board.getSize()).thenReturn(100);
        when(board.getAt(any(Point.class))).thenReturn(new EmptySpace(new Point(0, 0)));

        snake = new Snake(50, 50);
    }

    @Test
    public void test() {
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);

        snakeUp();
        assertHeadAntTail(50, 51, Direction.UP, Direction.DOWN);

        snakeLeft();
        assertHeadAntTail(49, 51, Direction.LEFT, Direction.RIGHT);

        snakeDown();
        assertHeadAntTail(49, 50, Direction.DOWN, Direction.UP);

        snakeRight();
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);
    }

    private void snakeRight() {
        snake.right();
        snake.walk(board);
    }

    private void snakeDown() {
        snake.down();
        snake.walk(board);
    }

    private void snakeLeft() {
        snake.left();
        snake.walk(board);
    }

    private void snakeUp() {
        snake.up();
        snake.walk(board);
    }

    private void assertHeadAntTail(int x, int y, Direction headDirection, Direction tailDirection) {
        assertEquals( "[headX, headY, headDirection, tailDirection]",
                asString(x, y, headDirection, tailDirection),

                asString(snake.getHead().getX(), snake.getHead().getY(),
                        snake.getDirection(), snake.getTailDirection()));
    }

    private String asString(Object...args) {
        return Arrays.asList(args).toString();
    }
}
