package com.codenjoy.dojo.transport.screen;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:42 PM
 */
public class FakePlayerDataSerializer implements PlayerDataSerializer<FakePlayer, FakePlayerData> {
    private final ObjectMapper objectMapper;

    public FakePlayerDataSerializer() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StdSerializer<TestPlot>(TestPlot.class) {
            @Override
            public void serialize(TestPlot value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeArrayFieldStart(value.getColor().getName());
                jgen.writeNumber(value.getX());
                jgen.writeNumber(value.getY());
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
        });
        objectMapper.registerModule(module);

    }

    @Override
    public void writeValue(Writer writer, Map<FakePlayer, FakePlayerData> playerScreens) throws IOException {
        objectMapper.writeValue(writer, playerScreens);
    }
}
