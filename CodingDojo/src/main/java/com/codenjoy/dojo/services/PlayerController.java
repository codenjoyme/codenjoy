package com.codenjoy.dojo.services;

import java.io.IOException;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 16:42
 */
public interface PlayerController {
    void requestControl(final Player player, final String board) throws IOException;

    void registerPlayerTransport(Player player, Joystick joystick);

    void unregisterPlayerTransport(Player player);
}
