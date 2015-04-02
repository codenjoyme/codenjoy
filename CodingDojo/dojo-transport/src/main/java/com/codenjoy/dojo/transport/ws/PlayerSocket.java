package com.codenjoy.dojo.transport.ws;

import com.codenjoy.dojo.transport.PlayerResponseHandler;
import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 10:05 PM
 */
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
