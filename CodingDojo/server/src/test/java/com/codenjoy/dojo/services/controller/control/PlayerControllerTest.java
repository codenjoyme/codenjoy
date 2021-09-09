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


import com.codenjoy.dojo.services.controller.AbstractControllerTest;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.controller.control.PlayerController;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlayerControllerTest extends AbstractControllerTest {

    @Autowired
    private PlayerController playerController;

    @Before
    public void setup() {
        setupJetty();
        createPlayer("user");
    }

    @Override
    protected Object control() {
        return new DirectionActJoystick() {
            @Override
            public void down() {
                serverMessages.add("down");
            }

            @Override
            public void up() {
                serverMessages.add("up");
            }

            @Override
            public void left() {
                serverMessages.add("left");
            }

            @Override
            public void right() {
                serverMessages.add("right");
            }

            @Override
            public void act(int... p) {
                serverMessages.add("act" + Arrays.toString(p));
            }
        };
    }

    @Override
    protected Controller controller() {
        return playerController;
    }

    @Test
    public void shouldLeft() {
        client.willAnswer("LEFT").start();
        waitForResponse(player(0));

        assertEquals("[left]", serverMessages.toString());
    }

    @Test
    public void shouldRight() {
        client.willAnswer("right").start();
        waitForResponse(player(0));

        assertEquals("[right]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldUp() {
        client.willAnswer("Up").start();
        waitForResponse(player(0));

        assertEquals("[up]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldAct() {
        client.willAnswer("aCt").start();
        waitForResponse(player(0));

        assertEquals("[act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldActWithParameters() {
        client.willAnswer("ACt(1,2 ,3, 5)").start();
        waitForResponse(player(0));

        assertEquals("[act[1, 2, 3, 5]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldDown() {
        client.willAnswer("DowN").start();
        waitForResponse(player(0));

        assertEquals("[down]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldRightAct() {
        client.willAnswer("right,Act").start();
        waitForResponse(player(0));

        assertEquals("[right, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldMixed() {
        client.willAnswer("Act,right, left ,act").start();
        waitForResponse(player(0));

        assertEquals("[act[], right, left, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldCheckRequest() {
        client.willAnswer("act").start();
        waitForResponse(player(0));

        assertEquals("board=some-request-0", client.getRequest());
    }
    
    @Test
    public void shouldServerGotOnlyOneWhenClientAnswerTwice() {
        // given, when
        client.willAnswer("LEFT").times(2).start();
        waitForResponse(player(0));

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }

    @Test
    public void shouldClientGotOnlyOneWhenServerRequestTwice() {
        // given, when
        client.willAnswer("LEFT").times(1).onlyOnce().start();
        waitForResponse(player(0), 2);

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }
}
