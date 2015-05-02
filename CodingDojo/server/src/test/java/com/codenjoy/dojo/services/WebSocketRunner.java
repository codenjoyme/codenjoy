package com.codenjoy.dojo.services;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSocketRunner {

    private WebSocket.Connection connection;
    private WebSocketClientFactory factory;
    private String answer;
    private static boolean started;
    private String request;
    private boolean closed;
    private int times;
    private boolean onlyOnce;
    private boolean answered;
    public List<String> messages = new LinkedList<String>();

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
        connection.close();
        factory.stop();
    }

    private void start(String server, final String userName) throws Exception {
        factory = new WebSocketClientFactory();
        factory.start();

        WebSocketClient client = factory.newWebSocketClient();
        connection = client.open(new URI(server + "?user=" + userName), new WebSocket.OnTextMessage() {
            public void onOpen(Connection connection) {
                System.out.println("client started!");
                started = true;
            }

            public void onClose(int closeCode, String message) {
                System.out.println("client closed!");
                closed = true;
            }

            public void onMessage(String data) {
                System.out.println("client got message: " + data);
                messages.add(data);

                if (answer == null) {
                    throw new IllegalArgumentException("Answer is null!");
                }
                try {
                    if (!answered) {
                        for (int index = 0; index < times; index++) {
                            connection.sendMessage(answer);
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
        }).get(5000, TimeUnit.MILLISECONDS);
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
}
