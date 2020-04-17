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


import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.nullobj.NullInformation;
import com.codenjoy.dojo.services.nullobj.NullPlayerScores;
import lombok.SneakyThrows;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
public class PlayerControllerTest {
    private static String USER_NAME = "apofig@gmail.com";
    private static String CODE = Hash.getCode("apofig@gmail.com", "secureSoul");

    private static WebSocketRunnerMock client;

    private String url;

    private Joystick joystick;
    private Player player;

    private String serverAddress;

    private static List<String> serverMessages = new LinkedList<>();

    @MockBean
    private Registration registration;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private TimerService timer;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Before
    public void setupJetty() throws Exception {
        url = String.format("http://localhost:%s%s", port, contextPath);

        serverAddress = String.format("ws://localhost:%s%s", port, contextPath + "/ws");

        System.out.println("web application started at: " + url);

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

        createPlayer();
    }

    public void clean() {
        if (player != null) {
            playerController.unregisterPlayerTransport(player);
        }
        if (client != null) {
            client.reset();
        }
        serverMessages.clear();
    }

    public void createPlayer() throws Exception {
        clean();

        player = new Player(USER_NAME, "127.0.0.1", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);

        playerController.registerPlayerTransport(player, joystick);

        // SecureAuthenticationService спросит Registration а можно ли этому юзеру что-то делать?
        when(registration.checkUser(USER_NAME, CODE)).thenReturn(USER_NAME);

        client = new WebSocketRunnerMock(serverAddress, USER_NAME, CODE);
    }

    @AfterClass
    public static void tearDown() {
        client.stop();
    }

    @Test
    public void shouldLeft() {
        client.willAnswer("LEFT").start();
        waitForPlayerResponse();

        assertEquals("[left]", serverMessages.toString());
    }

    @Test
    public void shouldRight() {
        client.willAnswer("right").start();
        waitForPlayerResponse();

        assertEquals("[right]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldUp() {
        client.willAnswer("Up").start();
        waitForPlayerResponse();

        assertEquals("[up]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldAct() {
        client.willAnswer("aCt").start();
        waitForPlayerResponse();

        assertEquals("[act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldActWithParameters() {
        client.willAnswer("ACt(1,2 ,3, 5)").start();
        waitForPlayerResponse();

        assertEquals("[act[1, 2, 3, 5]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldDown() {
        client.willAnswer("DowN").start();
        waitForPlayerResponse();

        assertEquals("[down]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldRightAct() {
        client.willAnswer("right,Act").start();
        waitForPlayerResponse();

        assertEquals("[right, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldMixed() {
        client.willAnswer("Act,right, left ,act").start();
        waitForPlayerResponse();

        assertEquals("[act[], right, left, act[]]", serverMessages.toString());
        clean();
    }

    @Test
    public void shouldCheckRequest() {
        client.willAnswer("act").start();
        waitForPlayerResponse();

        assertEquals("board=some-request-0", client.getRequest());
    }

    private void waitForPlayerResponse() {
        waitForPlayerResponse(1);
    }

    @Test
    public void shouldServerGotOnlyOneWhenClientAnswerTwice() {
        // given, when
        client.willAnswer("LEFT").times(2).start();
        waitForPlayerResponse();

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }

    // TODO как-нибудь когда будет достаточно времени и желания позапускать этот тест и разгадать, почему зависает тут тест
    @SneakyThrows
    private void waitForPlayerResponse(int times) {
        Thread.sleep(300);
        for (int index = 0; index < times; index++) {
            playerController.requestControl(player, "some-request-" + index);
        }
        int count = 0;
        while (count < 20 && serverMessages.isEmpty()) {
            Thread.sleep(300);
            count++;
        }
    }

    @Test
    public void shouldClientGotOnlyOneWhenServerRequestTwice() {
        // given, when
        client.willAnswer("LEFT").times(1).onlyOnce().start();
        waitForPlayerResponse(2);

        // then
        assertEquals("[board=some-request-0]", client.messages.toString());
        assertEquals("[left]", serverMessages.toString());
    }
}
