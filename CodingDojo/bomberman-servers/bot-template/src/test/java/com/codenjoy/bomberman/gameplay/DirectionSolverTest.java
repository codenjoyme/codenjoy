package com.codenjoy.bomberman.gameplay;


import org.junit.Test;

import com.codenjoy.bomberman.domain.Board;
import com.codenjoy.bomberman.domain.Direction;
import com.codenjoy.bomberman.gameplay.DirectionSolver;

import static org.junit.Assert.assertEquals;

public class DirectionSolverTest {



    private void assertB(Board board, String direction) {
    	DirectionSolver solver = new DirectionSolver(board);
        assertEquals(direction, solver.get());
    }

    @Test
    public void should_when() {
        assertB(new Board(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
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
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"),
                Direction.ACT.toString());
    }

}
