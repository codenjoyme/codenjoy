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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.codenjoy.dojo.services.algs.DeikstraFindWayTest.Elements.*;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void testFindShortestWayWhenOnlyOneDirectionAllowed() {
        String board = "XXXXXXX\n" +
                "XS˅F˂OX\n" +
                "X˃˅O˄OX\n" +
                "XO˅O˄OX\n" +
                "XO˃˃˄OX\n" +
                "XOOOOOX\n" +
                "XXXXXXX\n";

        assertP(board,
                "{[1,4]=[RIGHT],\n" +
                "[1,5]=[DOWN, RIGHT],\n" +
                "[2,2]=[RIGHT],\n" +
                "[2,3]=[DOWN],\n" +
                "[2,4]=[DOWN],\n" +
                "[2,5]=[DOWN],\n" +
                "[3,2]=[RIGHT],\n" +
                "[4,2]=[UP],\n" +
                "[4,3]=[UP],\n" +
                "[4,4]=[UP],\n" +
                "[4,5]=[LEFT]}");

        asrtWay(board,
                "XXXXXXX\n" +
                "XS˅F*OX\n" +
                "X**O*OX\n" +
                "XO*O*OX\n" +
                "XO***OX\n" +
                "XOOOOOX\n" +
                "XXXXXXX\n");
    }

    @Test
    public void testOnlyRight() {
        String board =
                "XXXXX\n" +
                "XXXXX\n" +
                "XS˃FX\n" +
                "XXXXX\n" +
                "XXXXX\n";

        assertP(board,
                "{[1,2]=[RIGHT],\n" +
                "[2,2]=[RIGHT]}");

        asrtWay(board,
                "XXXXX\n" +
                "XXXXX\n" +
                "XS*FX\n" +
                "XXXXX\n" +
                "XXXXX\n");
    }

    @Test
    public void testOnlyLeft() {
        String board =
                "XXXXX\n" +
                "XXXXX\n" +
                "XF˂SX\n" +
                "XXXXX\n" +
                "XXXXX\n";

        assertP(board,
                "{[2,2]=[LEFT],\n" +
                "[3,2]=[LEFT]}");

        asrtWay(board,
                "XXXXX\n" +
                "XXXXX\n" +
                "XF*SX\n" +
                "XXXXX\n" +
                "XXXXX\n");
    }

    @Test
    public void testOnlyUp() {
        String board =
                "XXXXX\n" +
                "XXFXX\n" +
                "XX˄XX\n" +
                "XXSXX\n" +
                "XXXXX\n";

        assertP(board,
                "{[2,1]=[UP],\n" +
                "[2,2]=[UP]}");

        asrtWay(board,
                "XXXXX\n" +
                "XXFXX\n" +
                "XX*XX\n" +
                "XXSXX\n" +
                "XXXXX\n");
    }

    @Test
    public void testOnlyDown() {
        String board =
                "XXXXX\n" +
                "XXSXX\n" +
                "XX˅XX\n" +
                "XXFXX\n" +
                "XXXXX\n";

        assertP(board,
                "{[2,2]=[DOWN],\n" +
                "[2,3]=[DOWN]}");

        asrtWay(board,
                "XXXXX\n" +
                "XXSXX\n" +
                "XX*XX\n" +
                "XXFXX\n" +
                "XXXXX\n");
    }


    enum Elements implements CharElements {
        ONLY_UP('˄'),
        ONLY_DOWN('˅'),
        ONLY_LEFT('˂'),
        ONLY_RIGHT('˃'),

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
        asrtWay(expected, expected);
    }

    private void asrtWay(String map, String expected) {
        AbstractBoard board = new AbstractBoard() {
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }

            @Override
            protected int inversionY(int y) {
                return size - 1 - y;
            }
        };

        assertEquals(expected,
                TestUtils.printWay(map,
                        START, FINISH,
                        NONE, WAY,
                        board,
                        b -> getPossible(b)));
    }

    // TODO to use in AITest
    private void assertP(String map, String expected) {
        AbstractBoard board = new AbstractBoard() {
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }

            @Override
            protected int inversionY(int y) {
                return size - 1 - y;
            }
        };

        board = (AbstractBoard) board.forString(map);

        Map<Point, List<Direction>> ways = new DeikstraFindWay().getPossibleWays(board.size(), getPossible(board));

        Map<Point, List<Direction>> result = new TreeMap<>();
        for (Map.Entry<Point, List<Direction>> entry : ways.entrySet()) {
            List<Direction> value = entry.getValue();
            if (!value.isEmpty()) {
                result.put(entry.getKey(), value);
            }
        }

        assertEquals(expected, result.toString().replace("], [", "],\n["));
    }

    private <T extends AbstractBoard> DeikstraFindWay.Possible getPossible(T board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                if (board.isAt(from, FINISH)) return false;
                Point dest = where.change(from);
                if (board.isAt(dest, START)) return false;

                if (board.isAt(from, ONLY_UP, ONLY_DOWN, ONLY_LEFT, ONLY_RIGHT)) {
                    if (where == Direction.UP && !board.isAt(from, ONLY_UP)) return false;
                    if (where == Direction.DOWN && !board.isAt(from, ONLY_DOWN)) return false;
                    if (where == Direction.LEFT && !board.isAt(from, ONLY_LEFT)) return false;
                    if (where == Direction.RIGHT && !board.isAt(from, ONLY_RIGHT)) return false;
                }
                return true;
            }

            @Override
            public boolean possible(Point point) {
                if (board.isAt(point, BRICK)) return false;
                if (board.isAt(point, WALL)) return false;
                return true;
            }
        };
    }

}
