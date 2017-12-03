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


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketRunner {

    public static final String DEFAULT_USER = "apofig@gmail.com";
    private static final String LOCAL = "127.0.0.1:8080";
    private static final String REMOTE = "tetrisj.jvmhost.net:12270";
    public static final String WS_URI_PATTERN = "ws://%s/codenjoy-contest/ws";

    public static boolean printToConsole = true;
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
            this.uri = String.format(WS_URI_PATTERN, host);
        }
    }

    private Session session;
    private WebSocketClient wsClient;
    private Solver solver;
    private ClientBoard board;
    private Runnable onClose;

    public WebSocketRunner(Solver solver, ClientBoard board) {
        this.solver = solver;
        this.board = board;
    }

    /**
     * @param host Servers enum
     * @see Host
     * @see WebSocketRunner#run(String, String, String, Solver, ClientBoard)
     */
    public static WebSocketRunner run(Host host, String userName, String code,
                                      Solver solver, ClientBoard board)
    {
        // если запускаем на серваке бота, то в консоль не принтим
        printToConsole = (host != Host.REMOTE_LOCAL);

        // на локали файлик LOCAL означает что мы игнорим что выбрал игрок
        if (new File("LOCAL").exists()) {
            host = Host.LOCAL;
        }

        return run(host.uri, userName, code, solver, board);
    }

    /**
     * To connect on server in your LAN.
     * @param server String server and port. Format 192.168.0.1:8080
     * @see WebSocketRunner#run(String, String, String, Solver, ClientBoard)
     */
    public static WebSocketRunner runOnServer(String server, String userName, String code,
                                              Solver solver, ClientBoard board)
    {
         return run(String.format(WS_URI_PATTERN, server), userName, code, solver, board);
    }

     /**
     * To connect on server in your LAN.
     * @param uri String websocker server uri
     * @see WebSocketRunner#WS_URI_PATTERN
     *
     * @param userName email that you enter on registration page
     * @param solver your AI
     * @param board Board class
     * @return WebSocketRunner intance
     */
    public static WebSocketRunner run(String uri, String userName, String code,
                                      Solver solver, ClientBoard board)
    {
        try {
            if (clients.containsKey(userName)) {
                return clients.get(userName);
            }
            final WebSocketRunner client = new WebSocketRunner(solver, board);
            client.start(uri, userName, code);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    client.stop();
                }
            });

            clients.put(userName, client);
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stop() {
        try {
            session.close();
        } catch (Exception e) {
            print(e);
        }
    }

    private void start(final String server, final String userName, final String code) throws Exception {
        final Pattern urlPattern = Pattern.compile("^board=(.*)$");

        wsClient = new WebSocketClient();
        wsClient.start();

        onClose = () -> {
            if (solver instanceof OneCommandSolver) {
                return;
            }
            printReconnect();
            connectLoop(server, userName, code, urlPattern);
        };

        connectLoop(server, userName, code, urlPattern);
    }

    @WebSocket
    public class ClientSocket {

        private Pattern pattern;

        public ClientSocket(Pattern pattern) {
            this.pattern = pattern;
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            print("Opened connection " + session.toString());
        }

        @OnWebSocketClose
        public void onClose(int closeCode, String message) {
            if (onClose != null) {
                onClose.run();
            }
            print("Closed with message: '" + message + "' and code: " + closeCode);
        }

        @OnWebSocketError
        public void onError(Session session, Throwable reason) {
            if (isUnauthorizedAccess(reason)) {
                print("Connection error: Unauthorized access. Please register user and/or write valid EMAIL/CODE in the client.");
            } else {
                print("Error with message: '" + reason.toString());
            }
        }

        @OnWebSocketMessage
        public void onMessage(String data) {
            print("Data from server: " + data);
            try {
                Matcher matcher = pattern.matcher(data);
                if (!matcher.matches()) {
                    throw new RuntimeException("Error parsing data: " + data);
                }

                board.forString(matcher.group(1));
                print("Board: " + board);

                String answer = solver.get(board);
                print("Answer: " + answer);

                session.getRemote().sendString(answer);
            } catch (Exception e) {
                print(e);
            }
            printBreak();
        }
    }

    private boolean isUnauthorizedAccess(Throwable exception) {
        return exception instanceof UpgradeException && ((UpgradeException)exception).getResponseStatusCode() == 401;
    }

    private void connectLoop(String server, String userName, String code, Pattern urlPattern) {
        while (true) {
            try {
                tryToConnect(server, userName, code, urlPattern);
                break;
            } catch (ExecutionException e) {
                if (!isUnauthorizedAccess(e.getCause())) {
                    print(e);
                }
                printReconnect();
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

    private void tryToConnect(String server, String userName, String code, Pattern pattern) throws Exception {
        URI uri = new URI(String.format("%s?user=%s&code=%s", server, userName, code));
        print(String.format("Connecting '%s' to '%s'...", userName, uri));

        if (session != null) {
            session.close();
        }

        session = wsClient.connect(new ClientSocket(pattern), uri).get(5000, TimeUnit.MILLISECONDS);
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
