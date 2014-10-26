package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
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
public class SingleTanksTest {

    private int size = 5;
    private Battlecity tanks;
    private Dice dice1;
    private Dice dice2;
    private SingleBattlecity tanks1;
    private SingleBattlecity tanks2;
    private Player player1;
    private Player player2;

    public void givenGame() {
        tanks = new Battlecity(size, Arrays.asList(new Construction[0]));
        tanks1 = new SingleBattlecity(tanks, null, dice1);
        tanks2 = new SingleBattlecity(tanks, null, dice2);
        player1 = tanks1.getPlayer();
        player2 = tanks2.getPlayer();
    }

    @Test
    public void shouldRandomPositionWhenNewGame() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 1, 2, 2);

        givenGame();

        tanks1.newGame();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        tanks2.newGame();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldRandomPositionWhenKillTank() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 2, 2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        tanks1.getPlayer().getTank().act();
        tick();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        tick();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

    }

    @Test
    public void shouldRandomPositionButAtFreeSpaceWhenKillTank() {
        dice1 = givenDice(1, 1);
        dice2 = givenDice(1, 2, 0, 0, 2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        tanks1.getPlayer().getTank().act();
        tick();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        tick();

        assertDraw(player1,
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

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

    private void assertDraw(Player player, String field) {
        assertEquals(field, TanksTest.getPrinterFor(tanks, player).toString());
    }

}
