package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Printer;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:32 PM
 */
public class BoomEngineGoodTest {

    private static final int SIZE = 21;
    private BoomEngine engine = new BoomEngineGood(null);

    @Test
    public void testOneBarrier() {
        List<Wall> barriers = Arrays.asList(new Wall(3, 3), new Wall(3, 2), new Wall(2, 3), new Wall(2, 2));
        PointImpl source = new PointImpl(0, 0);
        int radius = 17;

        assertBoom(barriers, source, radius,
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "҉҉҉҉҉                \n" +
                "҉҉҉҉҉҉҉҉             \n" +
                "҉҉҉҉҉҉҉҉             \n" +
                "҉҉҉҉҉҉҉҉             \n" +
                "҉҉҉҉҉҉҉              \n" +
                "҉҉҉҉҉҉҉              \n" +
                "҉҉҉҉҉҉               \n" +
                "҉҉҉҉҉҉               \n" +
                "҉҉҉҉҉                \n" +
                "҉҉҉҉҉                \n" +
                "҉҉҉҉          ҉҉҉    \n" +
                "҉҉҉҉        ҉҉҉҉҉    \n" +
                "҉҉҉       ҉҉҉҉҉҉҉    \n" +
                "҉҉҉     ҉҉҉҉҉҉҉҉҉҉   \n" +
                "҉҉☼☼  ҉҉҉҉҉҉҉҉҉҉҉҉   \n" +
                "҉҉☼☼҉҉҉҉҉҉҉҉҉҉҉҉҉҉   \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   \n" +
                "☻҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   \n");
    }

    @Test
    public void testOneBarrierAtCenter() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8),
                new Wall(12, 12), new Wall(13, 13), new Wall(12, 13), new Wall(13, 12));
        PointImpl source = new PointImpl(10, 10);
        int radius = 7;

        assertBoom(barriers, source, radius,
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "       ҉҉҉҉҉҉҉       \n" +
                "      ҉҉҉҉҉҉҉҉       \n" +
                "     ҉҉҉҉҉҉҉҉        \n" +
                "    ҉҉҉҉҉҉҉҉҉        \n" +
                "   ҉҉҉҉҉҉҉҉҉☼☼  ҉҉   \n" +
                "   ҉҉҉҉҉҉҉҉҉☼☼҉҉҉҉   \n" +
                "   ҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   \n" +
                "   ҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉   \n" +
                "   ҉҉҉҉ ☼☼҉҉҉҉҉҉҉҉   \n" +
                "        ☼☼҉҉҉҉҉҉҉҉   \n" +
                "          ҉҉҉҉҉҉҉҉   \n" +
                "         ҉҉҉҉҉҉҉҉    \n" +
                "         ҉҉҉҉҉҉҉     \n" +
                "         ҉҉҉҉҉҉      \n" +
                "         ҉҉҉҉҉       \n" +
                "                     \n" +
                "                     \n" +
                "                     \n");
    }

    @Test
    public void testCircle() {
        List<Wall> barriers = Arrays.asList();
        PointImpl source = new PointImpl(4, 4);
        int radius = 1;

        assertBoom(barriers, source, radius,
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
                "   ҉҉҉               \n" +
                "   ҉☻҉               \n" +
                "   ҉҉҉               \n" +
                "                     \n" +
                "                     \n" +
                "                     \n");
    }

    @Test
    public void testTwoBarriers() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8),
                new Wall(4, 4), new Wall(5, 5), new Wall(4, 5), new Wall(5, 4));
        PointImpl source = new PointImpl(10, 10);
        int radius = 17;

        assertBoom(barriers, source, radius,
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉☼☼҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉    ☼☼҉҉҉҉҉҉҉҉҉҉҉\n" +
                "         ҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "         ҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "    ☼☼   ҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "    ☼☼   ҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "        ҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "        ҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "        ҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "        ҉҉҉҉҉҉҉҉҉҉҉҉҉\n");
    }

    @Test
    public void testTwoBarriersInOtherVay() {
        List<Wall> barriers = Arrays.asList(new Wall(12, 12), new Wall(11, 11), new Wall(12, 11), new Wall(11, 12),
                new Wall(16, 16), new Wall(17, 17), new Wall(16, 17), new Wall(17, 16));
        PointImpl source = new PointImpl(10, 10);
        int radius = 12;

        assertBoom(barriers, source, radius,
                "   ҉҉҉҉҉҉҉҉҉҉        \n" +
                "  ҉҉҉҉҉҉҉҉҉҉҉        \n" +
                " ҉҉҉҉҉҉҉҉҉҉҉҉        \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉   ☼☼   \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉    ☼☼   \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉         \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉         \n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉         \n" +
                "҉҉҉҉҉҉҉҉҉҉҉☼☼    ҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉☼☼҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                "҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉\n" +
                " ҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉ \n" +
                "  ҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉  \n" +
                "   ҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   \n");
    }

    @Test
    public void testBigBoomAtClassicWalls() {
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(11, 11);
        int radius = 3;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼         ҉҉҉       ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼       ҉҉҉☻҉҉҉     ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼         ҉҉҉       ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
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
    public void testBigBoomAtClassicWalls2() {
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(12, 11);
        int radius = 3;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ҉҉ ҉҉     ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉☻҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼         ҉҉ ҉҉     ☼\n" +
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
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(11, 12);
        int radius = 3;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉҉҉      ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼☻☼ ☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉҉҉      ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
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
    public void testBigBoomAtClassicWalls4() {
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(1, 1);
        int radius = 15;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉                 ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉                 ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉                 ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉҉                 ☼\n" +
                "☼҉☼҉☼҉☼҉☼҉☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☻҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls5() {
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(11, 11);
        int radius = 15;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼         ҉҉҉       ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼         ҉҉҉       ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼҉☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉҉☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼҉☼ ☼ ☼\n" +
                "☼         ҉҉҉       ☼\n" +
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
        List<PointImpl> barriers = new LinkedList<PointImpl>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(12, 11);
        int radius = 15;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼   ҉               ☼\n" +
                "☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼҉☼\n" +
                "☼     ҉           ҉ ☼\n" +
                "☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼҉☼ ☼\n" +
                "☼       ҉      ҉҉   ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼ ☼\n" +
                "☼         ҉҉ ҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼҉☼҉☼ ☼\n" +
                "☼҉҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼҉☼҉☼ ☼\n" +
                "☼         ҉҉ ҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼ ☼\n" +
                "☼       ҉      ҉҉   ☼\n" +
                "☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼҉☼ ☼\n" +
                "☼     ҉           ҉ ☼\n" +
                "☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼҉☼\n" +
                "☼   ҉               ☼\n" +
                "☼ ☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼ ҉                 ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls7() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        PointImpl source = new PointImpl(11, 12);
        int radius = 15;

        assertBoom(barriers, source, radius,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼   ҉      ҉      ҉ ☼\n" +
                "☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼\n" +
                "☼     ҉   ҉҉҉   ҉   ☼\n" +
                "☼ ☼ ☼ ☼҉☼ ☼҉☼ ☼҉☼ ☼ ☼\n" +
                "☼       ҉ ҉҉҉҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉҉҉      ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼☻☼ ☼ ☼ ☼ ☼\n" +
                "☼        ҉҉҉҉҉      ☼\n" +
                "☼ ☼ ☼ ☼ ☼҉☼҉☼҉☼ ☼ ☼ ☼\n" +
                "☼       ҉ ҉҉҉҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼҉☼ ☼҉☼ ☼҉☼ ☼ ☼\n" +
                "☼     ҉    ҉   ҉҉   ☼\n" +
                "☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼ ☼҉☼ ☼\n" +
                "☼   ҉      ҉      ҉ ☼\n" +
                "☼ ☼҉☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼҉☼\n" +
                "☼ ҉        ҉        ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼          ҉        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    private void assertBoom(List<? extends PointImpl> barriers, PointImpl source, int radius, String expected) {
        List<Blast> blasts = engine.boom(barriers, SIZE, source, radius);

        String actual = print(blasts, barriers, source);

        assertEquals(expected, actual);
    }

    public static String print(final List<Blast> blast, final List<? extends PointImpl> barriers, final PointImpl source) {
        return new Printer(SIZE, new GamePrinter() {
            @Override
            public void init() {
                // do nothing
            }

            @Override
            public Enum get(Point pt) {
                if (source.itsMe(pt)) return Elements.BOMB_BOMBERMAN;

                if (blast.contains(pt)) return Elements.BOOM;

                if (barriers.contains(pt)) return Elements.WALL;

                return Elements.EMPTY;
            }
        }).toString();
    }

}
