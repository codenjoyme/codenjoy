package com.globallogic.snake.console;

import static org.junit.Assert.*;

import com.globallogic.snake.model.Walls;
import org.junit.Before;
import org.junit.Test;

import com.globallogic.snake.console.SnakePrinterImpl;
import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.Stone;
import com.globallogic.snake.model.Snake;

public class SnakePrinterTest {

	private static final int BOARD_SIZE = 5;
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
        walls.add(1, 1);
        walls.add(2, 2);
        walls.add(3, 3);

        printer.printWalls(walls);
		
		assertEquals(
				"*******\n" +
				"*     *\n" +
				"*   * *\n" +
				"*  *  *\n" +
				"* *   *\n" +
				"*     *\n" +
				"*******\n", printer.asString());
	}
	
	@Test
	public void checkPrintApple() {
		printer.printApple(new Apple(2, 2));
		printer.printApple(new Apple(1, 1));
		
		assertEquals(
				"       \n" +
				"       \n" +
				"       \n" +
				"   @   \n" +
				"  @    \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
	@Test
	public void checkPrintStone() {
		printer.printStone(new Stone(2, 2));
		printer.printStone(new Stone(1, 3));
		printer.printStone(new Stone(3, 3));
		
		assertEquals(
				"       \n" +
				"       \n" +
				"  X X  \n" +
				"   X   \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
	@Test
	public void checkPrintSnake() {
		Snake snake = new Snake(2, 2);
		snake.grow();
		snake.move(2, 3);
		snake.grow();
		snake.move(3, 3);
		snake.grow();
		snake.move(3, 4);
		snake.grow();
		snake.move(4, 4);
		printer.printSnake(snake);		
		
		assertEquals(
				"       \n" +
				"    0# \n" +
				"   00  \n" +
				"  00   \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.asString());
	}
	
}
