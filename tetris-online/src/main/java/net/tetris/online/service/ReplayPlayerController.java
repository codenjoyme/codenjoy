package net.tetris.online.service;

import com.codenjoy.dojo.tetris.model.Figure;
import net.tetris.dom.TetrisJoystik;
import net.tetris.services.Player;
import net.tetris.services.PlayerCommand;
import net.tetris.services.PlayerController;
import com.codenjoy.dojo.tetris.model.Plot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:46 PM
 */
public class ReplayPlayerController implements PlayerController {
    private static Logger logger = LoggerFactory.getLogger(ReplayPlayerController.class);

    private GameLogFile logFile;

    public ReplayPlayerController(GameLogFile logFile) {
        this.logFile = logFile;
    }

    @Override
    public void requestControl(Player player, Figure.Type type, int x, int y, TetrisJoystik joystick, List<Plot> plots, List<Figure.Type> futureFigures) throws IOException {
        new PlayerCommand(joystick, logFile.getCurrentResponse(), player).execute();
        logger.debug("Executed player command: {}", logFile.getCurrentResponse());
    }

    @Override
    public void registerPlayerTransport(Player player, TetrisJoystik joystick) {
    }

    @Override
    public void unregisterPlayerTransport(Player player) {
    }
}
