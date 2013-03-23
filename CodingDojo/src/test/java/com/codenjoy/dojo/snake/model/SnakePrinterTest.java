package com.codenjoy.dojo.snake.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import org.junit.Before;
import org.junit.Test;

import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

public class SnakePrinterTest {

	private static final int BOARD_SIZE = 7;
	private SnakePrinter printer;
    private Snake snake;

    @Before
	public void init() {
        Board board = mock(Board.class);
        when(board.getSize()).thenReturn(BOARD_SIZE);

        printer = new SnakePrinter(board);
		printer.clean();
	}
	
	@Test
	public void checkCleanBoard() {
		assertEquals("       \n       \n       \n       \n       \n       \n       \n", printer.asString());
	}
	
	@Test
	public void checkPrintWall() {
        Walls walls = new Walls();
        walls.add(2, 2);
        walls.add(3, 3);
        walls.add(4, 4);

        printer.printWalls(walls);
		
		assertEquals(
				"       \n" +
                "       \n" +
                "    ☼  \n" +
                "   ☼   \n" +
                "  ☼    \n" +
                "       \n" +
                "       \n", printer.asString());
	}

    @Test
    public void checkPrintBasicWalls() {   // тут тестируем больше BasicWalls чем printer
        printer.printWalls(new BasicWalls(BOARD_SIZE));

        assertEquals(
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n", printer.asString());
    }
	
	@Test
	public void checkPrintApple() {
		printer.printApple(new Apple(3, 3));
		printer.printApple(new Apple(2, 2));
		
		assertEquals(
				"       \n" +
				"       \n" +
				"       \n" +
				"   ☺   \n" +
				"  ☺    \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
	@Test
	public void checkPrintStone() {
		printer.printStone(new Stone(3, 3));
		printer.printStone(new Stone(2, 4));
		printer.printStone(new Stone(4, 4));
		
		assertEquals(
				"       \n" +
				"       \n" +
				"  ☻ ☻  \n" +
				"   ☻   \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
	@Test
	public void checkPrintSnake() {
		shouldSnake();
        moveUp();
        moveRight();
        moveUp();
        moveRight();

        assertSnake( 
                "       \n" +
                "    ╔► \n" +
                "   ╔╝  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
	}

    private void assertSnake(String expected) {
        printer.printSnake(snake);
        assertEquals(expected, printer.asString());
    }

    private void moveUp() {
        move(0, 1);
        snake.up();
    }

    private void move(int dx, int dy) {
        snake.move(snake.getX() + dx, snake.getY() + dy);
        snake.grow();
    }

    private void moveDown() {
        move(0, -1);
        snake.down();
    }

    private void moveLeft() {
        move(-1, 0);
        snake.left();
    }

    private void shouldSnake() {
        snake = new Snake(3, 3);
        snake.right();
    }

    private void moveRight() {
        move(1, 0);
        snake.right();
    }

    @Test
    public void checkPrintSnakeTailRight() {
        shouldSnake();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╘►   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailLeft() {
        shouldSnake();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ◄╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailDown() {
        shouldSnake();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ▼   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailUp() {
        shouldSnake();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "   ▲   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailVerticalUp() {
        shouldSnake();
        moveUp();
        moveUp();
        assertSnake(
                "       \n" +
                "   ▲   \n" +
                "   ║   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailVerticalDown() {
        shouldSnake();
        moveDown();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ║   \n" +
                "   ▼   \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailHorizontalLeft() {
        shouldSnake();
        moveLeft();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                " ◄═╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }


    @Test
    public void checkPrintSnakeTailHorizontalRight() {
        shouldSnake();
        moveRight();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘═► \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateLeftUp() {
        shouldSnake();
        moveLeft();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "  ▲    \n" +
                "  ╚╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateLeftDown() {
        shouldSnake();
        moveLeft();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╔╕   \n" +
                "  ▼    \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateUpLeft() {
        shouldSnake();
        moveUp();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "  ◄╗   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateUpRight() {
        shouldSnake();
        moveUp();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "   ╔►  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateDownLeft() {
        shouldSnake();
        moveDown();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ◄╝   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateDownRight() {
        shouldSnake();
        moveDown();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ╚►  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateRightDown() {
        shouldSnake();
        moveRight();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘╗  \n" +
                "    ▼  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailHorizontalRightUp() {
        shouldSnake();
        moveRight();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "    ▲  \n" +
                "   ╘╝  \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnake2() {
        shouldSnake();
        moveDown();
        moveLeft();
        moveDown();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ╔╝   \n" +
                " ◄╝    \n" +
                "       \n");
    }

	
}
