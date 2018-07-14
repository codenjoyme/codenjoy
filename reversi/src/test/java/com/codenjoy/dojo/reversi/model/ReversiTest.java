package com.codenjoy.dojo.reversi.model;

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


import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.reversi.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class ReversiTest {

    private Reversi game;
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
        Level level = new LevelImpl(board);
        game = new Reversi(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // поле изначально инциализировалось 4 фишками по 2 на каждый цвет
    @Test
    public void shouldFieldAtStart() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        assertE("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");
    }

    // я могу походить и перевернуть одну фишку горизонтально влево
    @Test
    public void shouldChangeChip_left() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        hero.act(5, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ooo  " +
                "        " +
                "        " +
                "        ");
    }

    // я могу походить и перевернуть одну фишку горизонтально вправо
    @Test
    public void shouldChangeChip_right() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        hero.act(2, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  ooo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");
    }

    // я могу походить и перевернуть одну фишку вертикально вверх
    @Test
    public void shouldChangeChip_up() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        hero.act(4, 2);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xo   " +
                "   oo   " +
                "    o   " +
                "        " +
                "        ");
    }

    // я могу походить и перевернуть одну фишку вертикально вниз
    @Test
    public void shouldChangeChip_down() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        hero.act(3, 5);
        game.tick();

        assertE("        " +
                "        " +
                "   o    " +
                "   oo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");
    }

    // я не могу походить если ничего не переверну (вниз)
    @Test
    public void shouldNotChangeChip_whenNoEnemy_down() {
        givenFl("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");

        hero.act(4, 5);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xo   " +
                "   ox   " +
                "        " +
                "        " +
                "        ");
    }

    // я могу одновременно переворачивать в нескольких направлениях
    @Test
    public void shouldChangeChipsInAllDirections() {
        givenFl("        " +
                "    o   " +
                "    x   " +
                "  ox xo " +
                "    x   " +
                "    o   " +
                "        " +
                "        ");

        hero.act(4, 4);
        game.tick();

        assertE("        " +
                "    o   " +
                "    o   " +
                "  ooooo " +
                "    o   " +
                "    o   " +
                "        " +
                "        ");
    }

    // я могу переворачивать только в тех направлениях где есть моя фишка через 1
    @Test
    public void shouldNotChangeChips_whenNoSupportChip() {
        givenFl("        " +
                "    o   " +
                "    x   " +
                "   x x  " +
                "    x   " +
                "        " +
                "        " +
                "        ");

        hero.act(4, 4);
        game.tick();

        assertE("        " +
                "    o   " +
                "    o   " +
                "   xox  " +
                "    x   " +
                "        " +
                "        " +
                "        ");
    }

    // я могу перевернуть некоторое число фишек в любом направлении
    @Test
    public void shouldChangeSeveralChips_whenSupportChipInOtherSide() {
        givenFl("    o   " +
                "    x   " +
                "    x   " +
                "oxxx xxo" +
                "    x   " +
                "    x   " +
                "    x   " +
                "    o   ");

        hero.act(4, 4);
        game.tick();

        assertE("    o   " +
                "    o   " +
                "    o   " +
                "oooooooo" +
                "    o   " +
                "    o   " +
                "    o   " +
                "    o   ");
    }

    // я не могу перевернуть некоторое число фишек в любом направлении если у меня нет
    // фишки напротив
    @Test
    public void shouldNotChangeSeveralChips_whenNoSupportChipInOtherSide() {
        givenFl("        " +
                "    x   " +
                "    x   " +
                " xxx xx " +
                "    x   " +
                "    x   " +
                "    x   " +
                "        ");

        hero.act(4, 4);
        game.tick();

        assertE("        " +
                "    x   " +
                "    x   " +
                " xxx xx " +
                "    x   " +
                "    x   " +
                "    x   " +
                "        ");
    }

    // я могу перевернуть некоторое число фишек в любом направлении только
    // если у меня есть фишка напротив
    @Test
    public void shouldChangeSeveralChips_whenSupportChipInOtherSide_case2() {
        givenFl("    o   " +
                "    x   " +
                "    x   " +
                "oxxx xx " +
                "    x   " +
                "    x   " +
                "    x   " +
                "        ");

        hero.act(4, 4);
        game.tick();

        assertE("    o   " +
                "    o   " +
                "    o   " +
                "oooooxx " +
                "    x   " +
                "    x   " +
                "    x   " +
                "        ");
    }

    // я могу походить и перевернуть одну фишку диагонально в разные стороны
    @Test
    public void shouldChangeSeveralChips_whenWhenDiagonalDirections() {
        givenFl("       o" +
                "  o   x " +
                "   x x  " +
                "        " +
                "   x x  " +
                "  x   o " +
                " x      " +
                "o       ");

        hero.act(4, 4);
        game.tick();

        assertE("       o" +
                "  o   o " +
                "   o o  " +
                "    o   " +
                "   o o  " +
                "  o   o " +
                " o      " +
                "o       ");
    }

    // TODO я победил когда поле заполнено и моих фишек больше всего
    // TODO я проиграл когда поле заполнено и моих фишек меньше всего
    // TODO сделать валидацию на act(x, y)
    // TODO попробовать походить черными сперва
}
