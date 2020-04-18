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
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeikstraFindWayTest {

    @Test
    public void testFindShortestWay() {
        asrtWay("XXXXXXX" +
                "XS    X" +
                "X*    X" +
                "X*    X" +
                "X*    X" +
                "X****FX" +
                "XXXXXXX");
    }

    @Test
    public void testFindShortestWayWhenBrickOnWay() {
        asrtWay("XXXXXXX" +
                "XS    X" +
                "X**   X" +
                "XO*   X" +
                "X *   X" +
                "X ***FX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XS    X" +
                "X**   X" +
                "XO**  X" +
                "X O*  X" +
                "X  **FX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XS    X" +
                "X**   X" +
                "XO**  X" +
                "X O***X" +
                "X   OFX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XS    X" +
                "X**** X" +
                "XO O* X" +
                "X O **X" +
                "X   OFX" +
                "XXXXXXX");
    }

    @Test
    public void testFindShortestWayWhenNoWay() {
        asrtWay("XXXXXXX" +
                "XSX   X" +
                "XXX   X" +
                "X     X" +
                "X     X" +
                "X    FX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XSO   X" +
                "XOO   X" +
                "X     X" +
                "X     X" +
                "X    FX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XS O  X" +
                "X  O  X" +
                "XOOO  X" +
                "X     X" +
                "X    FX" +
                "XXXXXXX");

        asrtWay("XXXXXXX" +
                "XS X  X" +
                "X  X  X" +
                "XXXX  X" +
                "X     X" +
                "X    FX" +
                "XXXXXXX");
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
        final AbstractBoard board = (AbstractBoard)new AbstractBoard<Elements>() {
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }
        }.forString(expected.replace(Elements.WAY.ch(), Elements.NONE.ch()));

        List<Point> starts = board.get(Elements.START);
        Point start = starts.get(0);
        List<Point> goals = board.get(Elements.FINISH);
        List<Direction> way = new DeikstraFindWay().getShortestWay(board.boardSize(),
                start, goals,
                new DeikstraFindWay.Possible() {
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
                    public boolean possible(Point atWay) {
                        return !board.isAt(atWay.getX(), atWay.getY(), Elements.BRICK);
                    }
                });

        Point current = start;
        for (int index = 0; index < way.size(); index++) {
            Direction direction = way.get(index);
            current = direction.change(current);

            Elements element = getElement(way, index);
            board.set(current.getX(), current.getY(), element.ch());
        }

        String actual = board.boardAsString();

        assertEquals(TestUtils.injectN(expected), actual);
    }

    private Elements getElement(List<Direction> way, int index) {
        if (index == way.size() - 1) {
            return Elements.FINISH;
        }
        return Elements.WAY;
    }

}
