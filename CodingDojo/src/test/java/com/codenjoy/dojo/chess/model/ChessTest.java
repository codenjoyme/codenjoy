package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class ChessTest {

    private Chess game;
    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Player player1;
    private Player player2;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);

        game = new Chess(level, dice);

        listener1 = mock(EventListener.class);
        player1 = new Player(listener1);
        game.newGame(player1);

        listener2 = mock(EventListener.class);
        player2 = new Player(listener2);
        game.newGame(player2);
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(Printer.getSimpleFor(game.reader(), player1, Elements.NONE), expected);
    }

    // есть карта со всеми играми
    @Test
    public void shouldFieldAtStart() {
        givenFl("tksfaskt" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "TKSFASKT");

        assertE("tksfaskt" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "TKSFASKT");
    }

    // я ходить
    @Test
    @Ignore
    public void shouldWalk() {
        givenFl("tksfaskt" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "TKSFASKT");

        player1.act();
        game.tick();

        assertE("tksfaskt" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "TKSFASKT");
    }

}
