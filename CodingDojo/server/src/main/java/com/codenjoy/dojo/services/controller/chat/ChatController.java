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
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.springframework.stereotype.Component;

@Component
public class ChatController implements Controller<String, ChatControl> {

    private final PlayerTransport transport;

    // autowiring by name
    public ChatController(PlayerTransport chatPlayerTransport) {
        transport = chatPlayerTransport;
        transport.setDefaultFilter(Object::toString);
    }

    @Override
    public void requestControlToAll(String json) {
        transport.sendStateToAll(json);
    }

    @Override
    public boolean requestControl(Player player, String json) {
        return transport.sendState(player.getId(), json);
    }

    @Override
    public void registerPlayerTransport(Player player, ChatControl chatControl) {
        transport.registerPlayerEndpoint(player.getId(),
                new ChatResponseHandler(player, chatControl, transport));
    }

    @Override
    public void unregisterPlayerTransport(Player player) {
        transport.unregisterPlayerEndpoint(player.getId());
    }
}