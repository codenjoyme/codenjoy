package com.codenjoy.dojo.transport.ws;

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


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

@WebSocket
public class PlayerSocket {

    public static final boolean CLIENT_SEND_FIRST = true;
    public static final boolean SERVER_SEND_FIRST = !CLIENT_SEND_FIRST;

    private ResponseHandler handler = NullResponseHandler.NULL;
    private Session session;
    private String id;
    private boolean requested;
    private Runnable onClose;

    public PlayerSocket(String id, boolean requested) {
        this.id = id;
        this.requested = requested;
    }

    @OnWebSocketMessage
    public void onWebSocketText(String message) {
        if (requested) {
            requested = false;
            handler.onResponse(this, message);
        }
    }

    @OnWebSocketClose
    public void onWebSocketClose(int statusCode, String reason) {
        requested = false;
        handler.onClose(this, statusCode, reason);
        if (session != null) {
            session.close();
        }
        if (onClose != null) {
            onClose.run();
        }
    }

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        this.session = session;
        handler.onConnect(this, session);
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable cause) {
        handler.onError(this, cause);
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

    public void setHandler(ResponseHandler handler) {
        this.handler = handler;
    }

    public boolean isOpen() {
        return session != null && session.isOpen();
    }

    Session getSession() {
        return session;
    }

    public String getId() {
        return id;
    }

    public void onClose(Runnable onClose) {
        this.onClose = onClose;
    }
}
