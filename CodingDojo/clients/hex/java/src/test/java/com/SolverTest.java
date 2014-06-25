package com;

import com.utils.BoardImpl;
import com.utils.Dice;
import org.junit.Before;
import org.junit.Test;

import static com.Direction.*;

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
        asertAI("☼☼☼☼☼☼☼" +
                "☼  ☺  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼", "ACT(3,5),LEFT");

        asertAI("☼☼☼☼☼☼☼" +
                "☼ ☺☺  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼", "ACT(2,5),DOWN");

        asertAI("☼☼☼☼☼☼☼" +
                "☼ ☺☺  ☼" +
                "☼ ☺   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼", "ACT(2,4),RIGHT");

        asertAI("☼☼☼☼☼☼☼" +
                "☼ ☺☺  ☼" +
                "☼ ☺☺  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼", "ACT(2,4),DOWN");
    }


    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction value) {
        when(dice.next(anyInt())).thenReturn(value.value);
    }
}
