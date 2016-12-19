package com.codenjoy.dojo.services;

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
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSocketRunner {

    private Session session;
    private WebSocketClient wsClient;
    private String answer;
    private static boolean started;
    private String request;
    private static boolean closed;
    private int times;
    private boolean onlyOnce;
    private boolean answered;
    public List<String> messages = new LinkedList<>();

    public WebSocketRunner() {
        reset();
    }

    public static WebSocketRunner run(final String server, final String userName) throws Exception {
        final WebSocketRunner client = new WebSocketRunner();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.start(server, userName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            client.stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).start();

        System.out.println("client starting...");
        while (!started) {
            Thread.sleep(10);
        }

        return client;
    }

    public void stop() throws Exception {
        session.close();
    }

    private void start(String server, final String userName) throws Exception {
        wsClient = new WebSocketClient();
        wsClient.start();

        session = wsClient.connect(new ClientSocket(), new URI(server + "?user=" + userName)).get(5000, TimeUnit.MILLISECONDS);
    }

    public WebSocketRunner willAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public String getRequest() {
        return request;
    }

    public void reset() {
        messages.clear();
        request = null;
        answer = null;
        times = 1;
        onlyOnce = false;
        answered = false;
        closed = false;
        started = false;
    }

    public WebSocketRunner times(int times) {
        this.times = times;
        return this;
    }

    public WebSocketRunner onlyOnce() {
        this.onlyOnce = true;
        return this;
    }

    @WebSocket
    private class ClientSocket {

        @OnWebSocketConnect
        private void onConnect(Session session) {
            System.out.println("client started!");
            started = true;
        }

        @OnWebSocketClose
        private void onClose(int closeCode, String message) {
            System.out.println("client closed!");
            closed = true;
        }

        @OnWebSocketMessage
        private void onMessage(String data) {
            System.out.println("client got message: " + data);
            messages.add(data);

            if (answer == null) {
                throw new IllegalArgumentException("Answer is null!");
            }
            try {
                if (!answered) {
                    for (int index = 0; index < times; index++) {
                        session.getRemote().sendString(answer);
                    }
                    if (onlyOnce) {
                        answered = true;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            request = data;
        }
    }
}
