package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class BombermanSolverTest {

    private Dice dice = mock(Dice.class);
    private final YourSolver solver = new YourSolver(dice);

    private void assertB(String board, Direction direction) {
        assertEquals(direction.toString(), solver.get((Board) new Board().forString(board)));
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
