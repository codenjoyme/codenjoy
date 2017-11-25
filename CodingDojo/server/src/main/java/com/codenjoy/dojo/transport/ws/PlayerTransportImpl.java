package com.codenjoy.dojo.transport.ws;

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


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerTransportImpl implements PlayerTransport {

    private final PlayerSocket DUMMY_SOCKET = new PlayerSocket();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, SocketHandlerPair> endpoints = new HashMap<>();

    @Override
    public void sendState(String id, GameState state) throws IOException {
        lock.readLock().lock();
        SocketHandlerPair pair;
        try {
            pair = endpoints.get(id);
            if (pair == null || pair.playerSocket == null) {
                return;
            }
            pair.playerSocket.sendMessage(state.asString());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void registerPlayerEndpoint(String id, PlayerResponseHandler responseHandler, Object endpointSettings) {
        lock.writeLock().lock();
        try {
            SocketHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketHandlerPair();
            }
            pair.handler = responseHandler;
            pair.playerSocket.setHandler(responseHandler);
            endpoints.put(id, pair);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unregisterPlayerEndpoint(String id) {
        lock.writeLock().lock();
        try {
            SocketHandlerPair pair = endpoints.get(id);
            if (pair == null || pair.playerSocket == null) {
                return;
            }
            endpoints.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void registerPlayerSocket(String id, PlayerSocket playerSocket) {
        lock.writeLock().lock();
        try {
            SocketHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketHandlerPair();
            }
            pair.playerSocket = playerSocket;
            pair.playerSocket.setHandler(pair.handler);
            endpoints.put(id, pair);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unregisterPlayerSocket(String id) {
        lock.writeLock().lock();
        try {
            SocketHandlerPair pair = endpoints.get(id);
            if (pair == null || pair.playerSocket == null) {
                return;
            }
            pair.playerSocket = DUMMY_SOCKET;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private class SocketHandlerPair {
        private PlayerResponseHandler handler = NullPlayerResponseHandler.NULL;
        private PlayerSocket playerSocket = DUMMY_SOCKET;
    }
}
