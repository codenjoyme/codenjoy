package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.sudoku.services.SudokuEvents;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SudokuTest {

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

    private void givenFl(String board, String mask) {
        LevelImpl level = new LevelImpl(board, mask);

        game = new Sudoku(level);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        joystick = player.getJoystick();
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
                "☼  ?☼? ?☼???☼" +
                "☼ ??☼   ☼???☼" +
                "☼?  ☼???☼? ?☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ ??☼? ?☼?? ☼" +
                "☼ ??☼ ? ☼?? ☼" +
                "☼ ??☼? ?☼?? ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼? ?☼???☼  ?☼" +
                "☼???☼   ☼?? ☼" +
                "☼???☼? ?☼?  ☼" +
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

    // я не могу ходить на поля, которые уже заняты
    @Test
    public void shouldCantOpenOpenedCell() {
        shouldFieldAtStart();

        joystick.act(2, 3, 1);
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
        assertEquals(1, Sudoku.fix(1));
        assertEquals(2, Sudoku.fix(2));
        assertEquals(3, Sudoku.fix(3));
        assertEquals(5, Sudoku.fix(4));
        assertEquals(6, Sudoku.fix(5));
        assertEquals(7, Sudoku.fix(6));
        assertEquals(9, Sudoku.fix(7));
        assertEquals(10, Sudoku.fix(8));
        assertEquals(11, Sudoku.fix(9));
    }

    @Test
    public void shouldFailEventWhenFail() {
        shouldFieldAtStart();

        joystick.act(2, 2, 5);
        game.tick();

        verify(listener).event(SudokuEvents.FAIL);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldSuccessEventWhenSuccess() {
        shouldFieldAtStart();

        joystick.act(2, 8, 8);
        game.tick();

        verify(listener).event(SudokuEvents.SUCCESS);
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
                "☼???☼   ☼   ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        joystick.act(1, 9, 3);
        game.tick();

        joystick.act(2, 9, 4);
        game.tick();

        joystick.act(3, 9, 5);
        game.tick();

        verify(listener, times(3)).event(SudokuEvents.SUCCESS);
        verify(listener).event(SudokuEvents.WIN);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldIsGameOverWhenWin() {
        shouldWinEventWhenAllSuccess();

        assertTrue(game.isGameOver());

        game.newGame(player);

        assertFalse(game.isGameOver());

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
    public void shouldLooseWhenNewGame() {
        shouldFieldAtStart();

        joystick.act(2, 2, 5);
        game.tick();
        verify(listener).event(SudokuEvents.FAIL);

        joystick.act(0); // просим новую игру
        game.tick();

        verify(listener).event(SudokuEvents.LOOSE);
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        game.newGame(player);

        assertFalse(game.isGameOver());
        assertE(INITIAL);
    }

}