package net.tetris.services;

import net.tetris.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerService <TContext> {
    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private GameSettings gameSettings;

    private List<Player> players = new ArrayList<>();
    private List<Glass> glasses = new ArrayList<>();
    private List<TetrisGame> games = new ArrayList<>();
    private List<GlassEventListener> scores = new ArrayList<>();
    private List<PlayerController> playerControllers = new ArrayList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);


    public Player addNewPlayer(final String name, final String callbackUrl, TContext context) {
        lock.writeLock().lock();
        try {
            FigureQueue playerQueue = createFiguresQueue(context);
            Levels levels = createLevels(playerQueue);

            int minScore = getPlayersMinScore();
            PlayerScores playerScores = new PlayerScores(minScore);
            levels.setChangeLevelListener(playerScores);

            TetrisGlass glass = new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT, playerScores, levels);
            final TetrisGame game = new TetrisGame(playerQueue, glass);
            Player player = new Player(name, callbackUrl, playerScores, levels);
            players.add(player);
            glasses.add(glass);
            games.add(game);
            scores.add(playerScores);
            playerControllers.add(createPlayerController(context));
            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected PlayerController createPlayerController(TContext context) {
        return playerController;
    }

    protected Levels createLevels(FigureQueue playerQueue) {
        return gameSettings.getGameLevels(playerQueue);
    }

    protected FigureQueue createFiguresQueue(TContext param) {
        return new PlayerFigures();
    }

    private int getPlayersMinScore() {
        int result = 0;
        for (Player player : players) {
            result = Math.min(player.getScore(), result);
        }
        return result;
    }

    public void nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            for (TetrisGame game : games) {
                game.nextStep();
            }

            HashMap<Player, PlayerData> map = new HashMap<>();
            HashMap<Player, List<Plot>> droppedPlotsMap = new HashMap<>();
            for (int i = 0; i < glasses.size(); i++) {
                Glass glass = glasses.get(i);
                ArrayList<Plot> allPlots = new ArrayList<>();
                allPlots.addAll(glass.getCurrentFigurePlots());
                List<Plot> droppedPlots = glass.getDroppedPlots();
                allPlots.addAll(droppedPlots);
                Player player = players.get(i);

                map.put(player, new PlayerData(allPlots, player.getScore(),
                        player.getTotalRemovedLines(),
                        player.getNextLevel().getNextLevelIngoingCriteria(),
                        player.getCurrentLevel() + 1));
                droppedPlotsMap.put(player, droppedPlots);
            }

            if (screenSender != null) {
                screenSender.sendUpdates(map);
            }

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                TetrisGame game = games.get(i);
                try {
                    if (game.getCurrentFigureType() == null) {
                        continue;
                    }
                    playerControllers.get(i).requestControl(player, game.getCurrentFigureType(), game.getCurrentFigureX(),
                            game.getCurrentFigureY(), game, droppedPlotsMap.get(player));
                } catch (IOException e) {
                    logger.error("Unable to send control request to player " + player.getName() +
                            " URL: " + player.getCallbackUrl(), e);
                }
            }
        } catch (Throwable t) {
            logger.error("Unexpected exception in PlayerService.nextStepForAllGames", t);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public List<Player> getPlayers() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(players);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean alreadyRegistered(String playerName) {
        lock.readLock().lock();
        try {
            return findPlayer(playerName) != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Player findPlayer(String playerName) {
        lock.readLock().lock();
        try {
            for (Player player : players) {
                if (player.getName().equals(playerName)) {
                    return player;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updatePlayer(Player player) {
        lock.writeLock().lock();
        try {
            for (Player playerToUpdate : players) {
                if (playerToUpdate.getName().equals(player.getName())) {
                    playerToUpdate.setCallbackUrl(player.getCallbackUrl());
                    return;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            players.clear();
            games.clear();
            glasses.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    List<Glass> getGlasses() {
        return glasses;
    }

    public Player findPlayerByIp(String ip) {
        lock.readLock().lock();
        try {
            for (Player player : players) {
                if (player.getCallbackUrl().contains(ip)) {
                    return player;
                }
            }
            return new NullPlayer();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void removePlayer(String ip) {
        lock.writeLock().lock();
        try {
            int index = players.indexOf(findPlayerByIp(ip));
            if (index < 0) return;
            players.remove(index);
            glasses.remove(index);
            games.remove(index);
            scores.remove(index);
            playerControllers.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

}
