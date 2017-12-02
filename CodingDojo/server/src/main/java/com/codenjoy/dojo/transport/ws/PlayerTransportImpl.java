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
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class PlayerTransportImpl implements PlayerTransport {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, SocketsHandlerPair> endpoints = new LinkedHashMap<>();
    private Map<PlayerSocket, Function<Object, Object>> filters = new HashMap<>();
    private Function<Object, Object> defaultFilter;

    @Override
    public void sendStateToAll(Object state) throws IOException {
        lock.readLock().lock();
        try {
            List<String> messages = new LinkedList<>();
            for (SocketsHandlerPair pair : endpoints.values()) {
                if (pair == null || pair.noSockets()) {
                    continue;
                }
                try {
                    pair.sendMessage(state);
                } catch (IOException e) {
                    messages.add(e.getMessage());
                }
            }
            if (!messages.isEmpty()) {
                throw new IOException("Error during send state to all players: " +
                        messages.toString());
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void sendState(String id, Object state) throws IOException {
        lock.readLock().lock();
        SocketsHandlerPair pair;
        try {
            pair = endpoints.get(id);
            if (pair == null || pair.noSockets()) {
                return;
            }
            pair.sendMessage(state);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void registerPlayerEndpoint(String id, PlayerResponseHandler responseHandler) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketsHandlerPair(id, this::getFilter);
            }
            pair.setHandler(responseHandler);
            endpoints.put(id, pair);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unregisterPlayerEndpoint(String id) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null || pair.noSockets()) {
                return;
            }
            SocketsHandlerPair removed = endpoints.remove(id);
            if (removed != null) {
                for (PlayerSocket socket : removed.getSockets()) {
                    filters.remove(socket);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void registerPlayerSocket(String id, PlayerSocket socket) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketsHandlerPair(id, this::getFilter);
            }
            pair.addSocket(socket);
            endpoints.put(id, pair);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Function<Object, Object> getFilter(PlayerSocket socket) {
        if (defaultFilter != null) {
            return defaultFilter;
        }
        return filters.get(socket);
    }

    @Override
    public void unregisterPlayerSocket(PlayerSocket socket) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(socket.getId());
            if (pair == null || pair.noSockets()) {
                return;
            }
            pair.removeClosedSockets();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setFilterFor(PlayerSocket socket, Function<Object, Object> filter) {
        lock.writeLock().lock();
        try {
            filters.put(socket, filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setDefaultFilter(Function<Object, Object> filter) {
        this.defaultFilter = filter;
    }

}
