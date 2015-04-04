package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.LengthToXY;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:45
 */
public class LengthToXYTest {

    @Test
    public void test() {
        LengthToXY xy = new LengthToXY(5);

        assertEquals(5*5 - 5, xy.getLength(0, 0));
        assertEquals(0, xy.getLength(0, 4));

        assertEquals(5*5 - 1, xy.getLength(4, 0));
        assertEquals(4, xy.getLength(4, 4));

        assertEquals("[0,0]", xy.getXY(xy.getLength(0, 0)).toString());
        assertEquals("[0,4]", xy.getXY(xy.getLength(0, 4)).toString());

        assertEquals("[4,0]", xy.getXY(xy.getLength(4, 0)).toString());
        assertEquals("[4,4]", xy.getXY(xy.getLength(4, 4)).toString());
    }
}
