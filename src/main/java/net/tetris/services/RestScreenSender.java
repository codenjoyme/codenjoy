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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:14 PM
 */
@Component
public class RestScreenSender implements ScreenSender {
    private List<UpdateRequest> requests = new ArrayList<>();
    private final ObjectMapper objectMapper;


    private ScheduledExecutorService restSenderExecutorService;

    private static Logger logger = LoggerFactory.getLogger(RestScreenSender.class);

    @Autowired
    public RestScreenSender(ScheduledExecutorService restSenderExecutorService) {
        this.restSenderExecutorService = restSenderExecutorService;
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
    public synchronized void sendUpdates(final Map<Player, List<Plot>> playerScreens, int score) {
        List<Callable<Void>> tasks = new ArrayList<>();

        for (final UpdateRequest updateRequest : requests) {
            tasks.add(new PlayerScreenSendCallable(updateRequest, playerScreens, score));
        }

        try {
            restSenderExecutorService.invokeAll(tasks, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for all glass data to be sent on client.", e);
        }
        requests.clear();
    }



    private void sendUpdateForRequest(Map<Player, PlayerData> playerScreens, UpdateRequest updateRequest) {
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
    }

    private class PlayerScreenSendCallable implements Callable<Void> {
        private final UpdateRequest updateRequest;
        private final Map<Player, List<Plot>> playerScreens;
        private int score;

        public PlayerScreenSendCallable(UpdateRequest updateRequest, Map<Player, List<Plot>> playerScreens, int score) {
            this.updateRequest = updateRequest;
            this.playerScreens = playerScreens;
            this.score = score;
        }

        @Override
        public Void call() {
            Map<Player, PlayerData> playersToUpdate = findScreensFor(updateRequest, playerScreens);
            if (playersToUpdate.isEmpty()) {
                updateRequest.getAsyncContext().complete();
                return null;
            }
            sendUpdateForRequest(playersToUpdate, updateRequest);
            return null;
        }

        private Map<Player, PlayerData> findScreensFor(UpdateRequest updateRequest, Map<Player, List<Plot>> playerScreens) {
                HashMap<Player, PlayerData> result = new HashMap<>();
                for (Map.Entry<Player, List<Plot>> entry : playerScreens.entrySet()) {
                    if (updateRequest.isForAllPlayers() ||
                            updateRequest.getPlayersToUpdate().contains(entry.getKey().getName())) {
                        result.put(entry.getKey(), new PlayerData(entry.getValue(), score));
                    }
                }
                return result;
            }

    }

    private class PlayerData {
        private PlayerData(List<Plot> plots, int score) {
            this.plots = plots;
            this.score = score;
        }

        private List<Plot> plots;
        private int score;

        public List<Plot> getPlots() {
            return plots;
        }

        public int getScore() {
            return score;
        }
    }
}
