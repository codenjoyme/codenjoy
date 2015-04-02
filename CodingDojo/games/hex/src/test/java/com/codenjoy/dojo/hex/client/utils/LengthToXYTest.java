package com.codenjoy.dojo.hex.client.utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Sanja on 26.06.14.
 */
public class LengthToXYTest {

    String board =
            "0123" +
            "4567" +
            "8901" +
            "2345";

    @Test
    public void shouldLengthToXY() {
        assertLtoXY(0, 3, 0);
        assertLtoXY(1, 3, 1);
        assertLtoXY(2, 3, 2);
        assertLtoXY(3, 3, 3);
        assertLtoXY(0, 2, 4);
        assertLtoXY(1, 2, 5);
        assertLtoXY(2, 2, 6);
        assertLtoXY(3, 2, 7);
        assertLtoXY(0, 1, 8);
        assertLtoXY(1, 1, 9);
        assertLtoXY(2, 1, 10);
        assertLtoXY(3, 1, 11);
        assertLtoXY(0, 0, 12);
        assertLtoXY(1, 0, 13);
        assertLtoXY(2, 0, 14);
        assertLtoXY(3, 0, 15);
    }


    private void assertLtoXY(int x, int y, int length) {
        int size = (int) Math.sqrt(board.length());
        LengthToXY xyl = new LengthToXY(size);
        Point xy = xyl.getXY(length);
        assertEquals(new Point(x, y).toString(), xy.toString());

        int l = xyl.getLength(x, y);
        assertEquals(l, length);
    }


}
