package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.minesweeper.client.utils.BoardImpl;
import com.codenjoy.dojo.minesweeper.client.utils.Dice;
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
        asertAI("☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*☺***☼" +
                "☼*****☼" +
                "☼☼☼☼☼☼☼", Direction.UP);

        asertAI("☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*☺***☼" +
                "☼* ***☼" +
                "☼*****☼" +
                "☼☼☼☼☼☼☼", Direction.UP);

        asertAI("☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*☺***☼" +
                "☼* ***☼" +
                "☼* ***☼" +
                "☼*****☼" +
                "☼☼☼☼☼☼☼", Direction.UP);

        asertAI("☼☼☼☼☼☼☼" +
                "☼*☺***☼" +
                "☼* ***☼" +
                "☼* ***☼" +
                "☼* ***☼" +
                "☼*****☼" +
                "☼☼☼☼☼☼☼", Direction.UP);
    }


    private void asertAI(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction value) {
        when(dice.next(anyInt())).thenReturn(value.value);
    }
}
