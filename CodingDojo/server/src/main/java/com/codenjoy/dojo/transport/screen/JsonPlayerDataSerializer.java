package com.codenjoy.dojo.transport.screen;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.PlayerDataSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Component
public class JsonPlayerDataSerializer implements PlayerDataSerializer<Player, PlayerData> {
    private final ObjectMapper objectMapper;

    public JsonPlayerDataSerializer() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StdSerializer<Plot>(Plot.class) {
            @Override
            public void serialize(Plot value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeArrayFieldStart(value.getColor().toString());
                jgen.writeNumber(value.getX());
                jgen.writeNumber(value.getY());
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
        });
//        objectMapper.setVisibility(
//                VisibilityChecker.Std.defaultInstance()
//                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        module.addSerializer(new StdSerializer<JSONObject>(JSONObject.class) {
            @Override
            public void serialize(JSONObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeRawValue(value.toString());
            }
        });
        objectMapper.registerModule(module);
    }

    @Override
    public void writeValue(Writer writer, Map<Player, PlayerData> playerScreens) throws IOException {
        objectMapper.writeValue(writer, playerScreens);
    }
}
