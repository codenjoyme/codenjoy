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
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestSettingsControllerTest.ContextConfiguration.class)
@WebAppConfiguration
public class RestSettingsControllerTest extends AbstractRestControllerTest {

    private Settings first;
    private Settings second;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService();
        }
    }

    @Autowired
    private RestSettingsController service;

    @Autowired
    private GameService gameService;

    @Before
    public void setUp() {
        super.setUp();

        first = gameService.getGame("first").getSettings();
        second = gameService.getGame("second").getSettings();

        first.clear();
        second.clear();

        first.addCheckBox("one").type(Boolean.class).def(true);
        first.addEditBox("two").type(Integer.class).def(12);

        second.addSelect("three", Arrays.asList("option1", "option2", "option3")).type(String.class).def("option1");
        second.addEditBox("four").type(String.class).def("some-data");
    }

    @Test
    public void shouldGetSet_caseGameDataAsStorage() {
        // when
        assertEquals("{}", service.set("first", "key", "value"));
        assertEquals("{}", post(200, "/rest/settings/second/key2", "value2"));

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

    @Test
    public void shouldGetSet_caseGameDataAsStorage_caseGeneral() {
        // when
        assertEquals("{}", service.set(RestSettingsController.GENERAL, "key", "value"));
        assertEquals("{}", post(200, "/rest/settings/" + RestSettingsController.GENERAL + "/key2", "value2"));

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
        assertEquals("{}", post(200, "/rest/settings/second/three", "option2"));

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
}
