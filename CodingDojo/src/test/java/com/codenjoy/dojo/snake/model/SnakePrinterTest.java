package com.codenjoy.dojo.snake.model;

import static org.junit.Assert.*;

import com.codenjoy.dojo.snake.model.SnakePrinterImpl;
import com.codenjoy.dojo.snake.model.Walls;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import org.junit.Before;
import org.junit.Test;

import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Stone;
import com.codenjoy.dojo.snake.model.Snake;

public class SnakePrinterTest {

	private static final int BOARD_SIZE = 7;
	private SnakePrinterImpl printer;
	
	@Before
	public void init() {
		printer = new SnakePrinterImpl();
		printer.size = BOARD_SIZE; // TODO очень некрасиво я тут сделал, нарушив инкапсуляцию. Но вернемся к этому позже 
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
		Snake snake = new Snake(3, 3);
		snake.grow();
		snake.move(3, 4);
		snake.grow();
		snake.move(4, 4);
		snake.grow();
		snake.move(4, 5);
		snake.grow();
		snake.move(5, 5);
		printer.printSnake(snake);		
		
		assertEquals(
				"       \n" +
				"    ○► \n" +
				"   ○○  \n" +
				"  ●○   \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
}
