package net.tetris.online.service;

import net.tetris.services.GameSettings;
import net.tetris.services.Player;
import net.tetris.services.PlayerService;
import net.tetris.services.levels.AllFigureLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * User: serhiy.zelenin
 * Date: 10/10/12
 * Time: 8:33 PM
 */
@Service
public class GameExecutorService {
    private Logger logger = LoggerFactory.getLogger(GameExecutorService.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ServiceConfiguration configuration;

    @Autowired
    private GameSettings gameSettings;

    void runGame(String userName, File appFile) {
        WebApp webApp = new WebApp(appFile, configuration);
        int port = webApp.deploy();
        gameSettings.setGameLevels(AllFigureLevels.class.getName());
        try {
            String callbackUrl = "http://localhost:" + port + "/tetrisServlet";
            Player player = playerService.addNewPlayer(userName, callbackUrl);
            logger.info("Adding new player {} with url: {}", userName, callbackUrl);
            for (int i = 0; i < 10; i++) {
                playerService.nextStepForAllGames();
            }
            playerService.clear();
        } catch (Throwable t) {
            logger.error("Error while running app file: " + appFile.getAbsolutePath(), t);
        } finally {
            webApp.shutDown();
            logger.info("Application {} shut down", appFile.getAbsolutePath());
        }
    }
}
