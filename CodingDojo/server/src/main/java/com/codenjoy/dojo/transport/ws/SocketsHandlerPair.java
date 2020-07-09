package com.codenjoy.dojo.transport.ws;

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


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by indigo on 2017-11-26.
 */
public class SocketsHandlerPair {

    private Function<PlayerSocket, Function<Object, Object>> filters;
    private String id;
    private ResponseHandler handler = NullResponseHandler.NULL;
    private List<PlayerSocket> sockets = new LinkedList<>();

    public SocketsHandlerPair(String id, Function<PlayerSocket, Function<Object, Object>> filters) {
        this.id = id;
        this.filters = filters;
    }

    public void addSocket(PlayerSocket socket) {
        sockets.add(socket);
        socket.setHandler(handler);
    }

    public void remove(PlayerSocket socket) {
        sockets.remove(socket);
    }

    public void setHandler(ResponseHandler handler) {
        this.handler = handler;
        for (PlayerSocket socket : sockets) {
            socket.setHandler(handler);
        }
    }

    public boolean noSockets() {
        return sockets.isEmpty();
    }

    public void sendMessage(Object data) throws IOException {
        for (PlayerSocket socket : sockets) {
            Function<Object, Object> filter = filters.apply(socket);
            if (filter != null) {
                socket.sendMessage(filter.apply(data).toString());
            }
        }
    }

    public void removeClosedSockets() {
        for (PlayerSocket socket : sockets.toArray(new PlayerSocket[0])) {
            if (!socket.isOpen()) {
                sockets.remove(socket);
            }
        }
    }

    public String getId() {
        return id;
    }

    public List<PlayerSocket> getSockets() {
        return sockets;
    }
}
