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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        givenFl(board, 1, 2);
    }

    private void givenFl(String board, int newNumbers, int scoreBase) {
        level = new LevelImpl(board);
        level.getSettings().getParameter("New numbers").type(Integer.class).update(newNumbers);
        level.getSettings().getParameter("Score base").type(Integer.class).update(scoreBase);

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
        assertEvens("[INC(5)]");
        assertE("82  " +
                "424 " +
                "4   " +
                "8   ");

        // when
        joystick.down();
        game.tick();

        // then
        assertEvens("[INC(3)]");
        assertE("    " +
                "8   " +
                "8   " +
                "844 ");

        // when
        joystick.right();
        game.tick();

        // then
        assertEvens("[INC(2)]");
        assertE("    " +
                "   8" +
                "   8" +
                "  88");

        // when
        joystick.up();
        game.tick();

        // then
        assertEvens("[INC(4)]");
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
        assertEvens("[INC(4)]");
        assertE("    " +
                "    " +
                "    " +
                "AA  ");

        // when
        joystick.right();
        game.tick();

        // then
        assertEvens("[INC(8)]");
        assertE("    " +
                "    " +
                "    " +
                "   B");
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

        assertEvens("[INC(1)]");
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
                "    ", newNumbers, 2);

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
        givenFl("4444" +
                "2222" +
                "4444" +
                "22  ");

        // when
        game.tick();

        // then
        assertFalse(game.isGameOver());

        // when
        joystick.up();
        dice(2, 0);
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertFalse(game.isGameOver());

        assertE("4444" +
                "2222" +
                "4444" +
                "222 ");

        // when
        joystick.up();
        dice(3, 0);
        game.tick();

        // then
        assertEvens("[GAME_OVER]");

        assertTrue(game.isGameOver());

        assertE("4444" +  // TODO а это ведь не реальный конец игры
                "2222" +
                "4444" +
                "2222");
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
        assertEvens("[INC(1048576), WIN]");

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
        assertE("4444" +
                "2222" +
                "4444" +
                "2222");

        // when
        joystick.left(); // ignore
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        assertE("4444" +
                "2222" +
                "4444" +
                "2222");
    }

    @Test
    public void shouldNewGameWhenGameOver() {
        shouldGameOverWhenNoSpace();
        assertE("4444" +
                "2222" +
                "4444" +
                "2222");

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

    @Test
    public void getScore() {
        int scoreBase = 3;
        givenFl(" ", 1, scoreBase);

        Map<Integer, Integer> map = new TreeMap<Integer, Integer>();

        for (Elements el : Elements.values()) {
            if (el == Elements.NONE) continue;

            map.put(el.number(), a2048.getScoreFor(el.number()));
        }
        assertEquals("{2=0, 4=1, 8=3, 16=9, 32=27, 64=81, 128=243, 256=729, " +
                "512=2187, 1024=6561, 2048=19683, 4096=59049, 8192=177147, " +
                "16384=531441, 32768=1594323, 65536=4782969, 131072=14348907, " +
                "262144=43046721, 524288=129140163, 1048576=387420489, " +
                "2097152=1162261467, 4194304=2147483647}", map.toString());
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
        for (int count = 1; count <= 10000; count ++) {
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

}
