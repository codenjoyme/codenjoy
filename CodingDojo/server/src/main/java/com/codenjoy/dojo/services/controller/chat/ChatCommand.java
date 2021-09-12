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
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    private Map<String, Consumer<ChatRequest>> map = new HashMap<>();

    public ChatCommand(ChatControl chat) {
        map.put(GET_ALL_ROOM,  request -> chat.getAllRoom(request.filter()));
        map.put(GET_ALL_TOPIC, request -> chat.getAllTopic(request.id(), request.filter()));
        map.put(GET_ALL_FIELD, request -> chat.getAllField(request.filter()));
        map.put(GET,           request -> chat.get(request.id(), request.room()));
        map.put(POST_ROOM,     request -> chat.postRoom(request.text(), request.room()));
        map.put(POST_FIELD,    request -> chat.postField(request.text(), request.room()));
        map.put(POST_TOPIC,    request -> chat.postTopic(request.id(), request.text(), request.room()));
        map.put(DELETE,        request -> chat.delete(request.id(), request.room()));
    }

    public void process(ChatRequest request) {
        map.get(request.method()).accept(request);
    }

}