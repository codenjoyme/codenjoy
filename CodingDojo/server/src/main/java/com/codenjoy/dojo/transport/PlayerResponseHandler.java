package com.codenjoy.dojo.transport;

public interface PlayerResponseHandler<TResponseContext> {
    void onResponseComplete(String responseContent, TResponseContext responseContext);

    void onError(TransportErrorType type, TResponseContext responseContext);
}
