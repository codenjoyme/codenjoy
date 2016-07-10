package net.tetris.services;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * User: serhiy.zelenin
 * Date: 10/28/12
 * Time: 10:08 AM
 */
public class PlayerCommandTest {

    private MockAdvancedTetrisJoystik joystick;
    private Player player;

    @Before
    public void setUp() {
        joystick = new MockAdvancedTetrisJoystik();
        player = new Player("testPlayer", "test", null, null, null);
    }

    @Test
    public void shouldMoveJoystick2() throws IOException, InterruptedException {
        executeCommand("DrOP/?.,,ROTATE=2,LeFt=1");

        assertEquals("drop,rotate=2,left=1", joystick.toString());
    }

    @Test
    public void shouldMoveJoystickNegative() throws IOException, InterruptedException {
        executeCommand("rotate=-1,left=-2,right=-3");

        assertEquals("rotate=-1,left=-2,right=-3", joystick.toString());
    }

    private void executeCommand(String commandString) {
        new PlayerCommand(joystick, commandString, player).execute();
    }

}
