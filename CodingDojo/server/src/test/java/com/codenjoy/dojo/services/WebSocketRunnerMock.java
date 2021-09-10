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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class WebSocketRunnerMock {

    private final String server;
    private final String id;
    private final String code;
    private Session session;
    private WebSocketClient wsClient;
    private String answer;
    private boolean replyToServerImmediately;
    private AtomicBoolean started = new AtomicBoolean();
    private String request;
    private AtomicBoolean closed = new AtomicBoolean();
    private int times;
    private boolean onlyOnce;
    private boolean answered;
    private List<String> messages = new LinkedList<>();
    private Throwable error;

    public WebSocketRunnerMock(String server, String id, String code) {
        this.server = server;
        this.id = id;
        this.code = code;
        replyToServerImmediately = false;
        reset();
    }

    @SneakyThrows
    public void start() {
        new Thread(() -> {
            start(server, id, code);
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

        log.info("Client starting...");
        while (!started.get()) {
            Thread.sleep(10);
        }
    }

    public void stop() {
        if (session != null) {
            session.close();
        }
    }

    public String messages() {
        String result = messages.toString().replace("\"", "'");
        messages.clear();
        return result;
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @SneakyThrows
    private void start(String server, String id, String code)  {
        wsClient = new WebSocketClient();
        wsClient.start();

        URI uri = new URI(server + "?user=" + id + "&code=" + code);
        log.info("Connecting to: " + uri);
        session = wsClient.connect(new ClientSocket(), uri).get(5000, TimeUnit.MILLISECONDS);
    }

    public WebSocketRunnerMock willAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public String request() {
        return request;
    }

    public void reset() {
        messages.clear();
        request = null;
        answer = null;
        times = 1;
        onlyOnce = false;
        answered = false;
        closed.set(false);
        started.set(false);
    }

    public WebSocketRunnerMock times(int times) {
        this.times = times;
        return this;
    }

    public WebSocketRunnerMock onlyOnce() {
        this.onlyOnce = true;
        return this;
    }

    public void replyToServerImmediately(boolean input) {
        replyToServerImmediately = input;
    }

    @WebSocket
    public class ClientSocket {

        @OnWebSocketConnect
        public void onConnect(Session session) {
            log.info("Client started");
            started.set(true);
        }

        @OnWebSocketClose
        public void onClose(int closeCode, String message) {
            log.info("Client closed");
            closed.set(false);
        }

        @OnWebSocketMessage
        public void onMessage(String data) {
            log.info("Client got message: " + data);
            messages.add(data);

            if (answer == null) {
                if (replyToServerImmediately) {
                    throw new IllegalArgumentException("Answer is null!");
                } else {
                    log.warn("Answer is null. Cant say to server reply.");
                }
            } else {
                if (!answered) {
                    for (int index = 0; index < times; index++) {
                        sendToServer(answer);
                    }
                    if (onlyOnce) {
                        answered = true;
                    }
                }
            }

            request = data;
        }

        @OnWebSocketError
        public void onError(Session session, Throwable reason) {
            error = reason;
        }
    }

    @SneakyThrows
    public void sendToServer(String message) {
        while (session == null || session.getRemote() == null) {
            Thread.sleep(10);
        }
        log.info("Client send: " + message);
        session.getRemote().sendString(message);
    }

    public Throwable error() {
        return error;
    }
}
