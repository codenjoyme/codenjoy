package com.codenjoy.dojo.client.highload;

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

import com.codenjoy.dojo.services.hash.Hash;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Runner {

    private int iteration;
    private Map<String, List<Character>> status;
    private List<String> processed = new CopyOnWriteArrayList<>();

    public Runner(String host, int count) {
        status = new HashMap<>();

        int numLength = String.valueOf(count).length();

        for (int index = 1; index <= count; index++) {
            String number = StringUtils.leftPad(String.valueOf(index), numLength, "0");
            String id = "demo" + number;
            String code = Hash.getCode(id, id);
            String url = String.format("http://" + host + "/codenjoy-contest/board/player/" +
                    "%s?code=%s", id, code);

            setupListener(id, url);
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
            if (processed.contains(name)) {
                processed.clear();
                iteration++;

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

            session.getRemote().sendString("DOWN,ACT(" + iteration + ")");
        }
    };

    private void setupListener(String name, String url) {
        List<Character> statusLine = new LinkedList<>();
        status.put(name, statusLine);

        new WebSocketClient(url, new MySocket(name, statusLine));
    }
}
