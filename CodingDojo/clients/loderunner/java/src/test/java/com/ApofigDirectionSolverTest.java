package com;

import com.utils.Board;
import com.utils.Dice;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 24.10.2014.
 */
public class ApofigDirectionSolverTest {

    private ApofigDirectionSolver solver;
    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        solver = new ApofigDirectionSolver(dice);
    }

    @Test
    public void test() {
        assertC("☼☼☼☼☼" +
                "☼► $☼" +
                "☼###☼" +
                "☼###☼" +
                "☼☼☼☼☼", "[RIGHT, RIGHT]");
    }

    private void assertC(String boardString, String expected) {
        List<Direction> command = solver.getShortestWay(new Board(boardString));
        assertEquals(expected, command.toString());
    }
}
