package com.codenjoy.dojo.snake.model;

import static junit.framework.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import org.junit.Before;
import org.junit.Test;

import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.ArtifactGenerator;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

public class SnakeTest {

	private static final int BOARD_SIZE = 9;
	private Board board;
	private Snake snake;
	private Stone stone;
	private ArtifactGenerator generator = new HaveNothing();
    private Walls walls = new BasicWalls(BOARD_SIZE);
	
	@Before
	public void startGame() {
		board = new BoardImpl(generator, walls, BOARD_SIZE);
		snake = board.getSnake();
		stone = board.getStone();
	}
		
	// На поле появляется змейка 
	@Test
	public void shouldSnakeAtBoardWhenGameStart() {		
		assertNotNull("змейка должна быть", board.getSnake());
	}
	
	// змейка находится в центре экрана при старте игры
	// исправить координаты центры змейки на старте	
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
		board = new BoardImpl(generator, walls, 3);
		snake = board.getSnake();
		
		assertSnakeAt(1, 1);		
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
		new BoardImpl(generator, walls, 4);
	}
	
	// Направление движеня змейки изначально в право.
	@Test
	public void shouldSnakeHasRightDirectionWhenGameStart() {
		assertSnakeDirection(Direction.RIGHT); 		
	}

	/**
	 * Метод проверяет куда направлена змейка сейчас. 
	 */
	private void assertSnakeDirection(Direction expectedDirection) {
		assertEquals("направление движения змейки", 
				expectedDirection, snake.getDirection());		
	}
	
	// Поле содержит один камень для начала. 
	@Test
	public void shouldBoardContainStoneWhenGameStart() {
		Stone stone = board.getStone();
		assertNotNull("Поле должно содержать камень", stone);	
	}
		
	// камень будет (при каждом обращении к нему через доску) 
	// иметь разные координаты что недопустимо 
	@Test
	public void shouldSnakeAtOnePositionDurringOnegame() {
		assertSame(stone, board.getStone()); // если объект один и тот же, то и будет там те же координаты
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
	// один едиственный метод down() который будет перемещать змейку за следующий такт вниз.
	// такты будем отсчитывать соовтествующим методом.	
	// при перемещении вниз меняется координата Y в меньшую сторону - это и проверяем
	@Test
	public void shouldTurnDownWhenCallSnakeDown() {
		int oldY = snake.getY();
		
		snake.down();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна уменьшаться", oldY - 1, newY);
	}
	
	// теперь я могу проверить как змейка двигается по инерции вниз
	@Test 
	public void shouldGoDownInertia() {
		snake.down();
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вниз должна увеличится", oldY - 1, newY);
	}
	
	// проверить что при перемещении вниз координата X не меняется
	@Test
	public void shouldNotChangeXPositionWhenMoveDown() {
		int oldX = snake.getX();
		
		snake.down();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вниз не должна меняться", oldX, newX);
	}
	
    // проверить что при перемещении вверх меняется координата Y в большую сторону
	// координата 0,0 размещена в левом верхнем углу. Почему так? не знаю, наверное из прошлого привычка
	@Test
	public void shouldTurnUpWhenCallSnakeUp() {
		int oldY = snake.getY();
		
		snake.up();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при повороте змейки вниз должна уменьшиться", oldY + 1, newY);
	}
	
	// проверить что при перемещении вверх координата X не меняется
	@Test // как обычно - ломаем
	public void shouldNotChangeXPositionWhenMoveUp() {
		int oldX = snake.getX();
		
		snake.up();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при повороте змейки вверх не должна меняться", oldX, newX);
	}
	
	// проверить движение вверх по инерции
	@Test 
	public void shouldGoUpInertia() {
		snake.up();
		board.tact();
		
		int oldY = snake.getY();		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении змейки вверх должна уменьшиться", oldY + 1, newY);
	}
	
	// При движении в противоположном направлении 
	// если длинна змейки 2 клетки (голова и хвост) то она может развернуться
	@Test  
	public void shouldTurn180LeftRightWhenSnakeSizeIs2() {
		snake.left();
		board.tact();
		board.tact();
		snake.right();
		board.tact();
		board.tact();		
		snake.down();
		board.tact();
		board.tact();
		snake.up();
		board.tact();
		board.tact();
		snake.down();
		board.tact();
		board.tact();
	}
	
	// При движении в противоположном направлении 
	// если длинна змейки 3 клетки (голова и хвост) то она себя съедает
	@Test  
	public void shouldGameOverWhenSnakeEatItself() {
		getLong3Snake();
		
		snake.left();
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
		killSnake();
				
		snake.up();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeDownAfterGameOver() {
		killSnake();
		
		snake.down();
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeLeftAfterGameOver() {		
		killSnake();
		
		snake.left();
	}
	
	/**
	 * Метод убивающий змейку в начале игры. 
	 */
	private void killSnake() {
		// тут нам надо съесть хоть одно яблоко 
		generator = new HaveApples();
		((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());
		startGame();
		board.tact();

		// а потом укусить себя :)
		snake.left();
		board.tact();
		
		assertGameOver();
	}
	
	// проверить поворот вправо	
	@Test  
	public void shouldMoveRightWhenTurnRight() {
		snake.down();
		board.tact();

		int oldX = snake.getX();
		
		snake.right();
		board.tact();
				
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после поворота вправо должна увеличиться", oldX + 1, newX);
	}
	
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryToTurnSnakeRightAfterGameOver() {
		killSnake();
		
		snake.right();
	}
		
	// проверить как змея ест сама себя при движении вниз
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveDown() {
		// given
		getLong3Snake();
				
		snake.down();
		board.tact();
		
		// when
		snake.up();
		board.tact();
 
		//then
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении вверх
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveUp() {
		// given
		getLong3Snake();		
		snake.up();
		board.tact();
		
		// when
		snake.down();
		board.tact();
		
		// then
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении влево
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveLeft() {
		// given
		getLong3Snake();
		
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		
		//when 
		snake.right();
		board.tact();
		
		//then
		assertGameOver();
	}
	
	// проверить как змея ест сама себя при движении вправо
	@Test  
	public void shouldGameOverWhenSnakeEatItselfDuringMoveRight() {
		// given
		getLong3Snake();
		
		snake.down();
		board.tact();
		snake.right();
		board.tact();
		
		//when
		snake.left();
		board.tact();
		
		//then
		assertGameOver();
	}
	
	// проверить что при перемещении влево меняется координата X  в меньшую сторону
	@Test
	public void shouldChangeXPositionWhenTurnLeft() {
		snake.down();
		board.tact();
		
		int oldX = snake.getX();
		
		snake.left();
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X после перемещения влево уменьшается", oldX - 1, newX);
	}
	
	// проверить что при перемещении влево координата Y не меняется
	@Test
	public void shouldNotChangeYPositionWhenTurnLeft() {
		snake.down();
		board.tact();
		
		int oldY = snake.getY();
		
		snake.left();
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y после перемещения влево не должна меняться", oldY, newY);
	} 
	
	// проверить движение влево по инерции
	@Test
	public void shouldNotChangeYPositionWhenTurnLeftInertia() {
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		
		int oldY = snake.getY();
		
		board.tact();
		int newY = snake.getY();
		
		assertEquals("новая позиция по Y при движении влево по инерции не должна меняться", oldY, newY);
	}
	
	@Test
	public void shouldChangeXPositionWhenTurnLeftInertia() {
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		
		int oldX = snake.getX();
		
		board.tact();
		int newX = snake.getX();
		
		assertEquals("новая позиция по X при движении влево по инерции уменьшается", oldX - 1, newX);
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов 
	// начнем с простого - 1) змейка движется по инерции вправо и натыкается на камень
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveRight() {		
		startGameWithStoneAt(snake.getX() + 1, snake.getY()); // прямо на пути камень		

		board.tact();

		assertGameOver(); 
	}
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 2) двигаясь по инерции вниз пока не наткнется на камень
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveDown() {
		startGameWithStoneAt(snake.getX(), snake.getY() - 1); // внизу камень
		snake.down();
		
		board.tact();

		assertGameOver();
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 3) двигаясь по инерции вверх пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveUp() {		
		startGameWithStoneAt(snake.getX(), snake.getY() + 1); // вверху камень
		snake.up();
		
		board.tact();

		assertGameOver();
	} 
	
	// Если змейка наткнется на камень, то она умрет. 
	// наткнуться на камень можно одним из 4 способов
	// 4) двигаясь по инерции влево пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatStoneDurringMoveLeft() {		
		startGameWithStoneAt(snake.getX() - 1, snake.getY() - 1); // слева снизу камень
		snake.down();
		board.tact(); 
		snake.left();
		
		board.tact();

		assertGameOver();
	}
	
	/**
	 * Метод стартует игру с камнем в заданной позиции
	 * @param x позиция X камня 
	 * @param y позиция Y камня 
	 */
	private void startGameWithStoneAt(int x, int y) {		 	
		generator = new HaveStone(x, y);
		startGame();
	}
	
	/**
	 * Метод стартует игру с яблоком в заданной позиции (яблоко несъедаемое!)
	 * @param x позиция X яблока 
	 * @param y позиция Y яблока 
	 */
	private void startGameWithAppleAt(int x, int y) {
        appleAt(x, y);
		startGame();
	}
		
	class HaveNothing implements ArtifactGenerator {

		@Override
		public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
			return new Apple(-1, -1);
		}

		@Override
		public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
			return new Stone(-1, -1);
		}
		
	}
	
	class HaveApple implements ArtifactGenerator {
			
		private int x;
		private int y;

		public HaveApple(int x, int y) {
			this.x = x;
			this.y = y;
		}
				
		@Override
		public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
			return new Stone(-1, -1);
		}

		@Override
		public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
			return new Apple(x, y);
		}
	}
	
	class HaveApples implements ArtifactGenerator {
		
		private Queue<Apple> apples = new LinkedList<Apple>();

		public void addApple(int x, int y) {
			apples.add(new Apple(x, y));			
		}
		
		@Override
		public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
			return new Stone(-1, -1);
		}

		@Override
		public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
			if (apples.size() == 0) {
				return new Apple(-1, -1); // больше яблок не будет, мы его поставим за пределами поля
			}
			return apples.remove();
		}
	}
	
	class MixGenerators implements ArtifactGenerator  {
	
		private ArtifactGenerator apples;
		private ArtifactGenerator stones;

		public MixGenerators (ArtifactGenerator stones, ArtifactGenerator apples) {
			this.stones = stones;
			this.apples = apples;
		}

		@Override
		public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
			return apples.generateApple(snake, apple, stone, walls, boardSize);
		}

		@Override
		public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
			return stones.generateStone(snake, apple, walls, boardSize);
		}
	}
	
	class HaveStone implements ArtifactGenerator {
		
		private int x;
		private int y;

		public HaveStone(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
			return new Stone(x, y);
		}

		@Override
		public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
			return new Apple(-1, -1);
		}
	}

    class HaveStones implements ArtifactGenerator {

        private Queue<Stone> stones = new LinkedList<Stone>();

        public void addStone(int x, int y) {
            stones.add(new Stone(x, y));
        }

        @Override
        public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
            if (stones.size() == 0) {
                return new Stone(-1, -1); // больше камней не будет, мы его поставим за пределами поля
            }
            return stones.remove();
        }

        @Override
        public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return new Apple(-1, -1);
        }
    }
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 1) двигаясь по инерции влево пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveLeft() {
		snake.down();
		board.tact();
		snake.left();
		
		board.tact();
		board.tact();
		board.tact();
		board.tact();			

		assertGameOver();
	}
	
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 2) двигаясь по инерции вниз пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveDown() {				
		snake.down();
		
		board.tact();
		board.tact();
		board.tact();
		board.tact();

		assertGameOver();
	}
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 3) двигаясь по инерции вверх пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveUp() {				
		snake.up();
		
		board.tact(); 
		board.tact();
		board.tact();
		board.tact(); 					

		assertGameOver();
	}	
	
	// если змейка наткнется на одну из 4х стен, то она умрет. 
	// насткнуться на стену она может одним из 12 способов:
	// 4) двигаясь по инерции вправо пока не наткнется на стену
	@Test
	public void shouldGameOverWhenEatWallDurringMoveRight() {							
		board.tact();
		board.tact();
		board.tact();	
		board.tact();

		assertGameOver();
	}	
	
	// проверить что нельзя больше вызывать tick когда игра закончена
	@Test(expected = IllegalStateException.class)  
	public void shouldExceptionWhenTryTotactAfterGameOver() {
		killSnake();
		
		board.tact();
	}
	
	// яблоко может появиться в любом месте поля
	@Test
	public void shouldBoardContainAppleWhenGameStart() {
		Apple apple = board.getApple();
		assertNotNull("Поле должно содержать яблоко", apple);	
	}
	
	// после съедения яблока появляется тут же другое яблоко.
	@Test
	public void shouldAppearNewAppleWhenEatApple() {
		int appleX = snake.getX() + 1;
		int appleY = snake.getY();
		startGameWithAppleAt(appleX, appleY); // на пути змейки есть яблоко (оно там будет всегда появляться)
		board.tact();		
		
		Apple newApple = board.getApple();
		assertEquals(appleX, newApple.getX()); // потому координаты старого и нового яблока совпадают
		assertEquals(appleY, newApple.getY());
	}

    // после съедения камня появляется тут же другой камень.
    @Test
    public void shouldAppearNewStoneWhenEatStone() {
        int stoneX = snake.getX();
        int stoneY = snake.getY() + 1;

        getLongSnakeWithStoneAt(stoneX, stoneY, 11); // а вот тут только первый камень появится в заданном месте

        snake.up();
        board.tact();
        board.tact();

        Stone newStone = board.getStone();

        assertNotSame(stoneX, newStone.getX()); // потому координаты после съедания должны отличаться
        assertNotSame(stoneY, newStone.getY());
    }
	
	// Змейка может съесть яблоки и при этом ее длинна увеличится на 1. 
	@Test
	public void shouldSnakeIncreaseLengthWhenEatApple() {
		startGameWithAppleAt(snake.getX() + 1, snake.getY()); // на пути змейки есть яблоко
		board.tact();		
		
		assertEquals("Длинна змеи", 3, snake.getLength());
	}
	
	// теперь скушаем два раза яблоко :)
	@Test
	public void shouldSnakeIncreaseLengthTwiceWhenEatAppleTwice() {
		// на пути змейки есть два подряд яблока
		generator = new HaveApples();
		((HaveApples)generator).addApple(snake.getX() + 1, snake.getY()); // немного криво, но пока так TODO 
		((HaveApples)generator).addApple(snake.getX() + 2, snake.getY());
		startGame(); 
		
		board.tact();
		board.tact();
		board.tact();
		
		assertEquals("Длинна змеи", 4, snake.getLength());
	}
	
	// Если змейка съест сама себя - она умрет. 
	// Тут надо, чтобы змейка была нормальной длинны, чтобы иметь возможность съесть себя за хвост.	
	@Test
	public void shouldGameOverWhenEatItself() {		
		getLong5Snake();		
		
		// теперь попробуем укусить себя за хвост		
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		snake.up();
		board.tact();
		
		assertGameOver();	 
	}

	/**
	 * на пути змейки есть три подряд яблока, она увеличивается до размера, когда может себя съесть - 5
	 */
	private void getLong5Snake() {
		generator = new HaveApples();
		((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());  
		((HaveApples)generator).addApple(snake.getX() + 2, snake.getY());
		((HaveApples)generator).addApple(snake.getX() + 3, snake.getY());
		startGame(); 
		
		board.tact();
		board.tact();
		board.tact();		
		assertEquals("Длинна змеи", 5, snake.getLength());
	} 
	
	// хочу проверить, что змейка длинной в 4 никогда себя не съест.
	@Test
	public void shouldNotEatItselfWhenlengthIs4() {		
		getLong4Snake();		
		
		// теперь попробуем укусить себя за хвост - это не должно получиться		
		goOneCircle();
		goOneCircle();
		
		assertTrue("Змея должна быть жива!", snake.isAlive());
	}

	private void goOneCircle() {
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		snake.up();
		board.tact();
		snake.right();
		board.tact();
	}

	/**
	 * на пути змейки есть подряд два яблока, она увеличивается до размера, когда еще не может себя съесть - 4 
	 */
	private void getLong4Snake() {
		generator = new HaveApples();
		((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());  
		((HaveApples)generator).addApple(snake.getX() + 2, snake.getY());
		startGame(); 
		
		board.tact();
		board.tact();
		board.tact();		
		assertEquals("Длинна змеи", 4, snake.getLength());
	}
	
	/**
	 * на пути змейки есть одно яблоко, она увеличивается до размера когда может себя укусить разворачиваясь на 180 
	 */
	private void getLong3Snake() {
		generator = new HaveApples();
		((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());  
		startGame(); 
		
		board.tact();
		board.tact();		
		assertEquals("Длинна змеи", 3, snake.getLength());
	}
	
	// теперь давайте попробуем реализовать другое поведение - змейка может кушать камни, 
	// но тогда она сокращается в размере на 10 квадратиков.
	@Test
	public void shouldDivSnakeWhenEatStone (){ 
		getLongSnakeWithStoneAt(snake.getX(), snake.getY() + 1, 11);

		snake.up();
		board.tact();
		board.tact();
				
		assertEquals("Длинна змеи после съедения камня", 1, snake.getLength());
	}  
		
	/**
	 * Получаем змейку длинной в 11 и кмнем в заданной позиции
	 */
	private void getLongSnakeWithStoneAt(int x, int y, int snakeLength) {
        assertTrue(snakeLength <= 11);
		HaveApples appleGenerator = new HaveApples();
		if (snakeLength >= 3) appleGenerator.addApple(snake.getX() + 1, snake.getY());
        if (snakeLength >= 4) appleGenerator.addApple(snake.getX() + 2, snake.getY());
        if (snakeLength >= 5) appleGenerator.addApple(snake.getX() + 3, snake.getY());
        if (snakeLength >= 6) appleGenerator.addApple(snake.getX() + 4, snake.getY());
        if (snakeLength >= 7) appleGenerator.addApple(snake.getX() + 4, snake.getY() - 1);
        if (snakeLength >= 8) appleGenerator.addApple(snake.getX() + 3, snake.getY() - 1);
        if (snakeLength >= 9) appleGenerator.addApple(snake.getX() + 2, snake.getY() - 1);
        if (snakeLength >= 10) appleGenerator.addApple(snake.getX() + 1, snake.getY() - 1);
        if (snakeLength >= 11) appleGenerator.addApple(snake.getX()    , snake.getY() - 1);
		
		HaveStones stoneGenerator = new HaveStones();
        stoneGenerator.addStone(x, y);
        stoneGenerator.addStone(2, 2); // второй камень, так чтобы если вдруг съели его то он появился в другом месте

		generator = new MixGenerators(stoneGenerator, appleGenerator);
		
		startGame(); 
		
		board.tact();
		board.tact();
		board.tact();
		board.tact();
		snake.down();
		board.tact();
		snake.left();
		board.tact();
		board.tact();
		board.tact();
		board.tact();
		board.tact();
		board.tact();
		board.tact();
		snake.up();
		board.tact();	
		snake.right();
		board.tact();
		board.tact();
		board.tact();

// это так, чтобы было видно что на поле. Вообще такие тесты не стоит писать, потому что они очень сложные в поддержке		
//		System.out.println(new SnakePrinter().print(board));
//		@********
//		*       *
//		*       *
//		*   X   *
//		*000#   *
//		*0000000*
//		*       *
//		*       *
//		*********

		assertEquals("Длинна змеи", snakeLength, snake.getLength());
	}

    // когда змейка наткнется на стену на пределых поля - она умрет
    @Test
    public void shouldKillWhenEatWalls() {
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        assertGameOver();
    }

    // когда змейка наткнется на стену на пределых поля - она умрет
    @Test
    public void shouldNoMoreTactWhenGameOver() {
        board.getWalls().add(snake.getX() + 1, snake.getY()); // прямо на пути пользовательская стена

        board.tact();

        assertGameOver();
    }

    // а что, если змейка скушает камень а ее размер был 10? По идее геймовер
    @Test
    public void shouldGameOverWhen10LengthSnakeEatStone (){
        getLongSnakeWithStoneAt(snake.getX(), snake.getY() + 1, 10);

        snake.up();
        board.tact();

        assertGameOver();
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnRight() {
        startGameWithoutWalls();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertSnakeDirection(Direction.RIGHT);
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnDown() {
        startGameWithoutWalls();
        snake.down();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertSnakeDirection(Direction.DOWN);
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnUp() {
        startGameWithoutWalls();
        snake.up();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertSnakeDirection(Direction.UP);
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnLeft() {
        startGameWithoutWalls();
        snake.left();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertSnakeDirection(Direction.LEFT);
    }

    private void boardSizeTacts() {
        // за это время змейка должна была вернуться на свое место
        for (int count = 0; count < BOARD_SIZE; count++) {
            board.tact();
        }
    }

    private void startGameWithoutWalls() {
        walls = new Walls();
        startGame();
    }

    // проверить что если нет стен, и змейка проходит сквозь стены
    // она телепортировавшись натыкается на яблоко, которое должна съесть!
    @Test
    public void shouldEatAppleWhenTeleported() {
        appleAt(0, 4); // яблоко на границе
        startGameWithoutWalls();
        assertSnakeAt(4, 4);

        boardSizeTacts();  // в какой-то момент мы телепортируемся прям на яблочко

        assertSnakeSize(3); // и длинна должна стать на 1 больше
    }

    private void appleAt(int x, int y) {
        generator = new HaveApple(x, y);
    }

    // проверить что maxlength увеличивается и не меняется после съедания каменя
    @Test
    public void shouldIncreaseMaxLength() {
        HaveApples appleGenerator = new HaveApples();
        appleGenerator.addApple(snake.getX() + 1, snake.getY());
        appleGenerator.addApple(snake.getX() + 2, snake.getY());
        appleGenerator.addApple(snake.getX() + 3, snake.getY());
        appleGenerator.addApple(snake.getX() + 3, snake.getY() - 1);
        appleGenerator.addApple(snake.getX() + 2, snake.getY() - 1);
        appleGenerator.addApple(snake.getX() + 1, snake.getY() - 1);
        appleGenerator.addApple(snake.getX()    , snake.getY() - 1);
        appleGenerator.addApple(snake.getX() - 1, snake.getY() - 1);
        appleGenerator.addApple(snake.getX() - 2, snake.getY() - 1);

        HaveStones stoneGenerator = new HaveStones();
        stoneGenerator.addStone(snake.getX() - 3, snake.getY() - 1);

        appleGenerator.addApple(snake.getX() - 3, snake.getY() - 2);
        appleGenerator.addApple(snake.getX() - 2, snake.getY() - 2);
        appleGenerator.addApple(snake.getX() - 1, snake.getY() - 2);
        appleGenerator.addApple(snake.getX()    , snake.getY() - 2);
        appleGenerator.addApple(snake.getX() + 1, snake.getY() - 2);
        appleGenerator.addApple(snake.getX() + 2, snake.getY() - 2);
        appleGenerator.addApple(snake.getX() + 3, snake.getY() - 2);
        appleGenerator.addApple(snake.getX() + 3, snake.getY() - 3);
        appleGenerator.addApple(snake.getX() + 2, snake.getY() - 3);
        appleGenerator.addApple(snake.getX() + 1, snake.getY() - 3);
        appleGenerator.addApple(snake.getX()    , snake.getY() - 3);
        appleGenerator.addApple(snake.getX() - 1, snake.getY() - 3);
        appleGenerator.addApple(snake.getX() - 2, snake.getY() - 3);
        appleGenerator.addApple(snake.getX() - 3, snake.getY() - 3);

        generator = new MixGenerators(stoneGenerator, appleGenerator);

        startGame();

        board.tact();
        board.tact();
        board.tact();
        snake.down();
        board.tact();
        snake.left();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        board.tact(); // съели камень
        // проверили что длинна изменилась, но не maxlength
        assertEquals(11, board.getMaxLength());
        assertEquals(1, snake.getLength());
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼◄      ☼\n" +
                "☼☺      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                board.toString());

        snake.down();
        board.tact();
        snake.right();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        snake.down();
        board.tact();
        snake.left();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        // проверили что и длинна изменилась и maxlength
        assertEquals(15, board.getMaxLength());
        assertEquals(15, snake.getLength());
    }

}
