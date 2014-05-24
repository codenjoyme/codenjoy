package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.a2048.services.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class A2048Test {

    private static final boolean WITH_BREAK = true;
    private static final boolean WITHOUT_BREAK = false;
    private A2048 a2048;
    private SingleA2048 game;
    private Joystick joystick;
    private Dice dice;
    private EventListener listener;
    private Level level;

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
        level = new LevelImpl(board);
        level.getSettings().getParameter("New numbers").type(Integer.class).update(newNumbers);
        level.getSettings().getParameter("Mode").type(Integer.class).update((mode)?1:0);

        a2048 = new A2048(level, dice);
        when(dice.next(anyInt())).thenReturn(-1); // ничего не генерим нового на поле с каждым тиком

        listener = mock(EventListener.class);
        game = new SingleA2048(a2048, listener);
        game.newGame();
        this.joystick = game.getJoystick();
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(new Printer(a2048.getSize(), new A2048Printer(a2048)), expected);
    }

    // есть поле
    @Test
    public void shouldFieldAtStart() {
        givenFl(" 2  " +
                "    " +
                "  2 " +
                "    ");

        // when
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

        // then
        assertEvens("[INC(32)]");
        assertE("82  " +
                "424 " +
                "4   " +
                "8   ");

        // when
        joystick.down();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
        assertE("    " +
                "8   " +
                "8   " +
                "844 ");

        // when
        joystick.right();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
        assertE("    " +
                "   8" +
                "   8" +
                "  88");

        // when
        joystick.up();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
        assertE("  8A" +
                "   8" +
                "    " +
                "    ");

        // when
        joystick.left();
        game.tick();

        // then
        assertE("8A  " +
                "8   " +
                "    " +
                "    ");

        // when
        joystick.down();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
        assertE("    " +
                "    " +
                "    " +
                "AA  ");

        // when
        joystick.right();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
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
                "    ", 3, false);

        // when
        dice(1,1, 2,3, 3,3);
        joystick.left();
        game.tick();

        // then
        assertEvens("[INC(6)]");
        assertE("  22" +
                "    " +
                " 2  " +
                "    ");

        // when
        dice(-1, -1);
        joystick.down();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
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
        game.tick();

        // then
        assertE("    " +
                "    " +
                "   4" +
                "    ");

        assertEvens("[INC(4)]");

        // when
        joystick.up();
        dice(1, 2);
        game.tick();

        // then
        assertE("   4" +
                " 2  " +
                "    " +
                "    ");

        assertEvens("[INC(2)]");
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
        game.tick();

        // then
        assertE("    " +
                " 2  " +
                "    " +
                "    ");

        // when
        joystick.up();

        dice(2, 2);
        game.tick();

        // then
        assertE(" 2  " +
                "  2 " +
                "    " +
                "    ");
    }

    @Test
    public void shouldNewNumbersWhenTick() {
        int newNumbers = 4;
        givenFl("    " +
                "    " +
                "    " +
                "    ", newNumbers, false);

        // when
        joystick.up();

        dice(0,1, 1,2, 2,2, 2,3);
        game.tick();

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
        game.tick();

        // then
        assertE("    " +
                " 2  " +
                "    " +
                "    ");

        // when
        dice(2, 2);    // только если юзер сделал какое-то действие
//        joystick.up();
        game.tick();

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
        game.tick();

        // then
        assertEvens("[INC(84)]");
        assertFalse(game.isGameOver());

        // when
        joystick.up();
        dice(1, 0);
        game.tick();

        // then
        assertEvens("[INC(2)]");
        verifyNoMoreInteractions(listener);

        assertFalse(game.isGameOver());

        assertE("4848" +
                "8282" +
                "4848" +
                "828 ");

        // when
        joystick.up();
        dice(3, 0);
        game.tick();

        // then
        assertEvens("[INC(2), GAME_OVER]");

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

        // then
        assertEvens("[INC(4194304), WIN]");

        assertTrue(game.isGameOver());

        givenFl("    " +
                "    " +
                "    " +
                "   S");

        // when
        game.tick();

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
        game.tick();

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
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

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
        game.tick();

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
        ArgumentCaptor captor = ArgumentCaptor.forClass(A2048Events.class);
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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
        game.tick();

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
    public void shouldPrintWithBreak() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ", 1, WITH_BREAK);

        // when
        game.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                "     " +
                "  x  ");
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
        game.tick();

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
        game.tick();

        // then
        assertE("2 x2 " +
                "     " +
                "x   x" +
                "     " +
                "2 x2 ");

        // when
        joystick.right();
        game.tick();

        // then
        assertE(" 2x 2" +
                "     " +
                "x   x" +
                "     " +
                " 2x 2");

        // when
        joystick.up();
        game.tick();

        // then
        assertE(" 4x 2" +
                "     " +
                "x   x" +
                "    2" +
                "  x  ");

        // when
        joystick.left();
        game.tick();

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
        game.tick();

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
        game.tick();

        // then
        assertE("  x  " +
                "     " +
                "x   x" +
                " 2   " +
                "  x  ");
    }
}
