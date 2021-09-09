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
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerTest;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.WebSocketRunnerMock;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.nullobj.NullInformation;
import com.codenjoy.dojo.services.nullobj.NullPlayerScores;
import lombok.SneakyThrows;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
public abstract class AbstractControllerTest {

    public static final String URL = "%s://localhost:%s%s";

    protected static WebSocketRunnerMock client;
    protected static List<String> serverMessages = new LinkedList<>();

    private String url;
    private String serverAddress;
    protected List<Player> players = new LinkedList<>();

    @MockBean
    private Registration registration;

    @Autowired
    private TimerService timer;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    protected void setupJetty() {
        serverAddress = String.format(URL, "ws", port, contextPath + "/ws");
        url = String.format(URL, "http", port, contextPath);
        System.out.println("web application started at: " + url);
        timer.pause();
    }

    protected void clean() {
        players.forEach(player ->
                controller().unregisterPlayerTransport(player));

        if (client != null) {
            client.reset();
        }
        serverMessages.clear();
    }

    protected abstract Controller controller();

    protected void createPlayer(String id) {
        clean();

        Player player = new Player(id, "127.0.0.1", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);
        player.setCode(Hash.getCode(id, id));
        players.add(player);

        controller().registerPlayerTransport(player, control());

        // SecureAuthenticationService спросит Registration а можно ли этому юзеру что-то делать?
        when(registration.checkUser(player.getId(), player.getCode())).thenReturn(player.getId());

        client = new WebSocketRunnerMock(serverAddress, player.getId(), player.getCode());
    }

    protected abstract Object control();

    @AfterClass
    public static void tearDown() {
        client.stop();
    }

    protected void waitForResponse(Player player) {
        waitForResponse(player, 1);
    }

    // TODO как-нибудь когда будет достаточно времени и желания позапускать этот тест и разгадать, почему зависает тут тест
    @SneakyThrows
    protected void waitForResponse(Player player, int times) {
        Thread.sleep(300);
        for (int index = 0; index < times; index++) {
            controller().requestControl(player, "some-request-" + index);
        }
        int count = 0;
        while (count < 20 && serverMessages.isEmpty()) {
            Thread.sleep(300);
            count++;
        }
    }

    protected Player player(int index) {
        return players.get(index);
    }
}
