package net.tetris.services;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tetris.web.controller.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RestScreenSender.class);

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
        logger.debug("Scheduling update {}", updateRequest);
        requests.add(updateRequest);
    }

    //TODO: ADD thread pooled sending
    @Override
    public synchronized void sendUpdates(Map<Player, List<Plot>> playerScreens) {
        logger.debug("Start sending screens {}", playerScreens);
        for (UpdateRequest updateRequest : requests) {
            Map<Player, List<Plot>> playersToUpdate = findScreensFor(updateRequest, playerScreens);
            if (playersToUpdate.isEmpty()) {
                updateRequest.getAsyncContext().complete();
                continue;
            }
            sendUpdateForRequest(playersToUpdate, updateRequest);
        }
        requests.clear();
        logger.debug("Player screens sent", playerScreens);
    }

    private Map<Player, List<Plot>> findScreensFor(UpdateRequest updateRequest, Map<Player, List<Plot>> playerScreens) {
        HashMap<Player, List<Plot>> result = new HashMap<>();
        for (Map.Entry<Player, List<Plot>> entry : playerScreens.entrySet()) {
            if (updateRequest.isForAllPlayers() ||
                    updateRequest.getPlayersToUpdate().contains(entry.getKey().getName())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }


    private void sendUpdateForRequest(Map<Player, List<Plot>> playerScreens, UpdateRequest updateRequest) {
        logger.debug("Sending updates to players: {}", playerScreens);
        AsyncContext asyncContext = updateRequest.getAsyncContext();
        ServletResponse response = asyncContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            objectMapper.writeValue(writer, playerScreens);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncContext.complete();
        }
        logger.debug("Updates to players sent");
    }
}
