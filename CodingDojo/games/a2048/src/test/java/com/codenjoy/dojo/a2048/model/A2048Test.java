package com.codenjoy.dojo.a2048.model;

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


import com.codenjoy.dojo.a2048.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class A2048Test {

    private static final boolean WITH_BREAK = true;
    private static final boolean WITHOUT_BREAK = false;
    private static final int ADD_NEW_AT_CORNER = -1;
    private A2048 field;
    private Game game;
    private Joystick joystick;
    private Dice dice;
    private EventListener listener;
    private Level level;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        reset(dice);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        givenFl(board, 1, WITHOUT_BREAK);
    }

    private void givenFl(String board, int newNumbers, boolean mode) {
        givenFl(board, newNumbers, (mode)?1:0);
    }

    private void givenFl(String board, int newNumbers, int mode) {
        level = new LevelImpl(board);
        level.getSettings().getParameter("New numbers").type(Integer.class).update(newNumbers);
        level.getSettings().getParameter("Mode").type(Integer.class).update(mode);

        field = new A2048(level, dice);
        when(dice.next(anyInt())).thenReturn(-1); // ничего не генерим нового на поле с каждым тиком

        listener = mock(EventListener.class);
        game = new Single(new Player(listener), printer);
        game.on(field);
        game.newGame();
        this.joystick = game.getJoystick();
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(
                field.reader(), null).print());
    }

    // есть поле
    @Test
    public void shouldFieldAtStart() {
        givenFl(" 2  " +
                "    " +
                "  2 " +
                "    ");

        // when
        field.tick();

        // then
        assertE(" 2  " +
                "    " +
                "  2 " +
                "    ");
    }

    @Test
    public void shouldMoveNumbersWhenUseJoystickUp() {
        givenFl(" 2  " +
                "    " +
                "  4 " +
                "    ");

        joystick.up();
        field.tick();

        assertE(" 24 " +
                "    " +
                "    " +
                "    ");
    }

    @Test
    public void shouldMoveNumbersWhenUseJoystickDown() {
        givenFl(" 2  " +
                "    " +
                "  4 " +
                "    ");

        joystick.down();
        field.tick();

        assertE("    " +
                "    " +
                "    " +
                " 24 ");
    }

    @Test
    public void shouldMoveNumbersWhenUseJoystickRight() {
        givenFl(" 2  " +
                "    " +
                "  4 " +
                "    ");

        joystick.right();
        field.tick();

        assertE("   2" +
                "    " +
                "   4" +
                "    ");
    }

    @Test
    public void shouldMoveNumbersWhenUseJoystickLeft() {
        givenFl(" 2  " +
                "    " +
                "  4 " +
                "    ");

        joystick.left();
        field.tick();

        assertE("2   " +
                "    " +
                "4   " +
                "    ");
    }

    @Test
    public void shouldSumNumbersWhenEquals_moveRight() {
        givenFl("    " +
                "    " +
                "2 2 " +
                "    ");

        joystick.right();
        field.tick();

        assertE("    " +
                "    " +
                "   4" +
                "    ");
    }

    @Test
    public void shouldSumNumbersWhenEquals_moveUp() {
        givenFl("  2 " +
                "    " +
                "  2 " +
                "    ");

        joystick.up();
        field.tick();

        assertE("  4 " +
                "    " +
                "    " +
                "    ");
    }

    @Test
    public void shouldSumNumbersWhenEquals_moveLeft() {
        givenFl("  2 " +
                "    " +
                " 22 " +
                "    ");

        joystick.left();
        field.tick();

        assertE("2   " +
                "    " +
                "4   " +
                "    ");
    }

    @Test
    public void shouldSumNumbersWhenEquals_moveDown() {
        givenFl("  2 " +
                "  2 " +
                " 22 " +
                "    ");

        joystick.down();
        field.tick();

        assertE("    " +
                "    " +
                "  2 " +
                " 24 ");
    }

    @Test
    public void shouldNoDoubleSum() {
        givenFl("  2 " +
                "  2 " +
                "  2 " +
                "  2 ");

        joystick.down();
        field.tick();

        assertE("    " +
                "    " +
                "  4 " +
                "  4 ");
    }

    @Test
    public void shouldNoDoubleSum2() {
        givenFl("    " +
                "  4 " +
                "  2 " +
                "  2 ");

        joystick.down();
        field.tick();

        assertE("    " +
                "    " +
                "  4 " +
                "  4 ");
    }

    @Test
    public void shouldCombineSum() {
        givenFl("442 " +
                "4 24" +
                " 22 " +
                " 4 4");

        // when
        joystick.left();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("82  " +
                "424 " +
                "4   " +
                "8   ");

        // when
        joystick.down();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("    " +
                "8   " +
                "8   " +
                "844 ");

        // when
        joystick.right();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("    " +
                "   8" +
                "   8" +
                "  88");

        // when
        joystick.up();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("  8A" +
                "   8" +
                "    " +
                "    ");

        // when
        joystick.left();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("8A  " +
                "8   " +
                "    " +
                "    ");

        // when
        joystick.down();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("    " +
                "    " +
                "    " +
                "AA  ");

        // when
        joystick.right();
        field.tick();

        // then
        assertEvens("[SUM(32)]");
        assertE("    " +
                "    " +
                "    " +
                "   B");
    }

    @Test
    public void shouldAddScoreWhenMultipleAdd() {
        givenFl("    " +
                "    " +
                "    " +
                "    ", 3, WITHOUT_BREAK);

        // when
        dice(1,1, 2,3, 3,3);
        joystick.left();
        field.tick();

        // then
        assertEvens("[SUM(6)]");
        assertE("  22" +
                "    " +
                " 2  " +
                "    ");

        // when
        dice(-1, -1);
        joystick.down();
        field.tick();

        // then
        assertEvens("[SUM(6)]");
        assertE("    " +
                "    " +
                "    " +
                " 222");
    }

    @Test
    public void shouldFireEventWhenIncScore() {
        givenFl("    " +
                "    " +
                "2 2 " +
                "    ");

        // when
        joystick.right();
        field.tick();

        // then
        assertE("    " +
                "    " +
                "   4" +
                "    ");

        assertEvens("[SUM(4)]");

        // when
        joystick.up();
        dice(1, 2);
        field.tick();

        // then
        assertE("   4" +
                " 2  " +
                "    " +
                "    ");

        assertEvens("[SUM(6)]");
    }

    @Test
    public void shouldNewRandomNumberWhenTick() {
        givenFl("    " +
                "    " +
                "    " +
                "    ");

        // when
        joystick.up();

        dice(1, 2);
        field.tick();

        // then
        assertE("    " +
                " 2  " +
                "    " +
                "    ");

        // when
        joystick.up();

        dice(2, 2);
        field.tick();

        // then
        assertE(" 2  " +
                "  2 " +
                "    " +
                "    ");
    }

    @Test
    public void shouldNewNumberAtCornerWhenTick() {
        givenFl("    " +
                "    " +
                "    " +
                "    ", ADD_NEW_AT_CORNER, WITHOUT_BREAK);

        // when
        joystick.up();
        field.tick();

        // then
        assertE("2  2" +
                "    " +
                "    " +
                "2  2");

        // when
        joystick.up();
        field.tick();

        // then
        assertE("4  4" +
                "    " +
                "    " +
                "2  2");

        // when
        joystick.left();
        field.tick();

        // then
        assertE("8  2" +
                "    " +
                "    " +
                "4  2");

        // when
        joystick.right();
        field.tick();

        // then
        assertE("2 82" +
                "    " +
                "    " +
                "2 42");
    }

    @Test
    public void shouldNewNumbersWhenTick() {
        int newNumbers = 4;
        givenFl("    " +
                "    " +
                "    " +
                "    ", newNumbers, WITHOUT_BREAK);

        // when
        joystick.up();

        dice(0,1, 1,2, 2,2, 2,3);
        field.tick();

        // then
        assertE("  2 " +
                " 22 " +
                "2   " +
                "    ");
    }

    @Test
    public void shouldNewNumberOnlyIfUserAct() {
        givenFl("    " +
                "    " +
                "    " +
                "    ");

        // when
        dice(1, 2);   // Если поле чистое то одна двоечка добавляется после первого тика
        field.tick();

        // then
        assertE("    " +
                " 2  " +
                "    " +
                "    ");

        // when
        dice(2, 2);    // только если юзер сделал какое-то действие
//        joystick.up();
        field.tick();

        // then
        assertE("    " +
                " 2  " +
                "    " +
                "    ");
    }

    @Test
    public void shouldGameOverWhenNoSpace() {
        givenFl("4848" +
                "8282" +
                "4848" +
                "8 8 ");

        // when
        field.tick();

        // then
        assertEvens("[SUM(84)]");
        assertFalse(game.isGameOver());

        // when
        joystick.up();
        dice(1, 0);
        field.tick();

        // then
        assertEvens("[SUM(86)]");
        verifyNoMoreInteractions(listener);

        assertFalse(game.isGameOver());

        assertE("4848" +
                "8282" +
                "4848" +
                "828 ");

        // when
        joystick.up();
        dice(3, 0);
        field.tick();

        // then
        assertEvens("[SUM(88), GAME_OVER]");

        assertTrue(game.isGameOver());

        assertE("4848" +
                "8282" +
                "4848" +
                "8282");
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGo() {
        givenFl("2222" +
                "2222" +
                "2222" +
                "2222");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoHorizontal_1() {
        givenFl("4848" +
                "2284" +
                "4848" +
                "8484");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoHorizontal_2() {
        givenFl("4848" +
                "8224" +
                "4848" +
                "8484");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoHorizontal_3() {
        givenFl("4848" +
                "8422" +
                "4848" +
                "8484");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoVertical_1() {
        givenFl("4248" +
                "8284" +
                "4848" +
                "8484");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoVertical_2() {
        givenFl("4848" +
                "8284" +
                "4248" +
                "8484");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldNoGameOverWhenNoSpaceButCanGoVertical_3() {
        givenFl("4848" +
                "8484" +
                "4248" +
                "8284");

        // when
        field.tick();

        // then
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldGameOverWhen2048() {
        givenFl("    " +
                "    " +
                "   R" +
                "   R");
        assertFalse(game.isGameOver());

        // when
        joystick.down();
        field.tick();

        // then
        assertEvens("[SUM(4194304), WIN]");

        assertTrue(game.isGameOver());

        givenFl("    " +
                "    " +
                "    " +
                "   S");

        // when
        field.tick();

        // then
        verifyNoMoreInteractions(listener);

        givenFl("    " +
                "    " +
                "    " +
                "   S");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldDoNothingWhenGameOver() {
        shouldGameOverWhenNoSpace();
        assertE("4848" +
                "8282" +
                "4848" +
                "8282");

        // when
        joystick.left(); // ignore
        field.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        assertE("4848" +
                "8282" +
                "4848" +
                "8282");
    }

    @Test
    public void shouldNewGameWhenGameOver() {
        shouldGameOverWhenNoSpace();
        assertE("4848" +
                "8282" +
                "4848" +
                "8282");

        // when
        dice(0, 2);
        game.newGame();
        field.tick();

        // then
        assertEvens("[SUM(2)]");

        assertFalse(game.isGameOver());

        assertE("    " +
                "2   " +
                "    " +
                "    ");
    }

    @Test
    public void shouldPrintAll() {
        givenFl("RQPONMLKJIHGFEDCBA842 " +
                "QPONMLKJIHGFEDCBA842  " +
                "PONMLKJIHGFEDCBA842   " +
                "ONMLKJIHGFEDCBA842    " +
                "NMLKJIHGFEDCBA842     " +
                "MLKJIHGFEDCBA842      " +
                "LKJIHGFEDCBA842       " +
                "KJIHGFEDCBA842        " +
                "JIHGFEDCBA842         " +
                "IHGFEDCBA842          " +
                "HGFEDCBA842           " +
                "GFEDCBA842            " +
                "FEDCBA842             " +
                "EDCBA842              " +
                "DCBA842               " +
                "CBA842                " +
                "BA842                 " +
                "A842                  " +
                "842                   " +
                "42                    " +
                "2                     " +
                "                      ");

        // when
        joystick.down();
        field.tick();

        assertE("                      " +
                "R                     " +
                "QQ                    " +
                "PPP                   " +
                "OOOO                  " +
                "NNNNN                 " +
                "MMMMMM                " +
                "LLLLLLL               " +
                "KKKKKKKK              " +
                "JJJJJJJJJ             " +
                "IIIIIIIIII            " +
                "HHHHHHHHHHH           " +
                "GGGGGGGGGGGG          " +
                "FFFFFFFFFFFFF         " +
                "EEEEEEEEEEEEEE        " +
                "DDDDDDDDDDDDDDD       " +
                "CCCCCCCCCCCCCCCC      " +
                "BBBBBBBBBBBBBBBBB     " +
                "AAAAAAAAAAAAAAAAAA    " +
                "8888888888888888888   " +
                "44444444444444444444  " +
                "222222222222222222222 ");
    }

    private void assertEvens(String expected) {
        ArgumentCaptor captor = ArgumentCaptor.forClass(Events.class);
        int count = expected.split(",").length;
        verify(listener, times(count)).event(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
        reset(listener);
    }

    @Test
    public void performanceTest() {
        for (int count = 1; count <= 100; count ++) {
            caseSmth();
        }
    }

    private void caseSmth() {
        givenFl("RQPONMLKJIHGFEDCBA842 " +
                "QPONMLKJIHGFEDCBA842  " +
                "PONMLKJIHGFEDCBA842   " +
                "ONMLKJIHGFEDCBA842    " +
                "NMLKJIHGFEDCBA842     " +
                "MLKJIHGFEDCBA842      " +
                "LKJIHGFEDCBA842       " +
                "KJIHGFEDCBA842        " +
                "JIHGFEDCBA842         " +
                "IHGFEDCBA842          " +
                "HGFEDCBA842           " +
                "GFEDCBA842            " +
                "FEDCBA842             " +
                "EDCBA842              " +
                "DCBA842               " +
                "CBA842                " +
                "BA842                 " +
                "A842                  " +
                "842                   " +
                "42                    " +
                "2                     " +
                "                      ");

        // when
        joystick.down();
        field.tick();

        assertE("                      " +
                "R                     " +
                "QQ                    " +
                "PPP                   " +
                "OOOO                  " +
                "NNNNN                 " +
                "MMMMMM                " +
                "LLLLLLL               " +
                "KKKKKKKK              " +
                "JJJJJJJJJ             " +
                "IIIIIIIIII            " +
                "HHHHHHHHHHH           " +
                "GGGGGGGGGGGG          " +
                "FFFFFFFFFFFFF         " +
                "EEEEEEEEEEEEEE        " +
                "DDDDDDDDDDDDDDD       " +
                "CCCCCCCCCCCCCCCC      " +
                "BBBBBBBBBBBBBBBBB     " +
                "AAAAAAAAAAAAAAAAAA    " +
                "8888888888888888888   " +
                "44444444444444444444  " +
                "222222222222222222222 ");

        joystick.left();
        field.tick();

        assertE("                      " +
                "R                     " +
                "R                     " +
                "QP                    " +
                "PP                    " +
                "OON                   " +
                "NNN                   " +
                "MMML                  " +
                "LLLL                  " +
                "KKKKJ                 " +
                "JJJJJ                 " +
                "IIIIIH                " +
                "HHHHHH                " +
                "GGGGGGF               " +
                "FFFFFFF               " +
                "EEEEEEED              " +
                "DDDDDDDD              " +
                "CCCCCCCCB             " +
                "BBBBBBBBB             " +
                "AAAAAAAAA8            " +
                "8888888888            " +
                "44444444442           ");

        joystick.left();
        field.tick();

        assertE("                      " +
                "R                     " +
                "R                     " +
                "QP                    " +
                "Q                     " +
                "PN                    " +
                "ON                    " +
                "NML                   " +
                "MM                    " +
                "LLJ                   " +
                "KKJ                   " +
                "JJIH                  " +
                "III                   " +
                "HHHF                  " +
                "GGGF                  " +
                "FFFED                 " +
                "EEEE                  " +
                "DDDDB                 " +
                "CCCCB                 " +
                "BBBBA8                " +
                "AAAAA                 " +
                "888882                ");

        joystick.right();
        field.tick();

        assertE("                      " +
                "                     R" +
                "                     R" +
                "                    QP" +
                "                     Q" +
                "                    PN" +
                "                    ON" +
                "                   NML" +
                "                     N" +
                "                    MJ" +
                "                    LJ" +
                "                   KIH" +
                "                    IJ" +
                "                   HIF" +
                "                   GHF" +
                "                  FGED" +
                "                    FF" +
                "                   EEB" +
                "                   DDB" +
                "                  CCA8" +
                "                   ABB" +
                "                  8AA2");

        joystick.down();
        field.tick();

        assertE("                      " +
                "                      " +
                "                      " +
                "                      " +
                "                      " +
                "                      " +
                "                     S" +
                "                    QP" +
                "                    PQ" +
                "                    OO" +
                "                    NL" +
                "                    LN" +
                "                    IK" +
                "                    JH" +
                "                   NHJ" +
                "                   KEG" +
                "                   HFD" +
                "                   HEF" +
                "                   EDC" +
                "                  FDA8" +
                "                  CCBB" +
                "                  8BA2");
    }

    @Test
    public void shouldPrintWithBreakWhenSizeIs5() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ", 1, WITH_BREAK);

        // when
        field.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                "     " +
                "  x  ");
    }

    @Test
    public void shouldPrintWithBreakWhenSizeIs6() {
        givenFl("      " +
                "      " +
                "      " +
                "      " +
                "      " +
                "      ", 1, WITH_BREAK);

        // when
        field.tick();

        // then
        assertE("  xx  " +
                "      " +
                "x    x" +
                "x    x" +
                "      " +
                "  xx  ");
    }

    @Test
    public void shouldPrintWithBreakWhenSizeIs7() {
        givenFl("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       ", 1, WITH_BREAK);

        // when
        field.tick();

        // then
        assertE("  xxx  " +
                "   x   " +
                "x     x" +
                "xx   xx" +
                "x     x" +
                "   x   " +
                "  xxx  ");
    }

    @Test
    public void shouldPrintWithBreakWhenSizeIs8() {
        givenFl("        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        ", 1, WITH_BREAK);

        // when
        field.tick();

        // then
        assertE("   xx   " +
                "   xx   " +
                "        " +
                "xx    xx" +
                "xx    xx" +
                "        " +
                "   xx   " +
                "   xx   ");
    }



    @Test
    public void shouldBreakNotMove() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ", 1, WITH_BREAK);

        // when
        joystick.left();
        field.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                "     " +
                "  x  ");
    }

    @Test
    public void shouldNumberStopAtBreak() {
        givenFl("2   2" +
                "     " +
                "     " +
                "     " +
                "2   2", 1, WITH_BREAK);

        // when
        joystick.left();
        field.tick();

        // then
        assertE("2 x2 " +
                "     " +
                "x   x" +
                "     " +
                "2 x2 ");

        // when
        joystick.right();
        field.tick();

        // then
        assertE(" 2x 2" +
                "     " +
                "x   x" +
                "     " +
                " 2x 2");

        // when
        joystick.up();
        field.tick();

        // then
        assertE(" 4x 2" +
                "     " +
                "x   x" +
                "    2" +
                "  x  ");

        // when
        joystick.left();
        field.tick();

        // then
        assertE("4 x2 " +
                "     " +
                "x   x" +
                "2    " +
                "  x  ");
    }

    @Test
    public void shouldNotClearBreak() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ", 1, WITH_BREAK);

        // when
        game.newGame();
        field.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                "     " +
                "  x  ");
    }

    @Test
    public void shouldNewNumbersWithBreak() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ", 1, WITH_BREAK);

        // when
        dice(1, 1);
//        joystick.left(); // do nothing
        field.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                " 2   " +
                "  x  ");
    }

    @Test
    public void shouldBuf2244() {
        givenFl("22 44 " +
                " 2244 " +
                " 224 4" +
                "2 244 " +
                "22 4 4" +
                "2 24 4");

        joystick.left();
        field.tick();

        assertE("48    " +
                "48    " +
                "48    " +
                "48    " +
                "48    " +
                "48    ");
    }

    @Test
    public void shouldResetWhenAct0() {
        givenFl("    " +
                "2 2 " +
                " 22 " +
                " 22 ");

        dice(1,2, 3,3);
        joystick.act();
        field.tick();
        game.newGame();

        assertE("    " +
                "    " +
                "    " +
                "    ");
    }

    @Test
    public void shouldGameOverWhenCantGoWithBreaks() {
        givenFl("42  42" +
                "242424" +
                " 2424 " +
                " 4242 " +
                "424242" +
                "24  24", 1, WITH_BREAK);

        // when
        joystick.left(); // ignore
        field.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        assertE("42xx42" +
                "242424" +
                "x2424x" +
                "x4242x" +
                "424242" +
                "24xx24");
    }
}
