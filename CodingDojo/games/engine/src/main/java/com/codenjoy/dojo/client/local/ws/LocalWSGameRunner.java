package com.codenjoy.dojo.client.local.ws;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.GameType;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.codenjoy.dojo.client.WebSocketRunner.BOARD_FORMAT2;

public class LocalWSGameRunner {

    private List<ConnectionStatus> statuses = new LinkedList<>();
    private LocalGameRunner runner;
    private GameType gameType;

    public static void run(GameType gameType, String host, int port, int timeout) {
        new LocalWSGameRunner().start(gameType, host, port, timeout);
    }

    public void start(GameType gameType, String host, int port, int timeout) {
        this.gameType = gameType;
        LocalGameRunner.timeout = timeout;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printWelcome = true;
        runner = new LocalGameRunner(gameType);

        CompletableFuture.runAsync(() -> startWsServer(host, port));

        runner.run(tick -> {});
    }

    private ConnectionStatus status(WebSocket socket) {
        return statuses.stream()
                .filter(status -> status.getSocket() == socket)
                .findFirst()
                .orElse(null);
    }

    private String getAnswer(ConnectionStatus status, ClientBoard clientBoard) {
        String board = ((AbstractBoard)clientBoard).boardAsString().replaceAll("\n", "");

        if (!status.getSocket().isOpen()) {
            exit(status);
            return "";
        }

        status.getSocket().send(String.format(BOARD_FORMAT2, board));
        return status.pullAction();
    }

    private void startWsServer(String host, int port) {
        WebSocketServer server = new WebSocketServer(new InetSocketAddress(host, port)) {
            @Override
            public void onOpen(WebSocket socket, ClientHandshake clientHandshake) {
                System.out.println(">>> Open connection");

                ConnectionStatus status = new ConnectionStatus(socket);
                statuses.add(status);

                status.setSolver(board -> getAnswer(status, board));

                try {
                    runner.add(status.getSolver(), gameType.getBoard().newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onClose(WebSocket socket, int i, String s, boolean b) {
                System.out.println(">>> Closed connection");
                ConnectionStatus status = status(socket);

                if (status != null) {
                    exit(status);
                }
            }

            @Override
            public void onMessage(WebSocket socket, String action) {
                ConnectionStatus status = status(socket);

                status.setAction(action);
            }

            @Override
            public void onError(WebSocket socket, Exception e) {
                System.out.println(">>> Error connection: " + e.toString());
                ConnectionStatus status = status(socket);

                if (status != null) {
                    exit(status);
                }
                LocalGameRunner.exit = true;
            }

            @Override
            public void onStart() {
                System.out.println(">>> Started server");
            }
        };
        try {
            server.run();
        } catch (Exception e) {
            System.out.println(">>> Staring server error: " + e.toString());
            LocalGameRunner.exit = true;
        }
    }

    private void exit(ConnectionStatus status) {
        statuses.remove(status);
        status.release();
        runner.remove(status.getSolver());
    }
}
