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
					
		assertStoneChangePosition(firstStone, secondStone, thirdStone);		
	}

	private Stone getNewStone() {
		return generator.generateStone(snake, BOARD_SIZE);
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
	
	// камень может быть за пределами доски, а должен быть всегда на доске! Это бага
	@Test
	public void shouldStoneAlwaysAtTheBoard() {
		// тут поставил цикл, чтобы проверить что никогда
		// камень не генерится за пределами доски
		for (int countRun = 0; countRun < 100000; countRun ++) { 
			Stone stone = getNewStone(); 
			
			assertTrue("камень должен быть в перделах доски по оси X", stone.getX() <= BOARD_SIZE);
			assertTrue("камень должен быть в перделах доски по оси Y", stone.getY() <= BOARD_SIZE);
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
	 * Метод проверяет что за больше число запусков игр камень не будет в заданной позиции никогда.
	 * @param x координата x
	 * @param y координата y
	 */
	private void assertStoneNotFoundAt(int x, int y) {	
		boolean found = isStonePresentInSomeGameAt(x, y);		
		assertFalse(String.format("Камень никогда не должен был появляться в позиции x:%s y:%s", x, y), found);
	}
}
