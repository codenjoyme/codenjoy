package com.globallogic.snake;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
		printer.printWalls();
		
		assertEquals(
				"*******\n" +
				"*     *\n" +
				"*     *\n" +
				"*     *\n" +
				"*     *\n" +
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
				"  @    \n" +
				"   @   \n" +
				"       \n" +
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
				"       \n" +
				"   X   \n" +
				"  X X  \n" +
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
				"       \n" +
				"       \n" +
				"  00   \n" +
				"   00  \n" +
				"    0# \n" +
				"       \n", printer.asString());
	}
	
}
