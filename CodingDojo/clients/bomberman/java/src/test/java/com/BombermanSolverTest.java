package com;

import com.utils.Board;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BombermanSolverTest {

    private final YourDirectionSolver solver = new YourDirectionSolver();

    private void assertB(String board, Direction direction) {
        assertEquals(direction.toString(), solver.get(new Board(board)));
    }

    @Test
    public void should_when() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☺        # # ☼" +
                "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                "☼##           ☼" +
                "☼ ☼ ☼#☼ ☼ ☼ ☼ ☼" +
                "☼   #    # #  ☼" +
                "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                "☼             ☼" +
                "☼#☼ ☼ ☼#☼ ☼ ☼#☼" +
                "☼  #  #       ☼" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                "☼ ##      #   ☼" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                "☼ #   #  &    ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼",
                Direction.ACT);
    }

}
