package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.services.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import javax.lang.model.util.Types;

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
        LevelImpl level = new LevelImpl(board);

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
        assertEvens("[INC(8), INC(4), INC(8)]");
        assertE("82  " +
                "424 " +
                "4   " +
                "8   ");

        // when
        joystick.down();
        game.tick();

        // then
        assertEvens("[INC(8), INC(4)]");
        assertE("    " +
                "8   " +
                "8   " +
                "844 ");

        // when
        joystick.right();
        game.tick();

        // then
        assertEvens("[INC(8)]");
        assertE("    " +
                "   8" +
                "   8" +
                "  88");

        // when
        joystick.up();
        game.tick();

        // then
        assertEvens("[INC(16)]");
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
        assertEvens("[INC(16)]");
        assertE("    " +
                "    " +
                "    " +
                "AA  ");

        // when
        joystick.right();
        game.tick();

        // then
        assertEvens("[INC(32)]");
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

        assertEvens("[INC(4)]");
    }

    @Test
    public void shouldNewRandomNumberWhenTick() {
        givenFl("    " +
                "    " +
                "    " +
                "    ");

        // when
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
    public void shouldGameOverWhenNoSpace() {
        givenFl("2222" +
                "2222" +
                "2222" +
                "22  ");

        // when
        game.tick();

        // then
        assertFalse(game.isGameOver());

        // when
        dice(2, 0);
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertFalse(game.isGameOver());

        assertE("2222" +
                "2222" +
                "2222" +
                "222 ");

        // when
        dice(3, 0);
        game.tick();

        // then
        assertEvens("[GAME_OVER]");

        assertTrue(game.isGameOver());

        assertE("2222" +  // TODO а это ведь не реальный конец игры
                "2222" +
                "2222" +
                "2222");
    }

    @Test
    public void shouldGameOverWhen2048() {
        givenFl("    " +
                "    " +
                "   F" +
                "   F");
        assertFalse(game.isGameOver());

        // when
        joystick.down();
        game.tick();

        // then
        assertEvens("[INC(2048), WIN]");

        assertTrue(game.isGameOver());

        givenFl("    " +
                "    " +
                "    " +
                "   G");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldDoNothingWhenGameOver() {
        shouldGameOverWhenNoSpace();

        // when
        joystick.up();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        assertE("2222" +
                "2222" +
                "2222" +
                "2222");
    }

    private void assertEvens(String expected) {
        ArgumentCaptor captor = ArgumentCaptor.forClass(A2048Events.class);
        int count = expected.split(",").length;
        verify(listener, times(count)).event(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
        reset(listener);
    }

}
