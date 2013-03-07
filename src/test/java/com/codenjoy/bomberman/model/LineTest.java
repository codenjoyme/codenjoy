package com.codenjoy.bomberman.model;

import com.codenjoy.bomberman.console.BombermanPrinter;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:32 PM
 */
public class LineTest {

    private static final int SIZE = 21;

    @Test
    public void test() {
        List<Point> container = new LinkedList<Point>();

        Point barrier = new Point(3, 3);
        int x0 = 0;
        int y0 = 0;
        int R = 17;

        double dn = 0.01d/R;
        double n = 0;
        while (n < 2d*Math.PI) {
            int x = (int)(x0 + R*Math.cos(n));
            int y = (int)(y0 + R*Math.sin(n));

            List<Point> line = new Line().draw(x0, y0, x, y);
            if (line.contains(barrier)) {
                line = new Line().draw(x0, y0, barrier.getX(), barrier.getY());
            }

            container.addAll(line);

            n = n + dn;
        }

        String actual = new BombermanPrinter().printSmth(SIZE, container, '*').asString();
        assertEquals(
                "******************   \n" +
                "*****************    \n" +
                "*****************    \n" +
                "*****************    \n" +
                "**** ************    \n" +
                "***** ***********    \n" +
                "****** *********     \n" +
                "******* ********     \n" +
                "********  *****      \n" +
                "********   ****      \n" +
                "*********   **       \n" +
                "**********           \n" +
                "***********          \n" +
                "***********          \n" +
                "**********           \n" +
                "********             \n" +
                "******               \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n", actual);
    }
}
