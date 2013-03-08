package com.codenjoy.bomberman.model;

import com.codenjoy.bomberman.console.BombermanPrinter;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 8:29 PM
 */
public class WallsTest {

    private final static int SIZE = 9;
    private BombermanPrinter printer = new BombermanPrinter(SIZE);

    @Test
    public void testOriginalWalls() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼☺      ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                printer.print(new Board(new OriginalWalls(SIZE), null, SIZE)));
    }

    @Test
    public void testBasicWalls() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼☺      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                printer.print(new Board(new BasicWalls(SIZE), null, SIZE)));
    }

    @Test
    public void testWalls() {
        assertEquals(
                "☺        \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n",
                printer.print(new Board(new WallsImpl(), null, SIZE)));
    }

    @Test
    public void checkPrintDestroyWalls() {
        String actual = getBoardWithDestroyWalls();

        int countBlocks = actual.length() - actual.replace("#", "").length();
        assertEquals(SIZE * SIZE / 4, countBlocks);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼☺      ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n", actual.replace('#', ' '));
    }

    private String getBoardWithDestroyWalls() {
        return printer.print(new Board(new DestroyWalls(new OriginalWalls(SIZE), SIZE), null, SIZE));
    }

    @Test
    public void shouldDestroyWallsNeverCreatedAtBombermanWay() {
        for (int index = 0; index < 1000; index++) {
            String actual = getBoardWithDestroyWalls();
            int bombermanPosition = SIZE + 3;
            String substring = actual.substring(bombermanPosition, bombermanPosition + 2);
            assertTrue(substring, substring.equals("  "));
        }

    }
}
