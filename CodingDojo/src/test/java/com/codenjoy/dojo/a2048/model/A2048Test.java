package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.services.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
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
    public void shouldMoveNumbersWhenUseJoystick() {
        givenFl(" 2  " +
                "    " +
                "  2 " +
                "    ");

        joystick.up();
        game.tick();

        assertE(" 22 " +
                "    " +
                "    " +
                "    ");
    }
}
