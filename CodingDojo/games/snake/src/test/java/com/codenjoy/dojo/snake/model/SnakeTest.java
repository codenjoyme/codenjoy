package com.codenjoy.dojo.snake.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.ArtifactGenerator;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.Stone;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SnakeTest {

    private static final int BOARD_SIZE = 9;
    private static final int SNAKE_SIZE = 2;
    private Field board;
    private Hero snake;
    private Stone stone;
    private ArtifactGenerator generator = new HaveNothing();
    private EventListener listener;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        givenBoard(BOARD_SIZE);
    }
        
    // На поле появляется змейка 
    @Test
    public void shouldSnakeAtBoardWhenGameStart() {        
        assertNotSame(null, board.snake());
    }
    
    // змейка находится в центре экрана при старте игры
    // исправить координаты центры змейки на старте    
    @Test
    public void shouldSnakeAtCenterOfBoardWhenGameStart() {
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘►   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
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
        givenBoard(3);

        asrtBrd("☼☼☼\n" +
                "☼►☼\n" +
                "☼☼☼\n");
    }

    // Поле имеет квадрутную форму, кратную двум + 1.
    // Тут просто, если мы зададим размер поля какой-то другой, то он увеличится на 1
    @Test
    public void shouldSnakeAtCenter_sizeIsOdd() {
        givenBoard(4);

        // TODO сам понимаешь что-то тут не то :)
        asrtBrd("     \n" +
                "☼☼☼☼ \n" +
                "☼╘►☼ \n" +
                "☼  ☼ \n" +
                "☼☼☼☼ \n");
    }

    void givenBoard(int size) {
        givenBoard(size, new BasicWalls(size), SNAKE_SIZE);
    }

    private void givenBoard(int size, Walls walls, int snakeSize) {
        board = new Snake(generator, walls, size, snakeSize);
        listener = mock(EventListener.class);
        board.newGame(new Player(listener));
        snake = board.snake();
        stone = board.getStone();
    }

    @Test
    public void shouldSnakeAtCenter_sizeIs5() {
        givenBoard(5);

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼╘► ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
    
    // Змейка размером в две клеточки. 
    @Test
    public void shouldSnakeLengthIs2_whenStartGame() {
        assertEquals(2, snake.getLength());
    }

    // Если змейка изначально размером в три клеточки, то она проявится не сразу
    // она как бы выползает из пещеры
    @Test
    public void shouldSnakeLengthIs5_ifYouWant() {
        givenBoard(BOARD_SIZE, new BasicWalls(BOARD_SIZE), 5);

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘►   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(4, 4);
        assertEquals(5, snake.getLength());

        // when
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘═►  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(5, 4);
        assertEquals(5, snake.getLength());

        // when
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘══► ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(6, 4);
        assertEquals(5, snake.getLength());

        // when
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘═══►☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(7, 4);
        assertEquals(5, snake.getLength());

        // when
        snake.up();
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ▲☼\n" +
                "☼   ╘══╝☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(7, 5);
        assertEquals(5, snake.getLength());

        // when
        snake.up();
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ▲☼\n" +
                "☼      ║☼\n" +
                "☼    ╘═╝☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertSnakeAt(7, 6);
        assertEquals(5, snake.getLength());
    }
    
    // Направление движеня змейки изначально в право.
    @Test
    public void shouldSnakeHasRightDirection_whenGameStart() {
        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘►   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    // Если камня нет, то его координаты -1, -1
    @Test
    public void shouldBoardContainStone_whenGameStart() {
        // when
        Stone stone = board.getStone();

        // then
        assertEquals("[-1,-1]", stone.toString());
    }
        
    // камень (при каждом обращении к нему через доску)
    // убдет возвращать один и тот же объект
    @Test
    public void shouldSnakeAtOnePosition_duringOneGame() {
        assertSame(stone, board.getStone());
    }
        
    @Test
    public void shouldGoRight_inertia() {
        // given
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼  ╘►   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼   ╘►  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        board.tick();

        // then
        asrtBrd("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼    ╘► ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTurnDown_whenCallSnakeDown() {
        int oldY = snake.getY();
        
        snake.down();
        board.tick();
        int newY = snake.getY();
        
        assertEquals(oldY - 1, newY);
    }
    
    // теперь я могу проверить как змейка двигается по инерции вниз
    @Test 
    public void shouldGoDownInertia() {
        snake.down();
        board.tick();
        
        int oldY = snake.getY();        
        board.tick();
        int newY = snake.getY();
        
        assertEquals("новая позиция по Y при движении змейки вниз должна увеличится", oldY - 1, newY);
    }
    
    // проверить что при перемещении вниз координата X не меняется
    @Test
    public void shouldNotChangeXPositionWhenMoveDown() {
        int oldX = snake.getX();
        
        snake.down();
        board.tick();
        int newX = snake.getX();
        
        assertEquals("новая позиция по X при повороте змейки вниз не должна меняться", oldX, newX);
    }
    
    // проверить что при перемещении вверх меняется координата Y в большую сторону
    // координата 0,0 размещена в левом верхнем углу. Почему так? не знаю, наверное из прошлого привычка
    @Test
    public void shouldTurnUpWhenCallSnakeUp() {
        int oldY = snake.getY();
        
        snake.up();
        board.tick();
        int newY = snake.getY();
        
        assertEquals("новая позиция по Y при повороте змейки вниз должна уменьшиться", oldY + 1, newY);
    }
    
    // проверить что при перемещении вверх координата X не меняется
    @Test // как обычно - ломаем
    public void shouldNotChangeXPositionWhenMoveUp() {
        int oldX = snake.getX();
        
        snake.up();
        board.tick();
        int newX = snake.getX();
        
        assertEquals("новая позиция по X при повороте змейки вверх не должна меняться", oldX, newX);
    }
    
    // проверить движение вверх по инерции
    @Test 
    public void shouldGoUpInertia() {
        snake.up();
        board.tick();
        
        int oldY = snake.getY();        
        board.tick();
        int newY = snake.getY();
        
        assertEquals("новая позиция по Y при движении змейки вверх должна уменьшиться", oldY + 1, newY);
    }
    
    // При движении в противоположном направлении 
    // если длинна змейки 2 клетки (голова и хвост) то она может развернуться
    @Test  
    public void shouldTurn180LeftRightWhenSnakeSizeIs2() {
        snake.left();
        board.tick();
        board.tick();
        snake.right();
        board.tick();
        board.tick();        
        snake.down();
        board.tick();
        board.tick();
        snake.up();
        board.tick();
        board.tick();
        snake.down();
        board.tick();
        board.tick();
    }
    
    // При движении в противоположном направлении 
    // если длинна змейки 3 клетки (голова и хвост) то она себя съедает
    @Test  
    public void shouldGameOverWhenSnakeEatItself() {
        getLong3Snake();
        
        snake.left();
        board.tick();    
        
        assertGameOver();
    }
        
    /**
     * Метод првоеряет, что игра окончена
     */
    private void assertGameOver() {
        assertTrue("Ожидается конец игры", !snake.isAlive());
    }
    
    // Умрет - значит конец игры. Если конец игры, значит любое обращение 
    // к доске (методам доски) ничего не меняет.
    @Test
    public void shouldDoNothingWhenTryToTurnSnakeUpAfterGameOver() {
        killSnake();

        Direction direction = snake.getDirection();

        snake.up();

        assertEquals(direction, snake.getDirection());
    }

    @Test
    public void shouldDoNothingWhenTryToTurnSnakeDownAfterGameOver() {
        killSnake();

        Direction direction = snake.getDirection();

        snake.down();

        assertEquals(direction, snake.getDirection());
    }

    @Test
    public void shouldDoNothingWhenTryToTurnSnakeLeftAfterGameOver() {
        killSnakeWhenMoveRight();

        Direction direction = snake.getDirection();

        snake.left();

        assertEquals(direction, snake.getDirection());
    }

    /**
     * Метод убивающий змейку в начале игры.
     */
    private void killSnake() {
        // тут нам надо съесть хоть одно яблоко
        generator = new HaveApples();
        ((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());
        setup();
        board.tick();

        // а потом укусить себя :)
        snake.left();
        board.tick();
        
        assertGameOver();
    }

    private void killSnakeWhenMoveRight() {
        // тут нам надо съесть хоть одно яблоко
        generator = new HaveApples();
        ((HaveApples)generator).addApple(snake.getX() - 1, snake.getY());
        setup();
        board.tick();

        snake.left();
        board.tick();
        board.tick();

        // а потом укусить себя :)
        snake.right();
        board.tick();

        assertGameOver();
    }
    
    // проверить поворот вправо    
    @Test  
    public void shouldMoveRightWhenTurnRight() {
        snake.down();
        board.tick();

        int oldX = snake.getX();
        
        snake.right();
        board.tick();
                
        int newX = snake.getX();
        
        assertEquals("новая позиция по X после поворота вправо должна увеличиться", oldX + 1, newX);
    }
    
    @Test
    public void shouldDoNothingWhenTryToTurnSnakeRightAfterGameOver() {
        killSnake();

        Direction direction = snake.getDirection();

        snake.right();

        assertEquals(direction, snake.getDirection());
    }
        
    // проверить как змея ест сама себя при движении вниз
    @Test  
    public void shouldGameOverWhenSnakeEatItselfDuringMoveDown() {
        // given
        getLong3Snake();
                
        snake.down();
        board.tick();
        
        // when
        snake.up();
        board.tick();
 
        //then
        assertGameOver();
    }
    
    // проверить как змея ест сама себя при движении вверх
    @Test  
    public void shouldGameOverWhenSnakeEatItselfDuringMoveUp() {
        // given
        getLong3Snake();        
        snake.up();
        board.tick();
        
        // when
        snake.down();
        board.tick();
        
        // then
        assertGameOver();
    }
    
    // проверить как змея ест сама себя при движении влево
    @Test  
    public void shouldGameOverWhenSnakeEatItselfDuringMoveLeft() {
        // given
        getLong3Snake();
        
        snake.down();
        board.tick();
        snake.left();
        board.tick();
        
        //when 
        snake.right();
        board.tick();
        
        //then
        assertGameOver();
    }
    
    // проверить как змея ест сама себя при движении вправо
    @Test  
    public void shouldGameOverWhenSnakeEatItselfDuringMoveRight() {
        // given
        getLong3Snake();
        
        snake.down();
        board.tick();
        snake.right();
        board.tick();
        
        //when
        snake.left();
        board.tick();
        
        //then
        assertGameOver();
    }
    
    // проверить что при перемещении влево меняется координата X  в меньшую сторону
    @Test
    public void shouldChangeXPositionWhenTurnLeft() {
        snake.down();
        board.tick();
        
        int oldX = snake.getX();
        
        snake.left();
        board.tick();
        int newX = snake.getX();
        
        assertEquals("новая позиция по X после перемещения влево уменьшается", oldX - 1, newX);
    }
    
    // проверить что при перемещении влево координата Y не меняется
    @Test
    public void shouldNotChangeYPositionWhenTurnLeft() {
        snake.down();
        board.tick();
        
        int oldY = snake.getY();
        
        snake.left();
        board.tick();
        int newY = snake.getY();
        
        assertEquals("новая позиция по Y после перемещения влево не должна меняться", oldY, newY);
    } 
    
    // проверить движение влево по инерции
    @Test
    public void shouldNotChangeYPositionWhenTurnLeftInertia() {
        snake.down();
        board.tick();
        snake.left();
        board.tick();
        
        int oldY = snake.getY();
        
        board.tick();
        int newY = snake.getY();
        
        assertEquals("новая позиция по Y при движении влево по инерции не должна меняться", oldY, newY);
    }
    
    @Test
    public void shouldChangeXPositionWhenTurnLeftInertia() {
        snake.down();
        board.tick();
        snake.left();
        board.tick();
        
        int oldX = snake.getX();
        
        board.tick();
        int newX = snake.getX();
        
        assertEquals("новая позиция по X при движении влево по инерции уменьшается", oldX - 1, newX);
    } 
    
    // Если змейка наткнется на камень, то она умрет. 
    // наткнуться на камень можно одним из 4 способов 
    // начнем с простого - 1) змейка движется по инерции вправо и натыкается на камень
    @Test
    public void shouldGameOverWhenEatStoneDurringMoveRight() {        
        startGameWithStoneAt(snake.getX() + 1, snake.getY()); // прямо на пути камень        

        board.tick();

        assertGameOver(); 
    }
    
    // Если змейка наткнется на камень, то она умрет. 
    // наткнуться на камень можно одним из 4 способов
    // 2) двигаясь по инерции вниз пока не наткнется на камень
    @Test
    public void shouldGameOverWhenEatStoneDurringMoveDown() {
        startGameWithStoneAt(snake.getX(), snake.getY() - 1); // внизу камень
        snake.down();
        
        board.tick();

        assertGameOver();
    } 
    
    // Если змейка наткнется на камень, то она умрет. 
    // наткнуться на камень можно одним из 4 способов
    // 3) двигаясь по инерции вверх пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatStoneDurringMoveUp() {        
        startGameWithStoneAt(snake.getX(), snake.getY() + 1); // вверху камень
        snake.up();
        
        board.tick();

        assertGameOver();
    } 
    
    // Если змейка наткнется на камень, то она умрет. 
    // наткнуться на камень можно одним из 4 способов
    // 4) двигаясь по инерции влево пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatStoneDurringMoveLeft() {        
        startGameWithStoneAt(snake.getX() - 1, snake.getY() - 1); // слева снизу камень
        snake.down();
        board.tick(); 
        snake.left();
        
        board.tick();

        assertGameOver();
    }
    
    /**
     * Метод стартует игру с камнем в заданной позиции
     * @param x позиция X камня 
     * @param y позиция Y камня 
     */
    private void startGameWithStoneAt(int x, int y) {             
        generator = new HaveStone(x, y);
        setup();
    }
    
    /**
     * Метод стартует игру с яблоком в заданной позиции (яблоко несъедаемое!)
     * @param x позиция X яблока 
     * @param y позиция Y яблока 
     */
    private void startGameWithAppleAt(int x, int y) {
        appleAt(x, y);
        setup();
    }
        
    class HaveNothing implements ArtifactGenerator {

        @Override
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return new Apple(-1, -1);
        }

        @Override
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
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
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
            return new Stone(-1, -1);
        }

        @Override
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return new Apple(x, y);
        }
    }
    
    class HaveApples implements ArtifactGenerator {
        
        private Queue<Apple> apples = new LinkedList<Apple>();

        public void addApple(int x, int y) {
            apples.add(new Apple(x, y));            
        }
        
        @Override
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
            return new Stone(-1, -1);
        }

        @Override
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
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
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return apples.generateApple(snake, apple, stone, walls, boardSize);
        }

        @Override
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
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
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
            return new Stone(x, y);
        }

        @Override
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return new Apple(-1, -1);
        }
    }

    class HaveStones implements ArtifactGenerator {

        private Queue<Stone> stones = new LinkedList<Stone>();

        public void addStone(int x, int y) {
            stones.add(new Stone(x, y));
        }

        @Override
        public Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize) {
            if (stones.size() == 0) {
                return new Stone(-1, -1); // больше камней не будет, мы его поставим за пределами поля
            }
            return stones.remove();
        }

        @Override
        public Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize) {
            return new Apple(-1, -1);
        }
    }
    
    // если змейка наткнется на одну из 4х стен, то она умрет. 
    // насткнуться на стену она может одним из 12 способов:
    // 1) двигаясь по инерции влево пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatWallDurringMoveLeft() {
        snake.down();
        board.tick();
        snake.left();
        
        board.tick();
        board.tick();
        board.tick();
        board.tick();            

        assertGameOver();
    }
    
    
    // если змейка наткнется на одну из 4х стен, то она умрет. 
    // насткнуться на стену она может одним из 12 способов:
    // 2) двигаясь по инерции вниз пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatWallDurringMoveDown() {                
        snake.down();
        
        board.tick();
        board.tick();
        board.tick();
        board.tick();

        assertGameOver();
    }
    
    // если змейка наткнется на одну из 4х стен, то она умрет. 
    // насткнуться на стену она может одним из 12 способов:
    // 3) двигаясь по инерции вверх пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatWallDurringMoveUp() {                
        snake.up();
        
        board.tick(); 
        board.tick();
        board.tick();
        board.tick();                     

        assertGameOver();
    }    
    
    // если змейка наткнется на одну из 4х стен, то она умрет. 
    // насткнуться на стену она может одним из 12 способов:
    // 4) двигаясь по инерции вправо пока не наткнется на стену
    @Test
    public void shouldGameOverWhenEatWallDurringMoveRight() {                            
        board.tick();
        board.tick();
        board.tick();    
        board.tick();

        assertGameOver();
    }    

    // проверить что tick ничего не делает, когда игра закончена
    @Test
    public void shouldDoNothingWhenTryTotactAfterGameOver() {
        killSnake();
        assertGameOver();

        Point head = snake.getHead();
        int x = head.getX();
        int y = head.getY();

        board.tick();

        assertEquals(pt(x, y), snake.getHead());

        assertGameOver();
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
        board.tick();        
        
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
        board.tick();
        board.tick();

        Stone newStone = board.getStone();

        assertNotSame(stoneX, newStone.getX()); // потому координаты после съедания должны отличаться
        assertNotSame(stoneY, newStone.getY());
    }
    
    // Змейка может съесть яблоки и при этом ее длинна увеличится на 1. 
    @Test
    public void shouldSnakeIncreaseLengthWhenEatApple() {
        startGameWithAppleAt(snake.getX() + 1, snake.getY()); // на пути змейки есть яблоко
        board.tick();        
        
        assertEquals("Длинна змеи", 3, snake.getLength());
    }
    
    // теперь скушаем два раза яблоко :)
    @Test
    public void shouldSnakeIncreaseLengthTwiceWhenEatAppleTwice() {
        // на пути змейки есть два подряд яблока
        generator = new HaveApples();
        ((HaveApples)generator).addApple(snake.getX() + 1, snake.getY()); // немного криво, но пока так TODO 
        ((HaveApples)generator).addApple(snake.getX() + 2, snake.getY());
        setup();
        
        board.tick();
        board.tick();
        board.tick();
        
        assertEquals("Длинна змеи", 4, snake.getLength());
    }
    
    // Если змейка съест сама себя - она умрет. 
    // Тут надо, чтобы змейка была нормальной длинны, чтобы иметь возможность съесть себя за хвост.    
    @Test
    public void shouldGameOverWhenEatItself() {        
        getLong5Snake();        
        
        // теперь попробуем укусить себя за хвост        
        snake.down();
        board.tick();
        snake.left();
        board.tick();
        snake.up();
        board.tick();
        
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
        setup();
        
        board.tick();
        board.tick();
        board.tick();        
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
        board.tick();
        snake.left();
        board.tick();
        snake.up();
        board.tick();
        snake.right();
        board.tick();
    }

    /**
     * на пути змейки есть подряд два яблока, она увеличивается до размера, когда еще не может себя съесть - 4 
     */
    private void getLong4Snake() {
        generator = new HaveApples();
        ((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());  
        ((HaveApples)generator).addApple(snake.getX() + 2, snake.getY());
        setup();
        
        board.tick();
        board.tick();
        board.tick();        
        assertEquals("Длинна змеи", 4, snake.getLength());
    }
    
    /**
     * на пути змейки есть одно яблоко, она увеличивается до размера когда может себя укусить разворачиваясь на 180 
     */
    private void getLong3Snake() {
        generator = new HaveApples();
        ((HaveApples)generator).addApple(snake.getX() + 1, snake.getY());  
        setup();
        
        board.tick();
        board.tick();        
        assertEquals("Длинна змеи", 3, snake.getLength());
    }
    
    // теперь давайте попробуем реализовать другое поведение - змейка может кушать камни, 
    // но тогда она сокращается в размере на 10 квадратиков.
    @Test
    public void shouldDivSnakeWhenEatStone (){ 
        getLongSnakeWithStoneAt(snake.getX(), snake.getY() + 1, 11);

        snake.up();
        board.tick();
        board.tick();
                
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
        
        setup();
        
        board.tick();
        board.tick();
        board.tick();
        board.tick();
        snake.down();
        board.tick();
        snake.left();
        board.tick();
        board.tick();
        board.tick();
        board.tick();
        board.tick();
        board.tick();
        board.tick();
        snake.up();
        board.tick();    
        snake.right();
        board.tick();
        board.tick();
        board.tick();

// это так, чтобы было видно что на поле. Вообще такие тесты не стоит писать, потому что они очень сложные в поддержке        
//        System.out.println(new SnakePrinter().print(board));
//        @********
//        *       *
//        *       *
//        *   X   *
//        *000#   *
//        *0000000*
//        *       *
//        *       *
//        *********

        assertEquals("Длинна змеи", snakeLength, snake.getLength());
    }

    // когда змейка наткнется на стену на пределых поля - она умрет
    @Test
    public void shouldKillWhenEatWalls() {
        board.tick();
        board.tick();
        board.tick();
        board.tick();

        assertGameOver();
    }

    // когда змейка наткнется на стену на пределых поля - она умрет
    @Test
    public void shouldNoMoreTactWhenGameOver() {
        board.getWalls().add(snake.getX() + 1, snake.getY()); // прямо на пути пользовательская стена

        board.tick();

        assertGameOver();
    }

    // а что, если змейка скушает камень а ее размер был 10? По идее геймовер
    @Test
    public void shouldGameOverWhen10LengthSnakeEatStone (){
        getLongSnakeWithStoneAt(snake.getX(), snake.getY() + 1, 10);

        snake.up();
        board.tick();

        assertGameOver();
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnRight() {
        startGameWithoutWalls();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnDown() {
        startGameWithoutWalls();
        snake.down();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertEquals(Direction.DOWN, snake.getDirection());
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnUp() {
        startGameWithoutWalls();
        snake.up();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertEquals(Direction.UP, snake.getDirection());
    }

    // проверить что если нет стен, то змейка проходит сквозь стены без смерти
    @Test
    public void shouldTeleportWhenTurnLeft() {
        startGameWithoutWalls();
        snake.left();
        assertSnakeAt(4, 4);

        boardSizeTacts();

        assertSnakeAt(4, 4);
        assertEquals(Direction.LEFT, snake.getDirection());
    }

    private void boardSizeTacts() {
        // за это время змейка должна была вернуться на свое место
        for (int count = 0; count < BOARD_SIZE; count++) {
            board.tick();
        }
    }

    private void startGameWithoutWalls() {
        givenBoard(BOARD_SIZE, new Walls(), SNAKE_SIZE);
    }

    // проверить что если нет стен, и змейка проходит сквозь стены
    // она телепортировавшись натыкается на яблоко, которое должна съесть!
    @Test
    public void shouldEatAppleWhenTeleported() {
        appleAt(0, 4); // яблоко на границе
        startGameWithoutWalls();
        assertSnakeAt(4, 4);

        boardSizeTacts();  // в какой-то момент мы телепортируемся прям на яблочко

        assertEquals(3, snake.getLength());
    }

    private void appleAt(int x, int y) {
        generator = new HaveApple(x, y);
    }

    @Test
    public void shouldGameReturnsRealJoystick() {
        killSnake();

        assertGameOver();
        Joystick joystick = board.snake();

        // when
        board.newGame(new Player(mock(EventListener.class)));

        // then
        assertNotSame(joystick, board.snake());
    }

    private void asrtBrd(String expected) {
        assertEquals(expected, printer.getPrinter(
                board.reader(), null).print());
    }


}
