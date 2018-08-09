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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.Stone;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrinterTest {

    private static final int BOARD_SIZE = 7;
    private Printer printer;
    private Hero snake;
    private Field board;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();
    private EventListener listener = mock(EventListener.class);

    @Before
    public void init() {
        board = mock(Field.class);
        when(board.getSize()).thenReturn(BOARD_SIZE);
        when(board.getApple()).thenReturn(null);
        when(board.getStone()).thenReturn(null);
        when(board.getWalls()).thenReturn(new Walls());
        when(board.snake()).thenReturn(null);

        printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return BOARD_SIZE;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new HashSet<Point>(){{
                    board.getWalls().forEach(this::add);
                    if (board.snake() != null) {
                        board.snake().forEach(this::add);
                    }
                    add(board.getApple());
                    add(board.getStone());
                    remove(null);
                }};
            }
        }, null);
    }

    @Test
    public void checkCleanBoard() {
        assertEquals("       \n       \n       \n       \n       \n       \n       \n", printer.print());
    }

    @Test
    public void checkPrintWall() {
        Walls walls = new Walls();
        walls.add(2, 2);
        walls.add(3, 3);
        walls.add(4, 4);
        when(board.getWalls()).thenReturn(walls);

        assertEquals(
                "       \n" +
                "       \n" +
                "    ☼  \n" +
                "   ☼   \n" +
                "  ☼    \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintBasicWalls() {   // тут тестируем больше BasicWalls чем printer
        when(board.getWalls()).thenReturn(new BasicWalls(BOARD_SIZE));

        assertEquals(
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n", printer.print());
    }

    @Test
    public void checkPrintApple() {
        when(board.getApple()).thenReturn(new Apple(2, 2));

        assertEquals(
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "  ☺    \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintStone() {
        when(board.getStone()).thenReturn(new Stone(4, 4));

        assertEquals(
                "       \n" +
                "       \n" +
                "    ☻  \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.print());
    }

    @Test
    public void checkPrintSnake() {
        shouldSnake();
        moveUp();
        moveRight();
        moveUp();
        moveRight();

        assertSnake( 
                "       \n" +
                "    ╔► \n" +
                "   ╔╝  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    private void assertSnake(String expected) {
        when(board.snake()).thenReturn(snake);
        assertEquals(expected, printer.print());
    }

    private void moveUp() {
        move(0, 1);
        snake.up();
    }

    private void move(int dx, int dy) {
        snake.move(snake.getX() + dx, snake.getY() + dy);
        snake.grow();
    }

    private void moveDown() {
        move(0, -1);
        snake.down();
    }

    private void moveLeft() {
        move(-1, 0);
        snake.left();
    }

    private void shouldSnake() {
        snake = new Hero(3, 3);

        when(board.createSnake()).thenReturn(snake);
        listener = mock(EventListener.class);
        Player player = new Player(listener);
        player.newHero(board);

        snake.right();
    }

    private void moveRight() {
        move(1, 0);
        snake.right();
    }

    @Test
    public void checkPrintSnakeTailRight() {
        shouldSnake();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╘►   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailLeft() {
        shouldSnake();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ◄╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailDown() {
        shouldSnake();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ▼   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailUp() {
        shouldSnake();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "   ▲   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailVerticalUp() {
        shouldSnake();
        moveUp();
        moveUp();
        assertSnake(
                "       \n" +
                "   ▲   \n" +
                "   ║   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailVerticalDown() {
        shouldSnake();
        moveDown();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ║   \n" +
                "   ▼   \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailHorizontalLeft() {
        shouldSnake();
        moveLeft();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                " ◄═╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }


    @Test
    public void checkPrintSnakeTailHorizontalRight() {
        shouldSnake();
        moveRight();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘═► \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateLeftUp() {
        shouldSnake();
        moveLeft();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "  ▲    \n" +
                "  ╚╕   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateLeftDown() {
        shouldSnake();
        moveLeft();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "  ╔╕   \n" +
                "  ▼    \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateUpLeft() {
        shouldSnake();
        moveUp();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "  ◄╗   \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateUpRight() {
        shouldSnake();
        moveUp();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "   ╔►  \n" +
                "   ╙   \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateDownLeft() {
        shouldSnake();
        moveDown();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ◄╝   \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateDownRight() {
        shouldSnake();
        moveDown();
        moveRight();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "   ╚►  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailRotateRightDown() {
        shouldSnake();
        moveRight();
        moveDown();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╘╗  \n" +
                "    ▼  \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnakeTailHorizontalRightUp() {
        shouldSnake();
        moveRight();
        moveUp();
        assertSnake(
                "       \n" +
                "       \n" +
                "    ▲  \n" +
                "   ╘╝  \n" +
                "       \n" +
                "       \n" +
                "       \n");
    }

    @Test
    public void checkPrintSnake2() {
        shouldSnake();
        moveDown();
        moveLeft();
        moveDown();
        moveLeft();
        assertSnake(
                "       \n" +
                "       \n" +
                "       \n" +
                "   ╓   \n" +
                "  ╔╝   \n" +
                " ◄╝    \n" +
                "       \n");
    }


}
