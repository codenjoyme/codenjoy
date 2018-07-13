package com.codenjoy.dojo.pong;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.pong.client.Board;
import com.codenjoy.dojo.pong.client.YourSolver;
import com.codenjoy.dojo.pong.client.ai.PongSolver;
import com.codenjoy.dojo.pong.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(
                0, 1, // ball horizontal and vertical init speed vector
                0, 0, // solvers rateCoefficients
                0, 0, // -- " --
                0, 0,
                0, 0,
                0, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "           " +
                        "-----------" +
                        "           " +
                        "           " +
                        "           " +
                        "     o     " +
                        "           " +
                        "           " +
                        "           " +
                        "-----------" +
                        "           ";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ArrayList<Solver>() {{
                    add(new PongSolver(dice));
                    add(new YourSolver(dice));
                }},
                new ArrayList<ClientBoard>() {{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("DICE:0\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #       H \n" +
                        "1: #   o   H \n" +
                        "1: #       H \n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H       # \n" +
                        "2: H   o   # \n" +
                        "2: H       # \n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: #  o    H \n" +
                        "1:         H \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: H  o    # \n" +
                        "2:         # \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1:   o     H \n" +
                        "1:         H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2:   o     # \n" +
                        "2:         # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1:         H \n" +
                        "1:  o      H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2:         # \n" +
                        "2:  o      # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: o       H \n" +
                        "1:         H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: o       # \n" +
                        "2:         # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "Fire Event: LOOSE\n" +
                        "Fire Event: WIN\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #   o   H \n" +
                        "1: #       H \n" +
                        "1:         H \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H   o   # \n" +
                        "2: H       # \n" +
                        "2:         # \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #       H \n" +
                        "1: #       H \n" +
                        "1: #  o    H \n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H       # \n" +
                        "2: H       # \n" +
                        "2: H  o    # \n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: #       H \n" +
                        "1:   o     H \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: H       # \n" +
                        "2:   o     # \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1:         H \n" +
                        "1:  o      H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2:         # \n" +
                        "2:  o      # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: o       H \n" +
                        "1:         H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: o       # \n" +
                        "2:         # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "Fire Event: LOOSE\n" +
                        "Fire Event: WIN\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #   o   H \n" +
                        "1: #       H \n" +
                        "1:         H \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H   o   # \n" +
                        "2: H       # \n" +
                        "2:         # \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #       H \n" +
                        "1: #       H \n" +
                        "1: #  o    H \n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H       # \n" +
                        "2: H       # \n" +
                        "2: H  o    # \n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: #       H \n" +
                        "1:   o     H \n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: H       # \n" +
                        "2:   o     # \n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1:         H \n" +
                        "1:  o      H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2:         # \n" +
                        "2:  o      # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:           \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:           \n" +
                        "1: #         \n" +
                        "1: #         \n" +
                        "1: #       H \n" +
                        "1: o       H \n" +
                        "1:         H \n" +
                        "1:-----------\n" +
                        "1:           \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:           \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:           \n" +
                        "2: H         \n" +
                        "2: H         \n" +
                        "2: H       # \n" +
                        "2: o       # \n" +
                        "2:         # \n" +
                        "2:-----------\n" +
                        "2:           \n" +
                        "2:\n" +
                        "2:Answer: STOP\n" +
                        "Fire Event: LOOSE\n" +
                        "Fire Event: WIN\n" +
                        "DICE:0\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
