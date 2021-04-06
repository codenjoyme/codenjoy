package com.codenjoy.dojo.services.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SettingsReaderTest {

    @Test
    public void shouldConvertToJson() {
        // given
        SettingsReader settings = new SomeGameSettings();

        // when then
        assertEquals("{\n" +
                        "  'PARAMETER1':15,\n" +
                        "  'PARAMETER2':true,\n" +
                        "  'PARAMETER3':0.5,\n" +
                        "  'PARAMETER4':'string'\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldConvertToJson_caseRound() {
        // given
        SettingsReader settings = new SomeRoundSettings();

        // when then
        assertEquals("{\n" +
                        "  'PARAMETER1':15,\n" +
                        "  'PARAMETER2':true,\n" +
                        "  'PARAMETER3':0.5,\n" +
                        "  'PARAMETER4':'string',\n" +
                        "  'ROUNDS_ENABLED':true,\n" +
                        "  'ROUNDS_MIN_TICKS_FOR_WIN':1,\n" +
                        "  'ROUNDS_PER_MATCH':1,\n" +
                        "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                        "  'ROUNDS_TIME':200,\n" +
                        "  'ROUNDS_TIME_BEFORE_START':5,\n" +
                        "  'ROUNDS_TIME_FOR_WINNER':1\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldConvertToJson_caseSemifinal() {
        // given
        SettingsReader settings = new SomeSemifinalSettings();

        // when then
        assertEquals("{\n" +
                        "  'PARAMETER1':15,\n" +
                        "  'PARAMETER2':true,\n" +
                        "  'PARAMETER3':0.5,\n" +
                        "  'PARAMETER4':'string',\n" +
                        "  'SEMIFINAL_ENABLED':false,\n" +
                        "  'SEMIFINAL_LIMIT':50,\n" +
                        "  'SEMIFINAL_PERCENTAGE':true,\n" +
                        "  'SEMIFINAL_RESET_BOARD':true,\n" +
                        "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                        "  'SEMIFINAL_TIMEOUT':900\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldConvertToJson_caseSemifinalRound() {
        // given
        SettingsReader settings = new SomeSemifinalRoundSettings();

        // when then
        assertEquals("{\n" +
                        "  'PARAMETER1':15,\n" +
                        "  'PARAMETER2':true,\n" +
                        "  'PARAMETER3':0.5,\n" +
                        "  'PARAMETER4':'string',\n" +
                        "  'ROUNDS_ENABLED':true,\n" +
                        "  'ROUNDS_MIN_TICKS_FOR_WIN':1,\n" +
                        "  'ROUNDS_PER_MATCH':1,\n" +
                        "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                        "  'ROUNDS_TIME':200,\n" +
                        "  'ROUNDS_TIME_BEFORE_START':5,\n" +
                        "  'ROUNDS_TIME_FOR_WINNER':1,\n" +
                        "  'SEMIFINAL_ENABLED':false,\n" +
                        "  'SEMIFINAL_LIMIT':50,\n" +
                        "  'SEMIFINAL_PERCENTAGE':true,\n" +
                        "  'SEMIFINAL_RESET_BOARD':true,\n" +
                        "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                        "  'SEMIFINAL_TIMEOUT':900\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldParseFromJson_whenNameNotFound() {
        // given
        SettingsReader settings = new SomeGameSettings();

        // when
        try {
            settings.update(new JSONObject("{\n" +
                    "  'NON_EXISTS':1,\n" +
                    "}"));
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals("Parameter not found: NON_EXISTS", e.getMessage());
        }
    }

    @Test
    public void shouldParseFromJson() {
        // given
        SettingsReader settings = new SomeGameSettings();

        // when
        settings.update(new JSONObject("{\n" +
                "  'PARAMETER4':'updated',\n" +
                "  'PARAMETER2':false,\n" +
                "  'PARAMETER1':23,\n" +
                "  'PARAMETER3':0.1\n" +
                "}"));

        // then
        assertEquals("{\n" +
                        "  'PARAMETER1':23,\n" +
                        "  'PARAMETER2':false,\n" +
                        "  'PARAMETER3':0.1,\n" +
                        "  'PARAMETER4':'updated'\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldParseFromJson_caseRoundSettings() {
        // given
        SettingsReader settings = new SomeRoundSettings();

        // when
        settings.update(new JSONObject("{\n" +
                "  'PARAMETER4':'updated',\n" +
                "  'PARAMETER2':false,\n" +
                "  'PARAMETER1':23,\n" +
                "  'PARAMETER3':0.1,\n" +
                "  'ROUNDS_MIN_TICKS_FOR_WIN':4,\n" +
                "  'ROUNDS_TIME_FOR_WINNER':10\n" +
                "}"));

        // then
        assertEquals("{\n" +
                        "  'PARAMETER1':23,\n" +
                        "  'PARAMETER2':false,\n" +
                        "  'PARAMETER3':0.1,\n" +
                        "  'PARAMETER4':'updated',\n" +
                        "  'ROUNDS_ENABLED':true,\n" +
                        "  'ROUNDS_MIN_TICKS_FOR_WIN':4,\n" +
                        "  'ROUNDS_PER_MATCH':1,\n" +
                        "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                        "  'ROUNDS_TIME':200,\n" +
                        "  'ROUNDS_TIME_BEFORE_START':5,\n" +
                        "  'ROUNDS_TIME_FOR_WINNER':10\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldParseFromJson_caseSemifinalSettings() {
        // given
        SettingsReader settings = new SomeSemifinalSettings();

        // when
         settings.update(new JSONObject("{\n" +
                "  'PARAMETER4':'updated',\n" +
                "  'PARAMETER2':false,\n" +
                "  'PARAMETER1':23,\n" +
                "  'PARAMETER3':0.1,\n" +
                "  'SEMIFINAL_ENABLED':true,\n" +
                "  'SEMIFINAL_LIMIT':10\n" +
                "}"));

        // then
        assertEquals("{\n" +
                        "  'PARAMETER1':23,\n" +
                        "  'PARAMETER2':false,\n" +
                        "  'PARAMETER3':0.1,\n" +
                        "  'PARAMETER4':'updated',\n" +
                        "  'SEMIFINAL_ENABLED':true,\n" +
                        "  'SEMIFINAL_LIMIT':10,\n" +
                        "  'SEMIFINAL_PERCENTAGE':true,\n" +
                        "  'SEMIFINAL_RESET_BOARD':true,\n" +
                        "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                        "  'SEMIFINAL_TIMEOUT':900\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

    @Test
    public void shouldParseFromJson_caseSemifinalRoundSettings() {
        // given
        SettingsReader settings = new SomeSemifinalRoundSettings();

        // when
        settings.update(new JSONObject("{\n" +
                "  'PARAMETER4':'updated',\n" +
                "  'PARAMETER2':false,\n" +
                "  'PARAMETER1':23,\n" +
                "  'PARAMETER3':0.1,\n" +
                "  'ROUNDS_MIN_TICKS_FOR_WIN':4,\n" +
                "  'ROUNDS_TIME_FOR_WINNER':10,\n" +
                "  'SEMIFINAL_ENABLED':true,\n" +
                "  'SEMIFINAL_LIMIT':10\n" +
                "}"));

        // then
        assertEquals("{\n" +
                        "  'PARAMETER1':23,\n" +
                        "  'PARAMETER2':false,\n" +
                        "  'PARAMETER3':0.1,\n" +
                        "  'PARAMETER4':'updated',\n" +
                        "  'ROUNDS_ENABLED':true,\n" +
                        "  'ROUNDS_MIN_TICKS_FOR_WIN':4,\n" +
                        "  'ROUNDS_PER_MATCH':1,\n" +
                        "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                        "  'ROUNDS_TIME':200,\n" +
                        "  'ROUNDS_TIME_BEFORE_START':5,\n" +
                        "  'ROUNDS_TIME_FOR_WINNER':10,\n" +
                        "  'SEMIFINAL_ENABLED':true,\n" +
                        "  'SEMIFINAL_LIMIT':10,\n" +
                        "  'SEMIFINAL_PERCENTAGE':true,\n" +
                        "  'SEMIFINAL_RESET_BOARD':true,\n" +
                        "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                        "  'SEMIFINAL_TIMEOUT':900\n" +
                        "}",
                JsonUtils.prettyPrint(settings.asJson()));
    }

}
