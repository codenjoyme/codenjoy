package com.globallogic.snake;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class RandomArtifactGeneratorTest {
	
	private RandomArtifactGenerator generator;
	private Snake snake;
	
	private static final int BOARD_SIZE = 7;

	@Before 
	public void initGenerator() {
		generator = new RandomArtifactGenerator();
		
		int position = getSnakeHeadPosition();
		snake = new Snake(position, position);
	}

	private int getSnakeHeadPosition() {
		return (BOARD_SIZE - 1)/2;
	}
	
	// Я бы хотел потестить другой момент, что камень при каждой новой игре размещается в новом месте
	@Test
	public void shouldStoneHasRandomPositionsWhenNewGameStarted() {				
		Stone firstStone = getNewStone();
		Stone secondStone = getNewStone();
		Stone thirdStone = getNewStone();
					
		assertPointChangePosition(firstStone, secondStone, thirdStone);		
	}

	private Stone getNewStone() {
		return generator.generateStone(snake, BOARD_SIZE);
	} 

	/**
	 * метод проверяет что хоть какая-то пара артефактов из переданных в качестве аргументов находится на разных местах.
	 * @param points артефакты
	 */
	private void assertPointChangePosition(Point... points) {
		boolean atSame = isPointsAtSamePosition(points);
		assertFalse(String.format("Все камни за количество игр равное %s были в одной и той же позиции (%s)", 
				points.length, Arrays.toString(points)), 
				atSame);
	}

	/**
	 * Метод говорит что какие-то из артефактов находятся на разных позициях. 
	 * @param points артефакты
	 * @return true, если хоть два артефакта находятся на разных позициях.     
	 */
	private boolean isPointsAtSamePosition(Point... points) {
		for (int pointIndex = 0; pointIndex < (points.length - 1); pointIndex ++) {
			Point one = points[pointIndex];
			Point another = points[pointIndex + 1];
			
			if ((one.getX() != another.getX()) || one.getY() != another.getY()) {
				return false; 
			}
		}
		return true;
	}  	
	
	// камень не может быть за пределами доски 
	@Test
	public void shouldStoneAlwaysAtTheBoard() {
		// тут поставил цикл, чтобы проверить что никогда
		// камень не генерится за пределами доски
		for (int countRun = 0; countRun < 100000; countRun ++) { 
			Stone stone = getNewStone(); 
			
			assertTrue("камень должен быть в перделах доски по оси X", stone.getX() < BOARD_SIZE);
			assertTrue("камень должен быть в перделах доски по оси Y", stone.getY() < BOARD_SIZE);
			assertTrue("камень должен быть в перделах доски по оси X", stone.getX() >= 0);
			assertTrue("камень должен быть в перделах доски по оси Y", stone.getY() >= 0);
		}
	}	
	
	// но кажется я допустил еще одну ошибку при использовании Random. Надо проверить что камень когданибудь но 
	// все же появится возле стенок доски. Да или вообще можно проверить что камень будет везде на поле, 
	// если мы переберем достаточное количество игр 
	@Test
	public void testRandomStonePosition() {
		int snakeHeadX = snake.getX();   
		int snakeHeadY = snake.getY(); 
		int snakeTailX = snakeHeadX - 1; 
		
		for (int y = 0; y < BOARD_SIZE; y ++) {
			for (int x = 0; x < BOARD_SIZE; x ++) {
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
			Stone stone = getNewStone();
			
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
		int snakeHeadX = getSnakeHeadPosition();   
		int snakeHeadY = snakeHeadX; 
		int snakeTailX = snakeHeadX - 1; 
		
		for (int x = snakeTailX; x <= BOARD_SIZE; x ++) {
			assertStoneNotFoundAt(x, snakeHeadY);				
		}
	}	  

	/**
	 * Метод проверяет что за больше число запусков игр артефакт не будет в заданной позиции никогда.
	 * @param x координата x
	 * @param y координата y
	 */
	private void assertStoneNotFoundAt(int x, int y) {	
		boolean found = isStonePresentInSomeGameAt(x, y);		
		assertFalse(String.format("Артефакт никогда не должен был появляться в позиции x:%s y:%s", x, y), found);
	}
	
	// На поле случайным образом во времени и пространстве появляются яблоки.
	// тут я не буду тестить то, что яблоки будут в каждой клетке и так далее. 
	@Test
	public void shouldWhen() {
		Apple firstApple = getNewApple();
		Apple secondApple = getNewApple();
		Apple thirdApple = getNewApple();
					
		assertPointChangePosition(firstApple, secondApple, thirdApple);
	}

	private Apple getNewApple() {
		return generator.generateApple(snake, BOARD_SIZE);
	}
	
	// аблоко не может быть за пределами доски 
	@Test
	public void shouldAppleAlwaysAtTheBoard() {
		// тут поставил цикл, чтобы проверить что никогда
		// яблоко не генерится за пределами доски
		for (int countRun = 0; countRun < 100000; countRun ++) { 
			Apple apple = getNewApple(); 
			
			assertTrue("яблоко должно быть в перделах доски по оси X", apple.getX() < BOARD_SIZE);
			assertTrue("яблоко должно быть в перделах доски по оси Y", apple.getY() < BOARD_SIZE);
			assertTrue("яблоко должно быть в перделах доски по оси X", apple.getX() >= 0);
			assertTrue("яблоко должно быть в перделах доски по оси Y", apple.getY() >= 0);
		}
	}
	
	// проверим что яблоки могут побывать везде на поле 
	@Test
	public void testRandomApplePosition() {
		int snakeHeadX = snake.getX();   
		int snakeHeadY = snake.getY(); 
		int snakeTailX = snakeHeadX - 1; 
		
		for (int y = 0; y < BOARD_SIZE; y ++) {
			for (int x = 0; x < BOARD_SIZE; x ++) {
				// яблоко не должно появляться на змее (она у нас 2 квадратика (голова и хвост)) 
				if (y == snakeHeadY && x == snakeTailX && x == snakeHeadY) { 
					continue; 
				}
				assertAppleInSomeGameAt(x, y);
			}
		}
	}
	
	/**
	 * Метод проверяет что за больше число запусков игр яблоко будет в заданной позиции хоть один раз.
	 * @param x координата x
	 * @param y координата y
	 */
	private void assertAppleInSomeGameAt(int x, int y) {	
		boolean found = isApplePresentInSomeGameAt(x, y);		
		assertTrue(String.format("Должен был быть найдено яблоко в позиции x:%s y:%s", x, y), found);
	}

	/**
	 * Метод говорит, что за больше число запусков игр яблоко будет в заданной позиции хоть один раз.
	 * @param x координата x
	 * @param y координата y
	 * @return true - если яблоко в этой координате появлялся
	 */
	private boolean isApplePresentInSomeGameAt(int x, int y) {
		boolean found = false;
		for (int countRun = 0; countRun < 100000; countRun ++) {
			Apple apple = getNewApple();
			
			found |= (x == apple.getX()) & (y == apple.getY());
			if (found) {
				break;
			}
		}
		return found;
	}
	
	// TODO яблоко не может появиться на змейке. 
	
	// TODO яблоко не может появиться на камнe. 
}
