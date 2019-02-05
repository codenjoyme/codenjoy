package com.codenjoy.dojo.services.controller;

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


import com.codenjoy.dojo.integration.mocker.SpringMockerJettyRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.nullobj.NullInformation;
import com.codenjoy.dojo.services.nullobj.NullPlayerScores;
import org.junit.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

// TODO разобратсья почему не рабоатет весь тест
public class PlayerControllerTest {

    public static final int PORT = 8082;
    public static final String CONTEXT_PATH = "/appcontext";
    private static WebSocketRunnerMock client;

    private static String url;

    private static PlayerController controller;
    private static TimerService timer;
    private static Registration registration;

    private static SpringMockerJettyRunner runner;

    private static Joystick joystick;
    private static Player player;

    private static final String SERVER = "ws://127.0.0.1:" + PORT + CONTEXT_PATH + "/ws";
    private static String USER_NAME = "apofig@gmail.com";
    private static String CODE = Hash.getCode("apofig@gmail.com", "secureSoul");

    private static List<String> serverMessages = new LinkedList<>();

    @BeforeClass
    public static void setupJetty() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", CONTEXT_PATH){{
            mockBean("registration");
        }};

        int port = runner.start(PORT);

        url = runner.getUrl();
        System.out.println("web application started at: " + url);

        timer = runner.getBean(TimerService.class, "timerService");
        controller = runner.getBean(PlayerController.class, "playerController");
        registration = runner.getBean(Registration.class, "registration");

        timer.pause();

        joystick = new DirectionActJoystick() {
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

        player = new Player(USER_NAME, "127.0.0.1", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);

        controller.registerPlayerTransport(player, joystick);

        // SecureAuthenticationService спросит Registration а можно ли этому юзеру что-то делать?
        when(registration.checkUser(USER_NAME, CODE)).thenReturn(USER_NAME);

        client = WebSocketRunnerMock.run(SERVER, USER_NAME, CODE);
    }

    @Before
    public void clean() {
        client.reset();
        serverMessages.clear();
    }

    @AfterClass
    public static void tearDown() {
        client.stop();
    }

    @Test
    public void shouldLeft() {
        client.willAnswer("LEFT");
        waitForPlayerResponse();

        assertEquals("[left]", serverMessages.toString());
    }

    @Test
    public void shouldRight() {
        client.willAnswer("right");
        waitForPlayerResponse();

        assertEquals("[right]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldUp() {
        client.willAnswer("Up");
        waitForPlayerResponse();

        assertEquals("[up]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldAct() {
        client.willAnswer("aCt");
        waitForPlayerResponse();

        assertEquals("[act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldActWithParameters() {
        client.willAnswer("ACt(1,2 ,3, 5)");
        waitForPlayerResponse();

        assertEquals("[act[1, 2, 3, 5]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldDown() {
        client.willAnswer("DowN");
        waitForPlayerResponse();

        assertEquals("[down]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldRightAct() {
        client.willAnswer("right,Act");
        waitForPlayerResponse();

        assertEquals("[right, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldMixed() {
        client.willAnswer("Act,right, left ,act");
        waitForPlayerResponse();

        assertEquals("[act[], right, left, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldCheckRequest() {
        client.willAnswer("act");
        waitForPlayerResponse();

        assertEquals("board=some-request-0", client.getRequest());
    }

    private void waitForPlayerResponse() {
        waitForPlayerResponse(1);
    }

    @Test
    public void shouldServerGotOnlyOneWhenClientAnswerTwice() {
        // given, when
        client.willAnswer("LEFT").times(2);
        waitForPlayerResponse();

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }

    private void waitForPlayerResponse(int times) {
        try {
            for (int index = 0; index < times; index++) {
                controller.requestControl(player, "some-request-" + index);
            }
            int count = 0;
            while (++count < 100 && serverMessages.isEmpty()) {
                Thread.sleep(300);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test @Ignore 
    public void shouldClientGotOnlyOneWhenServerRequestTwice() {
        // given, when
        client.willAnswer("LEFT").times(1).onlyOnce();
        waitForPlayerResponse(2);

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }
}
