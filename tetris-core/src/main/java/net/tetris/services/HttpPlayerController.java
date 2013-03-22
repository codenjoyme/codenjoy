package net.tetris.services;

import com.codenjoy.dojo.transport.GameState;
import com.codenjoy.dojo.transport.PlayerTransport;
import com.codenjoy.dojo.transport.http.HttpPlayerTransport;
import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;
import net.tetris.dom.TetrisGame;
import org.eclipse.jetty.client.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
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
        GameState gameState = new TetrisGameState(plots, type, x, y, futureFigures);

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


    private class TetrisGameState implements GameState {

        private final List<Plot> plots;
        private final Figure.Type type;
        private final int x;
        private final int y;
        private final List<Figure.Type> futureFigures;

        public TetrisGameState(List<Plot> plots, Figure.Type type, int x, int y, List<Figure.Type> futureFigures) {
            this.plots = plots;
            this.type = type;
            this.x = x;
            this.y = y;
            this.futureFigures = futureFigures;
        }

        @Override
        public String asString() {
            StringBuilder content = new StringBuilder();
            StringBuilder sb = exportGlassState(plots);

            try {
                content.append("figure=").append(type).append("&x=").append(x).append("&y=").append(y).append("&glass=").append(URLEncoder.encode(sb.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            if (futureFigures != null && !futureFigures.isEmpty()) {
                content.append("&next=");
                for (Figure.Type futureFigure : futureFigures) {
                    content.append(futureFigure.getName());
                }
            }
            return content.toString();
        }
    }

    private StringBuilder exportGlassState(List<Plot> plots) {
        char[][] glassState = new char[TetrisGame.GLASS_HEIGHT][TetrisGame.GLASS_WIDTH];
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            Arrays.fill(glassState[i], ' ');
        }

        for (Plot plot : plots) {
            glassState[plot.getY()][plot.getX()] = '*';
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TetrisGame.GLASS_HEIGHT; i++) {
            sb.append(glassState[i]);
        }
        return sb;
    }

}
