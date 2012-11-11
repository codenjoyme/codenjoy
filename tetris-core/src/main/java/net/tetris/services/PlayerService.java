package net.tetris.services;

import net.tetris.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
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
    private List<TContext> playerContexts = new ArrayList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private boolean sendScreenUpdates = true;


    public Player addNewPlayer(final String name, final String callbackUrl, TContext context) {
        lock.writeLock().lock();
        try {
            FigureQueue playerQueue = createFiguresQueue(context);
            Levels levels = createLevels(playerQueue);

            int minScore = getPlayersInitialScore();
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
            playerContexts.add(context);
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

    protected int getPlayersInitialScore() {
        int result = 0;
        for (Player player : players) {
            result = Math.min(player.getScore(), result);
        }
        return result;
    }

    public void nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < games.size(); i++) {
                TetrisGame game = games.get(i);
                game.nextStep();
                logger.debug("Next step. {} - {} ({},{})", new Object[]{players.get(i).getName(), game.getCurrentFigureType(), game.getCurrentFigureX(), game.getCurrentFigureY()});
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
                TetrisGame game = games.get(i);
            }

            if (sendScreenUpdates && screenSender != null && !map.isEmpty()) {
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
            LinkedList<TContext> contextsCopy = new LinkedList<>(playerContexts);
            LinkedList<Player> playersCopy = new LinkedList<>(players);
            Iterator<TContext> it = contextsCopy.iterator();
            for (Player player : playersCopy) {
                afterStep(player, it.next());
            }
        } catch (Throwable t) {
            logger.error("Unexpected exception in PlayerService.nextStepForAllGames", t);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void afterStep(Player player, TContext context) {
    }

    public void setSendScreenUpdates(boolean sendScreenUpdates) {
        this.sendScreenUpdates = sendScreenUpdates;
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
            scores.clear();
            playerControllers.clear();
            playerContexts.clear();
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
            playerContexts.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

}
