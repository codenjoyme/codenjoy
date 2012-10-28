package net.tetris.online.service;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.Levels;
import net.tetris.services.*;
import net.tetris.services.levels.AllFigureLevels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:17 PM
 */
@Service
public class ReplayService extends PlayerService<GameLogFile> {
    @Autowired
    private ServiceConfiguration configuration;

    public void replay(String playerName, String timestamp) {

        GameLogFile gameLogFile = null;
        try {
            gameLogFile = new GameLogFile(configuration, playerName, timestamp);
            Player player = addNewPlayer(playerName, "REPLAY", gameLogFile);

            while (gameLogFile.readNextStep()) {
                nextStepForAllGames();
            }
        } finally {
            if (gameLogFile != null) {
                gameLogFile.close();
            }
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
