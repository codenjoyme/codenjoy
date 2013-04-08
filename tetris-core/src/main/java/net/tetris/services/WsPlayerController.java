package net.tetris.services;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.ws.WebSocketPlayerTransport;
import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;

import java.io.IOException;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 11:04 PM
 */
public class WsPlayerController implements PlayerController {

    private PlayerTransport transport;

    @Override
    public void requestControl(Player player, Figure.Type type, int x, int y, Joystick joystick, List<Plot> plots, List<Figure.Type> futureFigures) throws IOException {
        GameState gameState = new TetrisGameState(plots, type, x, y, futureFigures);
        transport.sendState(player.getName(), gameState);
    }

    @Override
    public void registerPlayerTransport(Player player, Joystick joystick) {
        transport.registerPlayerEndpoint(player.getName(), new WsTetrisPlayerResponseHandler(player, joystick), null);
    }

    @Override
    public void unregisterPlayerTransport(Player player) {
        transport.unregisterPlayerEndpoint(player.getName());
    }

    public void setTransport(WebSocketPlayerTransport transport) {
        this.transport = transport;
    }

}
