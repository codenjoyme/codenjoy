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
import org.mockito.stubbing.OngoingStubbing;

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
    private Player player;

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

        a2048 = new A2048(level, dice);
        game = new SingleA2048(a2048, listener);
        listener = mock(EventListener.class);
        player = new Player(listener);
        this.joystick = game.getJoystick();
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(new Printer(a2048.getSize(), new A2048Printer(a2048, player)), expected);
    }

    // есть поле
    @Test
    public void shouldFieldAtStart() {
        givenFl(" 2  " +
                "    " +
                "  2 " +
                "    ");

        game.tick();

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

        joystick.left();
        game.tick();

        assertE("82  " +
                "424 " +
                "4   " +
                "8   ");

        joystick.down();
        game.tick();

        assertE("    " +
                "8   " +
                "8   " +
                "844 ");

        joystick.right();
        game.tick();

        assertE("    " +
                "   8" +
                "   8" +
                "  88");

        joystick.up();
        game.tick();

        assertE("  8A" +
                "   8" +
                "    " +
                "    ");

        joystick.left();
        game.tick();

        assertE("8A  " +
                "8   " +
                "    " +
                "    ");

        joystick.down();
        game.tick();

        assertE("    " +
                "    " +
                "    " +
                "AA  ");

        joystick.right();
        game.tick();

        assertE("    " +
                "    " +
                "    " +
                "   B");
    }
}
