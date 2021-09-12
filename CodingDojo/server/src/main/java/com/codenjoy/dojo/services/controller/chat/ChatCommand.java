package com.codenjoy.dojo.services.controller.chat;

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

import com.codenjoy.dojo.services.chat.ChatControl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ChatCommand {

    public static final String ADD = "add";
    public static final String GET_ALL_ROOM = "getAllRoom";
    public static final String GET_ALL_TOPIC = "getAllTopic";
    public static final String GET_ALL_FIELD = "getAllField";
    public static final String GET = "get";
    public static final String POST_ROOM = "postRoom";
    public static final String POST_TOPIC = "postTopic";
    public static final String POST_FIELD = "postField";
    public static final String DELETE = "delete";
    public static final String ERROR = "error";
    public static final String NONE = null;

    private Map<String, Function<ChatRequest, String>> map = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    public ChatCommand(ChatControl chat) {
        map.put(GET_ALL_ROOM,  request -> answer(ADD,  chat.getAllRoom(request.filter())));
        map.put(GET_ALL_TOPIC, request -> answer(ADD,  chat.getAllTopic(request.id(), request.filter())));
        map.put(GET_ALL_FIELD, request -> answer(ADD,  chat.getAllField(request.filter())));
        map.put(GET,           request -> answer(ADD,  Arrays.asList(chat.get(request.id(), request.room()))));
        map.put(POST_ROOM,     request -> answer(NONE, chat.postRoom(request.text(), request.room())));
        map.put(POST_FIELD,    request -> answer(NONE, chat.postField(request.text(), request.room())));
        map.put(POST_TOPIC,    request -> answer(NONE, chat.postTopic(request.id(), request.text(), request.room())));
        map.put(DELETE,        request -> answer(NONE, chat.delete(request.id(), request.room())));
    }

    private String answer(String command, Object data) {
        if (command == null) {
            return null;
        }
        return String.format("{\"command\":\"%s\", \"data\":%s}",
                command, json(data));
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
            return answer(ERROR, new Error(exception));
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
