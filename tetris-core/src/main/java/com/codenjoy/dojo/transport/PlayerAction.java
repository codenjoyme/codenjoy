package com.codenjoy.dojo.transport;

/**
 * User: serhiy.zelenin
 * Date: 3/21/13
 * Time: 6:08 PM
 */
public interface PlayerAction<TResponseContext> {
    void onResponseComplete(String responseContent, TResponseContext responseContext);

    void onError(TransportErrorType type, TResponseContext responseContext);
}
