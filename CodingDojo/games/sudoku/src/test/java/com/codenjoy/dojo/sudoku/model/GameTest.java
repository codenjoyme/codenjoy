package com.codenjoy.dojo.sudoku.model;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.sudoku.model.level.Level;
import com.codenjoy.dojo.sudoku.services.Event;
import com.codenjoy.dojo.sudoku.services.GameSettings;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameTest {

    public static final String INITIAL =
            "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
            "☼53 ☼ 7 ☼   ☼" +
            "☼6  ☼195☼   ☼" +
            "☼ 98☼   ☼ 6 ☼" +
            "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
            "☼8  ☼ 6 ☼  3☼" +
            "☼4  ☼8 3☼  1☼" +
            "☼7  ☼ 2 ☼  6☼" +
            "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
            "☼ 6 ☼   ☼28 ☼" +
            "☼   ☼419☼  5☼" +
            "☼   ☼ 8 ☼ 79☼" +
            "☼☼☼☼☼☼☼☼☼☼☼☼☼";

    private Sudoku game;
    private EventListener listener;
    private Player player;
    private Joystick joystick;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();
    private GameSettings settings;

    private void givenFl(String board, String mask) {
        Level level = new Level(removeBoard(board), removeBoard(mask));

        settings = new GameSettings();
        game = new Sudoku(level, 0, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        game.newGame(player);
        joystick = player.getJoystick();
    }

    private String removeBoard(String map) {
        return map.replaceAll("☼", "");
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player).print());
    }

    // я вижу поле
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼",

                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼  *☼* *☼***☼" +
                "☼ **☼   ☼***☼" +
                "☼*  ☼***☼* *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ **☼* *☼** ☼" +
                "☼ **☼ * ☼** ☼" +
                "☼ **☼* *☼** ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼* *☼***☼  *☼" +
                "☼***☼   ☼** ☼" +
                "☼***☼* *☼*  ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

//        assertE(INITIAL);
    }

    // я могу походить
    @Test
    public void shouldTryToOpenNumber() {
        shouldFieldAtStart();

        joystick.act(2, 8, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼ 5 ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    // я могу передумать и походить иначе
    @Test
    public void shouldCanOpenNumberTwice() {
        shouldFieldAtStart();

        joystick.act(2, 8, 5);
        game.tick();

        joystick.act(2, 8, 8);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼ 8 ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    // я могу передумать и сбросить установленное ранее значение
    @Test
    public void shouldCanClearMySpep() {
        shouldFieldAtStart();

        assertE(INITIAL);

        joystick.act(2, 8, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼ 5 ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        joystick.act(2, 8, 0);
        game.tick();

        assertE(INITIAL);
    }

    // я не могу ходить на поля, которые уже заняты
    @Test
    public void shouldCantOpenOpenedCell() {
        shouldFieldAtStart();

        joystick.act(2, 3, 1);
        game.tick();

        assertE(INITIAL);
    }

    // я не могу сбросить поля, которые установлены изначально
    @Test
    public void shouldCantClearOpenedCell() {
        shouldFieldAtStart();

        joystick.act(2, 3, 0);
        game.tick();

        assertE(INITIAL);
    }

    // я могу походить В другом отсеке. Стенки игнорятся при расчете координат
    @Test
    public void shouldIgnoreBoardWhenOpenCell() {
        shouldFieldAtStart();

        joystick.act(6, 1, 2);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 72☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldFixCoordinates() {
        assertEquals(1, Player.fix(1));
        assertEquals(2, Player.fix(2));
        assertEquals(3, Player.fix(3));
        assertEquals(5, Player.fix(4));
        assertEquals(6, Player.fix(5));
        assertEquals(7, Player.fix(6));
        assertEquals(9, Player.fix(7));
        assertEquals(10, Player.fix(8));
        assertEquals(11, Player.fix(9));
    }

    @Test
    public void shouldFailEventWhenFail() {
        shouldFieldAtStart();

        joystick.act(2, 2, 5);
        game.tick();

        verify(listener).event(Event.FAIL);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldSuccessEventWhenSuccess() {
        shouldFieldAtStart();

        joystick.act(2, 8, 8);
        game.tick();

        verify(listener).event(Event.SUCCESS);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldWinEventWhenAllSuccess() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼",

                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼   ☼   ☼   ☼" +
                "☼   ☼   ☼   ☼" +
                "☼   ☼   ☼   ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼   ☼   ☼   ☼" +
                "☼   ☼   ☼   ☼" +
                "☼   ☼   ☼   ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼   ☼   ☼   ☼" +
                "☼   ☼   ☼   ☼" +
                "☼***☼   ☼   ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        joystick.act(1, 9, 3);
        game.tick();

        joystick.act(2, 9, 4);
        game.tick();

        joystick.act(3, 9, 5);
        game.tick();

        verify(listener, times(3)).event(Event.SUCCESS);
        verify(listener).event(Event.WIN);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldIsGameOverWhenWin() {
        shouldWinEventWhenAllSuccess();

        assertTrue(game.isGameOver());
        assertTrue(game.isWin());

        game.newGame(player);

        assertFalse(game.isGameOver());
        assertFalse(game.isWin());

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼   ☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantGoTwicePerTact() {
        shouldFieldAtStart();

        joystick.act(2, 8, 8); // игнорится
        joystick.act(2, 8, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼ 5 ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldIgnoreBadCoordinates() {
        shouldFieldAtStart();

        assertIgnoreCommand(0, 0, 8);
        assertIgnoreCommand(-1, 1, 8);
        assertIgnoreCommand(1, -1, 8);
        assertIgnoreCommand(16, 1, 8);
        assertIgnoreCommand(1, 110, 8);
        assertIgnoreCommand(1, 1, 10);
        assertIgnoreCommand(1, 1, 0);
        assertIgnoreCommand(1, 1, 100);
    }

    private void assertIgnoreCommand(int x, int y, int n) {
        joystick.act(x, y, n);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldLoseWhenNewGame() {
        shouldFieldAtStart();

        joystick.act(2, 2, 5);
        game.tick();
        verify(listener).event(Event.FAIL);

        joystick.act(0); // просим новую игру
        game.tick();

        verify(listener).event(Event.LOSE);
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        game.newGame(player);

        assertFalse(game.isGameOver());
        assertE(INITIAL);
    }

}
