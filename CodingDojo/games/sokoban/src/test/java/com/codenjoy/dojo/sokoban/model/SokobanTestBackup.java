package com.codenjoy.dojo.sokoban.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.sokoban.services.Player;
import com.codenjoy.dojo.sokoban.model.game.Sokoban;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Hero;
import com.codenjoy.dojo.sokoban.model.itemsImpl.LevelImpl;
import com.codenjoy.dojo.sokoban.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SokobanTestBackup {

    private Sokoban game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Sokoban(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener,"Player1");
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я ходить
    @Test
    public void shouldWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды я никуда не иду
    @Test
    public void shouldStopWhenNoMoreRightCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallUp() {
        givenFl("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallDown() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");
    }

    // я могу оставить бомбу
    @Test
    public void shouldMakeBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // на бомбе я взрываюсь
    @Test
    @Ignore // TODO вернуть к жизни
    public void shouldDieOnBomb() {
        shouldMakeBomb();

        assertTrue(hero.isAlive());

        hero.up();
        game.tick();
        verify(listener).event(Events.LOOSE);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertFalse(hero.isAlive());
    }

    // я могу оставить бомб сколько хочу
    @Test
    public void shouldMakeBombTwice() {
        shouldMakeBomb();

        hero.act();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ x☺☼" +
                "☼☼☼☼☼");
    }

    // я могу собирать золото и получать очки
    // новое золото появится в рендомном месте
    @Test
    @Ignore // TODO вернуть к жизни
    public void shouldGetGold() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(1, 3);
        hero.right();
        game.tick();
        verify(listener).event(Events.WIN);

        assertE("☼☼☼☼☼" +
                "☼$  ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // выполнения команд left + act не зависят от порядка - если они сделаны в одном тике, то будет дырка слева без перемещения
    @Test
    public void shouldMakeBomb2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.down();
        hero.act();
//        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    @Ignore // TODO вернуть к жизни
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(2, 2);
        hero.right();
        game.tick();
        verify(listener).event(Events.WIN);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ $☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я не могу ставить две бомбы на одной клетке
    @Test
    @Ignore // TODO вернуть к жизни
    public void shouldMakeOnlyOneBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        dice(1, 2);
        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.newGame(player);
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
}
