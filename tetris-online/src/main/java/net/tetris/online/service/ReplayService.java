package net.tetris.online.service;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.Levels;
import net.tetris.services.*;
import net.tetris.services.levels.AllFigureLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:17 PM
 */
@Service
public class ReplayService extends PlayerService<GameLogFile> {
    private static Logger logger = LoggerFactory.getLogger(ReplayService.class);
    @Autowired
    private ServiceConfiguration configuration;

    private AtomicInteger inc = new AtomicInteger();

    public void replay(String playerName, String timestamp) {
        logger.info("Start replay for : {}, timestamp: {} ", playerName, timestamp);
        Player player = null;
        GameLogFile gameLogFile = null;
        try {
            gameLogFile = new GameLogFile(configuration, playerName, timestamp);
            if (!gameLogFile.readNextStep()) {
                return;
            }
            player = addNewPlayer(playerName, "REPLAY"+inc.incrementAndGet(), gameLogFile);

            do {
                nextStepForAllGames();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            } while (gameLogFile.readNextStep());
        } finally {
            if (gameLogFile != null) {
                gameLogFile.close();
            }
            if (player != null) {
                removePlayer(player.getCallbackUrl());
            }
            logger.info("Replay for : {}, timestamp: {} finished", playerName, timestamp);
        }
    }

    @Override
    protected Levels createLevels(FigureQueue playerQueue) {
        //well, this seems odd. We need a specific FigureQueue instance here which will be used only for score calculation
        return new AllFigureLevels(new PlayerFigures());
    }

    @Override
    protected FigureQueue createFiguresQueue(final GameLogFile gameLogFile) {
        return new FigureQueue() {
            @Override
            public Figure next() {
                return gameLogFile.getCurrentFigure().createNewFigure();
            }
        };
    }

    @Override
    protected PlayerController createPlayerController(GameLogFile gameLogFile) {
        return new ReplayPlayerController(gameLogFile);
    }
}
