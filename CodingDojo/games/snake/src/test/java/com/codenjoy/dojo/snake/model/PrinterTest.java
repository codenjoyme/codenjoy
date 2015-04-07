package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.model.artifacts.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrinterTest {

	private static final int BOARD_SIZE = 7;
	private Printer printer;
    private Hero snake;
    private Field board;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Before
	public void init() {
        board = mock(Field.class);
        when(board.getSize()).thenReturn(BOARD_SIZE);
        when(board.getApple()).thenReturn(null);
        when(board.getStone()).thenReturn(null);
        when(board.getWalls()).thenReturn(null);
        when(board.getSnake()).thenReturn(null);

        printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return BOARD_SIZE;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();

                if (board.getWalls() != null) {
                    for (Wall wall : board.getWalls()) {
                        result.add(wall);
                    }
                }

                if (board.getSnake() != null) {
                    for (Tail tail : board.getSnake()) {
                        result.add(tail);
                    }
                }

                if (board.getApple() != null) {
                    result.add(board.getApple());
                }

                if (board.getStone() != null) {
                    result.add(board.getStone());
                }
                return result;
            }
        }, null);
	}
	
	@Test
	public void checkCleanBoard() {
		assertEquals("       \n       \n       \n       \n       \n       \n       \n", printer.print());
	}
	
	@Test
	public void checkPrintWall() {
        Walls walls = new Walls();
        walls.add(2, 2);
        walls.add(3, 3);
        walls.add(4, 4);
        when(board.getWalls()).thenReturn(walls);

		assertEquals(
				"       \n" +
                "       \n" +
                "    ☼  \n" +
                "   ☼   \n" +
                "  ☼    \n" +
                "       \n" +
                "       \n", printer.print());
	}

    @Test
    public void checkPrintBasicWalls() {   // тут тестируем больше BasicWalls чем printer
        when(board.getWalls()).thenReturn(new BasicWalls(BOARD_SIZE));

        assertEquals(
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n", printer.print());
    }
	
	@Test
	public void checkPrintApple() {
        when(board.getApple()).thenReturn(new Apple(2, 2));

        assertEquals(
				"       \n" +
				"       \n" +
				"       \n" +
				"       \n" +
				"  ☺    \n" +
				"       \n" +
				"       \n", printer.print());
	}
	
	@Test
	public void checkPrintStone() {
        when(board.getStone()).thenReturn(new Stone(4, 4));

        assertEquals(
				"       \n" +
				"       \n" +
				"    ☻  \n" +
				"       \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.print());
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
        when(board.getSnake()).thenReturn(snake);
        assertEquals(expected, printer.print());
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
        snake = new Hero(3, 3);
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
