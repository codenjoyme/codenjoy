package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.loderunner.services.LoderunnerEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Ticker;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 5:22
 */
public class SingleLoderunnerTest {

    // появляется другие игроки, игра становится мультипользовательской
    @Test
    public void shouldMultipleGame() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼###H☼" +
                "☼  $H☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);
        Ticker ticker = new Ticker(loderunner);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, ticker, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, ticker, listener2);

        EventListener listener3 = mock(EventListener.class);
        Game game3 = new SingleLoderunner(loderunner, ticker, listener3);

        when(dice.next(anyInt())).thenReturn(1, 4);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(2, 2);
        game2.newGame();

        when(dice.next(anyInt())).thenReturn(3, 4);
        game3.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼► ► ☼\n" +
                "☼###H☼\n" +
                "☼ ►$H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game1.getJoystick().act();
        game2.getJoystick().left();
        game3.getJoystick().right();

        game1.tick();
        game2.tick();
        game3.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼R. ►☼\n" +
                "☼#*#H☼\n" +
                "☼◄ $H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game3.destroy();

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼R   ☼\n" +
                "☼# #H☼\n" +
                "☼◄ $H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game1.getJoystick().right();

        game1.tick();
        game2.tick();

        game1.tick();
        game2.tick();

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼# #H☼\n" +
                "☼◄►$H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        game1.getJoystick().left();
        game1.getJoystick().act();

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼# #H☼\n" +
                "☼]Я$H☼\n" +
                "☼*###☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        for (int c = 2; c < Brick.DRILL_TIMER; c++) {
            game1.tick();
            game2.tick();
        }

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼###H☼\n" +
                "☼ Я$H☼\n" +
                "☼Ѡ###☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        verify(listener2).event(LoderunnerEvents.KILL_HERO);
        verify(listener1).event(LoderunnerEvents.KILL_ENEMY);
        assertTrue(game2.isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);
        game2.newGame();

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼###H☼\n" +
                "☼ Я$H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        game1.getJoystick().right();

        when(dice.next(anyInt())).thenReturn(1, 2);

        game1.tick();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼###H☼\n" +
                "☼$ ►H☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        verify(listener1).event(LoderunnerEvents.GET_GOLD);

        assertEquals(1, game1.getCurrentScore());
        assertEquals(1, game1.getMaxScore());

        assertEquals(0, game2.getCurrentScore());
        assertEquals(0, game2.getMaxScore());

        game1.clearScore();

        assertEquals(0, game1.getCurrentScore());
        assertEquals(0, game1.getMaxScore());
    }
}
