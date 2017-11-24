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


import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service("playerTransport")
public class WebSocketPlayerTransport implements PlayerTransport {
    static PlayerResponseHandler NULL_HANDLER = new NullPlayerResponseHandler();
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

    /**
     * Случается, когда игрок зарегистрировался в игре на страничке регистрации
     * @param id идентификатор пользователя - его email
     * @param responseHandler обработчик
     * @param o дополнительные данные
     */
    @Override
    public void registerPlayerEndpoint(String id, PlayerResponseHandler responseHandler, Object o) {
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

    /**
     * Случается, когда игрок подключился по вебсокетам к серверу
     * @param id идентификатор пользователя - его email
     * @param playerSocket вебсокет
     */
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

    private static class NullPlayerResponseHandler implements PlayerResponseHandler {
        @Override
        public void onResponseComplete(String responseContent, Object o) {
        }

        @Override
        public void onError(TransportErrorType type, Object o) {
        }
    }

    private class SocketHandlerPair {
        private PlayerResponseHandler handler = NULL_HANDLER;
        //dummy player socket
        private PlayerSocket playerSocket = DUMMY_SOCKET;
    }
}
