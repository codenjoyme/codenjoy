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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:17 PM
 */
@Service
public class ReplayService extends PlayerService<GameLogFile> implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ReplayService.class);
    @Autowired
    private ServiceConfiguration configuration;

    @Autowired
    private ScheduledThreadPoolExecutor replayExecutor;

    private AtomicInteger inc = new AtomicInteger();
    private List<GameLogFile> scheduledReplays = Collections.synchronizedList(new LinkedList<GameLogFile>());
    private Set<String> cancelRequests = Collections.synchronizedSet(new HashSet<String>());

    @PostConstruct
    public void init() {
        replayExecutor.scheduleAtFixedRate(this, 100, 200, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void destroy() {
        replayExecutor.shutdownNow();
    }

    public void replay(String playerName, String timestamp) {
        logger.info("Attempting to start replay for : {}, timestamp: {} ", playerName, timestamp);
        scheduledReplays.add(new GameLogFile(configuration, playerName, timestamp));
    }

    @Override
    public void run() {
        try {
            for (GameLogFile replayFile : scheduledReplays) {
                if (replayFile.readNextStep()) {
                    addNewPlayer(replayFile.getPlayerName(), "REPLAY" + inc.incrementAndGet(), replayFile);
                    logger.info("Replay for : {}, timestamp: {} started", replayFile.getPlayerName(), replayFile.getTimeStamp());
                } else {
                    logger.info("Replay impossible. Log file is empty for : {}, timestamp: {}. ",
                            replayFile.getPlayerName(), replayFile.getTimeStamp());
                }
            }
            scheduledReplays.clear();
        } catch (Throwable t) {
            logger.error("Not able to add new replays!", t);
        }
        nextStepForAllGames();
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

    //WARN: initial player score for replay is not tested!
    @Override
    protected int getPlayersInitialScore() {
        return 0;
    }

    @Override
    protected PlayerController createPlayerController(GameLogFile gameLogFile) {
        return new ReplayPlayerController(gameLogFile);
    }

    @Override
    protected void afterStep(Player player, GameLogFile gameLogFile) {
        if (cancelRequests.contains(player.getName() + gameLogFile.getTimeStamp())) {
            closePlayerGame(player, gameLogFile);
            logger.info("Replay for : {}, timestamp: {} is cancelled", player.getName(), gameLogFile.getTimeStamp());
            return;
        }
        if (gameLogFile.readNextStep()) {
            return;
        }
        closePlayerGame(player, gameLogFile);
        logger.info("Replay for : {}, timestamp: {} finished", player.getName(), gameLogFile.getTimeStamp());
    }

    private void closePlayerGame(Player player, GameLogFile gameLogFile) {
        gameLogFile.close();
        removePlayer(player.getCallbackUrl());
    }

    public boolean hasScheduledReplays() {
        return !scheduledReplays.isEmpty();
    }

    public void cancelReplay(String user, String timestamp) {
        cancelRequests.add(user + timestamp);
    }
}
