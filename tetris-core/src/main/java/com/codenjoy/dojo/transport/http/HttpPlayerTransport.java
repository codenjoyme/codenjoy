package com.codenjoy.dojo.transport.http;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerAction;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: serhiy.zelenin
 * Date: 3/21/13
 * Time: 5:09 PM
 */
public class HttpPlayerTransport implements PlayerTransport<String, HttpResponseContext>{
    private HttpClient client;
    private int timeout;
    private boolean sync = false;
    private int connectorType;
    private int corePoolSize;
    private int maximumPoolSize;
    private Map<String,PlayerEndpoint> endpoints = Collections.synchronizedMap(new HashMap<String, PlayerEndpoint>());

    private static Logger logger = LoggerFactory.getLogger(HttpPlayerTransport.class);

    public HttpPlayerTransport(int timeout, boolean sync, int connectorType, int corePoolSize, int maximumPoolSize) {
        this.timeout = timeout;
        this.sync = sync;
        this.connectorType = connectorType;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
    }

    @Override
    public void sendState(String id, GameState state) throws IOException {
        PlayerEndpoint endpoint = endpoints.get(id);

        ContentExchange exchange = new MyContentExchange(endpoint.action, id);

        exchange.setMethod("GET");

//        exchange.setURL(url.toString());
        String url = endpoint.callbackUrl + "?" + state.asString();
        exchange.setURL(url);
        logger.debug("Request sent to {}, url {}", id, url);
        client.send(exchange);
        if (sync) {
            try {
                exchange.waitForDone();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for player response?", e);
            }
        }

    }

    @Override
    public void registerPlayerEndpoint(String id, PlayerAction<HttpResponseContext> action, String callbackUrl) {
        endpoints.put(id, new PlayerEndpoint(action, callbackUrl));
    }


    @PostConstruct
    public void init() throws Exception {
        client = new HttpClient();
        client.setConnectBlocking(sync);
        client.setConnectorType(connectorType);
        client.setThreadPool(new ExecutorThreadPool(corePoolSize, maximumPoolSize, timeout, TimeUnit.MILLISECONDS));
        client.setTimeout(timeout);
        client.start();
    }

    private class PlayerEndpoint {
        private PlayerAction<HttpResponseContext> action;
        private String callbackUrl;

        public PlayerEndpoint(PlayerAction<HttpResponseContext> action, String callbackUrl) {
            this.action = action;
            this.callbackUrl = callbackUrl;
        }
    }

    public static class MyContentExchange extends ContentExchange {
        private PlayerEndpoint endpoint;
        private PlayerAction<HttpResponseContext> action;
        private String id;

        public MyContentExchange(PlayerAction<HttpResponseContext> action, String id) {
            this.action = action;
            this.id = id;
        }

        @Override
        protected void onExpire() {
            logger.warn("Request expired: ID: {}, address: {}, request: {}",
                    new Object[]{id, getAddress(), getRequestURI()});

            action.onError(TransportErrorType.EXPIRED, new HttpResponseContext(getResponseStatus(), getRequestURI(), getAddress()));
        }

        protected void onResponseComplete() throws IOException {
            String responseContent = this.getResponseContent();
            action.onResponseComplete(responseContent, new HttpResponseContext(this.getResponseStatus(), getRequestURI(), getAddress()));
        }
    }

}
