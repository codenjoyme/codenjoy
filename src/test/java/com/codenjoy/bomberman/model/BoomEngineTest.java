package com.codenjoy.bomberman.model;

import com.codenjoy.bomberman.console.BombermanPrinter;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:32 PM
 */
public class BoomEngineTest {

    private static final int SIZE = 21;

    @Test
    public void test() {

        List<Point> barriers = Arrays.asList(new Point(3, 3), new Point(3, 2), new Point(2, 3), new Point(2, 2));
        Point source = new Point(0, 0);
        int radius = 17;

        List<Point> container = BoomEngine.boom(barriers, SIZE, source, radius);

        String actual = new BombermanPrinter(SIZE)
                .printSmth(container, '*')
                .printSmth(barriers, 'X')
                .printSmth(Arrays.asList(source), '@').asString();

        assertEquals(
                "@*****************   \n" +
                "*****************    \n" +
                "**XX*************    \n" +
                "**XX  ***********    \n" +
                "***     *********    \n" +
                "***       *******    \n" +
                "****        ****     \n" +
                "****          **     \n" +
                "*****                \n" +
                "*****                \n" +
                "******               \n" +
                "******               \n" +
                "*******              \n" +
                "*******              \n" +
                "********             \n" +
                "********             \n" +
                "******               \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n", actual);
    }

}
