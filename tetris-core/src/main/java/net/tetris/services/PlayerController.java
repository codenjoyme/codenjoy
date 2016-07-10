package net.tetris.services;

import com.codenjoy.dojo.tetris.model.Figure;
import net.tetris.dom.TetrisJoystik;

import java.io.IOException;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 6/3/12
 * Time: 10:40 PM
 */
public interface PlayerController {
    void requestControl(final Player player, Figure.Type type, int x, int y, final TetrisJoystik joystick, List<Plot> plots, List<Figure.Type> futureFigures) throws IOException;

    void registerPlayerTransport(Player player, TetrisJoystik joystick);

    void unregisterPlayerTransport(Player player);
}
