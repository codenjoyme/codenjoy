package com.codenjoy.dojo.collapse.model;

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

import com.codenjoy.dojo.collapse.TestGameSettings;
import com.codenjoy.dojo.collapse.services.Event;
import com.codenjoy.dojo.collapse.services.GameSettings;
import com.codenjoy.dojo.games.collapse.Element;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.dice.MockDice;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GameTest {

    private Collapse game;
    private EventListener listener;
    private Player player;
    private Joystick joystick;
    private MockDice dice;
    private PrinterFactory<Element, Player> printerFactory;
    private GameSettings settings;

    @Before
    public void setup() {
        printerFactory = new PrinterFactoryImpl<>();
        settings = new TestGameSettings();
    }

    private void givenFl(String board) {
        Level level = new Level(board);

        dice = new MockDice();
        game = new Collapse(level, dice, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        game.newGame(player);
        joystick = player.getHero();
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player).print());
    }

    // я вижу поле
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼123☼" +
                "☼456☼" +
                "☼789☼" +
                "☼☼☼☼☼");

        // then
        assertE("☼☼☼☼☼" +
                "☼123☼" +
                "☼456☼" +
                "☼789☼" +
                "☼☼☼☼☼");
    }

    // я могу походить заменив местами две циферки влево
    @Test
    public void shouldExchangeLeft() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼231☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.left();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼321☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вправо
    @Test
    public void shouldExchangeRight() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼123☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼132☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вниз
    @Test
    public void shouldExchangDown() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼121☼" +
                "☼131☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼131☼" +
                "☼121☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вверх
    @Test
    public void shouldExchangUp() {
        givenFl("☼☼☼☼☼" +
                "☼131☼" +
                "☼121☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼121☼" +
                "☼131☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я не могу походить заменив циферку на место стенки - стенка не трогается
    @Test
    public void shouldCantExchangeWithWall() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼111☼" +
                "☼211☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 1);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼111☼" +
                "☼211☼" +
                "☼☼☼☼☼");
    }

    // если в ходе моих перемещений образуются конгломераты :) то я получаю выиграшные очки за каждый блок
    @Test
    public void shouldCleanAfterExchange() {
        givenFl("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼3  ☼" +
                "☼2  ☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        assertEvent("SUCCESS(4)");

        // when
        joystick.act(1, 3);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼211☼" +
                "☼ 11☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEvent("SUCCESS(4)");
    }

    // если в ходе моих перемещений образуются конгломераты :)
    // удаляются конгломераты всех цветов
    @Test
    public void shouldCleanAfterExchange2() {
        givenFl("☼☼☼☼☼" +
                "☼333☼" +
                "☼233☼" +
                "☼222☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEvent("SUCCESS(9)");
    }

    private void assertEvent(String expected) {
        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(listener).event(event.capture());
        assertEquals(expected, event.getValue().toString());

        Mockito.verifyNoMoreInteractions(listener);
        reset(listener);
    }

    // после того как конгломерат исчезнет сверху упадет еще немного новых рендомных циферок
    @Test
    public void shouldNewNumbersAfterClear() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        joystick.act(1, 2);
        joystick.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼3  ☼" +
                "☼2  ☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        dice(6 - 1, 7 - 1, 8 - 1, 9 - 1);
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼379☼" +
                "☼268☼" +
                "☼333☼" +
                "☼☼☼☼☼");

    }

    private void dice(Integer... next) {
        dice.then(next);
    }

    // ходить надо в течении одного тика, все недоделанные ходы за тик стиратся
    @Test
    public void shouldOnlyActWithDirectionPerOneTick() {
        givenFl("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        // проигнорится, мы не указали куда двигаем
        joystick.act(1, 2);
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        // проигнорится, мы не указали что двигаем
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        // проигнорится, мы не указали куда двигаем
        joystick.act(1, 2);
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");
    }

    // если после удаления конгломерата ничего не делать, то со следующим тиком упадут те, кто сверху над пустотами
    @Test
    public void shouldFallWhenClear() {
        givenFl("☼☼☼☼☼" +
                "☼151☼" +
                "☼313☼" +
                "☼332☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(3, 1);
        joystick.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼151☼" +
                "☼ 12☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        // when
        dice(6 - 1, 7 - 1, 8 - 1, 9 - 1);
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼789☼" +
                "☼651☼" +
                "☼112☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldFallWhenClear2() {
        givenFl("☼☼☼☼☼☼" +
                "☼1234☼" +
                "☼ 9  ☼" +
                "☼   8☼" +
                "☼5 67☼" +
                "☼☼☼☼☼☼");

        // when
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼1111☼" +
                "☼1114☼" +
                "☼1238☼" +
                "☼5967☼" +
                "☼☼☼☼☼☼");
    }
}
