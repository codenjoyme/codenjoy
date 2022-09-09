package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.config.ThreeGamesConfiguration;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static com.codenjoy.dojo.web.rest.RestSettingsController.GENERAL;
import static com.codenjoy.dojo.web.rest.RestSettingsController.SETTINGS;

@Import(ThreeGamesConfiguration.class)
public class RestSettingsControllerTest extends AbstractRestControllerTest {

    public static final String NO_ROOM_NAME = null;

    public static final String ACCESS_IS_DENIED =
            "org.springframework.security.access.AccessDeniedException: Access is denied";

    public static final String PLEASE_JOIN_GAME_TO_SEE_ROOM_SETTINGS =
            "{'UNREGISTERED USER':'Please join the game to check room settings'}";

    private Settings first;
    private Settings second;

    @Before
    public void setup() {
        with.login.asAdmin();
        super.setup();

        first = games.getGameType("first", "first").getSettings();
        second = games.getGameType("second", "second").getSettings();

        first.clear();
        second.clear();

        first.addCheckBox("one").type(Boolean.class).def(true);
        first.addEditBox("two").type(Integer.class).def(12);

        second.addSelect("three", Arrays.asList("option1", "option2", "option3")).type(String.class).def("option1");
        second.addEditBox("four").type(String.class).def("some-data");
    }

    @Test
    public void shouldGet_withoutAdminRole() {
        // setUp key
        assertEquals("{}", post(200, "/rest/settings/first/" + NO_ROOM_NAME + "/key", "value"));
        // when login as User
        with.login.asUser();

        // then try get Key
        assertEquals("value", get("/rest/settings/first/" + NO_ROOM_NAME + "/key"));
    }

    @Test
    public void shouldNotPost_withoutAdminRole() {
        // given no admin rights
        with.login.asUser();

        // when POST
        // then should get Access denied message
        assertPostError(ACCESS_IS_DENIED, "/rest/settings/first/" + NO_ROOM_NAME + "/key", "value");
    }

    @Test
    public void shouldGetSet_caseGameDataAsStorage() {
        // when
        assertEquals("{}", post(200, "/rest/settings/first/" + NO_ROOM_NAME + "/key", "value"));
        assertEquals("{}", post(200, "/rest/settings/second/" + NO_ROOM_NAME + "/key2", "value2"));

        // then
        assertEquals("value", get("/rest/settings/first/" + NO_ROOM_NAME + "/key"));
        assertEquals("value2", get("/rest/settings/second/" + NO_ROOM_NAME + "/key2"));

        // then do not touch any settings
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12'],'type':'editbox','value':'12','valueType':'Integer'}]}",
                quote(get("/rest/settings/first/" + NO_ROOM_NAME + "/" + SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option1','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                quote(get("/rest/settings/second/" + NO_ROOM_NAME + "/" + SETTINGS)));
    }

    @Test
    public void shouldGetSet_caseGameDataAsStorage_caseGeneral() {
        // when
        assertEquals("{}", post(200, "/rest/settings/" + GENERAL + "/" + NO_ROOM_NAME + "/key", "value"));
        assertEquals("{}", post(200, "/rest/settings/" + GENERAL + "/" + NO_ROOM_NAME + "/key2", "value2"));

        // then
        assertEquals("value", get("/rest/settings/" + GENERAL + "/" + NO_ROOM_NAME + "/key"));
        assertEquals("value2", get("/rest/settings/" + GENERAL + "/" + NO_ROOM_NAME + "/key2"));

        // then do not touch any settings
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12'],'type':'editbox','value':'12','valueType':'Integer'}]}",
                quote(get("/rest/settings/first/" + NO_ROOM_NAME + "/" + SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option1','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                quote(get("/rest/settings/second/" + NO_ROOM_NAME + "/" + SETTINGS)));
    }

    @Test
    public void shouldGetSet_caseSettingsAsStorage() {
        // when
        assertEquals("{}", post(200, "/rest/settings/first/" + NO_ROOM_NAME + "/two", "135"));
        assertEquals("{}", post(200, "/rest/settings/second/" + NO_ROOM_NAME + "/three", "option2"));

        // then
        assertEquals("true", get("/rest/settings/first/" + NO_ROOM_NAME + "/one"));
        assertEquals("135", get("/rest/settings/first/" + NO_ROOM_NAME + "/two"));
        assertEquals("option2", get("/rest/settings/second/" + NO_ROOM_NAME + "/three"));
        assertEquals("some-data", get("/rest/settings/second/" + NO_ROOM_NAME + "/four"));

        // then
        assertEquals("{'parameters':[" +
                        "{'def':'true','multiline':false,'name':'one','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'12','multiline':false,'name':'two','options':['12','135'],'type':'editbox','value':'135','valueType':'Integer'}]}",
                quote(get("/rest/settings/first/" + NO_ROOM_NAME  + "/" + SETTINGS)));

        assertEquals("{'parameters':[" +
                        "{'def':'option1','multiline':false,'name':'three','options':['option1','option2','option3'],'type':'selectbox','value':'option2','valueType':'String'}," +
                        "{'def':'some-data','multiline':false,'name':'four','options':['some-data'],'type':'editbox','value':'some-data','valueType':'String'}]}",
                quote(get("/rest/settings/second/" + NO_ROOM_NAME  + "/" + SETTINGS)));
    }

    @Test
    public void shouldSet_removeQuotesOnJson() {
        // when
        assertEquals("{}", post(200, "/rest/settings/first/" + NO_ROOM_NAME + "/one", quotes("true")));
        assertEquals("{}", post(200, "/rest/settings/first/" + NO_ROOM_NAME + "/two", quotes("135")));

        // then
        assertEquals("true", get("/rest/settings/first/" + NO_ROOM_NAME + "/one"));
        assertEquals("135", get("/rest/settings/first/" + NO_ROOM_NAME + "/two"));
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
        assertEquals("{}", post(200, "/rest/settings/second/" + NO_ROOM_NAME + "/four", quotes(input)));
        assertEquals(expected, get("/rest/settings/second/" + NO_ROOM_NAME + "/four"));
    }

    @Test
    public void shouldGetForPlayer() {
        // as admin (player not in game)
        // when then
        assertEquals(PLEASE_JOIN_GAME_TO_SEE_ROOM_SETTINGS,
                quote(get("/rest/settings/player")));

        // as user (player in game)
        // when then
        with.login.asUser();
        assertEquals("{'Parameter 1':'15','Parameter 2':'true'}",
                quote(get("/rest/settings/player")));

        // logout (player not in game)
        // when then
        with.login.asNone();
        assertEquals(PLEASE_JOIN_GAME_TO_SEE_ROOM_SETTINGS,
                quote(get("/rest/settings/player")));
    }
}