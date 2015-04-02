package com.codenjoy.dojo.loderunner.client;

import com.codenjoy.dojo.loderunner.client.utils.Board;
import com.codenjoy.dojo.loderunner.client.utils.RandomDice;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

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

    private static final String SERVER = "ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws";
    private static final String LOCAL = "ws://127.0.0.1:8080/codenjoy-contest/ws";
    private static String USER_NAME = "apofig";

    private WebSocket.Connection connection;
    private DirectionSolver solver;
    private WebSocketClientFactory factory;

    public WebSocketRunner(DirectionSolver solver) {
        this.solver = solver;
    }

    public static void main(String[] args) throws Exception {
        String host = SERVER;
        DirectionSolver mySolver = new YourDirectionSolver(new RandomDice());

        if (args.length != 0) {
            host = ("local".equals(args[0]))?LOCAL:("server".equals(args[0]))?SERVER:args[0];
            if (args.length >= 2) USER_NAME = args[1];
            if (args.length >= 3 && args[2].equals("apofig")) {
                mySolver = new ApofigDirectionSolver(new RandomDice());
            }
        }

        System.out.printf("Connecting '%s' to '%s'...\n", USER_NAME, host);
        run(host, USER_NAME, mySolver);
    }

    private static void run(String server, String userName, DirectionSolver mySolver) throws Exception {
        final WebSocketRunner client = new WebSocketRunner(mySolver);
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
                    String answer = solver.get(new Board(matcher.group(1)));
                    connection.sendMessage(answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }
}
