package com.codenjoy.dojo.services;

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


import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.assertEquals;
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
        assertEquals(true, PlayerCommand.isValid(command));
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
    public void shouldActWithoutParameters() {
        execute("act()");

        InOrder inOrder = inOrder(joystick);
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

    @Test
    public void shouldMessageWithoutParametersCommand() {
        execute("message");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("");
    }

    @Test
    public void shouldMessageWithParametersCommand() {
        execute("message('hello world')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("hello world");
    }

    @Test
    public void shouldMessageWithNRInParametersCommand() {
        execute("message('hel\nlo w\nor\rld')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("hel\nlo w\nor\rld");
    }

    @Test
    public void shouldNotTrimMessageCommand() {
        execute("message('hello,    world')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("hello,    world");
    }

    @Test
    public void shouldLeaveCharsCommand() {
        assertMessageIsValid("ASDFGHJKL:\"ZXCVBNM<>?1234567890-=`~!@#$%^&*()_+ ");
        assertMessageIsValid("qwertyuiopasdfghjklzxcvbnm,./;'[]QWERTYUIOP{}");
    }

    private void assertMessageIsValid(String expected) {
        execute("message('" + expected + "')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message(expected);
    }

    @Test
    public void shouldParseWholeMessageCommand() {
        execute("message('('hello')('world')')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("('hello')('world')");
    }

    @Test
    public void shouldParseWholeMessageCommand2() {
        execute("message('(\"hello\")(\"world\")')");

        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).message("(\"hello\")(\"world\")");
    }
}
