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
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.helper.LoginHelper;
import com.codenjoy.dojo.services.log.DebugService;
import com.codenjoy.dojo.services.room.RoomService;
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
@WebAppConfiguration
public abstract class AbstractRestControllerTest {

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected PlayerService players;

    @Autowired
    protected Registration registration;

    @Autowired
    protected Deals deals;

    @Autowired
    protected DealsView dealsView;

    @Autowired
    protected ConfigProperties config;

    @Autowired
    protected SemifinalService semifinal;

    @Autowired
    protected DebugService debug;

    @Autowired
    protected RoomService rooms;

    @Autowired
    protected GameServiceImpl games;

    @Autowired
    protected SaveService saves;

    @Autowired
    protected TeamService teamService;

    @Autowired
    protected Chat chat;

    @Autowired
    protected FieldService fields;

    @Autowired
    protected LoginHelper login;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        debug.resume();

        login.removeAll();
        games.init(); // тут чистятся rooms и связанные с ними сеттинги
    }

    @After
    public void checkErrors() {
        SmartAssert.checkResult(getClass());
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

    protected void assertGetError(String message, String uri) {
        String source = get(500, uri);
        JSONObject error = tryParseAsJson(source);
        assertEquals(message, error.getString("message"));
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
