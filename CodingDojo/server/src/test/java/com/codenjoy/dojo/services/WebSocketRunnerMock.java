package com.codenjoy.dojo.services;

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


import lombok.SneakyThrows;
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

public class WebSocketRunnerMock {

    private final String server;
    private final String userName;
    private final String code;
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

    public WebSocketRunnerMock(String server, String userName, String code) {
        this.server = server;
        this.userName = userName;
        this.code = code;
        reset();
    }

    public void start() {
        new Thread(() -> {
            start(server, userName, code);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        this.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }).start();

        System.out.println("client starting...");
        while (!started) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        session.close();
    }

    @SneakyThrows
    private void start(String server, String userName, String code)  {
        wsClient = new WebSocketClient();
        wsClient.start();

        URI uri = new URI(server + "?user=" + userName + "&code=" + code);
        System.out.println("Connecting to: " + uri);
        session = wsClient.connect(new ClientSocket(), uri).get(5000, TimeUnit.MILLISECONDS);
    }

    public WebSocketRunnerMock willAnswer(String answer) {
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

    public WebSocketRunnerMock times(int times) {
        this.times = times;
        return this;
    }

    public WebSocketRunnerMock onlyOnce() {
        this.onlyOnce = true;
        return this;
    }

    @WebSocket
    public class ClientSocket {

        @OnWebSocketConnect
        public void onConnect(Session session) {
            System.out.println("client started!");
            started = true;
        }

        @OnWebSocketClose
        public void onClose(int closeCode, String message) {
            System.out.println("client closed!");
            closed = true;
        }

        @OnWebSocketMessage
        public void onMessage(String data) {
            System.out.println("client got message: " + data);
            messages.add(data);

            if (answer == null) {
                throw new IllegalArgumentException("Answer is null!");
            }
            if (!answered) {
                for (int index = 0; index < times; index++) {
                    send();
                }
                if (onlyOnce) {
                    answered = true;
                }
            }

            request = data;
        }
    }

    @SneakyThrows
    private void send() {
        session.getRemote().sendString(answer);
    }
}
