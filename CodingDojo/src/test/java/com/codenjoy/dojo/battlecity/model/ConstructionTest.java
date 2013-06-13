package com.codenjoy.dojo.battlecity.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 13.06.13
 * Time: 19:44
 */
public class ConstructionTest {

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
        assertDestroyFrom('╕', Direction.DOWN, Direction.LEFT);
        assertDestroyFrom('╒', Direction.DOWN, Direction.RIGHT);
        assertDestroyFrom('─', Direction.DOWN, Direction.UP);

        assertDestroyFrom('╛', Direction.UP, Direction.LEFT);
        assertDestroyFrom('╘', Direction.UP, Direction.RIGHT);
        assertDestroyFrom('─', Direction.UP, Direction.DOWN);

        assertDestroyFrom('╙', Direction.RIGHT, Direction.UP);
        assertDestroyFrom('│', Direction.RIGHT, Direction.LEFT);
        assertDestroyFrom('╓', Direction.RIGHT, Direction.DOWN);

        assertDestroyFrom('╜', Direction.LEFT, Direction.UP);
        assertDestroyFrom('│', Direction.LEFT, Direction.RIGHT);
        assertDestroyFrom('╖', Direction.LEFT, Direction.DOWN);
    }

    private void assertDestroyFrom(char expected, Direction... directions) {
        Construction construction = new Construction(0, 0);
        for (Direction direction : directions) {
            construction.destroyFrom(direction);
        }
        assertEquals(expected, construction.getChar().ch);
    }
}
