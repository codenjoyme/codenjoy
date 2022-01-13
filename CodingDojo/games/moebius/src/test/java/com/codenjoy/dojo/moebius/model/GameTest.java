package com.codenjoy.dojo.moebius.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.moebius.TestGameSettings;
import com.codenjoy.dojo.moebius.services.Event;
import com.codenjoy.dojo.moebius.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.dice.MockDice;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameTest {

    private Moebius game;
    private MockDice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer;
    private Joystick joystick;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = new MockDice();
        printer = new PrinterFactoryImpl();
        settings = new TestGameSettings();
    }

    private void dice(Integer... next) {
        dice.then(next);
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        listener = mock(EventListener.class);
        game = new Moebius(level, dice, settings);
        player = new Player(listener, settings);
        game.newGame(player);
        joystick = player.getHero();
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта с границами
    @Test
    public void shouldFieldAtStart() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // за тик рендомно появляется 1 линия
    @Test
    public void shouldAddRandomLine_whenTick() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(1, 1, 0);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 3, 2);
        game.tick();

        assertE("╔═══╗" +
                "║╔  ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(2, 3, 3);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗ ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 3, 4);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 2, 5);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚ ║║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 1, 6);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚ ║║" +
                "║╝ ╬║" +
                "╚═══╝");
    }

    // если не так линия, то ошибку получаем
    @Test
    public void shouldError_whenBadLineType() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        try {
            dice(3, 3, 7);
            game.tick();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Неопознанная линия: ' '", e.getMessage());
        }
    }

    // линия не может появиться на другой линии
    @Test
    public void shouldAddRandomLine_onlyAtFreeSpace() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(1, 1, 0);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 1,
             1, 1,
             1, 1,
             1, 1,
             2, 2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║ ╚ ║" +
                "║╝  ║" +
                "╚═══╝");
    }

    // линия не может появиться на границе
    @Test
    public void shouldAddRandomLine_onlyAtFreeSpaceBetweenBorders() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(0, 0,
            3, 15,
            0, 3,
            3, 4,
            4, 3,
            3, 0,
            -1, 3,
            3, -1,
            15, 3,
            1, 1, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╚  ║" +
                "╚═══╝");
    }

    // я могу повернуть одну любую линию по часовой стрелке
    @Test
    public void shouldRotateClockwiseOneLine_caseCorner() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╗ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╝ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╚ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╔ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╗ ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRotateClockwiseOneLine_caseLine() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ═ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ║ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ═ ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRotateClockwiseOneLine_caseCross() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╬ ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║ ╬ ║" +
                "╚═══╝");
    }

    // я не могу повернуть пустое место
    @Test
    public void shouldRotateEmptySpace() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // любая команда кроме act игнорится
    @Test
    public void shouldIgnoreCommandsExceptAct() {
        givenFl("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");

        joystick.left();
        joystick.right();
        joystick.up();
        joystick.down();
        game.tick();
        game.tick();
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");
    }

    // если я во время вращения сделаю круг, то все линии пропадут
    @Test
    public void shouldRemoveLine_whenCycling() {
        givenFl("╔═══╗" +
                "║╔╗ ║" +
                "║╚╗ ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 2);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 4));

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╚  ║" +
                "╚═══╝");
    }

    private void verifyEvent(Event expected) {
        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(listener).event(event.capture());
        Event actual = event.getValue();
        assertEquals(expected.type(), actual.type());
        assertEquals(expected.value(), actual.value());
    }

    @Test
    public void shouldRemoveLine_whenCycling2() {
        givenFl("╔═══╗" +
                "║╔═╗║" +
                "║║ ║║" +
                "║╚═╗║" +
                "╚═══╝");

        joystick.act(3, 1);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 8));

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╚  ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling3() {
        givenFl("╔════╗" +
                "║╔══╗║" +
                "║║╔═╝║" +
                "║║╚═╗║" +
                "║╚══╗║" +
                "╚════╝");

        joystick.act(4, 1);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 16));

        assertE("╔════╗" +
                "║    ║" +
                "║    ║" +
                "║    ║" +
                "║╚   ║" +
                "╚════╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling_withNoise() {
        givenFl("╔══════╗" +
                "║╔═╔║══║" +
                "║║╔══╗╗║" +
                "║╔║║╔║║║" +
                "║╚║║║║║║" +
                "║║╚══╗║║" +
                "║╔╗╔╗║║║" +
                "╚══════╝");

        joystick.act(5, 2);
        dice(2, 2, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 12));

        assertE("╔══════╗" +
                "║╔═╔║══║" +
                "║║    ╗║" +
                "║╔ ║╔ ║║" +
                "║╚ ║║ ║║" +
                "║║╚   ║║" +
                "║╔╗╔╗║║║" +
                "╚══════╝");
    }

    // любая команда кроме act игнорится
    @Test
    public void shouldGameOver_whenNoMoreSpace() {
        givenFl("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");

        assertTrue(player.isAlive());

        game.tick();

        verifyEvent(new Event(Event.Type.GAME_OVER));

        assertFalse(player.isAlive());
    }

    // если есть CROSS линия, то по ней можно пройтись дважды
    @Test
    public void shouldRemoveLine_whenCycling_caseCross() {
        givenFl("╔═════╗" +
                "║  ╔═╗║" +
                "║  ║ ║║" +
                "║╔═╬═╝║" +
                "║║ ║  ║" +
                "║╚═╗  ║" +
                "╚═════╝");

        joystick.act(3, 1);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 15));

        assertE("╔═════╗" +
                "║     ║" +
                "║     ║" +
                "║     ║" +
                "║     ║" +
                "║╚    ║" +
                "╚═════╝");
    }

    // если кросс линия использована не полностью, то она все равно удаляется
    @Test
    public void shouldRemoveLine_whenCycling_caseCrossDoNotRemove() {
        givenFl("╔═════╗" +
                "║╔═╗  ║" +
                "║║ ║  ║" +
                "║║═╬═╝║" +
                "║║ ║  ║" +
                "║╚═╗  ║" +
                "╚═════╝");

        joystick.act(3, 1);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 12));

        assertE("╔═════╗" +
                "║     ║" +
                "║     ║" +
                "║ ═ ═╝║" +
                "║     ║" +
                "║╚    ║" +
                "╚═════╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling_caseCrossDoNotRemove2() {
        givenFl("╔═════╗" +
                "║╔═══╗║" +
                "║║ ║ ║║" +
                "║╚═╬═╗║" +
                "║  ║  ║" +
                "║  ║  ║" +
                "╚═════╝");

        joystick.act(5, 3);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Event(Event.Type.WIN, 12));

        assertE("╔═════╗" +
                "║     ║" +
                "║  ║  ║" +
                "║     ║" +
                "║  ║  ║" +
                "║╚ ║  ║" +
                "╚═════╝");
    }
}
