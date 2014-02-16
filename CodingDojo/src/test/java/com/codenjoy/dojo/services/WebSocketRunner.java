package com.codenjoy.dojo.services;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 11:42 PM
 */
public class WebSocketRunner {

    private WebSocket.Connection connection;
    private WebSocketClientFactory factory;
    private String answer;
    private static boolean started = false;
    private String request = null;
    private boolean closed = false;

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

                if (answer == null) {
                    throw new IllegalArgumentException("Answer is null!");
                }
                try {
                    connection.sendMessage(answer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                request = data;
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }

    public void willAnswer(String answer) {
        this.answer = answer;
    }

    public String getRequest() {
        return request;
    }

    public void reset() {
        request = null;
        answer = null;
    }
}
