package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.loderunner.services.LoderunnerEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 5:22
 */
public class SingleLoderunnerTest {

    // появляется другие игроки, игра становится мультипользовательской
    @Test
    public void shouldMultipleGame() { // TODO разделить тест на части и порефакторить дублирование
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼   $☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, listener2);

        EventListener listener3 = mock(EventListener.class);
        Game game3 = new SingleLoderunner(loderunner, listener3);

        when(dice.next(anyInt())).thenReturn(1, 4);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(2, 2);
        game2.newGame();

        when(dice.next(anyInt())).thenReturn(3, 4);
        game3.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼► ► ☼\n" +
                "☼####☼\n" +
                "☼ ► $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game1.getJoystick().right();
        game2.getJoystick().left();
        game3.getJoystick().right();

        game1.tick(); // достаточно тикнуть у одной доски

        expected =
                "☼☼☼☼☼☼\n" +
                "☼ ► ►☼\n" +
                "☼####☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game1.getJoystick().act();
        game3.destroy();

        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼ R. ☼\n" +
                "☼##*#☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());
        assertEquals(expected, game3.getBoardAsString());

        game1.getJoystick().right();

        game1.tick();
        game1.tick();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼◄ ►$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        game1.getJoystick().left();
        game1.getJoystick().act();
        game2.getJoystick().right();

        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ [Я$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        for (int c = 2; c < Brick.DRILL_TIMER; c++) {
            game1.tick();
        }

        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼#Ѡ##☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        verify(listener2).event(LoderunnerEvents.KILL_HERO);
        verify(listener1).event(LoderunnerEvents.KILL_ENEMY);
        assertTrue(game2.isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);
        game2.newGame();

        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
        assertEquals(expected, game2.getBoardAsString());

        game1.getJoystick().right();

        when(dice.next(anyInt())).thenReturn(1, 2);

        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼$  ►☼\n" +
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

    // можно ли проходить героям друг через дурга? Нет
    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtWay() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, listener2);

        when(dice.next(anyInt())).thenReturn(1, 2);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(2, 2);
        game2.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►►  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().right();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►►  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game2.getJoystick().left();
        game2.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►◄  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtLadder() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H  ☼" +
                "☼ H  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, listener2);

        when(dice.next(anyInt())).thenReturn(1, 2);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(2, 4);
        game2.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼ ►  ☼\n" +
                "☼ H  ☼\n" +
                "☼►H  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().right();
        game2.getJoystick().down();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().up();
        game2.getJoystick().down();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().up();
        game2.getJoystick().down();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtPipe() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ~~ ☼" +
                "☼#  #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, listener2);

        when(dice.next(anyInt())).thenReturn(1, 3);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(4, 3);
        game2.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼►~~►☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().right();
        game2.getJoystick().left();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().right();
        game2.getJoystick().left();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().right();
        game2.getJoystick().left();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
    }

    // могу ли я сверлить под другим героем? Нет
    @Test
    public void shouldICantDrillUnderOtherHero() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼►►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        Dice dice = mock(Dice.class);
        Loderunner loderunner = new Loderunner(level, dice);

        EventListener listener1 = mock(EventListener.class);
        Game game1 = new SingleLoderunner(loderunner, listener1);

        EventListener listener2 = mock(EventListener.class);
        Game game2 = new SingleLoderunner(loderunner, listener2);

        when(dice.next(anyInt())).thenReturn(1, 2);
        game1.newGame();

        when(dice.next(anyInt())).thenReturn(2, 2);
        game2.newGame();

        String expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►►  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());

        game1.getJoystick().act();
        game2.getJoystick().left();
        game2.getJoystick().act();
        game1.tick();

        expected =
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►◄  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n";

        assertEquals(expected, game1.getBoardAsString());
    }

    // если я прыгаю сверху на героя, то я должен стоять у него на голове
}
