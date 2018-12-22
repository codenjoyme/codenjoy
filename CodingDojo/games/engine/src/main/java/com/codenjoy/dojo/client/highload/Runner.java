package com.codenjoy.dojo.client.highload;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Runner {

    private int count;
    private Map<String, List<Character>> status;
    private List<String> processed = new CopyOnWriteArrayList<>();
    private List<Session> connected = new CopyOnWriteArrayList<>();
    private boolean started = false;

    public Runner(String host, int count) {
        this.count = count;
        status = new HashMap<>();

        int numLength = String.valueOf(count).length();

        for (int index = 1; index <= count; index++) {
            String number = StringUtils.leftPad(String.valueOf(index), numLength, "0");
            String name = "demo" + number + "@codenjoy.com";
            String code = makeCode(name, name);
            String url = String.format("http://" + host + "/codenjoy-contest/board/player/" +
                    "%s?code=%s", name, code);

            setupListener(name, url);
        }
    }

    @WebSocket
    public class MySocket {
        private Session session;
        private final String name;
        private final List<Character> statusLine;

        public MySocket(String name, List<Character> statusLine) {
            this.name = name;
            this.statusLine = statusLine;
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            System.out.println(StringUtils.leftPad(name, 21) + "> !");
            this.session = session;
            connected.add(session);
            if (connected.size() == count) {
                started = true;
                connected.forEach(s -> {
                    try {
                        s.getRemote().sendString("DOWN");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            statusLine.add('!');
        }

        @OnWebSocketClose
        public void onClose(int closeCode, String message) {
            statusLine.add('-');
        }

        @OnWebSocketError
        public void onError(Session session, Throwable reason) {
            statusLine.add('x');
        }

        @OnWebSocketMessage
        public void onMessage(String data) throws IOException {
            if (!started) {
                return;
            }

            if (processed.contains(name)) {
                processed.clear();
                status.entrySet().stream()
                        .sorted(Comparator.comparing(entry -> entry.getKey()))
                        .forEach(entry -> {
                            System.out.print(StringUtils.leftPad(entry.getKey(), 21) + "> ");
                            String status = entry.getValue().toString().replaceAll(", ", "").replaceAll("[\\[\\]]", "");
                            System.out.println(status);
                        });
                System.out.println("----------------------------------------------------------------------------------------------");
            }
            processed.add(name);
            statusLine.add('+');

            session.getRemote().sendString("DOWN");
        }
    };

    private void setupListener(String name, String url) {
        List<Character> statusLine = new LinkedList<>();
        status.put(name, statusLine);

        new WebSocketClient(url, new MySocket(name, statusLine));
    }

    public String makeCode(String email, String password) {
        return "" + Math.abs(email.hashCode()) + Math.abs(password.hashCode());
    }
}
