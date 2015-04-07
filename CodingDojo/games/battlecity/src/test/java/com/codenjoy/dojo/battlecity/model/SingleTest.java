package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 09.11.13
 * Time: 17:59
 */
public class SingleTest {

    private int size = 5;
    private Battlecity game;
    private Dice dice1;
    private Dice dice2;
    private Single tanks1;
    private Single tanks2;
    private Player player1;
    private Player player2;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    public void givenGame() {
        game = new Battlecity(size, Arrays.asList(new Construction[0]));
        tanks1 = new Single(game, null, printerFactory, dice1);
        tanks2 = new Single(game, null, printerFactory, dice2);
        player1 = tanks1.getPlayer();
        player2 = tanks2.getPlayer();
    }

    @Test
    public void shouldRandomPositionWhenNewGame() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 1, 2, 2);

        givenGame();

        tanks1.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    @Test
    public void shouldRandomPositionWhenKillTank() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 2, 2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        tanks1.getPlayer().getTank().act();
        tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    @Test
    public void shouldRandomPositionButAtFreeSpaceWhenKillTank() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 2, 0, 0, 2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        tanks1.getPlayer().getTank().act();
        tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    private void tick() {
        tanks1.tick();  // тикать надо только один раз - и все применится для основной доски
    }

    private Dice givenDice(int... values) {
        Dice dice1 = mock(Dice.class);
        OngoingStubbing<Integer> when = when(dice1.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
        return dice1;
    }

    private void assertD(String field, Player player) {
        assertEquals(field, printerFactory.getPrinter(
                game.reader(), player).print());
    }

}
