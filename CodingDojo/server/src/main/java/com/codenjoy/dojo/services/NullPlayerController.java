package com.codenjoy.dojo.services;

import java.io.IOException;

public class NullPlayerController implements PlayerController {

    public static final PlayerController INSTANCE = new NullPlayerController();

    private NullPlayerController() {
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
