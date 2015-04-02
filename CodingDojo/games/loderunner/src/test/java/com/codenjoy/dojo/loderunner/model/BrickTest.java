package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 15:21
 */
public class BrickTest {

    @Test
    public void test() {
        Brick brick = new Brick(new PointImpl(0, 0));

        assertEquals(Elements.BRICK, brick.state(null));

        brick.drill(null);
        brick.tick();

        assertEquals(Elements.DRILL_PIT, brick.state(null));

        brick.tick();

        assertEquals(Elements.NONE, brick.state(null));

        brick.tick();

        assertEquals(Elements.NONE, brick.state(null));

        brick.tick();
        brick.tick();
        brick.tick();
        brick.tick();
        brick.tick();

        assertEquals(Elements.NONE, brick.state(null));

        brick.tick();

        assertEquals(Elements.PIT_FILL_4, brick.state(null));

        brick.tick();

        assertEquals(Elements.PIT_FILL_3, brick.state(null));

        brick.tick();

        assertEquals(Elements.PIT_FILL_2, brick.state(null));

        brick.tick();

        assertEquals(Elements.PIT_FILL_1, brick.state(null));

        brick.tick();

        assertEquals(Elements.BRICK, brick.state(null));
    }
}
