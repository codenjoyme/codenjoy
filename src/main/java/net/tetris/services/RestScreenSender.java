package net.tetris.services;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tetris.web.controller.UpdateRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:14 PM
 */
@Component
public class RestScreenSender implements ScreenSender {
    private List<UpdateRequest> requests = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public RestScreenSender() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StdSerializer<Plot>(Plot.class) {
            @Override
            public void serialize(Plot value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
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
    public synchronized void scheduleUpdate(UpdateRequest updateRequest) {
        requests.add(updateRequest);
    }

    @Override
    public synchronized void sendUpdates(Map<Player, List<Plot>> playerScreens) {
        for (UpdateRequest updateRequest : requests) {
            Map<Player, List<Plot>> playersToUpdate = findScreensFor(updateRequest.getPlayersToUpdate(), playerScreens);
            if (playersToUpdate.isEmpty()) {
                updateRequest.getAsyncContext().complete();
                continue;
            }
            sendUpdateForRequest(playersToUpdate, updateRequest);
        }
        requests.clear();
    }

    private Map<Player, List<Plot>> findScreensFor(Set<String> playersToUpdate, Map<Player, List<Plot>> playerScreens) {
        HashMap<Player, List<Plot>> result = new HashMap<>();
        for (Map.Entry<Player, List<Plot>> entry : playerScreens.entrySet()) {
            if (playersToUpdate.contains(entry.getKey().getName())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }


    private void sendUpdateForRequest(Map<Player, List<Plot>> playerScreens, UpdateRequest updateRequest) {
        AsyncContext asyncContext = updateRequest.getAsyncContext();
        ServletResponse response = asyncContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            objectMapper.writeValue(writer, playerScreens);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            asyncContext.complete();
        }
    }
}
