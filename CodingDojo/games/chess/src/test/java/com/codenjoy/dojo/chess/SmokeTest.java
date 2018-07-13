package com.codenjoy.dojo.chess;

//import com.codenjoy.dojo.chess.client.Board;
//import com.codenjoy.dojo.chess.client.ai.ApofigSolver;
import com.codenjoy.dojo.client.LocalGameRunner;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        // when
//        LocalGameRunner.run(new GameRunner(),
//                new ApofigSolver(null),
//                new Board());

        // then
        assertEquals("",
                String.join("\n", messages));

    }
}
