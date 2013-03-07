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


        for (int x = 0; x < SIZE; x++) {
            List<Point> line = new Line().draw(0, 0, x, 0);
            container.addAll(line);
        }

        for (int y = 0; y < SIZE; y++) {
            List<Point> line = new Line().draw(0, 0, SIZE - 1, y);
            container.addAll(line);
        }

        for (int x = SIZE - 1; x >= 0; x--) {
            List<Point> line = new Line().draw(0, 0, x, SIZE - 1);
            container.addAll(line);
        }

        for (int y = SIZE - 1; y >= 0; y--) {
            List<Point> line = new Line().draw(0, 0, 0, y);
            container.addAll(line);
        }

        String actual = new BombermanPrinter().printSmth(SIZE, container, '*').asString();
        assertEquals("*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n" +
                "*********************\n", actual);
    }
}
