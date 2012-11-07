package net.tetris.online.web.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component("jsonSerializer")
public class JacksonSerializer implements JsonSerializer{
    private ObjectMapper objectMapper;

    @PostConstruct
    public JacksonSerializer init() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StdSerializer<GameLogsData>(GameLogsData.class) {
            @Override
            public void serialize(GameLogsData gameLog, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeArrayFieldStart("aaData");
                List<String> fileNames = gameLog.getFileNames();
                if (fileNames != null && !fileNames.isEmpty()) {
                    for (String fileName : fileNames) {
                        jgen.writeStartArray();
                        jgen.writeObject(fileName);
                        jgen.writeObject(fileName);
                        jgen.writeEndArray();
                    }
                }
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
        });
        objectMapper.registerModule(module);
        return this;
    }

    public <T> void  serialize(Writer w, T value) throws IOException {
        objectMapper.writeValue(w, value);
    }
}
