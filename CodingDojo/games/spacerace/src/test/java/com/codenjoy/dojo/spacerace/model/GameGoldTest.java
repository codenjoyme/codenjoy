package com.codenjoy.dojo.spacerace.model;

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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.spacerace.services.GameSettings;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.BULLETS_COUNT;
import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.TICKS_TO_RECHARGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameGoldTest {

    private Spacerace game;
    private BulletCharger charger;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();
    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        settings = new GameSettings()
                .integer(TICKS_TO_RECHARGE, 100)
                .integer(BULLETS_COUNT, 1);
        charger = new BulletCharger(settings);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void diceNew(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));

        if (ints.length == 0) { // we work just with nothing
            when = when.thenReturn(-1);
        }

        if (ints.length == 1) { // we work just with stones
            when = when.thenReturn(-1, -1, -1, -1, ints[0], -1);
        }

        if (ints.length == 2) { // we work with stones and bombs
            when = when.thenReturn(-1, -1, -1, -1, ints[0], ints[1], -1);
        }

        if (ints.length == 4) { // we work stones, bombs and bulletPacks
            when = when.thenReturn(ints[2], ints[3], ints[0], ints[1], -1);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero(charger).get(0);

        game = new Spacerace(level, dice, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        dice(hero.getX(), hero.getY()); // позиция рассчитывается рендомно из dice
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    private void newBulletPackForHeroWithGivenBullets(int i) {
        SettingsReader settings = new GameSettings()
                .integer(TICKS_TO_RECHARGE, 1000)
                .integer(BULLETS_COUNT, i);

        charger = new BulletCharger(settings);
    }

    @Test
    public void bonusMoveDown() {

        // Given
        givenFl("☼   ☼" +
                "☼ $ ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        // when
        diceNew(-1, -1, -1, -1);

        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ $ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
        verify(listener, never()).event(Events.GET_GOLD);
    }

    @Test
    public void getBonus1() {

        // Given
        givenFl("☼   ☼" +
                "☼ $ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        // when
        diceNew(-1, -1, -1, -1);

        hero.move(Direction.UP);
        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        Mockito.verify(listener).event(Events.GET_GOLD);
        hero.move(Direction.DOWN);
        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

    }

    @Test
    public void getBonus2() {

        // Given
        givenFl("☼   ☼" +
                "☼ $ ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        // when
        diceNew(-1, -1, -1, -1);

        hero.move(Direction.UP);
        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        Mockito.verify(listener).event(Events.GET_GOLD);
        hero.move(Direction.LEFT);
        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼");

    }

    @Test
    public void getAtackBonus() {

        // Given
        givenFl("☼ $ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        // when
        diceNew(-1, -1, -1, -1);

        hero.recharge();
        hero.act();
        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼ $ ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        game.tick();

        // Then
        assertE("☼   ☼" +
                "☼ x ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
        Mockito.verify(listener).event(Events.GET_GOLD);

    }
}
