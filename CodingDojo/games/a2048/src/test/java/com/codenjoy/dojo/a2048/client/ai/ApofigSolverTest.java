package com.codenjoy.dojo.a2048.client.ai;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApofigSolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new ApofigSolver();
    }

    private Board givenBd(String board) {
        return (Board)new Board().forString(board);
    }

    @Test
    public void should2() {
        asertAI("2  I4" +
                "4  HA" +
                "8  CC" +
                "A   G" +
                "B   8", Direction.RIGHT);
    }

    @Test
        public void should() {
            assertEquals(1, givenBd(
                    " 22  " +
                    "     " +
                    "     " +
                    "     " +
                    "     ").getSumCountFor(Direction.LEFT));

        assertEquals(2, givenBd(
                " 22  " +
                "  22 " +
                "     " +
                "     " +
                "     ").getSumCountFor(Direction.LEFT));

        assertEquals(2, givenBd(
                " 222 " +
                "  22 " +
                "     " +
                "     " +
                "     ").getSumCountFor(Direction.LEFT));


    }


    private void asertAI(String board, Direction expected) {
        String actual = ai.get(givenBd(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
