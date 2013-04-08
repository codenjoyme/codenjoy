package net.tetris.services;

import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.TransportErrorType;
import net.tetris.dom.Joystick;

/**
* User: serhiy.zelenin
* Date: 4/8/13
* Time: 11:11 PM
*/
class WsTetrisPlayerResponseHandler implements PlayerResponseHandler {
    private Player player;
    private Joystick joystick;

    public WsTetrisPlayerResponseHandler(Player player, Joystick joystick) {
        this.player = player;
        this.joystick = joystick;
    }

    @Override
    public void onResponseComplete(String responseContent, Object o) {
        new PlayerCommand(joystick, responseContent, player).execute();
    }

    @Override
    public void onError(TransportErrorType type, Object o) {
    }
}
