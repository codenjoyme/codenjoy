package com.codenjoy.dojo.pong.model;

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
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.*;


public class BallDirectionTest {

    Wall horisontalWall = new Wall(pt(0, 0), BarrierOrientation.HORIZONTAL);
    Wall verticalWall = new Wall(pt(0, 0), BarrierOrientation.VERTICAL);

    @Test
    public void shouldChangeDirection_WhenReflectedFromHorisontalUpToDown() {
        BallDirection ballDirection = new BallDirection(QDirection.UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedDownToUp() {
        BallDirection ballDirection = new BallDirection(QDirection.DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftToRight() {
        BallDirection ballDirection = new BallDirection(QDirection.LEFT);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.RIGHT);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightToLeft() {
        BallDirection ballDirection = new BallDirection(QDirection.RIGHT);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.LEFT);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightUPToRightDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(QDirection.RIGHT_UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.RIGHT_DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightDownToRightUpWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(QDirection.RIGHT_DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.RIGHT_UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftUPToLeftDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(QDirection.LEFT_UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.LEFT_DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftDownToLeftUpWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(QDirection.LEFT_DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(horisontalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.LEFT_UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightUPToLeftUpWhenVertical() {
        BallDirection ballDirection = new BallDirection(QDirection.RIGHT_UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.LEFT_UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedRightDownToLeftDownWhenVertical() {
        BallDirection ballDirection = new BallDirection(QDirection.RIGHT_DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.LEFT_DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftUpToRightUpWhenVertical() {
        BallDirection ballDirection = new BallDirection(QDirection.LEFT_UP);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.RIGHT_UP);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

    @Test
    public void shouldChangeDirection_WhenReflectedLeftDownToRightDownWhenHorizontal() {
        BallDirection ballDirection = new BallDirection(QDirection.LEFT_DOWN);
        BallDirection reflectedDirection = ballDirection.reflectedFrom(verticalWall);

        BallDirection expectedDirection = new BallDirection(QDirection.RIGHT_DOWN);
        assertTrue(expectedDirection.equals(reflectedDirection));
    }

}
