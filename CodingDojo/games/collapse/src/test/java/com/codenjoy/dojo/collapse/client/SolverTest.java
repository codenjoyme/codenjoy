package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.collapse.client.utils.BoardImpl;
import com.codenjoy.dojo.collapse.client.utils.Dice;
import com.codenjoy.dojo.collapse.client.Direction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        asertAI("☼☼☼☼☼☼☼" +
                "☼12345☼" +
                "☼67891☼" +
                "☼23456☼" +
                "☼78912☼" +
                "☼34567☼" +
                "☼☼☼☼☼☼☼", "ACT(3,4),RIGHT");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction value) {
        when(dice.next(anyInt())).thenReturn(value.value);
    }
}
