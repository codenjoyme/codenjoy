package com.codenjoy.dojo.services.controller.control;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.controller.AbstractControllerTest;
import com.codenjoy.dojo.services.controller.Controller;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class PlayerControllerTest extends AbstractControllerTest<String, Joystick> {

    public static final String INITIAL_REQUEST = "any data";

    @Autowired
    private PlayerController controller;

    @Before
    public void setup() {
        super.setup();

        overwriteDeal();

        createPlayer("player", "room", "first");
        replyToServerImmediately(true);

        with.login.asNone();
    }

    @Override
    protected String endpoint() {
        return "ws";
    }


    // We wrap the Deal in spy to affect the return of the joystick,
    // to wrap it in a decorator and then eavesdrop on the values.
    // Oh Byte!
    private void overwriteDeal() {
        doAnswer(inv -> overwriteJoystick((Deal) inv.callRealMethod()))
                .when(deals).create(any(Player.class), anyString(), any(Game.class));
    }

    private Deal overwriteJoystick(Deal deal) {
        Deal spy = spy(deal);
        doAnswer(inv -> listenJoystick((LazyJoystick) inv.callRealMethod()))
                .when(spy).getJoystick();
        return spy;
    }

    public LazyJoystick listenJoystick(LazyJoystick real) {
        return new LazyJoystick(null) {
            @Override
            public void down() {
                serverReceived("down");
                real.down();
            }

            @Override
            public void up() {
                serverReceived("up");
                real.up();
            }

            @Override
            public void left() {
                serverReceived("left");
                real.left();
            }

            @Override
            public void right() {
                serverReceived("right");
                real.right();
            }

            @Override
            public void act(int... p) {
                serverReceived("act" + Arrays.toString(p));
                real.act(p);
            }

            @Override
            public synchronized void tick() {
                real.tick();
            }

            @Override
            public void message(String message) {
                serverReceived("message(" + message + ")");
                real.message(message);
            }

            @Override
            public synchronized String popLastCommands() {
                return real.popLastCommands();
            }
        };
    }

    @Override
    protected Controller<String, Joystick> controller() {
        return controller;
    }

    @Test
    public void shouldLeft() {
        // given
        client(0).willAnswer("LEFT").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[left]", receivedOnServer());
    }

    @Test
    public void shouldRight() {
        // given
        client(0).willAnswer("right").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[right]", receivedOnServer());
    }

    @Test
    public void shouldUp() {
        // given
        client(0).willAnswer("Up").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[up]", receivedOnServer());
    }

    @Test
    public void shouldAct() {
        // given
        client(0).willAnswer("aCt").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[act[]]", receivedOnServer());
    }

    @Test
    public void shouldActWithParameters() {
        // given
        client(0).willAnswer("ACt(1,2 ,3, 5)").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[act[1, 2, 3, 5]]", receivedOnServer());
    }

    @Test
    public void shouldDown() {
        // given
        client(0).willAnswer("DowN").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[down]", receivedOnServer());
    }

    @Test
    public void shouldRightAct() {
        // given
        client(0).willAnswer("right,Act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[right, act[]]", receivedOnServer());
    }

    @Test
    public void shouldMixed() {
        // given
        client(0).willAnswer("Act,right, left ,act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[act[], right, left, act[]]", receivedOnServer());
    }

    @Test
    public void shouldCheckRequest() {
        // given
        client(0).willAnswer("act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("board=" + INITIAL_REQUEST, client(0).request());
    }

    @Test
    public void shouldServerGotOnlyOneWhenClientAnswerTwice() {
        // given
        client(0).willAnswer("LEFT").times(2).start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[board=" + INITIAL_REQUEST + "]", client(0).messages());
        assertEquals("[left]", receivedOnServer());
    }

    @Test
    public void shouldClientGotOnlyOneWhenServerRequestTwice() {
        // given
        client(0).willAnswer("LEFT").times(1).onlyOnce().start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForServerReceived();

        // then
        assertEquals("[board=" + INITIAL_REQUEST + "]", client(0).messages());
        assertEquals("[left]", receivedOnServer());
    }
}
