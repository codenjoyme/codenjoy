package com.codenjoy.dojo.pong.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static org.junit.Assert.*;


public class BallDirectionTest {

    Wall horisontalWall = new Wall(new PointImpl(0,0), BarrierOrientation.HORISONTAL);
    Wall verticalWall = new Wall(new PointImpl(0,0), BarrierOrientation.VERTICAL);

    @Test
    public void shouldIncrementX_WhenDirectionIsRight() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT);
        int givenX = 5;
        int actualX = ballDirection.changeX(givenX);
        assertEquals(givenX+1, actualX);
    }

    @Test
    public void shouldDecrementX_WhenDirectionIsLeft() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT);
        int givenX = 5;
        int actualX = ballDirection.changeX(givenX);
        assertEquals(givenX-1, actualX);
    }

    @Test
    public void shouldDecrementY_WhenDirectionIsDown() {
        BallDirection ballDirection = new BallDirection(Direction.DOWN);
        int givenY = 5;
        int actualY = ballDirection.changeY(givenY);
        assertEquals(givenY-1, actualY);
    }

    @Test
    public void shouldIncrementY_WhenDirectionIsUp() {
        BallDirection ballDirection = new BallDirection(Direction.UP);
        int givenY = 5;
        int actualY = ballDirection.changeY(givenY);
        assertEquals(givenY+1, actualY);
    }

    @Test
    public void shouldChangeDirection_WhenReflectedFromHorisontalUpToDown() {
        BallDirection ballDirection = new BallDirection(Direction.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedDownToUp() {
        BallDirection ballDirection = new BallDirection(Direction.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftToRight() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.RIGHT);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightToLeft() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.LEFT);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightUPToRightDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT, Direction.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.RIGHT, Direction.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightDownToRightUpWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT, Direction.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.RIGHT, Direction.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftUPToLeftDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT, Direction.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.LEFT, Direction.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftDownToLeftUpWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT, Direction.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(Direction.LEFT, Direction.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightUPToLeftUpWhenVertical() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT, Direction.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.LEFT, Direction.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightDownToLeftDownWhenVertical() {
        BallDirection ballDirection = new BallDirection(Direction.RIGHT, Direction.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.LEFT, Direction.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftUpToRightUpWhenVertical() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT, Direction.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.RIGHT, Direction.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftDownToRightDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(Direction.LEFT, Direction.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(Direction.RIGHT, Direction.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

}