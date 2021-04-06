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

import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.GameAuthorities;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.SortedJSONArray;
import org.json.SortedJSONObject;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;

public abstract class AbstractRestControllerTest {

    public static GameServiceImpl gameService() {
        return new GameServiceImpl(){
            @Override
            public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                return Arrays.asList(FirstGameType.class, SecondGameType.class);
            }
        };
    }

    public static class PropertyOverrideContextInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext>
    {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            setup(context, "messages", "messages.db");
            setup(context, "log", "logs.db");
            setup(context, "payment", "payment.db");
            setup(context, "saves", "saves.db");
            setup(context, "users", "users.db");
            setup(context, "settings", "settings.db");

//            TestPropertySourceUtils.addPropertiesFilesToEnvironment(
//                    context, "context-override-application.properties");
        }

        public void setup(ConfigurableApplicationContext context, String db, String file) {
            String dbFile = "target/" + file + new Random().nextInt();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    context, "database.files." + db + "=" + dbFile);
        }
    }

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected PlayerService playerService;

    @Autowired
    protected Registration registration;

    @Autowired
    protected PlayerGames playerGames;

    @Autowired
    protected ConfigProperties config;

    @Autowired
    protected GameService games;

    @Autowired
    protected SemifinalService semifinalService;

    @Autowired
    protected DebugService debugService;

    @Autowired
    protected RoomService roomService;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        debugService.resume();
    }

    @After
    public void checkErrors() {
        SmartAssert.checkResult(getClass());
    }

    protected void asAdmin() {
        login(new UsernamePasswordAuthenticationToken(
                config.getAdminLogin(),
                Hash.md5(config.getAdminPassword()))
        );
    }

    protected void asUser(String playerId, String password) {
        Player player = playerService.get(playerId);
        if (player == NullPlayer.INSTANCE) {
            fail("Expected: Player with id = " + playerId +
                    " But was: NullPlayer");
        }

        Registration.User user = registration.getUserById(playerId).orElse(null);
        if (user == null) {
            fail("Expected: Registered user with id = " + playerId +
                    " But was: Registration not found");
        }


        login(new UsernamePasswordAuthenticationToken(
                user,
                Hash.md5(password)
        ));
    }

    private void login(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    protected void asNone() {
        login(null);
    }

    protected PlayerGame register(String id, String ip, String room, String game) {
        String password = Hash.md5(id);
        String readableName = id + "-name";
        registration.register(id, id, readableName, password, "", GameAuthorities.USER.roles());
        playerService.register(id, game, room, ip);
        PlayerGame playerGame = playerGames.get(id);
        if (playerGame != NullPlayerGame.INSTANCE) {
            resetMocks(playerGame);
        }
        return playerGame;
    }

    private void resetMocks(PlayerGame playerGame) {
        reset(playerGame.getField());
        reset(playerGame.getGame().getPlayer());
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
    protected String get(String uri) {
        return process(200, MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    protected String get(int status, String uri) {
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
    protected String post(int status, String uri, String data) {
        return process(status, MockMvcRequestBuilders.post(uri, data)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    @SneakyThrows
    protected String delete(String uri) {
        return delete(200, uri);
    }

    @SneakyThrows
    protected String delete(int status, String uri) {
        return process(status, MockMvcRequestBuilders.delete(uri));
    }

    protected String process(int status, MockHttpServletRequestBuilder post) throws Exception {
        MvcResult mvcResult = mvc.perform(post
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(status, mvcResult.getResponse().getStatus());
        return mvcResult.getResponse().getContentAsString();
    }

    protected void assertException(String expected, Runnable supplier) {
        try {
            supplier.run();
            fail("expected exception");
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

    protected void assertPostError(String message, String uri, String data) {
        String source = post(500, uri, data);
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
    }

    protected void assertDeleteError(String message, String uri) {
        String source = delete(500, uri);
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
    }

    protected void assertError(String message, String uri) {
        String source = get(500, uri);
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
    }

    protected JSONObject tryParseAsJson(String source) {
        try {
            return new JSONObject(source);
        } catch (JSONException e) {
            System.out.println("actual data is: " + source);
            return new JSONObject(){{
                put("message", "no json value");
            }};
        }
    }

    protected String quote(String input) {
        if (input.startsWith("[")) {
            return new SortedJSONArray(input)
                    .toString()
                    .replace('\"', '\'');
        }

        return new SortedJSONObject(input)
                .toString()
                .replace('\"', '\'');
    }

    protected String unquote(String input) {
        input = input.replace('\'', '\"');
        if (input.startsWith("[")) {
            return new SortedJSONArray(input)
                    .toString();
        }

        return new SortedJSONObject(input)
                .toString();
    }

    protected String quotes(String input) {
        return "\"" + input + "\"";
    }
}
