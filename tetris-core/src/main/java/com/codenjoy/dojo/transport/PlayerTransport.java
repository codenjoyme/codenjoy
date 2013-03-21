package com.codenjoy.dojo.transport;

import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 3/21/13
 * Time: 5:07 PM
 */
public interface PlayerTransport<TEndpointSettings, TResponseContext> {
    void sendState(String id, GameState state) throws IOException;

    /**
     * Only one endpoint per player is allowed
     * @param id player unique identifier
     * @param endpointSettings specific endpoint settings
     */
    void registerPlayerEndpoint(String id, PlayerAction<TResponseContext> action, TEndpointSettings endpointSettings);
}
