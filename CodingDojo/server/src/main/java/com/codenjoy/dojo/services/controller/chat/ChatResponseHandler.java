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


import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.transport.ws.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

@Slf4j
public class ChatResponseHandler implements ResponseHandler {

    private final Player player;
    private final ChatControl chat;
    private final ChatCommand command;
    private final PlayerTransport transport;

    public ChatResponseHandler(Player player, ChatControl chat, PlayerTransport transport) {
        this.player = player;
        this.chat = chat;
        this.transport = transport;
        command = new ChatCommand(chat);
    }

    @Override
    public void onResponse(PlayerSocket socket, String message) {
        log.debug("Received response: {} from player: {}",
                message, player.getId());

        ChatRequest request = new ChatRequest(message);
        String result = command.process(request);
        transport.sendState(player.getId(), result);
    }

    @Override
    public void onClose(PlayerSocket socket, int statusCode, String reason) {
        log.debug("Websocket closed: {} from player: {} status code: {} reason: {}",
                player.getId(), statusCode, reason);
    }

    @Override
    public void onError(PlayerSocket socket, Throwable error) {
        log.error("Request error: player: {}, error: {}",
                player.getId(), error);
    }

    @Override
    public void onConnect(PlayerSocket socket, Session session) {
        log.debug("Connected: player: {}, session: {}",
                player.getId(), session);
    }
}