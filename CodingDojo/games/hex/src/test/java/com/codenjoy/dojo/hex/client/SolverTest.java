package com.codenjoy.dojo.hex.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.services.Dice;
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

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼☼☼☼☼☼☼", "ACT(3,5),UP");

        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼☼☼☼☼☼☼", "ACT(3,4),UP");

        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼☼☼☼☼☼☼", "ACT(3,3),UP");

        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼  ☺  ☼" +
                "☼☼☼☼☼☼☼", "ACT(3,2),UP");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
