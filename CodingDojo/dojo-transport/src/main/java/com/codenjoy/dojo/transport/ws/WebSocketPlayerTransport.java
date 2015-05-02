package com.codenjoy.dojo.transport.ws;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service("wsPlayerTransport")
public class WebSocketPlayerTransport implements PlayerTransport {
    static PlayerResponseHandler NULL_HANDLER = new NullPlayerResponseHandler();
    private final PlayerSocket DUMMY_SOCKET = new PlayerSocket(null, WebSocketPlayerTransport.this);

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
            pair.playerSocket.close();
            endpoints.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void registerPlayerSocket(String authId, PlayerSocket playerSocket) {
        lock.writeLock().lock();
        try {
            SocketHandlerPair pair = endpoints.get(authId);
            if (pair == null) {
                pair = new SocketHandlerPair();
            }
            pair.playerSocket = playerSocket;
            pair.playerSocket.setHandler(pair.handler);
            endpoints.put(authId, pair);
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
