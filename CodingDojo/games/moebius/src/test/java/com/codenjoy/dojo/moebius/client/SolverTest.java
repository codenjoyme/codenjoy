package com.codenjoy.dojo.moebius.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assrtAI("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝", "ACT(1, 2)");

        assrtAI("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝", "ACT(1, 2)");

        assrtAI("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝", "ACT(1, 2)");
    }

    private void assrtAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
