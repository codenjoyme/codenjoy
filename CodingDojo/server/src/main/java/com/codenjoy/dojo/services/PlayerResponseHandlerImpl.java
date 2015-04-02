package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.TransportErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: serhiy.zelenin
 * Date: 3/21/13
 * Time: 6:57 PM
 */
public class PlayerResponseHandlerImpl implements PlayerResponseHandler {
    private static Logger logger = LoggerFactory.getLogger(PlayerResponseHandlerImpl.class);
    private Player player;
    private Joystick joystick;

    public PlayerResponseHandlerImpl(Player player, Joystick joystick) {
        this.player = player;
        this.joystick = joystick;
    }

    @Override
    public void onResponseComplete(String responseContent, Object context) {
        logger.debug("Received response: {} for request: {}", responseContent, context);
        new PlayerCommand(joystick, responseContent, player).execute();

    }

    @Override
    public void onError(TransportErrorType type, Object context) {
        if (type == TransportErrorType.EXPIRED) {
            logger.warn("Request expired: player: {}, context: {}",
                    new Object[]{player.getName(), context});
        }
    }
}