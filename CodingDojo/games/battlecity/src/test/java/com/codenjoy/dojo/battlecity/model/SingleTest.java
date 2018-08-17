package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleTest {

    private int size = 5;
    private Battlecity field;
    private Dice dice1;
    private Dice dice2;
    private Single tanks1;
    private Single tanks2;
    private Player player1;
    private Player player2;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    public void givenGame() {
        field = new Battlecity(size, mock(Dice.class), Arrays.asList(new Construction[0]));
        player1 = new Player(null, dice1);
        player2 = new Player(null, dice2);
        tanks1 = new Single(player1, printerFactory);
        tanks1.on(field);
        tanks2 = new Single(player2, printerFactory);
        tanks2.on(field);
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

        tanks1.getPlayer().getHero().act();
        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        field.tick();

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

        tanks1.getPlayer().getHero().act();
        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

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
                this.field.reader(), player).print());
    }

}
