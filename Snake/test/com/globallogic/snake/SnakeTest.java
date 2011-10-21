package com.globallogic.snake;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class SnakeTest {

	private static final int BOARD_SIZE = 7;
	private Board board;
	private Snake snake;
	private Stone stone;
	
	@Before
	public void gateStart() {
		board = new Board(BOARD_SIZE);
		snake = board.getSnake();
		stone = board.getStone();
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
	
	// Я бы хотел потестить другой момент, что камень при каждой новой игре размещается в новом месте
	@Test
	public void shouldStoneHasRandomPositionsWhenNewGameStarted() {				
		Stone secondGameStone = getStoneFromAnotherGame();
		Stone thirdGameStone = getStoneFromAnotherGame();
					
		assertStoneChangePosition(stone, secondGameStone, thirdGameStone);		
	} 

	/**
	 * метод проверяет что хоть какаято пара камней из переданных в качестве аргументов находится на разных местах.
	 * @param stones камни
	 */
	private void assertStoneChangePosition(Stone... stones) {
		boolean atSame = isStonesAtSamePosition(stones);
		assertFalse(String.format("Все камни за количество игр равное %s были в одной и той же позиции (%s)", 
				stones.length, Arrays.toString(stones)), 
				atSame);
	}

	/**
	 * Метод говорит что какие-то из камней находятся на разных позициях. 
	 * @param stones камни
	 * @return true, если хоть два камня находятся на рызных позициях.     
	 */
	private boolean isStonesAtSamePosition(Stone... stones) {
		for (int stoneIndex = 0; stoneIndex < (stones.length - 1); stoneIndex ++) {
			Stone oneStone = stones[stoneIndex];
			Stone anotherStone = stones[stoneIndex + 1];
			
			if ((oneStone.getX() != anotherStone.getX()) || oneStone.getY() != anotherStone.getY()) {
				return false; 
			}
		}
		return true;
	}  	

	/**
	 * Метод запускает новую игру и возвращает камень с нее.
	 * @return камень с какой-то новой игры.
	 */
	private Stone getStoneFromAnotherGame() {
		Board newBoard = new Board(BOARD_SIZE); // доска должна иметь тот же размер, иначе нет смысла в тесте
		return newBoard.getStone();
	} 
	
	// камень может быть за пределами доски, а должен быть всегда на доске! Это бага
	@Test
	public void shouldStoneAlwaysAtTheBoard() {
		assertTrue("камень должен быть в перделах доски по оси X", stone.getX() <= BOARD_SIZE);
		assertTrue("камень должен быть в перделах доски по оси Y", stone.getY() <= BOARD_SIZE);
	}	
	
	// но кажется я допустил еще одну ошибку при использовании Random. Надо проверить что камень когданибудь но 
	// все же появится возле стенок доски. Да или вообще можно проверить что камень будет везде на поле, 
	// если мы переберем достаточное количество игр 
	@Test
	public void testRandomStonePosition() {
		int snakeHeadX = BOARD_SIZE/2 + 1;   
		int snakeHeadY = snakeHeadX; 
		int snakeTailX = snakeHeadX - 1; 
		
		for (int y = 0; y <= BOARD_SIZE; y ++) {
			for (int x = 0; x <= BOARD_SIZE; x ++) {
				if (y == snakeHeadY && x >= snakeTailX) { // камень не должен появляться ни на змее, ни на ее пути 
					continue; 
				}
				assertStoneInSomeGameAt(x, y);
			}
		}
	} 

	/**
	 * Метод проверяет что за больше число запусков игр камень будет в заданной позиции хоть один раз.
	 * @param x координата x
	 * @param y координата y
	 */
	private void assertStoneInSomeGameAt(int x, int y) {	
		boolean found = isStonePresentInSomeGameAt(x, y);		
		assertTrue(String.format("Должен был быть найден камень в позиции x:%s y:%s", x, y), found);
	}

	/**
	 * Метод говорит, что за больше число запусков игр камень будет в заданной позиции хоть один раз.
	 * @param x координата x
	 * @param y координата y
	 * @return true - если камень в этой координате появлялся
	 */
	private boolean isStonePresentInSomeGameAt(int x, int y) {
		boolean found = false;
		for (int countRun = 0; countRun < 100000; countRun ++) {
			board = new Board(BOARD_SIZE);
			stone = board.getStone();
			
			found |= (x == stone.getX()) & (y == stone.getY());
			if (found) {
				break;
			}
		}
		return found;
	}
	
	// еще камень никогда не должен находиться в трех местах - на змейке размером в два поля
	// и непосредственно на пути ее движения (прямо перед носом, а то не дай бог скорость будет 
	// большой и что тогда? игрок может не успеть)  
	@Test
	public void shouldNotStoneAtSnakeWay() {
		int snakeHeadX = BOARD_SIZE/2 + 1;   
		int snakeHeadY = snakeHeadX; 
		int snakeTailX = snakeHeadX - 1; 
		
		for (int x = snakeTailX; x <= BOARD_SIZE; x ++) {
			assertStoneNotFoundAt(x, snakeHeadY);				
		}
	}	  

	/**
	 * Метод проверяет что за больше число запусков игр камень не будет в заданной позиции никогда.
	 * @param x координата x
	 * @param y координата y
	 */
	private void assertStoneNotFoundAt(int x, int y) {	
		boolean found = isStonePresentInSomeGameAt(x, y);		
		assertFalse(String.format("Камень никогда не должен был появляться в позиции x:%s y:%s", x, y), found);
	}
	
	// камень будет (при каждом обращении к нему через доску) 
	// иметь разные координаты что недопустимо 
	@Test
	public void shouldSnakeAtOnePositionDurringOnegame() {
		assertStoneDoesntChangePosition(stone, board.getStone());
	}
	
	/**
	 * метод проверяет, что хоть какаято пара камней из переданных в качестве аргументов находится на разных местах.
	 * @param stones камни
	 */
	private void assertStoneDoesntChangePosition(Stone... stones) {
		boolean atSame = isStonesAtSamePosition(stones);
		assertTrue(String.format("Все камни должны быть в однйо позиции (%s)", Arrays.toString(stones)), 
				atSame);
	}
	
	// при перемещении в право не должна меняться Y координата 
	@Test
	public void shouldChangeXPositionWhenTurnRight() {
		int oldX = snake.getX();
		
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после перемещения должна увеличиться", oldX + 1, newX);
	}
	
	// при перемещении вправо не должна меняться Y координата
	@Test
	public void shouldNotChangeYPositionWhenTurnRight() {
		int oldY = snake.getY();
		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y после перемещения не должна меняться", oldY, newY);
	}
	
	// Управлять змейкой можно движениями влево, вправо, вверх, вниз и ускорение. 
	// Ускорение можно реализовать на UI путем учеличения числа циклов в секунду, 
	// модель наша будет оперировать циклами - одним перемещением змейки) 
	// так как это модель, то тут нет никаких UI кнопок и прочих штук - реализуем 
	// один едиственный метод turnDown() который будет перемещать змейку за следующий такт вниз.
	// такты будем отсчитывать соовтествующим методом.	
	// при перемещении вниз меняется координата Y в большую сторону - это и проверяем
	@Test
	public void shouldTurnDownWhenCallSnakeDown() {
		int oldY = snake.getY();
		
		snake.turnDown();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна увеличится", oldY + 1, newY);
	}
	
	// теперь я могу проверить как змейка двигается по инерции вниз
	@Test 
	public void shouldGoDownInertia() {
		snake.turnDown();		
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вниз должна увеличится", oldY + 1, newY);
	}
	
	// проверить что при перемещении вниз координата X не меняется
	@Test
	public void shouldNotChangeXPositionWhenMoveDown() {
		int oldX = snake.getX();
		
		snake.turnDown();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вниз не должна меняться", oldX, newX);
	}
	
    // проверить что при перемещении вверх меняется координата Y в меньшую сторону
	// координата 0,0 размещена в левом верхнем углу. Почему так? не знаю, наверное из прошлого привычка
	@Test
	public void shouldTurnUpWhenCallSnakeUp() {
		int oldY = snake.getY();
		
		snake.turnUp();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна уменьшиться", oldY - 1, newY);
	}
	
	// проверить что при перемещении вверх координата X не меняется
	@Test // как обычно - ломаем
	public void shouldNotChangeXPositionWhenMoveUp() {
		int oldX = snake.getX();
		
		snake.turnUp();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вверх не должна меняться", oldX, newX);
	}
	
	// проверить движение вверх по инерции
	@Test 
	public void shouldGoUpInertia() {
		snake.turnUp();		
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вверх должна уменьшиться", oldY - 1, newY);
	}
	
	// При движении в противоположном направлении змейка сама себя съедает, т.е. умирает.
	@Test  
	public void shouldGameOverWhenSnakeEatItself() {
		snake.turnLeft();
		board.tact();
		
		assertGameOver();
	}
		
	/**
	 * Метод првоеряет, что игра окончена
	 */
	private void assertGameOver() {
		assertTrue("Ожидается конец игры", board.isGameOver());		
	}
	
	// Умрет - значит конец игры. Если конец игры, значит любое обращение 
	// к доске (методам доски) вызывает исключение. 
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeUpAfterGameOver() {
		snake.turnLeft();
		board.tact();
		
		snake.turnUp();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeDownAfterGameOver() {
		snake.turnLeft();
		board.tact();
		
		snake.turnDown();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeLeftAfterGameOver() {
		snake.turnLeft();
		board.tact();
		
		snake.turnLeft();
	}
	
	// разремарить этот тест после того как научу змейку поворачиваться вправо TODO
//	@Test(expected = IllegalStateException.class)  
//	public void shouldExceptionWhenTryToTurnSnakeRightAfterGameOver() {
//		snake.turnLeft();
//		board.tact();
//		
//		snake.turnRight();
//	}
	
	// проверить поворот вправо
	
	// проверить как змея ест сама себя при движении вниз
	
	// проверить как змея ест сама себя при движении вверх
	
	// проверить как змея ест сама себя при движении влево
	
	// проверить что при перемещении влево меняется координата X  в меньшую сторону
//	@Test
//	public void shouldChangeXPositionWhenTurnLeft() {
//		int oldX = snake.getX();
//		
//		board.tact();
//		int newX = snake.getX();
//		
//		assertEquals("новая позиция по X после перемещения", oldX - 1, newX);
//	}
	// пока тест заремарю до лучших времен.		
	
	// проверить что при перемещении влево координата Y не меняется
	
	// проверить движение влево по инерции
	
	// Если змейка съест сама себя - она умрет. 

	// Если змейка наткнется на камень, то она умрет. Перед тем надо научить змейку ползать.	
	
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
