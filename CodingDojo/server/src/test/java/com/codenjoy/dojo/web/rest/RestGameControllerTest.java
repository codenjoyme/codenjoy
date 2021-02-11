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
import static com.codenjoy.dojo.stuff.SmartAssert.*;
import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.codenjoy.dojo.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.After;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;

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
        CodenjoyContext.setContext("codenjoy-contest");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        return process(MockMvcRequestBuilders.get(uri));
    }

    @SneakyThrows
    private String post(String uri, String data) {
        return process(MockMvcRequestBuilders.post(uri, data)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    private String process(MockHttpServletRequestBuilder post) throws Exception {
        MvcResult mvcResult = mvc.perform(post
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        return mvcResult.getResponse().getContentAsString();
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
        String expected1 = "{\n" +
                "  'boardSize':23,\n" +
                "  'clientUrl':'/codenjoy-contest/resources/user/first-servers.zip',\n" +
                "  'helpUrl':'/codenjoy-contest/resources/help/first.html',\n" +
                "  'info':'GameType[first]',\n" +
                "  'multiplayerType':{\n" +
                "    'disposable':true,\n" +
                "    'levels':false,\n" +
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
                "      'def':'12',\n" +
                "      'multiline':false,\n" +
                "      'name':'Parameter 1',\n" +
                "      'options':[\n" +
                "        '12',\n" +
                "        '15'\n" +
                "      ],\n" +
                "      'type':'editbox',\n" +
                "      'value':'15',\n" +
                "      'valueType':'Integer'\n" +
                "    },\n" +
                "    {\n" +
                "      'def':'true',\n" +
                "      'multiline':false,\n" +
                "      'name':'Parameter 2',\n" +
                "      'options':[\n" +
                "        'true'\n" +
                "      ],\n" +
                "      'type':'checkbox',\n" +
                "      'value':'true',\n" +
                "      'valueType':'Boolean'\n" +
                "    }\n" +
                "  ],\n" +
                "  'sprites':{\n" +
                "    'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz',\n" +
                "    'names':[\n" +
                "      'none',\n" +
                "      'wall',\n" +
                "      'hero'\n" +
                "    ],\n" +
                "    'url':'/codenjoy-contest/resources/sprite/first/*.png',\n" +
                "    'values':[\n" +
                "      ' ',\n" +
                "      '☼',\n" +
                "      '☺'\n" +
                "    ]\n" +
                "  },\n" +
                "  'version':'version 1.11b',\n" +
                "  'wsUrl':'ws[s]://SERVER:PORT/codenjoy-contest/ws?user=PLAYER_ID&code=CODE'\n" +
                "}";

        assertEquals(expected1, JsonUtils.prettyPrint(service.type("first")));
        assertEquals(expected1, JsonUtils.prettyPrint(get("/rest/game/first/info")));

        String expected2 = "{\n" +
                "  'boardSize':56,\n" +
                "  'clientUrl':'/codenjoy-contest/resources/user/second-servers.zip',\n" +
                "  'helpUrl':'/codenjoy-contest/resources/help/second.html',\n" +
                "  'info':'GameType[second]',\n" +
                "  'multiplayerType':{\n" +
                "    'disposable':false,\n" +
                "    'levels':true,\n" +
                "    'levelsCount':10,\n" +
                "    'multiplayer':true,\n" +
                "    'multiple':false,\n" +
                "    'quadro':false,\n" +
                "    'roomSize':1,\n" +
                "    'single':false,\n" +
                "    'singleplayer':false,\n" +
                "    'team':false,\n" +
                "    'tournament':false,\n" +
                "    'training':true,\n" +
                "    'triple':false,\n" +
                "    'type':'training'\n" +
                "  },\n" +
                "  'parameters':[\n" +
                "    {\n" +
                "      'def':'43',\n" +
                "      'multiline':false,\n" +
                "      'name':'Parameter 3',\n" +
                "      'options':[\n" +
                "        '43'\n" +
                "      ],\n" +
                "      'type':'editbox',\n" +
                "      'value':'43',\n" +
                "      'valueType':'Integer'\n" +
                "    },\n" +
                "    {\n" +
                "      'def':'false',\n" +
                "      'multiline':false,\n" +
                "      'name':'Parameter 4',\n" +
                "      'options':[\n" +
                "        'false',\n" +
                "        'true'\n" +
                "      ],\n" +
                "      'type':'checkbox',\n" +
                "      'value':'true',\n" +
                "      'valueType':'Boolean'\n" +
                "    }\n" +
                "  ],\n" +
                "  'sprites':{\n" +
                "    'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz',\n" +
                "    'names':[\n" +
                "      'none',\n" +
                "      'red',\n" +
                "      'green',\n" +
                "      'blue'\n" +
                "    ],\n" +
                "    'url':'/codenjoy-contest/resources/sprite/second/*.png',\n" +
                "    'values':[\n" +
                "      ' ',\n" +
                "      'R',\n" +
                "      'G',\n" +
                "      'B'\n" +
                "    ]\n" +
                "  },\n" +
                "  'version':'version 12',\n" +
                "  'wsUrl':'ws[s]://SERVER:PORT/codenjoy-contest/ws?user=PLAYER_ID&code=CODE'\n" +
                "}";

        assertEquals(expected2, JsonUtils.prettyPrint(service.type("second")));
        assertEquals(expected2, JsonUtils.prettyPrint(get("/rest/game/second/info")));
        
        assertEquals(null, service.type("non-exists"));
        assertEquals("", get("/rest/game/non-exists/info"));
    }

    @Test
    public void shouldHelpUrl() {
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

    @Test
    public void shouldWsUrl() {
        String expected = "ws[s]://SERVER:PORT/codenjoy-contest/ws?user=PLAYER_ID&code=CODE";
        assertEquals(expected, service.ws());
        assertEquals(expected, get("/rest/game/ws/url"));
    }

    @Test
    public void shouldAllSprites() {
        assertEquals("{first=[none= , wall=☼, hero=☺], second=[none= , red=R, green=G, blue=B]}", 
                service.allSprites().toString());
        
        assertEquals("{\"first\":[\"none= \",\"wall=☼\",\"hero=☺\"],\"second\":[\"none= \",\"red=R\",\"green=G\",\"blue=B\"]}", 
                get("/rest/game/sprites"));
    }

    @Test
    public void shouldIsGraphic() {
        assertEquals(true, service.isGraphic("first"));
        assertEquals(true, service.isGraphic("second"));
        assertEquals(null, service.isGraphic("non-exists"));
        
        assertEquals("true", get("/rest/game/first/sprites/exists"));
        assertEquals("true", get("/rest/game/second/sprites/exists"));
        assertEquals("",     get("/rest/game/non-exists/sprites/exists"));
    }

    @Test
    public void shouldSpritesNames() {
        assertEquals("[none, wall, hero]", service.spritesNames("first").toString());
        assertEquals("[none, red, green, blue]", service.spritesNames("second").toString());
        assertEquals(null, service.spritesNames("non-exists"));

        assertEquals("[\"none\",\"wall\",\"hero\"]", 
                get("/rest/game/first/sprites/names"));
        assertEquals("[\"none\",\"red\",\"green\",\"blue\"]", 
                get("/rest/game/second/sprites/names"));
        assertEquals("", 
                get("/rest/game/non-exists/sprites/names"));
    }

    @Test
    public void shouldSpritesValues() {
        assertEquals("[ , ☼, ☺]", service.spritesValues("first").toString());
        assertEquals("[ , R, G, B]", service.spritesValues("second").toString());
        assertEquals(null, service.spritesValues("non-exists"));

        assertEquals("[\" \",\"☼\",\"☺\"]", get("/rest/game/first/sprites/values"));
        assertEquals("[\" \",\"R\",\"G\",\"B\"]", get("/rest/game/second/sprites/values"));
        assertEquals("", get("/rest/game/non-exists/sprites/values"));
    }

    @Test
    public void shouldSpritesUrl() {
        assertEquals("/codenjoy-contest/resources/sprite/first/*.png", service.spritesUrl("first"));
        assertEquals("/codenjoy-contest/resources/sprite/second/*.png", service.spritesUrl("second"));
        assertEquals(null, service.spritesUrl("non-exists"));

        assertEquals("/codenjoy-contest/resources/sprite/first/*.png", get("/rest/game/first/sprites/url"));
        assertEquals("/codenjoy-contest/resources/sprite/second/*.png", get("/rest/game/second/sprites/url"));
        assertEquals("", get("/rest/game/non-exists/sprites/url"));
    }

    @Test
    public void shouldSpritesAlphabet() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, service.spritesAlphabet());
        assertEquals(expected, get("/rest/game/sprites/alphabet"));
    }
}
