package com.codenjoy.dojo.transport.http;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
@Component
@Qualifier("httpPlayerTransport")
public class HttpPlayerTransport implements PlayerTransport<String, HttpResponseContext>{
    private HttpClient client;
    private int timeout;
    private boolean sync = false;
    private int corePoolSize;
    private int maximumPoolSize;
    private Map<String,PlayerEndpoint> endpoints = Collections.synchronizedMap(new HashMap<String, PlayerEndpoint>());

    private static Logger logger = LoggerFactory.getLogger(HttpPlayerTransport.class);
    private String suffix;


    @Autowired
    public HttpPlayerTransport(@Value("${timeout}") int timeout,
                               @Value("${sync}") boolean sync,
                               @Value("${corePoolSize}") int corePoolSize, @Value("${maximumPoolSize}") int maximumPoolSize,
                               @Value("${suffix}") String suffix) {
        this.timeout = timeout;
        this.sync = sync;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.suffix = suffix;
    }

    @Override
    public void sendState(String id, GameState state) throws IOException {
        PlayerEndpoint endpoint = endpoints.get(id);

        ContentExchange exchange = new MyContentExchange(endpoint.responseHandler, id);

        exchange.setMethod("GET");

        String callbackUrl = endpoint.callbackUrl.endsWith("/") ? endpoint.callbackUrl : endpoint.callbackUrl + suffix;
        String url = callbackUrl + "?" + state.asString();
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
    public void registerPlayerEndpoint(String id, PlayerResponseHandler<HttpResponseContext> responseHandler, String callbackUrl) {
        endpoints.put(id, new PlayerEndpoint(responseHandler, callbackUrl));
    }

    @Override
    public void unregisterPlayerEndpoint(String id) {
        endpoints.remove(id);
    }


    @PostConstruct
    public void init() throws Exception {
        client = new HttpClient();
        client.setConnectBlocking(sync);
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setThreadPool(new ExecutorThreadPool(corePoolSize, maximumPoolSize, timeout, TimeUnit.MILLISECONDS));
        client.setTimeout(timeout);
        client.start();
    }

    @PreDestroy
    public void destroy() throws Exception {
        client.stop();
        client.destroy();
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    private class PlayerEndpoint {
        private PlayerResponseHandler<HttpResponseContext> responseHandler;
        private String callbackUrl;

        public PlayerEndpoint(PlayerResponseHandler<HttpResponseContext> responseHandler, String callbackUrl) {
            this.responseHandler = responseHandler;
            this.callbackUrl = callbackUrl;
        }
    }

    public static class MyContentExchange extends ContentExchange {
        private PlayerEndpoint endpoint;
        private PlayerResponseHandler<HttpResponseContext> responseHandler;
        private String id;

        public MyContentExchange(PlayerResponseHandler<HttpResponseContext> responseHandler, String id) {
            this.responseHandler = responseHandler;
            this.id = id;
        }

        @Override
        protected void onExpire() {
            logger.warn("Request expired: ID: {}, address: {}, request: {}",
                    new Object[]{id, getAddress(), getRequestURI()});

            responseHandler.onError(TransportErrorType.EXPIRED, new HttpResponseContext(getResponseStatus(), getRequestURI(), getAddress()));
        }

        protected void onResponseComplete() throws IOException {
            String responseContent = this.getResponseContent();
            responseHandler.onResponseComplete(responseContent, new HttpResponseContext(this.getResponseStatus(), getRequestURI(), getAddress()));
        }
    }

}
