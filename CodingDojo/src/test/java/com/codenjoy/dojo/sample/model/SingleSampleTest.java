package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Before;
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
public class SingleSampleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private SingleSample game1;
    private SingleSample game2;
    private SingleSample game3;
    private Dice dice;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼   $☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        dice = mock(Dice.class);
        Sample Sample = new Sample(level, dice);

        listener1 = mock(EventListener.class);
        game1 = new SingleSample(Sample, listener1);

        listener2 = mock(EventListener.class);
        game2 = new SingleSample(Sample, listener2);

        listener3 = mock(EventListener.class);
        game3 = new SingleSample(Sample, listener3);

        dice(1, 4);
        game1.newGame();

        dice(2, 2);
        game2.newGame();

        dice(3, 4);
        game3.newGame();
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void asrtFl1(String expected) {
        assertEquals(expected, game1.getBoardAsString());
    }

    private void asrtFl2(String expected) {
        assertEquals(expected, game2.getBoardAsString());
    }

    private void asrtFl3(String expected) {
        assertEquals(expected, game3.getBoardAsString());
    }

    // рисуем несколько игроков
    @Test
    public void shouldPrint() {
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl2(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl3(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // Каждый игрок может упраыляться за тик игры независимо
    @Test
    public void shouldJoystick() {
        game1.getJoystick().act();
        game1.getJoystick().down();
        game2.getJoystick().right();
        game3.getJoystick().down();

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x  $☼\n" +
                "☼☺ ☻ ☼\n" +
                "☼  ☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.destroy();

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺  $☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игрок может взорваться на бомбе
    @Test
    public void shouldKill() {
        game1.getJoystick().down();
        game1.getJoystick().act();
        game3.getJoystick().left();

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x☻ $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        game3.getJoystick().left();
        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼X  $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener3).event(SampleEvents.LOOSE);
        assertTrue(game3.isGameOver());

        dice(4, 1);
        game3.newGame();

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼   $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼   ☻☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игрок может подобрать золото
    @Test
    public void shouldGetGold() {
        game3.getJoystick().right();

        dice(1, 2);

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺  ☻☼\n" +
                "☼    ☼\n" +
                "☼$☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener3).event(SampleEvents.WIN);

        assertEquals(1, game3.getCurrentScore());
        assertEquals(1, game3.getMaxScore());

        assertEquals(0, game2.getCurrentScore());
        assertEquals(0, game2.getMaxScore());

        assertEquals(0, game1.getCurrentScore());
        assertEquals(0, game1.getMaxScore());

        game3.clearScore();

        assertEquals(0, game3.getCurrentScore());
        assertEquals(0, game3.getMaxScore());
    }

    // игрок не может пойи на другого игрока
    @Test
    public void shouldCantGoOnHero() {
        game1.getJoystick().right();
        game3.getJoystick().left();

        game1.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼ ☺☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }
}
