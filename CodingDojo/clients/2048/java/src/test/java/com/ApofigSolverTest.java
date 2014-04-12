package com;

import com.utils.BoardImpl;
import com.utils.Dice;
import org.junit.Before;
import org.junit.Test;

import static com.Direction.UP;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 05.10.13
 * Time: 11:56
 */
public class ApofigSolverTest {

    private Dice dice;
    private DirectionSolver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new ApofigDirectionSolver(dice);
    }

    private BoardImpl givenBd(String board) {
        return new BoardImpl(board);
    }

//    @Test
//    public void should() {
//        assertEquals(1, givenBd(
//                " 22  " +
//                "     " +
//                "     " +
//                "     " +
//                "     ").getSumCountFor(Direction.LEFT));
//
//        assertEquals(2, givenBd(
//                " 22  " +
//                "  22 " +
//                "     " +
//                "     " +
//                "     ").getSumCountFor(Direction.LEFT));
//
//        assertEquals(2, givenBd(
//                " 222 " +
//                "  22 " +
//                "     " +
//                "     " +
//                "     ").getSumCountFor(Direction.LEFT));
//
//
//    }


    private void asertAI(String board, Direction expected) {
        String actual = ai.get(givenBd(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction value) {
        when(dice.next(anyInt())).thenReturn(value.value);
    }
}
