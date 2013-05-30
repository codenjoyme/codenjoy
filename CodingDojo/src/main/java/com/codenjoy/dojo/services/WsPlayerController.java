package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.http.HttpPlayerTransport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 20:13
 */
public class WsPlayerController implements PlayerController {

    private PlayerTransport transport;

    public void requestControl(final Player player, final String board) throws IOException {
        transport.sendState(player.getName(), new BoardGameState(board));
    }

    public void registerPlayerTransport(Player player, Joystick joystick) {
        transport.registerPlayerEndpoint(player.getName(),
                new PlayerResponseHandlerImpl(player, joystick),
                player.getCallbackUrl());
    }

    public void unregisterPlayerTransport(Player player) {
        transport.unregisterPlayerEndpoint(player.getName());
    }

    public void setTransport(PlayerTransport transport) {
        this.transport = transport;
    }
}
