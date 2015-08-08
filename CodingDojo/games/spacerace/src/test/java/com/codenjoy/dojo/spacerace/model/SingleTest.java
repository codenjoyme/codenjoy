package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Ignore;
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
public class SingleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Single game1;
    private Single game2;
    private Single game3;
    private Dice dice;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new LevelImpl(
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        dice = mock(Dice.class);
        Spacerace spacerace = new Spacerace(level, dice,
                SpaceraceTest.UNLIMITED_CHARGER.getTicksToRecharge(),
                SpaceraceTest.UNLIMITED_CHARGER.getBulletsCount());
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(spacerace, listener1, factory);

        listener2 = mock(EventListener.class);
        game2 = new Single(spacerace, listener2, factory);

        listener3 = mock(EventListener.class);
        game3 = new Single(spacerace, listener3, factory);

        dice(1, 0);
        game1.newGame();

        dice(2, 0);
        game2.newGame();

        dice(3, 0);
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
        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻☻☼\n");

        asrtFl2(
                "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼☻☺☻☼\n");

        asrtFl3(
                "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼   ☼\n" +
                        "☼☻☻☺☼\n");
    }

    // Каждый игрок может упраыляться за тик игры независимо
    @Test
    public void shouldJoystick() {
        game1.getJoystick().up();
        game3.getJoystick().up();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺ ☻☼\n" +
                "☼ ☻ ☼\n");

        game1.getJoystick().up();
        game2.getJoystick().right();
        game3.getJoystick().left();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼ ☻ ☼\n" +
                "☼  ☻☼\n");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.destroy();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n");
    }

    // игрок может выстрелить в другого игрока
    // привязать пули к игроку, и каждый получат очки за свое
    @Test
    public void shouldKillOneHeroAnother() {
        game1.getJoystick().up();
        game2.getJoystick().up();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼  ☻☼\n");

        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().left();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼   ☼\n" +
                "☼ ☻ ☼\n");

        dice(-1, -1);
        game3.getJoystick().act();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼ * ☼\n" +
                "☼ ☻ ☼\n");

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺+ ☼\n" +
                "☼   ☼\n" +
                "☼ ☻ ☼\n");

        verify(listener3).event(Events.DESTROY_ENEMY);
        verify(listener2).event(Events.LOOSE);
        verifyNoMoreInteractions(listener1);

        assertTrue(game2.isGameOver());

        dice(1, 0);
        game2.newGame();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☻☻ ☼\n");
    }

    // если игрок идет на встречу булету, то он все равно должен погибать
    @Test
    public void shouldKillOneHeroAnother_caseGoOnBullet() {
        game1.getJoystick().up();
        game2.getJoystick().up();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼  ☻☼\n");

        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().left();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼   ☼\n" +
                "☼ ☻ ☼\n");

        dice(-1, -1);
        game3.getJoystick().act();
        game2.getJoystick().down();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼ + ☼\n" +
                "☼ ☻ ☼\n");

        verify(listener3).event(Events.DESTROY_ENEMY);
        verify(listener2).event(Events.LOOSE);
        verifyNoMoreInteractions(listener1);

        assertTrue(game2.isGameOver());

        dice(1, 0);
        game2.newGame();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☻☻ ☼\n");
    }

    // если игрок идет на встречу булету, то он все равно должен погибать
    @Test
    public void shouldKillOneHeroAnother_caseGoOnBullet_case2() {
        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().up();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻☻☼\n" +
                "☼   ☼\n");

        game1.getJoystick().up();
        game2.getJoystick().up();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼  ☻☼\n" +
                "☼   ☼\n");

        dice(-1, -1);
        game3.getJoystick().left();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻ ☼\n" +
                "☼ ☻ ☼\n" +
                "☼   ☼\n");

        game3.getJoystick().act();
        game2.getJoystick().up();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼ ☻ ☼\n" +
                "☼☺* ☼\n" +
                "☼ ☻ ☼\n" +
                "☼   ☼\n");

        game2.getJoystick().down();
        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺+ ☼\n" +
                "☼ ☻ ☼\n" +
                "☼   ☼\n");

        verify(listener3).event(Events.DESTROY_ENEMY);
        verify(listener2).event(Events.LOOSE);
        verifyNoMoreInteractions(listener1);

        assertTrue(game2.isGameOver());

        dice(1, 0);
        game2.newGame();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼ ☻ ☼\n" +
                "☼☻  ☼\n");
    }

    // игрок не может пойи на другого игрока
    @Test
    public void shodCantGoOnHero() {
        game1.getJoystick().right();
        game2.getJoystick().right();
        game3.getJoystick().left();

        game1.tick();

        asrtFl1("☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻☻☼\n");
    }
}
