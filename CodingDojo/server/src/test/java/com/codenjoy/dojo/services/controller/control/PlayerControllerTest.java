package com.codenjoy.dojo.services.controller.control;

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


import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.controller.AbstractControllerTest;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlayerControllerTest extends AbstractControllerTest<String, Joystick> {

    public static final String INITIAL_REQUEST = "any data";

    @Autowired
    private PlayerController playerController;

    @Before
    public void setup() {
        super.setup();

        createPlayer("player", "room", "first");
        login.asNone();
    }

    @Override
    protected String endpoint() {
        return "ws";
    }

    @Override
    protected Joystick control(String id) {
        return new DirectionActJoystick() {
            @Override
            public void down() {
                receivedOnServer.add("down");
            }

            @Override
            public void up() {
                receivedOnServer.add("up");
            }

            @Override
            public void left() {
                receivedOnServer.add("left");
            }

            @Override
            public void right() {
                receivedOnServer.add("right");
            }

            @Override
            public void act(int... p) {
                receivedOnServer.add("act" + Arrays.toString(p));
            }
        };
    }

    @Override
    protected Controller<String, Joystick> controller() {
        return playerController;
    }

    @Test
    public void shouldLeft() {
        // given
        client.willAnswer("LEFT").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[left]", receivedOnServer.toString());
    }

    @Test
    public void shouldRight() {
        // given
        client.willAnswer("right").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[right]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldUp() {
        // given
        client.willAnswer("Up").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[up]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldAct() {
        // given
        client.willAnswer("aCt").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[act[]]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldActWithParameters() {
        // given
        client.willAnswer("ACt(1,2 ,3, 5)").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[act[1, 2, 3, 5]]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldDown() {
        // given
        client.willAnswer("DowN").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[down]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldRightAct() {
        // given
        client.willAnswer("right,Act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[right, act[]]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldMixed() {
        // given
        client.willAnswer("Act,right, left ,act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[act[], right, left, act[]]", receivedOnServer.toString());
        clean();
    }

    @Test
    public void shouldCheckRequest() {
        // given
        client.willAnswer("act").start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("board=" + INITIAL_REQUEST, client.request());
    }

    @Test
    public void shouldServerGotOnlyOneWhenClientAnswerTwice() {
        // given
        client.willAnswer("LEFT").times(2).start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[board=" + INITIAL_REQUEST + "]", client.messages());
        assertEquals("[left]", receivedOnServer.toString());
    }

    @Test
    public void shouldClientGotOnlyOneWhenServerRequestTwice() {
        // given
        client.willAnswer("LEFT").times(1).onlyOnce().start();

        // when
        sendToClient(player(0), INITIAL_REQUEST);
        waitForClientsResponse();

        // then
        assertEquals("[board=" + INITIAL_REQUEST + "]", client.messages());
        assertEquals("[left]", receivedOnServer.toString());
    }
}
