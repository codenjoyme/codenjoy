package net.tetris.services;

import com.codenjoy.dojo.transport.PlayerResponseHandler;
import com.codenjoy.dojo.transport.TransportErrorType;
import net.tetris.dom.TetrisJoystik;

/**
* User: serhiy.zelenin
* Date: 4/8/13
* Time: 11:11 PM
*/
class WsTetrisPlayerResponseHandler implements PlayerResponseHandler {
    private Player player;
    private TetrisJoystik joystick;

    public WsTetrisPlayerResponseHandler(Player player, TetrisJoystik joystick) {
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
