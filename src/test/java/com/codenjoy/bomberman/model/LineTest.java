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
public class LineTest {

    private static final int SIZE = 21;

    @Test
    public void test() {
        List<Point> container = new LinkedList<Point>();

        List<Point> barriers = Arrays.asList(new Point(3, 3), new Point(3, 2), new Point(2, 3), new Point(2, 2));
        Point source = new Point(0, 0);

        int R = 17;

        double dn = 0.01d/R;
        double n = 0;
        while (n < 2d*Math.PI) {
            int x = (int)(source.getX() + R*Math.cos(n));
            int y = (int)(source.getY() + R*Math.sin(n));

            List<Point> line = new Line().draw(source.getX(), source.getY(), x, y);
            for (Point barrier : barriers) {
                if (line.contains(barrier)) {
                    line = new Line().draw(source.getX(), source.getY(), barrier.getX(), barrier.getY());
                }
            }

            for (Point pt : line) {
                if (isOnBoard(pt) && !container.contains(pt) && !barriers.contains(pt)) {
                    container.add(pt);
                }
            }

            n = n + dn;
        }

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

    private boolean isOnBoard(Point pt) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < SIZE && pt.getY() < SIZE;
    }
}
