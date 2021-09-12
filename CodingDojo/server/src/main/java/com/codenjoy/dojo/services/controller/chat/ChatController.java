package com.codenjoy.dojo.services.controller.chat;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.codenjoy.dojo.services.controller.chat.ChatCommand.ADD;
import static com.codenjoy.dojo.services.controller.chat.ChatCommand.DELETE;

@Component
public class ChatController implements Controller<String, ChatControl> {

    private final PlayerTransport transport;
    private final ChatService chatService;
    private ObjectMapper mapper;

    // autowiring by name
    public ChatController(PlayerTransport chatPlayerTransport, ChatService chatService) {
        transport = chatPlayerTransport;
        this.chatService = chatService;
        transport.setDefaultFilter(Object::toString);
        mapper = new ObjectMapper();
    }

    @Override
    public void register(Deal deal) {
        String id = deal.getPlayerId();
        ChatControl control = chatService.control(id, chatListener());
        transport.registerPlayerEndpoint(id,
                new ChatResponseHandler(deal.getPlayer(), control, transport));
    }

    @SneakyThrows
    private String json(Object object) {
        return mapper.writeValueAsString(object);
    }

    private ChatControl.OnChange chatListener() {
        return new ChatControl.OnChange() {
            @Override
            public void deleted(PMessage message, String playerId) {
                sendState(DELETE, Arrays.asList(message), playerId);
            }

            @Override
            public void created(PMessage message, String playerId) {
                sendState(ADD, Arrays.asList(message), playerId);
            }

            private void sendState(String command, Object data, String playerId) {
                if (data == null) {
                    return;
                }

                transport.sendState(playerId,
                        String.format("{\"command\":\"%s\", \"data\":%s}",
                                command, json(data)));
            }
        };
    }

    @Override
    public void unregister(Deal deal) {
        transport.unregisterPlayerEndpoint(deal.getPlayerId());
    }
}