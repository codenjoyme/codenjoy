package com.codenjoy.dojo.loderunner.client;

import com.codenjoy.dojo.loderunner.client.Direction;
import com.codenjoy.dojo.loderunner.client.DirectionSolver;
import com.codenjoy.dojo.loderunner.client.YourDirectionSolver;
import com.codenjoy.dojo.loderunner.client.utils.Board;
import com.codenjoy.dojo.loderunner.client.utils.Dice;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.loderunner.client.Direction.*;

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
        return new Board(board);
    }

    @Test
    public void should() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼ $  H☼" +
                "☼ ###H☼" +
                "☼    H☼" +
                "☼►   H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼", RIGHT);

        asertAI("☼☼☼☼☼☼☼" +
                "☼ $  H☼" +
                "☼ ###H☼" +
                "☼    H☼" +
                "☼ ►  H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼", RIGHT);

        asertAI("☼☼☼☼☼☼☼" +
                "☼ $  H☼" +
                "☼ ###H☼" +
                "☼    H☼" +
                "☼  ► H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼", RIGHT);

        asertAI("☼☼☼☼☼☼☼" +
                "☼ $  H☼" +
                "☼ ###H☼" +
                "☼    H☼" +
                "☼   ►H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼", RIGHT);
    }


    private void asertAI(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction value) {
        when(dice.next(anyInt())).thenReturn(value.value);
    }
}
