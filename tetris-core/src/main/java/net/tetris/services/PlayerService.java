package net.tetris.services;

import net.tetris.dom.*;
import net.tetris.services.levels.LevelsFactory;
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

    @Autowired
    private GameSaver saver;

    private List<Player> players = new ArrayList<>();
    private List<Glass> glasses = new ArrayList<>();
    private List<TetrisGame> games = new ArrayList<>();
    private List<GlassEventListener> scores = new ArrayList<>();
    private List<PlayerController> playerControllers = new ArrayList<>();
    private List<TContext> playerContexts = new ArrayList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private boolean sendScreenUpdates = true;

    public void savePlayerGame(String name) {
        lock.readLock().lock();
        try {
            Player player = getPlayer(name);
            if (player != null) {
                saver.saveGame(player);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void loadPlayerGame(String name) {
        lock.writeLock().lock();
        try {
            Player.PlayerBuilder builder = saver.loadGame(name);
            if (builder != null) {
                register(builder, null);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Player register(Player.PlayerBuilder builder, TContext context) {
        Player currentPlayer = getPlayer(builder.getPlayer().getName());

        int index = players.size();
        if (currentPlayer != null) {
            index = players.indexOf(currentPlayer);
            removePlayer(currentPlayer);
        }

        InformationCollector infoCollector = builder.getInformationCollector();
        FigureQueue playerQueue = builder.getPlayerQueue();
        PlayerScores playerScores = builder.getPlayerScores();
        Levels levels = builder.getLevels();

        TetrisGlass glass = new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT, infoCollector, levels);
        final TetrisGame game = new TetrisGame(playerQueue, glass);

        players.add(index, builder.getPlayer());
        glasses.add(index, glass);
        games.add(index, game);
        scores.add(index, playerScores);
        PlayerController controller = createPlayerController(context);
        playerControllers.add(index, controller);
        playerContexts.add(index, context);
        controller.registerPlayerTransport(builder.getPlayer(), game);
        return builder.getPlayer();
    }

    public Player addNewPlayer(final String name, final String callbackUrl, TContext context) {
        lock.writeLock().lock();
        try {
            Player.PlayerBuilder player = new Player.PlayerBuilder();
            player.setName(name);
            player.setCallbackUrl(callbackUrl);
            player.setScores(getPlayersInitialScore());

            FigureQueue queue = createFiguresQueue(context);
            player.forLevels(queue, createLevels(queue));

            return register(player, context);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected PlayerController createPlayerController(TContext context) {
        return playerController;
    }

    protected Levels createLevels(FigureQueue queue) {
        return new LevelsFactory().getGameLevels(queue, gameSettings.getCurrentGameLevels());
    }

    protected FigureQueue createFiguresQueue(TContext context) {
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
                        player.getNextLevelIngoingCriteria(),
                        player.getCurrentLevelNumber() + 1, player.getMessage()));
                droppedPlotsMap.put(player, droppedPlots);
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
                            game.getCurrentFigureY(), game, droppedPlotsMap.get(player), game.getFutureFigures());
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
            return getPlayer(playerName);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
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

    public void updatePlayers(List<PlayerInfo> players) {
        lock.writeLock().lock();
        try {   if (players == null) {return;}
            Iterator<PlayerInfo> iterator = players.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (player.getName() == null) {
                    iterator.remove();
                }
            }

            if (this.players.size() != players.size()) {
                throw new IllegalArgumentException("Diff players count");
            }

            for (int index = 0; index < players.size(); index ++) {
                Player playerToUpdate = this.players.get(index);
                Player newPlayer = players.get(index);

                playerToUpdate.setCallbackUrl(newPlayer.getCallbackUrl());
                playerToUpdate.setName(newPlayer.getName());
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

    public void removePlayerByIp(String ip) {
        lock.writeLock().lock();
        try {
            removePlayer(findPlayerByIp(ip));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removePlayerByName(String name) {
        lock.writeLock().lock();
        try {
            removePlayer(findPlayer(name));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void removePlayer(Player player) {
        int index = players.indexOf(player);
        if (index < 0) return;
        players.remove(index);
        glasses.remove(index);
        games.remove(index);
        scores.remove(index);
        PlayerController controller = playerControllers.remove(index);
        if (controller != null) {
            controller.unregisterPlayerTransport(player);
        }
        playerContexts.remove(index);
    }

    public List<PlayerInfo> getPlayersGames() {
        List<PlayerInfo> result = new LinkedList<>();
        for (Player player : players) {
            result.add(new PlayerInfo(player));
        }

        List<String> savedList = saver.getSavedList();
        for (String name : savedList) {
            boolean notFound = true;
            for (PlayerInfo player : result) {
                if (player.getName().equals(name)) {
                    player.setSaved(true);
                    notFound = false;
                }
            }

            if (notFound) {
                result.add(new PlayerInfo(name, true));
            }
        }

        Collections.sort(result, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo o1, PlayerInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return result;
    }
}
