package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.codenjoy.dojo.web.rest.pojo.PGameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestAdminControllerTest.ContextConfiguration.class)
@WebAppConfiguration
public class RestAdminControllerTest {

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

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestAdminController service;

    @Autowired
    private DebugService debugService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerGamesView playerGamesView;

    @Autowired
    private PlayerGames playerGames;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");

        debugService.resume();

        playerService.removeAll();

        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("admin@codenjoyme.com", "admin"));
    }

    @After
    public void checkErrors() {
        SmartAssert.checkResult();
    }

    @SneakyThrows
    protected String mapToJson(Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @SneakyThrows
    protected <T> T mapFromJson(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, clazz);
    }

    @SneakyThrows
    private String get(String uri) {
        return process(200, MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    private String get(int status, String uri) {
        return process(status, MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    private String post(int status, String uri) {
        return process(status, MockMvcRequestBuilders.post(uri));
    }

    @SneakyThrows
    private String post(String uri) {
        return process(200, MockMvcRequestBuilders.post(uri));
    }

    @SneakyThrows
    private String post(int status, String uri, String data) {
        return process(status, MockMvcRequestBuilders.post(uri, data)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    private String process(int status, MockHttpServletRequestBuilder post) throws Exception {
        MvcResult mvcResult = mvc.perform(post
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(status, mvcResult.getResponse().getStatus());
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    public void shouldStartStopGame_oneRoom() {
        // given
        // TODO я думаю что сервис напрямую дергать не надо, т.к. его никто так вызывать не будет, только через rest
        assertEquals(true, service.getEnabled("name"));
        assertEquals("true", get("/rest/admin/room/name/pause"));

        // when
        service.setEnabled("name", false);

        // then
        assertEquals(false, service.getEnabled("name"));
        assertEquals("false", get("/rest/admin/room/name/pause"));

        // when
        assertEquals("", get("/rest/admin/room/name/pause/true"));

        // then
        assertEquals(true, service.getEnabled("name"));
        assertEquals("true", get("/rest/admin/room/name/pause"));
    }

    @Test
    public void shouldStartStopGame_severalRooms() {
        // given
        assertEquals(true, service.getEnabled("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));

        // when
        service.setEnabled("name1", false);

        // then
        assertEquals(false, service.getEnabled("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));

        // when
        assertEquals("", get("/rest/admin/room/name1/pause/true"));

        // then
        assertEquals(true, service.getEnabled("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));
    }

    @Test
    public void shouldStartStopGame_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.getEnabled("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/pause/true");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.setEnabled("$bad$", true));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/pause");
    }

    private void assertException(String expected, Runnable supplier) {
        try {
            supplier.run();
            fail("expected exception");
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

    private void assertError(String message, String uri) {
        String source = get(500, uri);
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
    }

    private JSONObject tryParseAsJson(String source) {
        try {
            return new JSONObject(source);
        } catch (JSONException e) {
            System.out.println("actual data is: " + source);
            return new JSONObject(){{
                put("message", "no json value");
            }};
        }
    }

    @Test
    public void shouldSetClearScores() {
        // given
        register("player1", "ip1", "room1", "first");
        register("player2", "ip2", "room1", "first");

        register("player3", "ip3", "room2", "first");

        register("player4", "ip4", "room3", "second");

        assertScores("{player1=0, player2=0, player3=0, player4=0}");

        // when
        service.setScores("room1", "player1", "10");
        service.setScores("room1", "player2", "20");
        assertEquals("", get("/rest/admin/room/room2/scores/player3/set/30"));
        assertEquals("", get("/rest/admin/room/room3/scores/player4/set/40"));

        // then
        assertScores("{player1=10, player2=20, player3=30, player4=40}");

        // when
        assertEquals("", get("/rest/admin/room/room1/scores/clear"));

        // then
        assertScores("{player1=0, player2=0, player3=30, player4=40}");

        // when
        service.cleanScores("room2");

        // then
        assertScores("{player1=0, player2=0, player3=0, player4=40}");
    }

    private void assertScores(String expected) {
        assertEquals(expected, playerGamesView.getScores().toString());
    }

    @Test
    public void shouldSetClearScores_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.cleanScores("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/scores/clear");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.setScores("$bad$", "id", "12"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/scores/player3/set/30");

        // when then
        assertException("Parameter score is empty: ''",
                () -> service.setScores("room", "id", ""));

        assertError("java.lang.IllegalArgumentException: Parameter score is empty: 'null'",
                "/rest/admin/room/room/scores/player3/set/null");

        // when then
        assertException("Player id is invalid: '$badPlayer$'",
                () -> service.setScores("room", "$badPlayer$", "10"));

        assertError("java.lang.IllegalArgumentException: Player id is invalid: '$badPlayer$'",
                "/rest/admin/room/room/scores/$badPlayer$/set/30");
    }

    private PlayerGame register(String id, String ip, String roomName, String gameName) {
        playerService.register(id, ip, roomName, gameName);
        PlayerGame playerGame = playerGames.get(id);
        resetMocks(playerGame);
        return playerGame;
    }

    private void resetMocks(PlayerGame playerGame) {
        reset(playerGame.getField());
        reset(playerGame.getGame().getPlayer());
    }
}
