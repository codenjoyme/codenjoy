package com.codenjoy.dojo.client;

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


import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketRunner {

    public static final String DEFAULT_USER = "apofig@gmail.com";
    private static final String LOCAL = "127.0.0.1:8080";
    private static final String REMOTE = "tetrisj.jvmhost.net:12270";

    private static boolean printToConsole = true;
    private static Map<String, WebSocketRunner> clients = new ConcurrentHashMap<>();

    private static String getUrl() {
        return REMOTE;
    }

    public enum Host {
        // подключение клиента к удаленному серваку
        REMOTE(WebSocketRunner.getUrl()),

        // используется для запуска AI бота на локали сервера, без печати в консоль трешняка
        REMOTE_LOCAL(WebSocketRunner.getUrl()),

        // работа клиента с локальным серваком
        LOCAL(WebSocketRunner.LOCAL);

        public String host;
        public String uri;

        Host(String host) {
            this.host = host;
            this.uri = "ws://" + host + "/codenjoy-contest/ws";
        }
    }

    private WebSocket.Connection connection;
    private Solver solver;
    private ClientBoard board;
    private WebSocketClientFactory factory;
    private Runnable onClose;

    public WebSocketRunner(Solver solver, ClientBoard board) {
        this.solver = solver;
        this.board = board;
    }

    public static WebSocketRunner run(Host host, String userName, Solver solver, ClientBoard board) throws Exception {
        // если запускаем на серваке бота, то в консоль не принтим
        printToConsole = (host != Host.REMOTE_LOCAL);

        // на локали файлик LOCAL означает что мы игнорим что выбрал игрок
        if (new File("LOCAL").exists()) {
            host = Host.LOCAL;
        }

        return run(host.uri, userName, solver, board);
    }

    public static WebSocketRunner run(String uri, String userName, Solver solver, ClientBoard board) throws Exception {
        if (clients.containsKey(userName)) {
            return clients.get(userName);
        }
        final WebSocketRunner client = new WebSocketRunner(solver, board);
        client.start(uri, userName);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                client.stop();
            }
        });

        clients.put(userName, client);
        return client;
    }

    private void stop() {
        try {
            connection.close();
            factory.stop();
        } catch (Exception e) {
            print(e);
        }
    }

    private void start(final String server, final String userName) throws Exception {
        final Pattern urlPattern = Pattern.compile("^board=(.*)$");

        factory = new WebSocketClientFactory();
        factory.start();

        final WebSocketClient client = factory.newWebSocketClient();

        onClose = new Runnable() {
            @Override
            public void run() {
                printReconnect();
                connectLoop(server, userName, urlPattern, client);
            }
        };

        connectLoop(server, userName, urlPattern, client);
    }

    private void connectLoop(String server, String userName, Pattern urlPattern, WebSocketClient client) {
        while (true) {
            try {
                tryToConnect(server, userName, urlPattern, client);
                break;
            } catch (Exception e) {
                print(e);
                printReconnect();
            }
        }
    }

    private void printReconnect() {
        print("Waiting before reconnect...");
        printBreak();
        sleep(5000);
    }

    private void tryToConnect(String server, String userName, final Pattern urlPattern, WebSocketClient client) throws Exception {
        URI uri = new URI(server + "?user=" + userName);
        print(String.format("Connecting '%s' to '%s'...", userName, uri));

        if (connection != null) {
            connection.close();
        }

        connection = client.open(uri, new WebSocket.OnTextMessage() {
            public void onOpen(Connection connection) {
                print("Opened connection " + connection.toString());
            }

            public void onClose(int closeCode, String message) {
                if (onClose != null) {
                    onClose.run();
                }
                print("Closed with message: '" + message + "' and code: " + closeCode);
            }

            public void onMessage(String data) {
                print("Data from server: " + data);
                try {
                    Matcher matcher = urlPattern.matcher(data);
                    if (!matcher.matches()) {
                        throw new RuntimeException("Error parsing data: " + data);
                    }

                    board.forString(matcher.group(1));
                    print("Board: " + board);

                    String answer = solver.get(board);
                    print("Answer: " + answer);

                    connection.sendMessage(answer);
                } catch (Exception e) {
                    print(e);
                }
                printBreak();
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            print(e);
        }
    }

    private void printBreak() {
        print("-------------------------------------------------------------");
    }

    public static void print(String message) {
        if (printToConsole) {
            System.out.println(message);
        }
    }

    private void print(Exception e) {
        if (printToConsole) {
            e.printStackTrace(System.out);
        }
    }
}
