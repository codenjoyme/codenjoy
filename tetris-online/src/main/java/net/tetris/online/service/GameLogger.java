package net.tetris.online.service;

import net.tetris.services.Player;
import net.tetris.services.PlayerControllerListener;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * User: serhiy.zelenin
 * Date: 10/24/12
 * Time: 3:36 PM
 */
@Component
public class GameLogger implements PlayerControllerListener {
    private static Logger logger = LoggerFactory.getLogger(GameLogger.class);

    @Autowired
    private ServiceConfiguration configuration;
    private Player player;
    private String timeStamp;
    private File playerLogsDir;
    private File logFile;
    private PrintWriter printWriter;
    private GameLogFile gameLogFile;

    public GameLogger() {
    }

    public GameLogger(ServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start(Player player, String timeStamp) {
        this.player = player;
        this.timeStamp = timeStamp;
        if (gameLogFile != null) {
            gameLogFile.close();
        }
        gameLogFile = new GameLogFile(configuration, player.getName(), timeStamp);
    }

    @Override
    public void log(Player player, String request, String response) {
        gameLogFile.log(request, response);
    }

    public void stop() {
        gameLogFile.close();
    }
}
