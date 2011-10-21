package com.globallogic.snake;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SnakeTest {

	private Board board;
	private Snake snake; 
	
	@Before
	public void gateStart() {
		board = new Board(7);
		snake = board.getSnake();
	}
		
	// На поле появляется змейка 
	@Test
	public void shouldSnakeAtBoardWhenGameStart() {		
		assertNotNull("змейка должна быть", board.getSnake());
	}
	
	// змейка находится в центре экрана при старте игры
	@Test
	public void shouldSnakeAtCenterOfBoardWhenGameStart() {		
		assertSnakeAt(4, 4);		
	}

	/**
	 * Метод, который проверит, что голова змейки находится в конкретной позиции доски.
	 * @param x координата X
	 * @param y координата Y
	 */
	private void assertSnakeAt(int x, int y) {
		assertEquals("позиция X змейки", x, snake.getX());
		assertEquals("позиция Y змейки", y, snake.getY());
	}
	
	// теперь мне надо воспользоваться методом триангуляции и сделать так, чтобы змейка 
	// появлялась не в позиции 4,4 а все таки в центре доски игральной
	@Test
	public void shouldSnakeAtCenterOfSmallBoardWhenGameStart() {
		board = new Board(3); // тут немного неуклюже, а потому я отсавлю TODO и потом подумаю как это исправить
		snake = board.getSnake();
		
		assertSnakeAt(2, 2);		
	}
	
	// Змейка размером в две клеточки. 
	@Test
	public void shouldSnakeLengthIs2WhenStartGame() {
		assertSnakeSize(2);
	}

	/**
	 * Метод проверит, что змейка длинной в две клеточки. 
	 * @param expectedLength ожидаемая ждлинна змейки
	 */
	private void assertSnakeSize(int expectedLength) {
		assertEquals("длинна змейки", expectedLength, snake.getLength());		
	}
	
	// Поле имеет квадрутную форму, кратную двум + 1. 
	// Тут просто, если мы зададим размер поля какой-то другой, то будет исключение
	@Test(expected = IllegalArgumentException.class)
	public void shouldExceptionWhenBadBoardSize() {
		new Board(4);
	}
	
	// Направление движеня змейки изначально в право.
	@Test
	public void shouldSnakeHasRightDirectionWhenGameStart() {
		assertSnakeDirection("right"); 		
	}

	/**
	 * Метод проверяет куда направлена змейка сейчас. 
	 */
	private void assertSnakeDirection(String expectedDirection) {
		assertEquals("направление движения змейки", 
				expectedDirection, snake.getDirection());		
	}
	
	// Поле содержит один камень для начала. Я как-то сильно усложняю с этим LISt - пусть будет один камень.
	@Test
	public void shouldBoardContainsSomeStonesWhenGameStart() {
		Stone stone = board.getStone();
		assertNotNull("Поле должно содержать камень", stone);	
	}
	
	// Если змейка наткнется на камень, то она умрет. 
	// Умрет - значит конец игры. 
	// Если змейка съест сама себя - она умрет. 
	// При движении в противоположном направлении змейка сама себя съедает, т.е. умирает. 
	// Управлять змейкой можно движениями влево, вправо, вверх, вниз и ускорение. 
	// Змейка передвигается на одну клеточку в направлении движения за один такт.
	// На поле случайным образом во времени и пространстве появляются яблоки.
	// Змейка может съесть яблоки и при этом ее длинна увеличится на 1. 
	
	// Размер поля можно задать в начале игры.
	// Этот тест уже реализован, но все же пока оставим его. 
	// Тут я бы проверил, что размеры поля все же имеют те значения, что мы задали, 
	// а для этого надо научиться перемещать змейку и после перемещения спрашивать где 
	// она оказывается. Она должна быть во всех координатах от 1 до N (по X и Y), где 
	// N - размер поля. 
	
	
}
