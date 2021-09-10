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
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.codenjoy.dojo.web.rest.AbstractRestControllerTest;
import com.codenjoy.dojo.web.rest.TestLogin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(AbstractControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
public abstract class AbstractControllerTest<TData, TControl> {

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return new GameServiceImpl(){
                @Override
                public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                    return Arrays.asList(FirstGameType.class, SecondGameType.class);
                }
            };
        }
    }

    public static final String URL = "%s://localhost:%s%s/%s";

    private List<WebSocketRunnerMock> clients = new LinkedList<>();
    private List<String> receivedOnServer = new LinkedList<>();
    private String serverAddress;
    private List<Player> playersList = new LinkedList<>();

    @Autowired
    private Registration registration;

    @Autowired
    private TimerService timer;

    @Autowired
    private PlayerService players;

    @Autowired
    private ConfigProperties config;

    @SpyBean
    protected Deals deals;

    protected TestLogin login;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Before
    protected void setup() {
        tearDown();
        login = new TestLogin(config, players, registration, deals);

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

    protected void createPlayer(String id, String room, String game) {
        Deal deal = login.register(id, id, room, game);
        Player player = deal.getPlayer();
        player.setCode(registration.getCodeById(id));
        playersList.add(player);

        WebSocketRunnerMock client = new WebSocketRunnerMock(serverAddress, player.getId(), player.getCode());
        clients.add(client);
    }

    protected WebSocketRunnerMock client(int index) {
        return clients.get(index);
    }

    @After
    public void tearDown() {
        playersList.forEach(player ->
                controller().unregisterPlayerTransport(player));
        clients.forEach(WebSocketRunnerMock::reset);
        clients.forEach(WebSocketRunnerMock::stop);
        clients.clear();
        receivedOnServer.clear();
        SmartAssert.checkResult(getClass());
    }

    // TODO как-нибудь когда будет достаточно времени и желания позапускать этот тест и разгадать, почему зависает тут тест
    @SneakyThrows
    protected void waitForServerReceived() {
        int count = 0;
        while (count < 20 && receivedOnServer.isEmpty()) {
            Thread.sleep(300);
            count++;
        }
    }

    @SneakyThrows
    protected void waitForClientReceived(int index) {
        int count = 0;
        while (count < 20 && StringUtils.isEmpty(client(index).messages())) {
            Thread.sleep(300);
            count++;
        }
    }

    @SneakyThrows
    protected void sendToClient(Player player, TData request) {
        Thread.sleep(300);
        controller().requestControl(player, request);
    }

    protected Player player(int index) {
        return playersList.get(index);
    }

    protected String receivedOnServer() {
        return receivedOnServer.toString();
    }
}
