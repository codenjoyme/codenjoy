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
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.config.ThreeGamesConfiguration;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.helper.LoginHelper;
import com.codenjoy.dojo.stuff.SmartAssert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
@Import(ThreeGamesConfiguration.class)
public abstract class AbstractControllerTest<TData, TControl> {

    public static final int MAX = 20;

    public static final String URL = "%s://localhost:%s%s/%s";

    private List<WebSocketRunnerMock> clients = new LinkedList<>();
    private List<String> receivedOnServer = new LinkedList<>();
    private String serverAddress;
    private List<Deal> dealsList = new LinkedList<>();

    @Autowired
    private Registration registration;

    @Autowired
    private TimerService timer;

    @Autowired
    private Chat chat;

    @SpyBean
    protected Deals deals;

    @Autowired
    protected LoginHelper login;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Before
    protected void setup() {
        tearDown();

        login.removeAll();

        serverAddress = String.format(URL, "ws", port, contextPath, endpoint());
        String url = String.format(URL, "http", port, contextPath, "");
        log.info("Web application started at: " + url);
        timer.pause();
    }

    protected void replyToServerImmediately(boolean input) {
        clients.forEach(client -> client.replyToServerImmediately(input));
    }

    protected void serverReceived(String value) {
        receivedOnServer.add(value);
    }

    protected abstract String endpoint();

    protected abstract Controller<TData, TControl> controller();

    protected Deal createPlayer(String id, String room, String game) {
        Deal deal = login.register(id, id, room, game);
        dealsList.add(deal);
        Player player = deal.getPlayer();
        player.setCode(registration.getCodeById(id));

        WebSocketRunnerMock client = new WebSocketRunnerMock(serverAddress, player.getId(), player.getCode());
        clients.add(client);

        // remove all messages about a player joining the field
        removeFootprints();

        return deal;
    }

    private void removeFootprints() {
        waitForServerReceived(false);
        receivedOnServer.clear();
        chat.removeAll();
    }

    protected WebSocketRunnerMock client(int index) {
        return clients.get(index);
    }

    @After
    public void tearDown() {
        dealsList.forEach(deal ->
                controller().unregister(deal));
        clients.forEach(WebSocketRunnerMock::reset);
        clients.forEach(WebSocketRunnerMock::stop);
        clients.clear();
        receivedOnServer.clear();
        SmartAssert.checkResult(getClass());
    }

    protected void waitForServerReceived() {
        waitForServerReceived(true);
    }

    // TODO как-нибудь когда будет достаточно времени и желания позапускать этот тест и разгадать, почему зависает тут тест
    @SneakyThrows
    protected void waitForServerReceived(boolean shouldGetMessage) {
        int count = 0;
        while (count <= MAX && receivedOnServer.isEmpty()) {
            Thread.sleep(100);
            count++;
        }
        if (shouldGetMessage) {
            assertEquals("The server never received the message",
                    false, receivedOnServer.isEmpty());
        }
    }

    protected void waitForClientReceived(int index) {
        waitForClientReceived(index, true);
    }

    @SneakyThrows
    protected void waitForClientReceived(int index, boolean shouldGetMessage) {
        int count = 0;
        while (count <= MAX && client(index).isEmpty()) {
            Thread.sleep(100);
            count++;
        }
        if (shouldGetMessage) {
            assertEquals("The client never received the message",
                    false, client(index).isEmpty());
        }
    }

    @SneakyThrows
    protected void sendToClient(Player player, TData request) {
        Thread.sleep(300);
        controller().requestControl(player, request);
    }

    protected Player player(int index) {
        return dealsList.get(index).getPlayer();
    }

    protected String receivedOnServer() {
        String result = receivedOnServer.toString();
        receivedOnServer.clear();
        return result;
    }
}
