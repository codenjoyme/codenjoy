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
    private PlayerGamesView playerGamesView;

    @Autowired
    private PlayerGames playerGames;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");

        debugService.resume();

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

}
