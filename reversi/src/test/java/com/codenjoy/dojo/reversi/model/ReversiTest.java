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


import com.codenjoy.dojo.reversi.services.Events;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class ReversiTest {

    private Reversi game;
    private Hero hero1;
    private Hero hero2;
    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Player player1;
    private Player player2;
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
        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);
        player1 = new Player(listener1);
        player2 = new Player(listener2);
        game.newGame(player1);
        game.newGame(player2);
        hero1 = game.getHeroes().get(0);
        hero2 = game.getHeroes().get(1);
    }

    private void assertE(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // поле изначально инциализировалось 4 фишками по 2 на каждый цвет
    @Test
    public void shouldFieldAtStart() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        assertE("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ",
                player1);

        assertE("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ",
                player2);

        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player2);
    }

    // я могу походить и перевернуть одну фишку горизонтально влево
    @Test
    public void shouldChangeChip_left() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(5, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   ooo  " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я могу походить и перевернуть одну фишку горизонтально вправо
    @Test
    public void shouldChangeChip_right() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(2, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  ooo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я могу походить и перевернуть одну фишку вертикально вверх
    @Test
    public void shouldChangeChip_up() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(4, 2);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oo   " +
                "    o   " +
                "        " +
                "        ",
                player1);
    }

    // я могу походить и перевернуть одну фишку вертикально вниз
    @Test
    public void shouldChangeChip_down() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(3, 5);
        game.tick();

        assertE("        " +
                "        " +
                "   o    " +
                "   oo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я не могу походить если ничего не переверну (вниз)
    @Test
    public void shouldNotChangeChip_whenNoEnemy_down() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(4, 5);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я могу одновременно переворачивать в нескольких направлениях
    @Test
    public void shouldChangeChipsInAllDirections() {
        givenFl("        " +
                "    O   " +
                "    x   " +
                "  Ox xO " +
                "    x   " +
                "    O   " +
                "        " +
                "        ");

        hero1.act(4, 4);
        game.tick();

        assertE("        " +
                "    o   " +
                "    o   " +
                "  ooooo " +
                "    o   " +
                "    o   " +
                "        " +
                "        ",
                player1);
    }

    // я могу переворачивать только в тех направлениях где есть моя фишка через 1
    @Test
    public void shouldNotChangeChips_whenNoSupportChip() {
        givenFl("        " +
                "    O   " +
                "    x   " +
                "   x x  " +
                "    x   " +
                "        " +
                "        " +
                "        ");

        hero1.act(4, 4);
        game.tick();

        assertE("        " +
                "    o   " +
                "    o   " +
                "   XoX  " +
                "    X   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я могу перевернуть некоторое число фишек в любом направлении
    @Test
    public void shouldChangeSeveralChips_whenSupportChipInOtherSide() {
        givenFl("    O   " +
                "    x   " +
                "    x   " +
                "Oxxx xxO" +
                "    x   " +
                "    x   " +
                "    x   " +
                "    O   ");

        hero1.act(4, 4);
        game.tick();

        assertE("    o   " +
                "    o   " +
                "    o   " +
                "oooooooo" +
                "    o   " +
                "    o   " +
                "    o   " +
                "    o   ",
                player1);
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

        hero1.act(4, 4);
        game.tick();

        assertE("        " +
                "    X   " +
                "    X   " +
                " XXX XX " +
                "    X   " +
                "    X   " +
                "    X   " +
                "        ",
                player1);
    }

    // я могу перевернуть некоторое число фишек в любом направлении только
    // если у меня есть фишка напротив
    @Test
    public void shouldChangeSeveralChips_whenSupportChipInOtherSide_case2() {
        givenFl("    O   " +
                "    x   " +
                "    x   " +
                "Oxxx xx " +
                "    x   " +
                "    x   " +
                "    x   " +
                "        ");

        hero1.act(4, 4);
        game.tick();

        assertE("    o   " +
                "    o   " +
                "    o   " +
                "oooooXX " +
                "    X   " +
                "    X   " +
                "    X   " +
                "        ",
                player1);
    }

    // я могу походить и перевернуть одну фишку диагонально в разные стороны
    @Test
    public void shouldChangeSeveralChips_whenWhenDiagonalDirections() {
        givenFl("       O" +
                "  O   x " +
                "   x x  " +
                "        " +
                "   x x  " +
                "  x   O " +
                " x      " +
                "O       ");

        hero1.act(4, 4);
        game.tick();

        assertE("       o" +
                "  o   o " +
                "   o o  " +
                "    o   " +
                "   o o  " +
                "  o   o " +
                " o      " +
                "o       ",
                player1);
    }

    // попробовать походить черными сперва - не получится
    // а после белого - пожалуйста
    // но если один из игроков пропустил ход, то следующим ходит его опонент
    @Test
    public void shouldChangeChip_blackGoesFirst() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero2.act(5, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);

        hero1.act(5, 3);
        hero2.act(5, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xxx  " +
                "   Ox   " +
                "        " +
                "        " +
                "        ",
                player1);

        hero1.act(5, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   XXX  " +
                "   ooo  " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // я победил когда поле заполнено и моих фишек больше всего
    // я проиграл когда поле заполнено и моих фишек меньше всего
    @Test
    public void shouldWinLoose_noEmptySpace_whiteWin() {
        givenFl("oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oXoooooo" +
                "  oXoooo");

        hero2.act(1, 0);
        game.tick();

        assertE("OOOOOOOO" +
                "OOOOOOOO" +
                "OOOOOOOO" +
                "OOOOOOOO" +
                "OOOOOOOO" +
                "OOOOOOOO" +
                "OxOOOOOO" +
                " xxxOOOO",
                player1);

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        hero1.act(0, 0);
        game.tick();

        assertE("oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo" +
                "oooooooo",
                player1);

        verify(listener1).event(Events.WIN);
        verify(listener2).event(Events.LOOSE);
    }
    
    // побеждает черный, когда все поле занято белым
    @Test
    public void shouldWinLoose_noEmptySpace_blackWin() {
        givenFl("xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xOxxxxxx" +
                "  xOxxxx");

        hero1.act(1, 0);
        game.tick();

        assertE("XXXXXXXX" +
                "XXXXXXXX" +
                "XXXXXXXX" +
                "XXXXXXXX" +
                "XXXXXXXX" +
                "XXXXXXXX" +
                "XoXXXXXX" +
                " oooXXXX",
                player1);

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        hero2.act(0, 0);
        game.tick();

        assertE("xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx" +
                "xxxxxxxx",
                player1);

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // сделать валидацию на act(x, y)
    @Test
    public void shouldSkipNotValidActCommand_oneParamener() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(5);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    @Test
    public void shouldSkipNotValidActCommand_zerroParamener() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act();
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    @Test
    public void shouldSkipNotValidActCommand_twoInvalidCoordinates() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(8, -1);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    @Test
    public void shouldValidActCommand_threeParameners() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        hero1.act(2, 4, 2);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  ooo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // игра не идет (ходы игнорятся), даже если тикается, пока не добавится
    // ровно два игрока
    @Test
    public void shouldDoNothing_ifOnlyOnePlayer_black() {
        givenFl("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ");

        game.remove(player2);

        hero1.act(2, 4);
        game.tick();


        assertE("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ",
                player1);

        game.newGame(player2);
        hero2 = player2.hero;

        assertEquals(true, player2.isBlack());

        hero1.act(2, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  ooo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);

        hero2.act(2, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  OOO   " +
                "  xxx   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    @Test
    public void shouldDoNothing_ifOnlyOnePlayer_white() {
        givenFl("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ");

        game.remove(player1);

        hero2.act(2, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ",
                player1);

        game.newGame(player1);
        hero1 = player1.hero;

        assertEquals(true, player1.isWhite());

        hero2.act(2, 3);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xO   " +
                "  xxx   " +
                "        " +
                "        " +
                "        ",
                player1);

        hero1.act(2, 4);
        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "  ooo   " +
                "  XXX   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // если добавился третий игрок, то вылетает исключение без добавления игрока
    @Test
    public void shouldDoNothing_whenAddThirdPlayer() {
        givenFl("        " +
                "        " +
                "        " +
                "   Xo   " +
                "   oX   " +
                "        " +
                "        " +
                "        ");

        try {
            game.newGame(new Player(mock(EventListener.class)));
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Нельзя добавить игрока - поле занято!", exception.getMessage());
        }

        game.tick();

        assertE("        " +
                "        " +
                "        " +
                "   xO   " +
                "   Ox   " +
                "        " +
                "        " +
                "        ",
                player1);
    }

    // TODO побеждает белый, если черному больше некуда ходить и у него меньше фишек
    // TODO побеждает белый, если черному больше некуда ходить и у него вообще нет фишек
    // TODO побеждает черный, если белому больше некуда ходить и у него меньше фишек
    // TODO побеждает черный, если белому больше некуда ходить и у него вообще нет фишек

    // TODO если кто-то победил, тогда игра начинается снова с теми же пользователями

    // TODO добавить препятствия на поле
    // TODO борда может быть большего размера
}
