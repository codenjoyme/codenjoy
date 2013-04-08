package net.tetris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 11:15 PM
 */
@Component("playerService")
public class TetrisPlayerService extends PlayerService<String>{

    @Autowired
    private PlayerController wsPlayerController;

    public static String HTTP_PROTOCOL = "http";
    public static String WS_PROTOCOL = "ws";

    @Override
    protected PlayerController createPlayerController(String protocol) {
        if (WS_PROTOCOL.equals(protocol)){
            return wsPlayerController;
        }
        return playerController;
    }
}
