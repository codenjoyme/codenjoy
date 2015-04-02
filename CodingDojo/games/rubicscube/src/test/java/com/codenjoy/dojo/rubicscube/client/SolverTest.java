package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.rubicscube.client.utils.BoardImpl;
import com.codenjoy.dojo.rubicscube.client.utils.Dice;
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
        asertAI("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ", Face.DOWN, Rotate.CLOCKWISE);
    }


    private void asertAI(String board, Face face, Rotate rotate) {
        String actual = ai.get(board(board));
        assertEquals(String.format("ACT(%s, %s)", face.number, rotate.rotate), actual);
    }

    private void dice(Face face) {
        when(dice.next(anyInt())).thenReturn(face.number);
    }
}
