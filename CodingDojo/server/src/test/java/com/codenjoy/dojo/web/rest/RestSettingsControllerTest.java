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
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.SortedJSONObject;
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

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestSettingsControllerTest.ContextConfiguration.class)
@WebAppConfiguration
public class RestSettingsControllerTest {

    private Settings first;
    private Settings second;

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
    private RestSettingsController service;

    @Autowired
    private GameService gameService;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        first = gameService.getGame("first").getSettings();
        second = gameService.getGame("second").getSettings();

        first.clear();
        second.clear();

        first.addCheckBox("one").type(Boolean.class).def(true);
        first.addEditBox("two").type(Integer.class).def(12);

        second.addSelect("three", Arrays.asList("option1", "option2", "option3")).type(String.class).def("option1");
        second.addEditBox("four").type(String.class).def("some-data");

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
    public void shouldGetSet_caseGameDataAsStorage() {
        // when
        assertEquals("{}", service.set("first", "key", "value"));
        assertEquals("{}", post("/rest/settings/second/key2", "value2"));

        // then
        assertEquals("value", get("/rest/settings/first/key"));
        assertEquals("value2", service.get("second", "key2"));

        // then do not touch any settings
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12'],'type':'editbox','value':'12','valueType':'Integer'}]}",
                fix(get("/rest/settings/first/" + RestSettingsController.SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option1','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                fix(service.get("second", RestSettingsController.SETTINGS)));
    }

    private String fix(String input) {
        return new SortedJSONObject(input)
                .toString()
                .replace('\"', '\'');
    }

    @Test
    public void shouldGetSet_caseGameDataAsStorage_caseGeneral() {
        // when
        assertEquals("{}", service.set(RestSettingsController.GENERAL, "key", "value"));
        assertEquals("{}", post("/rest/settings/" + RestSettingsController.GENERAL + "/key2", "value2"));

        // then
        assertEquals("value", get("/rest/settings/" + RestSettingsController.GENERAL + "/key"));
        assertEquals("value2", service.get(RestSettingsController.GENERAL, "key2"));

        // then do not touch any settings
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12'],'type':'editbox','value':'12','valueType':'Integer'}]}",
                fix(get("/rest/settings/first/" + RestSettingsController.SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option1','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                fix(service.get("second", RestSettingsController.SETTINGS)));
    }

    @Test
    public void shouldGetSet_caseSettingsAsStorage() {
        // when
        assertEquals("{}", service.set("first", "two", "135"));
        assertEquals("{}", post("/rest/settings/second/three", "option2"));

        // then
        assertEquals("true", get("/rest/settings/first/one"));
        assertEquals("135", get("/rest/settings/first/two"));
        assertEquals("option2", service.get("second", "three"));
        assertEquals("some-data", service.get("second", "four"));

        // then
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12','135'],'type':'editbox','value':'135','valueType':'Integer'}]}",
                fix(get("/rest/settings/first/" + RestSettingsController.SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option2','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                fix(service.get("second", RestSettingsController.SETTINGS)));
    }

    @Test
    public void shouldSet_removeQuotesOnJson() {
        // when
        assertEquals("{}", service.set("first", "one", quotes("true")));
        assertEquals("{}", service.set("first", "two", quotes("135")));

        // then
        assertEquals("true", service.get("first", "one"));
        assertEquals("135", service.get("first", "two"));
    }

    @Test
    public void shouldSet_replaceNOnJson() {
        assertReplaceN("updated\ndata", "updated\\ndata");
        assertReplaceN("updated\ndata", "updated\\rdata");
        assertReplaceN("updated\ndata", "updated\\r\\ndata");
        assertReplaceN("updated\ndata", "updated\\n\\rdata");
        assertReplaceN("updated\ndata", "updated\n\\rdata");
        assertReplaceN("updated\ndata", "updated\\r\ndata");
        assertReplaceN("updated\ndata", "updated\n\rdata");
        assertReplaceN("updated\ndata", "updated\ndata");
        assertReplaceN("updated\ndata", "updated\rdata");
    }

    private void assertReplaceN(String expected, String input) {
        assertEquals("{}", service.set("second", "four", quotes(input)));
        assertEquals(expected, service.get("second", "four"));
    }

    private String quotes(String input) {
        return "\"" + input + "\"";
    }

}
