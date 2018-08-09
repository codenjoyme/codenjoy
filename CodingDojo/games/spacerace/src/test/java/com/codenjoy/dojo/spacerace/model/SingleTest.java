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

import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Single game1;
    private Single game2;
    private Single game3;
    private EventListener listener4;
    private EventListener listener5;
    private EventListener listener6;
    private Single game4;
    private Single game5;
    private Single game6;
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
                SpaceraceTest.getBulletCharger().getTicksToRecharge(),
                SpaceraceTest.getBulletCharger().getBulletsCount());
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(spacerace, new Player(listener1), factory);

        listener2 = mock(EventListener.class);
        game2 = new Single(spacerace, new Player(listener2), factory);

        listener3 = mock(EventListener.class);
        game3 = new Single(spacerace, new Player(listener3), factory);

        listener4 = mock(EventListener.class);
        game4 = new Single(spacerace, new Player(listener4), factory);

        listener5 = mock(EventListener.class);
        game5 = new Single(spacerace, new Player(listener5), factory);

        listener6 = mock(EventListener.class);
        game6 = new Single(spacerace, new Player(listener6), factory);

        dice(1, 0);
        game1.newGame();

        dice(2, 0);
        game2.newGame();

        dice(3, 0);
        game3.newGame();
    }

    private void diceOld(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y, x, y, x, y, -1);
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
        ((Hero)game3.getPlayer().getHero()).recharge();
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
        ((Hero)game3.getPlayer().getHero()).recharge();
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
        ((Hero)game3.getPlayer().getHero()).recharge();
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
        dice(-1, -1);
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

    // кол-во баллет паков 1
    @Test
    public void shodOneBulletPack() {

        dice(2, 1);
        game1.tick();
        dice(-1, -1);
        game1.tick();
        dice(-1, -1);
        game1.tick();
        dice(1, 1);
        game1.tick();

        asrtFl1("☼  7☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺☻☻☼\n");

    }

    // кол-во баллет паков 1
    @Test
    public void shodTwoBulletPacks() {
        dice(1, 1);
        game4.newGame();

        dice(2, 1);
        game5.newGame();

        dice(3, 1);
        game6.newGame();

        dice(2, 1);// появляются 2 баллет пака
        game1.tick();
        dice(1, 1);
        game1.tick();

        dice(-1, -1);// не появляется камень и бомба
        game1.tick();
        dice(-1, -1);
        game1.tick();

        dice(0, 1);// не появляется 3-й баллет пак
        game1.tick();

        asrtFl1("☼ 77☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☻☻☻☼\n" +
                "☼☺☻☻☼\n");
    }
}
