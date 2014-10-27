package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 26.06.14.
 */
public class SingleHexTest {

    private Hex game;
    private Level level;
    private Dice dice;
    private int count = 2;
    private EventListener listener;

    public void givenGame() {
        level = mock(Level.class);
        listener = mock(EventListener.class);
        when(level.getSize()).thenReturn(5);

        game = new Hex(level, dice);
        List<SingleHex> games = new LinkedList<SingleHex>();
        for (int index = 0; index < count; index++) {
            SingleHex game = new SingleHex(this.game, listener);
            games.add(game);
            game.newGame();
        }
    }

    // вводится 3-4-5-n игрок на поле
    @Test
    public void shouldManyPlayers() {
        count = Elements.heroesElements().size() + 1;
        givenDice(0,0, 0,2, 0,4, 1,1, 1,3, 2,0, 2,2, 2,4, 3,1, 3,3, 4,0, 4,2, 4,4);
        givenGame();

        // then
        assertF("♥ ◘ ☺" +
                " ♣ ◙ " +
                "☻ • ♀" +
                " ♦ ○ " +
                "☺ ♠ ♂");
    }

    // нельзя ходить по другим игрокам
    @Test
    public void shouldNoWayAtHero() {
        count = 3;
        givenDice(0,0, 0,1, 0,2);
        givenGame();

        assertF("     " +
                "     " +
                "♥    " +
                "☻    " +
                "☺    ");

        // when
        Player player = game.getPlayers().get(1);
        Joystick joystick = player.getJoystick();
        joystick.act(0, 1); // ☻
        joystick.up();
        game.tick();

        // then
        assertF("     " +
                "     " +
                "♥    " +
                "☻    " +
                "☺    ");

        verifyNoMoreInteractions(listener);
    }

    private void givenDice(int... values) {
        dice = mock(Dice.class);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertF(String expected) {
        for (Player player : game.getPlayers()) {
            LoderunnerTest.assertE(Printer.getSimpleFor(game.reader(), player, Elements.NONE).toString(), expected);
        }
    }

}
