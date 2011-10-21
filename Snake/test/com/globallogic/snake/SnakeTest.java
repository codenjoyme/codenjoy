package com.globallogic.snake;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SnakeTest {

	private Board board; 
	
	@Before
	public void gateStart() {
		board = new Board(7);
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
		Snake snake = board.getSnake();
		assertEquals(4, snake.getX());
		assertEquals(4, snake.getY());		
	}
	
	// Змейка размером в две клеточки. 
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
