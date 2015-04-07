package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by Sanja on 26.06.14.
 */
public class SingleTest {

    private Hex game;
    private Level level;
    private Dice dice;
    private int count = 2;
    private EventListener listener;
    private PrinterFactory printerFactory;

    public void givenGame() {
        level = mock(Level.class);
        printerFactory = new PrinterFactoryImpl();
        listener = mock(EventListener.class);
        when(level.getSize()).thenReturn(5);

        game = new Hex(level, dice);
        List<Single> games = new LinkedList<Single>();
        for (int index = 0; index < count; index++) {
            Single game = new Single(this.game, listener, printerFactory);
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
        assertF("♦ ○  " +
                " ♠ ♂ " +
                "♥ ◘ ♀" +
                " ♣ ◙ " +
                "☺ • ♀", game.getPlayers().get(0));
    }

    // нельзя ходить по другим игрокам
    @Test
    public void shouldNoWayAtHero() {
        count = 3;
        givenDice(0,0, 0,1, 0,2);
        givenGame();

        assertF("     " +
                "     " +
                "♦    " +
                "♥    " +
                "☺    ", game.getPlayers().get(0));

        assertF("     " +
                "     " +
                "♦    " +
                "☺    " +
                "☻    ", game.getPlayers().get(1));

        assertF("     " +
                "     " +
                "☺    " +
                "♥    " +
                "☻    ", game.getPlayers().get(2));

        // when
        Player player = game.getPlayers().get(1);
        Joystick joystick = player.getJoystick();
        joystick.act(0, 1); // ☻
        joystick.up();
        game.tick();

        // then
        assertF("     " +
                "     " +
                "♦    " +
                "♥    " +
                "☺    ", game.getPlayers().get(0));

        assertF("     " +
                "     " +
                "♦    " +
                "☺    " +
                "☻    ", game.getPlayers().get(1));

        assertF("     " +
                "     " +
                "☺    " +
                "♥    " +
                "☻    ", game.getPlayers().get(2));

        verifyNoMoreInteractions(listener);
    }

    private void givenDice(int... values) {
        dice = mock(Dice.class);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertF(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player).print());
    }

}
