package com.codenjoy.dojo.minesweeper;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.client.ai.Vaa25Solver;
import com.codenjoy.dojo.minesweeper.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.GameField;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {
    private int index;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 225;

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(1);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            public GameField createGame() {
                settings.addEditBox("Board size").type(Integer.class).update(7);
                settings.addEditBox("Mines on board").type(Integer.class).update(2);

                return super.createGame();
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new Vaa25Solver("test", dice),
                new Board());

        // then
//        assertEquals("",
//                String.join("\n", messages));

    }
}
