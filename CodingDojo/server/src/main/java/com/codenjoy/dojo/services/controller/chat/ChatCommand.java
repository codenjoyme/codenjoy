package com.codenjoy.dojo.services.controller.chat;

import com.codenjoy.dojo.services.chat.ChatControl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ChatCommand {

    private Map<String, Function<ChatRequest, String>> map = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    public ChatCommand(ChatControl chat) {
        map.put("getAllRoom",  request -> json(chat.getAllRoom(request.filter())));
        map.put("getAllTopic", request -> json(chat.getAllTopic(request.id(), request.filter())));
        map.put("getAllField", request -> json(chat.getAllField(request.filter())));
        map.put("get",         request -> json(chat.get(request.id(), request.room())));
        map.put("postRoom",    request -> json(chat.postRoom(request.text(), request.room())));
        map.put("postTopic",   request -> json(chat.postTopic(request.id(), request.text(), request.room())));
        map.put("postField",   request -> json(chat.postField(request.text(), request.room())));
        map.put("delete",      request -> json(chat.delete(request.id(), request.room())));
    }

    @SneakyThrows
    private String json(Object object) {
        return mapper.writeValueAsString(object);
    }

    public String process(ChatRequest request) {
        try {
            return map.get(request.method()).apply(request);
        } catch (Exception exception) {
            log.error("Error during chat request: " + request, exception);
            return json(new Error(exception));
        }
    }

    @Getter
    private static class Error {

        private final String error;
        private final String message;

        public Error(Exception exception) {
           this.error = exception.getClass().getSimpleName();
           this.message = exception.getMessage();
        }
    }

}
