package com;

import com.utils.BoardImpl;
import com.utils.Dice;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 05.10.13
 * Time: 11:56
 */
public class SolverTest {

    private Dice dice;
    private DirectionSolver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new YourDirectionSolver(dice);
    }

    private BoardImpl board(String board) {
        return new BoardImpl(board);
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
