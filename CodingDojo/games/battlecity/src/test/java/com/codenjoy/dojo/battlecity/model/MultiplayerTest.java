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


import com.codenjoy.dojo.battlecity.TestGameSettings;
import com.codenjoy.dojo.battlecity.model.items.Ice;
import com.codenjoy.dojo.battlecity.model.items.River;
import com.codenjoy.dojo.battlecity.model.items.Tree;
import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.AI_PRIZE_LIMIT;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerTest {

    private int size = 5;
    private Battlecity game;
    private Game tanks1;
    private Game tanks2;
    private Player player1;
    private Player player2;
    private PrinterFactory printerFactory;
    private GameSettings settings;
    private Dice dice;

    @Before
    public void setUp() {
        dice = mock(Dice.class);
        settings = new TestGameSettings();
        printerFactory = new PrinterFactoryImpl();
    }

    public void givenGame() {
        game = new Battlecity(size, dice, settings);

        game.addBorder(new DefaultBorders(size).get());

        player1 = new Player(null, settings);
        player2 = new Player(null, settings);
        tanks1 = new Single(player1, printerFactory);
        tanks1.on(game);
        tanks2 = new Single(player2, printerFactory);
        tanks2.on(game);
    }

    @Test
    public void shouldRandomPosition_whenNewGame() {
        dice(1, 1,
                2, 2);

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
    public void shouldCantDoAnything_whenRoundIsNotStarted_whenRoundsEnabled() {
        settings.bool(ROUNDS_ENABLED, true);

        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertEquals(false, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(false, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().right();
        hero1().act();

        hero2().right();
        game.tick();

        assertEquals(false, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(false, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    @Test
    public void shouldCanDoAnything_whenRoundsDisabled() {
        settings.bool(ROUNDS_ENABLED, false);

        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertEquals(true, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(true, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().right();
        hero1().act();

        hero2().right();
        game.tick();

        assertEquals(true, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(true, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˃ ☼\n" +
                "☼ ►•☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    public Tank hero2() {
        return (Tank) tanks2.getPlayer().getHero();
    }

    public Tank hero1() {
        return (Tank) tanks1.getPlayer().getHero();
    }

    @Test
    public void shouldRandomPosition_whenKillTank() {
        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenKillTank() {
        dice(1, 1,
                1, 2,
                0, 0, // skipped, not free, because hero
                2, 2);

        givenGame();

        tanks1.newGame();
        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenTrySpawnUnderTreeRiverOrIce() {
        dice(1, 1,
                1, 2,
                3, 3, // skipped, not free, because tree
                3, 2, // skipped, not free, because river
                3, 1, // skipped, not free, because ice
                2, 2);

        givenGame();
        game.addTree(new Tree(pt(3, 3)));
        game.addRiver(new River(pt(3, 2)));
        game.addIce(new Ice(pt(3, 1)));

        tanks1.newGame();
        tanks2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼˄ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼Ѡ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(tanks2.isGameOver());
        tanks2.newGame();

        game.tick();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼ ˄~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    private void dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertD(String field, Player player) {
        assertEquals(field, printerFactory.getPrinter(
                this.game.reader(), player).print());
    }

}
