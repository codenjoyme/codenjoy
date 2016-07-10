package net.tetris.services;

import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.TransportErrorType;
import com.codenjoy.dojo.transport.http.HttpResponseContext;
import net.tetris.dom.TetrisJoystik;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* User: serhiy.zelenin
* Date: 3/21/13
* Time: 6:57 PM
*/
public class HttpTetrisPlayerResponseHandler implements PlayerResponseHandler<HttpResponseContext> {
    private Player player;
    private PlayerControllerListener listener;
    private TetrisJoystik joystick;
    private static Logger logger = LoggerFactory.getLogger(HttpTetrisPlayerResponseHandler.class);

    public HttpTetrisPlayerResponseHandler(Player player, PlayerControllerListener listener, TetrisJoystik joystick) {
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
