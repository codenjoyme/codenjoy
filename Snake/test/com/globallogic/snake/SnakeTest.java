package com.globallogic.snake;

import static org.junit.Assert.*;

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
	
	// Есть поле.
	@Test
	public void shouldExistsBoardWhenGameStart() {
		new Board(0);
	}
	
	// На поле появляется змейка 
	@Test
	public void shouldSnakeAtBoardWhenGameStart() {		
		assertNotNull(board.getSnake());
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
		assertEquals(x, snake.getX());
		assertEquals(y, snake.getY());
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
	 * @param actualLength ожидаемая ждлинна змейки
	 */
	private void assertSnakeSize(int actualLength) {
		assertEquals(actualLength, snake.getLength());		
	}
	
	// Поле имеет квадрутную форму, кратную двум.
	// Размер поля можно задать в начале игры. 
	// Направление движеня змейки изначально в право.
	// Поле содержит камни. 
	// Если змейка наткнется на камень, то она умрет. 
	// Умрет - значит конец игры. 
	// Если змейка съест сама себя - она умрет. 
	// При движении в противоположном направлении змейка сама себя съедает, т.е. умирает. 
	// Управлять змейкой можно движениями влево, вправо, вверх, вниз и ускорение. 
	// Змейка передвигается на одну клеточку в направлении движения за один такт.
	// На поле случайным образом во времени и пространстве появляются яблоки.
	// Змейка может съесть яблоки и при этом ее длинна увеличится на 1. 
	
	
}
