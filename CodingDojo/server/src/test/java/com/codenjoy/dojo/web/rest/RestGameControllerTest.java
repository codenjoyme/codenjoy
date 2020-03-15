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
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestGameControllerTest.ContextConfiguration.class)
@WebAppConfiguration
public class RestGameControllerTest {
    
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
    private RestGameController service;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();    
    }

    protected String mapToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected <T> T mapFromJson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String get(String uri) {
        try {
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            assertEquals(200, mvcResult.getResponse().getStatus());
            return mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldExists() {
        assertEquals(true, service.exists("first"));
        assertEquals("true", get("/rest/game/first/exists"));
        
        assertEquals(true, service.exists("second"));
        assertEquals("true", get("/rest/game/second/exists"));

        assertEquals(false, service.exists("non-exists"));
        assertEquals("false", get("/rest/game/non-exists/exists"));
    }

    @Test
    public void shouldInfo() {
        String expected = "{\n" +
                "  'boardSize':23,\n" +
                "  'clientUrl':'//resources/user/first-servers.zip',\n" +
                "  'helpUrl':'//resources/help/first.html',\n" +
                "  'info':'GameType[first]',\n" +
                "  'multiplayerType':{\n" +
                "    'disposable':true,\n" +
                "    'levelsCount':1,\n" +
                "    'multiplayer':false,\n" +
                "    'multiple':false,\n" +
                "    'quadro':false,\n" +
                "    'roomSize':1,\n" +
                "    'single':true,\n" +
                "    'singleplayer':true,\n" +
                "    'team':false,\n" +
                "    'tournament':false,\n" +
                "    'training':false,\n" +
                "    'triple':false,\n" +
                "    'type':'single'\n" +
                "  },\n" +
                "  'parameters':[\n" +
                "    {\n" +
                "      'name':'Parameter 1',\n" +
                "      'options':[\n" +
                "        12,\n" +
                "        15\n" +
                "      ],\n" +
                "      'type':'editbox',\n" +
                "      'value':15\n" +
                "    },\n" +
                "    {\n" +
                "      'name':'Parameter 2',\n" +
                "      'options':[\n" +
                "        true\n" +
                "      ],\n" +
                "      'type':'checkbox',\n" +
                "      'value':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'sprites':{\n" +
                "    'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz',\n" +
                "    'names':[\n" +
                "      'none',\n" +
                "      'wall',\n" +
                "      'hero'\n" +
                "    ],\n" +
                "    'url':'//resources/sprite/first/*.png',\n" +
                "    'values':[\n" +
                "      ' ',\n" +
                "      '☼',\n" +
                "      '☺'\n" +
                "    ]\n" +
                "  },\n" +
                "  'version':'version 1.11b',\n" +
                "  'wsUrl':'ws[s]://SERVER:PORT//ws?user=PLAYER_ID&code=CODE'\n" +
                "}";
        
        assertEquals(expected, JsonUtils.prettyPrint(service.type("first")));
        assertEquals(expected, JsonUtils.prettyPrint(get("/rest/game/first/info")));
    }

    @Test
    public void shouldHelpUrl() {
        CodenjoyContext.setContext("codenjoy-contest");
        
        String expected1 = "/codenjoy-contest/resources/help/first.html";
        assertEquals(expected1, service.help("first"));
        assertEquals(expected1, get("/rest/game/first/help/url"));

        String expected2 = "/codenjoy-contest/resources/help/second.html";
        assertEquals(expected2, service.help("second"));
        assertEquals(expected2, get("/rest/game/second/help/url"));

        assertEquals(null, service.help("non-exists"));
        assertEquals("", get("/rest/game/non-exists/help/url"));
    }

    @Test
    public void shouldClientUrl() {
        String expected1 = "/codenjoy-contest/resources/user/first-servers.zip";
        assertEquals(expected1, service.client("first"));
        assertEquals(expected1, get("/rest/game/first/client/url"));

        String expected2 = "/codenjoy-contest/resources/user/second-servers.zip";
        assertEquals(expected2, service.client("second"));
        assertEquals(expected2, get("/rest/game/second/client/url"));

        assertEquals(null, service.client("non-exists"));
        assertEquals("", get("/rest/game/non-exists/client/url"));
    }
}
