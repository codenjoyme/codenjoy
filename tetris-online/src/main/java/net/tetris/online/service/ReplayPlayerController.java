package net.tetris.online.service;

import net.tetris.dom.Figure;
import net.tetris.dom.Joystick;
import net.tetris.online.service.GameLogFile;
import net.tetris.services.Player;
import net.tetris.services.PlayerCommand;
import net.tetris.services.PlayerController;
import net.tetris.services.Plot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    public void requestControl(Player player, Figure.Type type, int x, int y, Joystick joystick, List<Plot> plots) throws IOException {
        new PlayerCommand(joystick, logFile.getCurrentResponse(), player).execute();
        logger.debug("Executed player command: {}", logFile.getCurrentResponse());
    }
}
