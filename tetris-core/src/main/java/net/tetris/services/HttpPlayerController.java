package net.tetris.services;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerTransport;
import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;

import java.io.IOException;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public class HttpPlayerController implements PlayerController {


    private PlayerControllerListener listener;

    private PlayerTransport transport;

    public void requestControl(final Player player, final Figure.Type type, final int x, final int y, final Joystick joystick, final List<Plot> plots, final List<Figure.Type> futureFigures) throws IOException {
        GameState gameState = new TetrisGameState(plots, type, x, y, futureFigures, true);

        transport.sendState(player.getName(), gameState);
    }



    public void setListener(PlayerControllerListener listener) {
        this.listener = listener;
    }

    public void setTransport(PlayerTransport transport) {
        this.transport = transport;
    }

    public void registerPlayerTransport(Player player, Joystick joystick) {
        transport.registerPlayerEndpoint(player.getName(), new HttpTetrisPlayerResponseHandler(player, listener, joystick), player.getCallbackUrl());
    }

    public void unregisterPlayerTransport(Player player) {
        transport.unregisterPlayerEndpoint(player.getName());
    }



}
