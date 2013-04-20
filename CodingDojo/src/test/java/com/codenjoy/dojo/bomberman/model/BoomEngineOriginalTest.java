package com.codenjoy.dojo.bomberman.model;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 01:32 AM
 */
public class BoomEngineOriginalTest {

    private static final int SIZE = 21;
    private BoomEngine engine = new BoomEngineOriginal(null);

    @Test
    public void testOneBarrier() {
        List<Wall> barriers = Arrays.asList(new Wall(3, 3), new Wall(3, 2), new Wall(2, 3), new Wall(2, 2));
        Point source = new Point(3, 0);
        int radius = 7;
        int countBlasts = radius + 1 + 1 + 3;

        assertBoom(barriers, source, radius, countBlasts,
                "҉҉҉☻҉҉҉҉҉҉҉          \n" +
                "   ҉                 \n" +
                "  ☼☼                 \n" +
                "  ☼☼                 \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n");
    }

    @Test
    public void testOneBarrierAtCenter() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8),
                new Wall(12, 12), new Wall(13, 13), new Wall(12, 13), new Wall(13, 12));
        Point source = new Point(9, 12);
        int radius = 7;
        int countBlasts = 2*radius + 2 + 2 + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "        ☼☼           \n" +
                "        ☼☼           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "  ҉҉҉҉҉҉҉☻҉҉☼☼       \n" +
                "         ҉  ☼☼       \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "                     \n");
    }

    @Test
    public void testOneBarrier2() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8));
        Point source = new Point(13, 9);
        int radius = 4;
        int countBlasts = 3*radius + 1 + 3;

        assertBoom(barriers, source, radius, countBlasts,
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "        ☼☼   ҉       \n" +
                "        ☼☼҉҉҉☻҉҉҉҉   \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n");
    }

    @Test
    public void testBigBoomAtClassicWalls() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(11, 11);
        int radius = 3;
        int countBlasts = 4*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼       ҉҉҉☻҉҉҉     ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls2() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(12, 11);
        int radius = 3;
        int countBlasts = 2*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉☻҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls3() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(11, 12);
        int radius = 3;
        int countBlasts = 2*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼☻☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls4() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(1, 1);
        int radius = 15;
        int countBlasts = 2*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☻҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls5() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(11, 11);
        int radius = 15;
        int countBlasts = 2 * (SIZE - 2) - 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉҉☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls6() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(12, 11);
        int radius = 15;
        int countBlasts = SIZE - 2;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls7() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(SIZE).iterator());
        Point source = new Point(11, 12);
        int radius = 15;
        int countBlasts = SIZE - 2;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼☻☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    private void assertBoom(List<? extends Point> barriers, Point source, int radius, int countBlasts, String expected) {
        List<Blast> container = engine.boom(barriers, SIZE, source, radius);

        assertEquals(countBlasts, container.size());

        String actual = BombermanPrinter.get(SIZE)
                .printSmth(container, Blast.class, PlotColor.BOOM)
                .printSmth(barriers, Wall.class, PlotColor.WALL)
                .printSmth(Arrays.asList(source), Point.class, PlotColor.BOMB_BOMBERMAN).asString();

        assertEquals(expected, actual);
    }

}
