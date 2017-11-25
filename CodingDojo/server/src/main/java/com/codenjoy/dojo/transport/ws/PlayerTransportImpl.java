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

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, SocketsHandlerPair> endpoints = new HashMap<>();

    @Override
    public void sendState(String id, GameState state) throws IOException {
        lock.readLock().lock();
        SocketsHandlerPair pair;
        try {
            pair = endpoints.get(id);
            if (pair == null || pair.noSockets()) {
                return;
            }
            pair.sendMessage(state.asString());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void registerPlayerEndpoint(String id, PlayerResponseHandler responseHandler, Object endpointSettings) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketsHandlerPair(id);
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
            endpoints.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void registerPlayerSocket(String id, PlayerSocket playerSocket) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null) {
                pair = new SocketsHandlerPair(id);
            }
            pair.addSocket(playerSocket);
            endpoints.put(id, pair);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unregisterPlayerSocket(String id) {
        lock.writeLock().lock();
        try {
            SocketsHandlerPair pair = endpoints.get(id);
            if (pair == null || pair.noSockets()) {
                return;
            }
            // TODO кто и когда вызывает этот метод, и как понять какой сокет удален? Может задекорировать обработчик и на onclose/onerror вставить удаление. Только тут надо чтобы небыло многопоточных гонок
            pair.removeClosedSockets();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
