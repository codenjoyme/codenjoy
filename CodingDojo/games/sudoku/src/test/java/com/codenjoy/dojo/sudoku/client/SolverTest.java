package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new YourSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT(1,2,3)");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

}
