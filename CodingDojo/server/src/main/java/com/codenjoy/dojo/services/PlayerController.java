package com.codenjoy.dojo.services;

import java.io.IOException;

public interface PlayerController {

    void requestControl(final Player player, final String board) throws IOException;

    void registerPlayerTransport(Player player, Joystick joystick);

    void unregisterPlayerTransport(Player player);
}
