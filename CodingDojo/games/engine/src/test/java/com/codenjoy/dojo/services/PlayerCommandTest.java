package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class PlayerCommandTest {

    private Joystick joystick;

    @Before
    public void setup() {
        joystick = mock(Joystick.class);
    }

    @Test
    public void shouldLeftCommand() {
        execute("left");

        verify(joystick, only()).left();
    }

    private void execute(String command) {
        new PlayerCommand(joystick, command).execute();
    }

    @Test
    public void shouldRightCommand() {
        execute("RIGHT");

        verify(joystick, only()).right();
    }

    @Test
    public void shouldUpCommand() {
        execute("Up");

        verify(joystick, only()).up();
    }

    @Test
    public void shouldDownCommand() {
        execute("dOwn");

        verify(joystick, only()).down();
    }

    @Test
    public void shouldActCommand() {
        execute("act");

        verify(joystick, only()).act();
    }

    @Test
    public void shouldActPlusDirectionCommand() {
        execute("act, left");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act();
        inOrder.verify(joystick).left();
    }

    @Test
    public void shouldDirectionPlusActCommand() {
        execute("right,act");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).act();
    }

    @Test
    public void shouldDirectionsPlusActCommand() {
        execute("right,left,up,up, down  ,act,down");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick, times(2)).up();
        inOrder.verify(joystick).down();
        inOrder.verify(joystick).act();
        inOrder.verify(joystick).down();
    }

    @Test
    public void shouldActWithNegativeParameters() {
        execute("act(2, -5),act");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act(2, -5);
        inOrder.verify(joystick).act();
    }

    @Test
    public void shouldActWithParametersCommand() {
        execute("act(2, 5),down,act(1),up,act(1,2,   3,4, 5),act");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act(2, 5);
        inOrder.verify(joystick).down();
        inOrder.verify(joystick).act(1);
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).act(1,2,3,4,5);
        inOrder.verify(joystick).act();
    }

}
