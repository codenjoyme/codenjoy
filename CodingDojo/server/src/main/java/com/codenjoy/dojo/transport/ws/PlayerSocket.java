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


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class PlayerSocket {

    private Session session;
    private PlayerResponseHandler handler = WebSocketPlayerTransport.NULL_HANDLER;
    private boolean requested;

    public PlayerSocket() {
        requested = false;
    }

    @OnWebSocketMessage
    public void onWebSocketText(String message) {
        if (requested) {
            requested = false;
            handler.onResponseComplete(message, null);
        }
    }


    @OnWebSocketClose
    public void onWebSocketClose(int i, String s) {
        requested = false;
        if (session == null) {
            return;
        }
        session.close();
    }

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) throws IOException {
        if (session == null) {
            return;
        }
        if (!requested) {
            requested = true;
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        }
    }

    public void setHandler(PlayerResponseHandler handler) {
        this.handler = handler;
    }

}
