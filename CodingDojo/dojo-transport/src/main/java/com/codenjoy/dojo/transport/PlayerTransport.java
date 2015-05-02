package com.codenjoy.dojo.transport;

import java.io.IOException;

public interface PlayerTransport<TEndpointSettings, TResponseContext> {
    /**
     * Send game state to the player by given player id.
     * @param id - registered player id
     * @param state - GameState instance
     * @throws IOException
     */
    void sendState(String id, GameState state) throws IOException;

    /**
     * Only one endpoint per player is allowed
     * @param id player unique identifier
     * @param endpointSettings specific endpoint settings
     */
    void registerPlayerEndpoint(String id, PlayerResponseHandler<TResponseContext> responseHandler, TEndpointSettings endpointSettings);

    void unregisterPlayerEndpoint(String id);
}
