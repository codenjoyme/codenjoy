package com.codenjoy.dojo.client;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketRunner {

    public static enum Host {
        SERVER("ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws"),
        LOCAL("ws://127.0.0.1:8080/codenjoy-contest/ws");

        private String uri;

        Host(String uri) {
            this.uri = uri;
        }
    }

    private WebSocket.Connection connection;
    private DirectionSolver solver;
    private AbstractBoard board;
    private WebSocketClientFactory factory;

    public WebSocketRunner(DirectionSolver solver, AbstractBoard board) {
        this.solver = solver;
        this.board = board;
    }

    public static void run(Host host, String userName, DirectionSolver solver, AbstractBoard board) throws Exception {
        System.out.printf("Connecting '%s' to '%s'...\n", userName, host.uri);

        final WebSocketRunner client = new WebSocketRunner(solver, board);
        client.start(host.uri, userName);
        Runtime.getRuntime().addShutdownHook(new Thread(){
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

    private void stop() throws Exception {
        connection.close();
        factory.stop();
    }

    private void start(String server, String userName) throws Exception {
        final Pattern urlPattern = Pattern.compile("^board=(.*)$");

        factory = new WebSocketClientFactory();
        factory.start();

        WebSocketClient client = factory.newWebSocketClient();
        connection = client.open(new URI(server + "?user=" + userName), new WebSocket.OnTextMessage() {
            public void onOpen(Connection connection) {
                System.out.println("Opened connection " + connection.toString());
            }

            public void onClose(int closeCode, String message) {
                System.out.println("Closed with message: '" + message + "' and code: " + closeCode);
            }

            public void onMessage(String data) {
                System.out.println("Data from server: " + data);
                try {
                    Matcher matcher = urlPattern.matcher(data);
                    if ( !matcher.matches()) {
                        throw new RuntimeException("Error parsing data: " + data);
                    }

                    board.forString(matcher.group(1));
                    System.out.println("Board: " + board);

                    String answer = solver.get(board);
                    System.out.println("Answer: " + answer);

                    connection.sendMessage(answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }
}
