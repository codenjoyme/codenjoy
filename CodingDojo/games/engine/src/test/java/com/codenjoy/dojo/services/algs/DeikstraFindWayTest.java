package com.codenjoy.dojo.services.algs;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by indigo on 03.08.2016.
 */
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
        final AbstractBoard board = new AbstractBoard<Elements>() {
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }
        }.forString(expected.replace(Elements.WAY.ch(), Elements.NONE.ch()));

        List<Point> starts = board.get(Elements.START);
        Point start = starts.get(0);
        List<Point> goals = board.get(Elements.FINISH);
        List<Direction> way = new DeikstraFindWay().getShortestWay(board.size(),
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