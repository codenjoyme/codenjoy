package net.tetris.services;

import net.tetris.dom.Figure;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public class PlayerController implements InitializingBean {

    private HttpClient client;
    private int timeout;

    public void requestControl(Player player, Figure.Type type, int x, int y) throws IOException {
        ContentExchange exchange = new ContentExchange() {
            protected void onResponseComplete() throws IOException {
                super.onResponseComplete();
                String responseContent = this.getResponseContent();
            }
        };

        exchange.setMethod("GET");
        String callbackUrl = player.getCallbackUrl().endsWith("/") ? player.getCallbackUrl() : player.getCallbackUrl() + "/";
        exchange.setURL(callbackUrl + "?figure=" + type + "&x=" + x + "&y=" + y);
        client.send(exchange);
    }

    /**
     * Timeout for player request for the next direction
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setThreadPool(new ExecutorThreadPool(4, 256, timeout, TimeUnit.SECONDS));
        client.setTimeout(timeout);
        client.start();
    }
}
