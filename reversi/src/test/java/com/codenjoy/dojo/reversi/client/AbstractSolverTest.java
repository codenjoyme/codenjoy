package com.codenjoy.dojo.reversi.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractSolverTest {
    protected Dice dice;
    protected Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = getSolver();
    }

    protected abstract Solver getSolver();

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    protected void asertAI(String board) {
        String expected = getExpected(board);
        asertAI(board.replace('+', ' '), expected);
    }

    private String getExpected(String board) {
        LengthToXY xy = new LengthToXY((int) Math.sqrt(board.length()));
        int index = board.indexOf('+');
        if (index == -1) {
            return "";
        }
        Point pt = xy.getXY(index);
        return String.format("ACT(%s,%s)", pt.getX(), pt.getY());
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
