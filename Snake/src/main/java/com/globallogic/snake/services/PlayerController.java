package com.globallogic.snake.services;

import com.globallogic.snake.console.SnakePrinterImpl;
import com.globallogic.snake.model.BoardImpl;
import com.globallogic.snake.model.middle.Joystick;
import com.globallogic.snake.services.playerdata.Plot;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
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

    public void requestControl(final Player player, final Joystick joystick, final BoardImpl board) throws IOException {
        ContentExchange exchange = new MyContentExchange(joystick, player);

        exchange.setMethod("GET");
        String callbackUrl = player.getCallbackUrl().endsWith("/") ? player.getCallbackUrl() : player.getCallbackUrl() + "/";
        String stringBoard = exportBoardState(board);

        int x = board.getSnake().getX();
        int y = board.getSnake().getY();

        String url = callbackUrl + "?x=" + x + "&y=" + y + "&board=" + URLEncoder.encode(stringBoard, "UTF-8");
        exchange.setURL(url);
        client.send(exchange);
    }


    private String exportBoardState(BoardImpl board) {
        return new SnakePrinterImpl().print(board).replaceAll("\n", ""); // TODO remove dependency
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
        client.setThreadPool(new ExecutorThreadPool(32, 256, timeout, TimeUnit.SECONDS));
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
            process(this.getResponseContent());
        }

        public void process(String responseContent) {
            Pattern pattern = Pattern.compile("(left)|(right)|(up)|(down)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(responseContent);
            if (!matcher.find()) {
                wrongCommand();
                return;
            }
            String command = matcher.group(0).toLowerCase();

            switch (command) {
                case "left":
                    joystick.turnLeft(); break;
                case "right":
                    joystick.turnRight(); break;
                case "up":
                    joystick.turnUp(); break;
                case "down":
                    joystick.turnDown(); break;
                default :
                    wrongCommand();
            }
        }

        private void wrongCommand() {
            logger.error("Player " + player.getName() + " sent wrong command");
        }
    }
}
