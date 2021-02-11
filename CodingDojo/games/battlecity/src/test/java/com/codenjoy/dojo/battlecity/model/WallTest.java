package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.model.items.Wall;
import com.codenjoy.dojo.services.Direction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WallTest {

    @Test
    public void shouldDestroyOnce() {
        assertDestroyFrom('╩', Direction.UP);
        assertDestroyFrom('╦', Direction.DOWN);
        assertDestroyFrom('╣', Direction.LEFT);
        assertDestroyFrom('╠', Direction.RIGHT);
    }

    @Test
    public void shouldDestroyTwice() {
        assertDestroyFrom('╨', Direction.UP, Direction.UP);
        assertDestroyFrom('╥', Direction.DOWN, Direction.DOWN);
        assertDestroyFrom('╡', Direction.LEFT, Direction.LEFT);
        assertDestroyFrom('╞', Direction.RIGHT, Direction.RIGHT);
    }

    @Test
    public void shouldDestroyFromOneSideThreeTimes() {
        assertDestroyFrom(' ', Direction.UP, Direction.UP, Direction.UP);
        assertDestroyFrom(' ', Direction.DOWN, Direction.DOWN, Direction.DOWN);
        assertDestroyFrom(' ', Direction.LEFT, Direction.LEFT, Direction.LEFT);
        assertDestroyFrom(' ', Direction.RIGHT, Direction.RIGHT, Direction.RIGHT);
    }

    @Test
    public void shouldDestroyOnceAndFromOtherSideAnother() {
        assertDestroyFrom('┐', Direction.DOWN, Direction.LEFT);
        assertDestroyFrom('┌', Direction.DOWN, Direction.RIGHT);
        assertDestroyFrom('─', Direction.DOWN, Direction.UP);

        assertDestroyFrom('┘', Direction.UP, Direction.LEFT);
        assertDestroyFrom('└', Direction.UP, Direction.RIGHT);
        assertDestroyFrom('─', Direction.UP, Direction.DOWN);

        assertDestroyFrom('└', Direction.RIGHT, Direction.UP);
        assertDestroyFrom('│', Direction.RIGHT, Direction.LEFT);
        assertDestroyFrom('┌', Direction.RIGHT, Direction.DOWN);

        assertDestroyFrom('┘', Direction.LEFT, Direction.UP);
        assertDestroyFrom('│', Direction.LEFT, Direction.RIGHT);
        assertDestroyFrom('┐', Direction.LEFT, Direction.DOWN);
    }

    @Test
    public void shouldDestroyTwiceFromSomeSideAndFormOtherSideLast() {
        assertDestroyAll(Direction.LEFT);
        assertDestroyAll(Direction.RIGHT);
        assertDestroyAll(Direction.UP);
        assertDestroyAll(Direction.DOWN);
    }

    private void assertDestroyAll(Direction direction) {
        assertDestroyAll(Direction.LEFT, direction);
        assertDestroyAll(Direction.RIGHT, direction);
        assertDestroyAll(Direction.UP, direction);
        assertDestroyAll(Direction.DOWN, direction);
    }

    private void assertDestroyAll(Direction direction1, Direction direction2) {
        assertDestroyFrom(' ', Direction.LEFT, direction1, direction2);
        assertDestroyFrom(' ', Direction.RIGHT, direction1, direction2);
        assertDestroyFrom(' ', Direction.UP, direction1, direction2);
        assertDestroyFrom(' ', Direction.DOWN, direction1, direction2);
    }

    private void assertDestroyFrom(char expected, Direction... directions) {
        Wall wall = new Wall(0, 0);
        for (Direction direction : directions) {
            wall.destroyFrom(direction);
        }
        assertEquals(expected, wall.state(null).ch);
    }
}
