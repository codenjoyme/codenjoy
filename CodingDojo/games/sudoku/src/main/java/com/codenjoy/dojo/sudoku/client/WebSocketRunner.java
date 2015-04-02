package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.sudoku.client.utils.BoardImpl;
import com.codenjoy.dojo.sudoku.client.utils.RandomDice;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketRunner {

    private static final String SERVER = "ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws";
//    private static final String SERVER = "ws://127.0.0.1:8080/codenjoy-contest/ws";
    private static String USER_NAME = "apofig";

    private WebSocket.Connection connection;
    private DirectionSolver solver;
    private WebSocketClientFactory factory;

    public WebSocketRunner(DirectionSolver solver) {
        this.solver = solver;
    }

    public static void main(String[] args) throws Exception {
        run(SERVER, USER_NAME);
    }

    private static void run(String server, String userName) throws Exception {
        final WebSocketRunner client = new WebSocketRunner(new YourDirectionSolver(new RandomDice()));
        client.start(server, userName);
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

        org.eclipse.jetty.websocket.WebSocketClient client = factory.newWebSocketClient();
        connection = client.open(new URI(server + "?user=" + userName), new WebSocket.OnTextMessage() {
            public void onOpen(Connection connection) {
                System.out.println("Opened");
            }

            public void onClose(int closeCode, String message) {
                System.out.println("Closed");
            }

            public void onMessage(String data) {
                System.out.println("data = " + data);
                try {
                    Matcher matcher = urlPattern.matcher(data);
                    if ( !matcher.matches()) {
                        throw new RuntimeException("WTF? " + data);
                    }
                    String answer = solver.get(new BoardImpl(matcher.group(1)));
                    connection.sendMessage(answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }
}
