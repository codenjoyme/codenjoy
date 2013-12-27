package com.codenjoy.dojo.services;

import java.io.IOException;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:26
 */
public class NullPlayerController implements PlayerController {

    NullPlayerController() {
        // do nothing
    }

    @Override
    public void requestControl(Player player, String board) throws IOException {
        // do nothing
    }

    @Override
    public void registerPlayerTransport(Player player, Joystick joystick) {
        // do nothing
    }

    @Override
    public void unregisterPlayerTransport(Player player) {
        // do nothing
    }
}
