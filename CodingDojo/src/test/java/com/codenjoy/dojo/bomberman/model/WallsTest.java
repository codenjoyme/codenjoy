package com.codenjoy.dojo.bomberman.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 8:29 PM
 */
public class WallsTest {

    private final static int SIZE = 9;

    @Test
    public void testOriginalWalls() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                print(new OriginalWalls(SIZE)));
    }

    private String print(Walls walls) {
        return BombermanPrinter.get(SIZE)
                .printSmth(walls, Wall.class, '☼')
                .printSmth(walls, MeatChopper.class, '&')
                .printSmth(walls, DestroyWall.class, '#')
                .asString();
    }

    @Test
    public void testBasicWalls() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                print(new BasicWalls(SIZE)));
    }

    @Test
    public void testWalls() {
        assertEquals(
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n",
                print(new WallsImpl()));
    }

    @Test
    public void checkPrintDestroyWalls() {
        String actual = getBoardWithDestroyWalls();

        int countBlocks = actual.length() - actual.replace("#", "").length();
        assertEquals(SIZE * SIZE / 10, countBlocks);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n", actual.replace('#', ' '));
    }

    @Test
    public void checkPrintMeatChoppers() {
        String actual = print(new MeatChoppers(new OriginalWalls(SIZE), SIZE, 10, new RandomDice()));

        int countBlocks = actual.length() - actual.replace("&", "").length();
        assertEquals(10, countBlocks);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n", actual.replace('&', ' '));
    }

    private String getBoardWithDestroyWalls() {
        return print(new DestroyWalls(new OriginalWalls(SIZE), SIZE, new RandomDice()));
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
