package net.tetris.services;

import net.tetris.dom.Figure;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public class PlayerController {

    public void requestControl(Player player, Figure.Type type, int x, int y) throws IOException {
        HttpClient client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setTimeout(30 * 1000);
        try {
            client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
