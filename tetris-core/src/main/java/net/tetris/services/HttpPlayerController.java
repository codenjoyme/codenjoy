package net.tetris.services;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerAction;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.TransportErrorType;
import com.codenjoy.dojo.transport.http.HttpPlayerTransport;
import com.codenjoy.dojo.transport.http.HttpResponseContext;
import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;
import net.tetris.dom.TetrisGame;
import org.eclipse.jetty.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public class HttpPlayerController implements PlayerController {
    private static Logger logger = LoggerFactory.getLogger(HttpPlayerController.class);

    private HttpClient client;
    private int timeout;
    private boolean sync = false;
    private String suffix = "/";

    private PlayerControllerListener listener;
    private PlayerTransport transport;

    public void requestControl(final Player player, final Figure.Type type, final int x, final int y, final Joystick joystick, final List<Plot> plots, final List<Figure.Type> futureFigures) throws IOException {
        //TODO: move it out from here!!!
        String callbackUrl = player.getCallbackUrl().endsWith("/") ? player.getCallbackUrl() : player.getCallbackUrl() + suffix;
        transport.registerPlayerEndpoint(player.getName(), new TetrisPlayerAction(player, listener, joystick), callbackUrl);

        GameState gameState = new TetrisGameState(plots, type, x, y, futureFigures);

        transport.sendState(player.getName(), gameState);
    }



    /**
     * Timeout for player request for the next direction
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void init() throws Exception {
        //TODO: specific instance of player transport to be created and inited by spring
        transport = new HttpPlayerTransport(timeout, sync, HttpClient.CONNECTOR_SELECT_CHANNEL, 32, 256);
        ((HttpPlayerTransport) transport).init();
    }

    public void setListener(PlayerControllerListener listener) {
        this.listener = listener;
    }


    private static class TetrisPlayerAction implements PlayerAction<HttpResponseContext> {
        private Player player;
        private PlayerControllerListener listener;
        private Joystick joystick;

        public TetrisPlayerAction(Player player, PlayerControllerListener listener, Joystick joystick) {
            this.player = player;
            this.listener = listener;
            this.joystick = joystick;
        }

        @Override
        public void onResponseComplete(String responseContent, HttpResponseContext responseContext) {
            logger.debug("Received response: {} for request: {}", responseContent, responseContext.getRequestURI());
            if (listener != null) {
                if (responseContext.getResponseStatus() != 200) {
                    logger.warn("Received error response: {}, player: {}, address: {}, request: {}",
                            new Object[] {responseContext.getResponseStatus(), player.getName(), responseContext.getAddress(),
                                    responseContext.getRequestURI()});
                    listener.log(player, responseContext.getRequestURI(), "ERROR:" + responseContext.getResponseStatus());
                } else {
                    listener.log(player, responseContext.getRequestURI(), responseContent);
                }

            }
            new PlayerCommand(joystick, responseContent, player).execute();

        }

        @Override
        public void onError(TransportErrorType type, HttpResponseContext httpResponseContext) {
            if (type == TransportErrorType.EXPIRED) {
                logger.warn("Request expired: player: {}, address: {}, request: {}",
                        new Object[]{player.getName(), httpResponseContext.getAddress(), httpResponseContext.getRequestURI()});
                if (listener != null) {
                    listener.log(player, httpResponseContext.getRequestURI(), "EXPIRED");
                }
            }
        }
    }

    private class TetrisGameState implements GameState {

        private final List<Plot> plots;
        private final Figure.Type type;
        private final int x;
        private final int y;
        private final List<Figure.Type> futureFigures;

        public TetrisGameState(List<Plot> plots, Figure.Type type, int x, int y, List<Figure.Type> futureFigures) {
            this.plots = plots;
            this.type = type;
            this.x = x;
            this.y = y;
            this.futureFigures = futureFigures;
        }

        @Override
        public String asString() {
            StringBuilder content = new StringBuilder();
            StringBuilder sb = exportGlassState(plots);

            try {
                content.append("figure=").append(type).append("&x=").append(x).append("&y=").append(y).append("&glass=").append(URLEncoder.encode(sb.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            if (futureFigures != null && !futureFigures.isEmpty()) {
                content.append("&next=");
                for (Figure.Type futureFigure : futureFigures) {
                    content.append(futureFigure.getName());
                }
            }
            return content.toString();
        }
    }

    private StringBuilder exportGlassState(List<Plot> plots) {
        char[][] glassState = new char[TetrisGame.GLASS_HEIGHT][TetrisGame.GLASS_WIDTH];
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            Arrays.fill(glassState[i], ' ');
        }

        for (Plot plot : plots) {
            glassState[plot.getY()][plot.getX()] = '*';
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            sb.append(glassState[i]);
        }
        return sb;
    }

}
