package net.tetris.online.web.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tetris.online.service.Score;
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
        module.addSerializer(new StdSerializer<GameLogData>(GameLogData.class) {
            @Override
            public void serialize(GameLogData gameLog, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeArrayFieldStart("aaData");
                List<String> fileNames = gameLog.getRows();
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

        module.addSerializer(new StdSerializer<Score>(Score.class) {
            @Override
            public void serialize(Score score, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartArray();
                jgen.writeString(score.getPlayerName());
                jgen.writeString(score.getPlayerName());
                jgen.writeNumber(score.getScore());
                jgen.writeString(score.getTimestamp());
                jgen.writeEndArray();
            }
        });
        objectMapper.registerModule(module);
        return this;
    }

    public <T> void  serialize(Writer w, T value) throws IOException {
        objectMapper.writeValue(w, value);
    }
}
