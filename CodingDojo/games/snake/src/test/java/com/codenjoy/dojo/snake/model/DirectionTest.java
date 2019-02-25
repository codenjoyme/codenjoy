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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.artifacts.EmptySpace;
import com.codenjoy.dojo.snake.model.artifacts.Tail;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DirectionTest {

    private Field board;
    private Hero snake;
    private EventListener listener;

    @Before
    public void setup() {
        board = mock(Field.class);
        when(board.getSize()).thenReturn(100);
        when(board.getAt(any(PointImpl.class))).thenReturn(new EmptySpace(pt(0, 0)));

        snake = new Hero(50, 50);
        when(board.createSnake()).thenReturn(snake);
        listener = mock(EventListener.class);
        Player player = new Player(listener);
        player.newHero(board);
    }

    @Test
    public void shouldSnakeTailDirectionInvertedToHead_whenSnakeLengthIs2() {
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);

        snakeUp();
        assertHeadAntTail(50, 51, Direction.UP, Direction.DOWN);

        snakeLeft();
        assertHeadAntTail(49, 51, Direction.LEFT, Direction.RIGHT);

        snakeDown();
        assertHeadAntTail(49, 50, Direction.DOWN, Direction.UP);

        snakeRight();
        assertHeadAntTail(50, 50, Direction.RIGHT, Direction.LEFT);
    }

    @Test
    public void shouldSnakeTailDirection_whenSnakeLengthIs3() {
        snakeGrow();
        assertHeadAntTail(51, 50, Direction.RIGHT, Direction.LEFT);

        snakeUp();
        assertHeadAntTail(51, 51, Direction.UP, Direction.LEFT);

        snakeLeft();
        assertHeadAntTail(50, 51, Direction.LEFT, Direction.DOWN);

        snakeDown();
        assertHeadAntTail(50, 50, Direction.DOWN, Direction.RIGHT);

        snakeRight();
        assertHeadAntTail(51, 50, Direction.RIGHT, Direction.UP);

        snakeWalk();
        assertHeadAntTail(52, 50, Direction.RIGHT, Direction.LEFT);
    }

    @Test
    public void shouldBodyDirection_whenSnakeLengthIs3_goCounterclockwise() {
        snakeGrow();
        assertBody(50, 50, BodyDirection.HORIZONTAL);

        snakeUp();
        assertBody(51, 50, BodyDirection.TURNED_LEFT_UP);

        snakeUp();
        assertBody(51, 51, BodyDirection.VERTICAL);

        snakeLeft();
        assertBody(51, 52, BodyDirection.TURNED_LEFT_DOWN);

        snakeLeft();
        assertBody(50, 52, BodyDirection.HORIZONTAL);

        snakeDown();
        assertBody(49, 52, BodyDirection.TURNED_RIGHT_DOWN);

        snakeDown();
        assertBody(49, 51, BodyDirection.VERTICAL);

        snakeRight();
        assertBody(49, 50, BodyDirection.TURNED_RIGHT_UP);

        snakeRight();
        assertBody(50, 50, BodyDirection.HORIZONTAL);
    }

    @Test
    public void shouldBodyDirection_whenSnakeLengthIs3_goClockwise() {
        snakeGrow();
        assertBody(50, 50, BodyDirection.HORIZONTAL);

        snakeDown();
        assertBody(51, 50, BodyDirection.TURNED_LEFT_DOWN);

        snakeDown();
        assertBody(51, 49, BodyDirection.VERTICAL);

        snakeLeft();
        assertBody(51, 48, BodyDirection.TURNED_LEFT_UP);

        snakeLeft();
        assertBody(50, 48, BodyDirection.HORIZONTAL);

        snakeUp();
        assertBody(49, 48, BodyDirection.TURNED_RIGHT_UP);

        snakeUp();
        assertBody(49, 49, BodyDirection.VERTICAL);

        snakeRight();
        assertBody(49, 50, BodyDirection.TURNED_RIGHT_DOWN);

        snakeRight();
        assertBody(50, 50, BodyDirection.HORIZONTAL);
    }

    @Test
    public void shouldSnakeIteratorStartsFromHead() {
        snakeGrow();

        Iterator<Tail> iterator = snake.iterator();
        Tail head = iterator.next();
        Tail body = iterator.next();
        Tail tail = iterator.next();

        assertTrue(snake.itsMyHead(head));
        assertTrue(snake.itsMyBody(body));
        assertTrue(snake.itsMyTail(tail));
    }


    private void snakeWalk() {
        snake.walk(board);
    }

    private void snakeGrow() {
        snake.grow();
        snakeWalk();
    }

    private void snakeRight() {
        snake.right();
        snakeWalk();
    }

    private void snakeDown() {
        snake.down();
        snakeWalk();
    }

    private void snakeLeft() {
        snake.left();
        snakeWalk();
    }

    private void snakeUp() {
        snake.up();
        snakeWalk();
    }

    private void assertHeadAntTail(int x, int y, Direction headDirection, Direction tailDirection) {
        assertEquals( "[headX, headY, headDirection, tailDirection]",
                asString(x, y, headDirection, tailDirection),

                asString(snake.getHead().getX(), snake.getHead().getY(),
                        snake.getDirection(), snake.getTailDirection()));
    }

    private void assertBody(int x, int y, BodyDirection bodyDirection) {
        Iterator<Tail> iterator = snake.iterator();
        Tail head = iterator.next();
        Tail body = iterator.next();

        assertEquals( "[bodyX, bodyY, bodyDirection]",
                asString(x, y, bodyDirection),

                asString(body.getX(), body.getY(),
                        snake.getBodyDirection(body)));
    }

    private String asString(Object...args) {
        return Arrays.asList(args).toString();
    }
}
