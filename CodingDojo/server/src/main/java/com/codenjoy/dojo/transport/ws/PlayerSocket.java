package com.codenjoy.dojo.transport.ws;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.transport.PlayerResponseHandler;
import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

public class PlayerSocket implements WebSocket.OnTextMessage {

    private Connection connection;
    private String authId;
    private WebSocketPlayerTransport transport;
    private PlayerResponseHandler handler = WebSocketPlayerTransport.NULL_HANDLER;
    private boolean requested;

    public PlayerSocket(String authId, WebSocketPlayerTransport transport) {
        this.authId = authId;
        this.transport = transport;
        requested = false;
    }

    @Override
    public void onMessage(String message) {
        if (requested) {
            requested = false;
            handler.onResponseComplete(message, null);
        }
    }

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
        requested = false;
    }

    @Override
    public void onClose(int i, String s) {
        requested = false;
        if (authId == null) {
            return;
        }
        transport.unregisterPlayerSocket(authId);
    }

    public void sendMessage(String message) throws IOException {
        if (connection == null) {
            return;
        }
        if (!requested) {
            requested = true;
            connection.sendMessage(message);
        }
    }

    public void close() {
        requested = false;
        if (connection == null) {
            return;
        }
        connection.close();
    }

    public void setHandler(PlayerResponseHandler handler) {
        this.handler = handler;
    }

}
