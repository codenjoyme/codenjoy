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
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 8:29 PM
 */
public class WallsTest {

    private final static int SIZE = 9;
    private Field field;
    private Walls walls;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Before
    public void setup() {
        field = mock(Field.class);
        when(field.size()).thenReturn(SIZE);
        when(field.isBarrier(any(Point.class), anyBoolean())).thenAnswer(
                invocation -> walls.itsMe((Point)invocation.getArguments()[0])
        );
    }

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
                print(new OriginalWalls(v(SIZE))));
    }

    private String print(final Walls walls) {
        Printer<String> printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return SIZE;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return walls;
            }
        }, null);
        return printer.print();
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
        walls = new MeatChoppers(new OriginalWalls(v(SIZE)), v(10), new RandomDice());
        walls.init(field);
        walls.tick();
        String actual = print(walls);

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
        walls = new EatSpaceWalls(new OriginalWalls(v(SIZE)), v(SIZE * SIZE / 10), new RandomDice());
        walls.init(field);
        walls.tick();
        return print(walls);
    }

}
