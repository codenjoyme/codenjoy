package com.codenjoy.dojo.services.algs;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class DeikstraFindWayTest {

    @Test
    public void testFindShortestWay() {
        asrtWay("XXXXXXX\n" +
                "XS    X\n" +
                "X*    X\n" +
                "X*    X\n" +
                "X*    X\n" +
                "X****FX\n" +
                "XXXXXXX\n");
    }

    @Test
    public void testFindShortestWayWhenBrickOnWay() {
        asrtWay("XXXXXXX\n" +
                "XS    X\n" +
                "X**   X\n" +
                "XO*   X\n" +
                "X *   X\n" +
                "X ***FX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XS    X\n" +
                "X**   X\n" +
                "XO**  X\n" +
                "X O*  X\n" +
                "X  **FX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XS    X\n" +
                "X**   X\n" +
                "XO**  X\n" +
                "X O***X\n" +
                "X   OFX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XS    X\n" +
                "X**** X\n" +
                "XO O* X\n" +
                "X O **X\n" +
                "X   OFX\n" +
                "XXXXXXX\n");
    }

    @Test
    public void testFindShortestWayWhenNoWay() {
        asrtWay("XXXXXXX\n" +
                "XSX   X\n" +
                "XXX   X\n" +
                "X     X\n" +
                "X     X\n" +
                "X    FX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XSO   X\n" +
                "XOO   X\n" +
                "X     X\n" +
                "X     X\n" +
                "X    FX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XS O  X\n" +
                "X  O  X\n" +
                "XOOO  X\n" +
                "X     X\n" +
                "X    FX\n" +
                "XXXXXXX\n");

        asrtWay("XXXXXXX\n" +
                "XS X  X\n" +
                "X  X  X\n" +
                "XXXX  X\n" +
                "X     X\n" +
                "X    FX\n" +
                "XXXXXXX\n");
    }

    enum Elements implements CharElements {
        ONLY_UP('˄'),
        ONLY_DOWN('˃'),
        ONLY_LEFT('˅'),
        ONLY_RIGHT('˂'),

        WAY('*'),

        START('S'),
        FINISH('F'),

        WALL('X'),
        BRICK('O'),
        NONE(' ');

        final char ch;

        Elements(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }

        public static Elements valueOf(char ch) {
            for (Elements el : Elements.values()) {
                if (el.ch == ch) {
                    return el;
                }
            }
            throw new IllegalArgumentException("No such element for " + ch);
        }
    }

    private void asrtWay(String expected) {
        AbstractBoard board = new AbstractBoard() {
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }
        };

        assertEquals(expected,
                TestUtils.printWay(expected,
                        Elements.START, Elements.FINISH,
                        Elements.NONE, Elements.WAY,
                        board,
                        b -> getPossible(b)));
    }

    private <T extends AbstractBoard> DeikstraFindWay.Possible getPossible(T board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (board.isAt(x, y, Elements.WALL, Elements.FINISH)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (board.isAt(nx, ny, Elements.WALL, Elements.START)) return false;

                // TODO test me
                if (board.isAt(x, y, Elements.ONLY_UP, Elements.ONLY_DOWN, Elements.ONLY_LEFT, Elements.ONLY_RIGHT)) {
                    if (where == Direction.UP && !board.isAt(x, y, Elements.ONLY_UP)) return false;
                    if (where == Direction.DOWN && !board.isAt(x, y, Elements.ONLY_DOWN)) return false;
                    if (where == Direction.LEFT && !board.isAt(x, y, Elements.ONLY_LEFT)) return false;
                    if (where == Direction.RIGHT && !board.isAt(x, y, Elements.ONLY_RIGHT)) return false;
                }
                return true;
            }

            @Override
            public boolean possible(Point point) {
                return !board.isAt(point.getX(), point.getY(), Elements.BRICK);
            }
        };
    }

}
