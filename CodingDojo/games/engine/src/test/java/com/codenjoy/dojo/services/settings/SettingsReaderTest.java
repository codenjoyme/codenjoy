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
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

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

}
