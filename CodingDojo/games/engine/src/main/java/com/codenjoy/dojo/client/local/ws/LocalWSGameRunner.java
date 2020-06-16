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
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.GameType;
import lombok.SneakyThrows;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.codenjoy.dojo.client.WebSocketRunner.BOARD_FORMAT2;

public class LocalWSGameRunner {

    private String action;
    private Object waitAction = new Object();
    private Object waitBoard = new Object();
    private String board;

    public static void run(GameType gameType, String host, int port) {
        new LocalWSGameRunner().start(gameType, host, port);
    }

    public void start(GameType gameType, String host, int port) {
        CompletableFuture.runAsync(() -> startWsServer(host, port));

        Solver solver = board -> {
            LocalWSGameRunner.this.board = ((AbstractBoard) board).boardAsString().replaceAll("\n", "");

            // проинформировали что у нас на руках есть board
            synchronized (waitBoard) {
                waitBoard.notify();

                try {
                    waitBoard.wait();
                } catch (InterruptedException e) {
                    // случится, если что-то прервет Thread
                }
            }

            return action;
        };

        runGame(gameType, solver);
    }

    @SneakyThrows
    private LocalGameRunner runGame(GameType gameType, Solver solver) {
        LocalGameRunner.timeout = 1000;

        return LocalGameRunner.run(gameType,
                Arrays.asList(solver),
                Arrays.asList(gameType.getBoard().newInstance()));
    }

    private void startWsServer(String host, int port) {
        WebSocketServer server = new WebSocketServer(new InetSocketAddress(host, port)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                System.out.println("open");
                this.broadcast(String.format(BOARD_FORMAT2, board));
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                System.out.println("close");
            }

            @Override
            public void onMessage(WebSocket webSocket, String action) {
                LocalWSGameRunner.this.action = action;

                // проинформировали что у нас на руках есть action
                synchronized (waitBoard) {
                    waitBoard.notify();

                    try {
                        waitBoard.wait();
                    } catch (InterruptedException e) {
                        // случится, если что-то прервет Thread
                    }
                }

                this.broadcast(String.format(BOARD_FORMAT2, board));
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                System.out.println("error");
            }

            @Override
            public void onStart() {
                System.out.println("start");
            }
        };
        server.run();
    }


}
