package com.codenjoy.dojo.services;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:05 AM
 */
public class PlayerController {
    private static Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private HttpClient client;
    private int timeout;

    public void requestControl(final Player player, final Joystick joystick, final String board) throws IOException {
        ContentExchange exchange = new MyContentExchange(joystick, player);
        exchange.setMethod("GET");

        String callbackUrl = player.getCallbackUrl().endsWith("/") ? player.getCallbackUrl() : player.getCallbackUrl() + "/";
        String url = callbackUrl + "?board=" + URLEncoder.encode(board.replace("\n", ""), "UTF-8");
        exchange.setURL(url);
        client.send(exchange);
    }

    /**
     * Timeout for player request for the next direction
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void init() throws Exception {
        client = new HttpClient();
        client.setConnectBlocking(false);
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setThreadPool(new ExecutorThreadPool(32, 256, timeout, TimeUnit.MILLISECONDS));
        client.setTimeout(timeout);
        client.start();
    }

    public static class MyContentExchange extends ContentExchange {
        private final Joystick joystick;
        private final Player player;

        public MyContentExchange(Joystick joystick, Player player) {
            this.joystick = joystick;
            this.player = player;
        }

        protected void onResponseComplete() throws IOException {
            String responseContent = this.getResponseContent();
            if (responseContent != null) {
                process(responseContent);
            }
        }

        public void process(String responseContent) {
            Pattern pattern = Pattern.compile("([^,]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(responseContent);
            while (matcher.find()) {
                String command = matcher.group(0);
                if (command == null) {
                    continue;
                }
                command = command.toLowerCase();

                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("For player %s (%s) command is '%s'",
                            player.getName(), player.getCallbackUrl(), command));
                }

                if (command.contains("left")) {
                    joystick.left();
                } else if (command.contains("right")) {
                    joystick.right();
                } else if (command.contains("up")) {
                    joystick.up();
                } else if (command.contains("down")) {
                    joystick.down();
                } else if (command.contains("act")) {
                    joystick.act();
                } else {
                    wrongCommand(responseContent);
                }
            }
        }

        private void wrongCommand(String responseContent) {
            logger.error(String.format("Player %s (%s) sent wrong command. Response is '%s'",
                    player.getName(), player.getCallbackUrl(), responseContent));
        }
    }
}
