package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 01:32 AM
 */
public class BoomEngineOriginalTest {

    private static final int SIZE = 21;
    private BoomEngine engine = new BoomEngineOriginal(null);
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Test
    public void testOneBarrier() {
        List<Wall> barriers = Arrays.asList(new Wall(3, 3), new Wall(3, 2), new Wall(2, 3), new Wall(2, 2));
        Point source = pt(3, 0);
        int radius = 7;
        int countBlasts = radius + 1 + 1 + 3;

        assertBoom(barriers, source, radius, countBlasts,
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
                "                     \n" +
                "  ☼☼                 \n" +
                "  ☼☼                 \n" +
                "   ҉                 \n" +
                "҉҉҉☻҉҉҉҉҉҉҉          \n");
    }

    @Test
    public void testOneBarrierAtCenter() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8),
                new Wall(12, 12), new Wall(13, 13), new Wall(12, 13), new Wall(13, 12));
        Point source = pt(9, 12);
        int radius = 7;
        int countBlasts = 2*radius + 2 + 2 + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "                     \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "         ҉  ☼☼       \n" +
                "  ҉҉҉҉҉҉҉☻҉҉☼☼       \n" +
                "         ҉           \n" +
                "         ҉           \n" +
                "        ☼☼           \n" +
                "        ☼☼           \n" +
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
    public void testOneBarrier2() {
        List<Wall> barriers = Arrays.asList(new Wall(9, 9), new Wall(9, 8), new Wall(8, 9), new Wall(8, 8));
        Point source = pt(13, 9);
        int radius = 4;
        int countBlasts = 3*radius + 1 + 3;

        assertBoom(barriers, source, radius, countBlasts,
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "        ☼☼҉҉҉☻҉҉҉҉   \n" +
                "        ☼☼   ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "             ҉       \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n" +
                "                     \n");
    }

    @Test
    public void testBigBoomAtClassicWalls() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(11, 11);
        int radius = 3;
        int countBlasts = 4*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
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
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls2() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(12, 11);
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
                "☼        ҉҉҉☻҉҉҉    ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
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
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(11, 12);
        int radius = 3;
        int countBlasts = 2*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
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
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls4() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(1, 1);
        int radius = 15;
        int countBlasts = 2*radius + 1;

        assertBoom(barriers, source, radius, countBlasts,
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
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼҉                  ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☻҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls5() {
        List<Wall> barriers = new LinkedList<>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(11, 11);
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
                "☼҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉҉☼\n" +
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
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void testBigBoomAtClassicWalls6() {
        List<Wall> barriers = new LinkedList<Wall>();
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(12, 11);
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
                "☼҉҉҉҉҉҉҉҉҉҉҉☻҉҉҉҉҉҉҉☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼                   ☼\n" +
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
        CollectionUtils.addAll(barriers, new OriginalWalls(v(SIZE)).iterator());
        Point source = pt(11, 12);
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
                "☼ ☼ ☼ ☼ ☼ ☼☻☼ ☼ ☼ ☼ ☼\n" +
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
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    private void assertBoom(List<? extends Wall> barriers, Point source, int radius, int countBlasts, String expected) {
        List<Blast> blasts = engine.boom(barriers, SIZE, source, radius);

        assertEquals(countBlasts, blasts.size());

        String actual = print(blasts, barriers, source);

        assertEquals(expected, actual);
    }

    public String print(final List<Blast> blast, final List<? extends Wall> barriers, final Point source) {
        Printer<String> printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return SIZE;
            }

            class B extends PointImpl implements State<Elements, Object> {

                public B(Point point) {
                    super(point);
                }

                @Override
                public Elements state(Object player, Object... alsoAtPoint) {
                    return Elements.BOMB_BOMBERMAN;
                }
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(barriers);
                    add(new B(source));
                    addAll(blast);
                }};
            }
        }, null);
        return printer.print();
    }

}
