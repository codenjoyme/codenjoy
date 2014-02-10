package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SudokuTest {

    private Sudoku game;
    private EventListener listener;
    private Player player;
    private Joystick joystick;

    private void givenFl(String board, String mask) {
        LevelImpl level = new LevelImpl(board, mask);

        game = new Sudoku(level);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        joystick = player.getJoystick();
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(new Printer(game.getSize(), new SudokuPrinter(game, player)), expected);
    }

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
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }
}