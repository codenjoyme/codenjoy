package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.rubicscube.model.Element;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Printer;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 26.06.14.
 */
public class SingleHexTest {

    private Hex hex;
    private Level level;
    private Dice dice;

    public void givenGame() {
        level = mock(Level.class);
        when(level.getSize()).thenReturn(5);

        hex = new Hex(level, dice);
        List<SingleHex> games = new LinkedList<SingleHex>();
        for (int index = 0; index < Elements.heroesElements().size() + 1; index++) {
            SingleHex game = new SingleHex(hex, null);
            games.add(game);
            game.newGame();
        }
    }

    // вводится 3-4-5-n игрок на поле
    @Test
    public void shouldManyPlayers() {
        givenDice(0,0, 0,2, 0,4, 1,1, 1,3, 2,0, 2,2, 2,4, 3,1, 3,3, 4,0, 4,2, 4,4);
        givenGame();

        assertF("♥ ◘ ☺" +
                " ♣ ◙ " +
                "☻ • ♀" +
                " ♦ ○ " +
                "☺ ♠ ♂");
    }

    private void givenDice(int... values) {
        dice = mock(Dice.class);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertF(String expected) {
        for (Player player : hex.getPlayers()) {
            LoderunnerTest.assertE(new Printer(hex.getSize(), new HexPrinter(hex, player)).toString(), expected);
        }
    }

}
