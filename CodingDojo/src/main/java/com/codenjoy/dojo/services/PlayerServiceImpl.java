package com.codenjoy.dojo.services;

import com.codenjoy.dojo.bomberman.services.BombermanGame;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 6:48 AM
 */
@Component("playerService")
public class PlayerServiceImpl implements PlayerService {
    private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    private List<Player> players = new ArrayList<Player>();
    private List<Game> games = new ArrayList<Game>();

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    private GameType gameType = new BombermanGame();
//    private GameType gameType = new SnakeGame();

    // for testing
    void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public Player addNewPlayer(final String name, final String callbackUrl) {
        lock.writeLock().lock();
        try {
            return register(new PlayerInfo(name, callbackUrl));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void removePlayer(Player player) {
        int index = players.indexOf(player);
        if (index < 0) return;
        players.remove(index);
        games.remove(index);
    }

    private Player register(PlayerInfo playerInfo) {
        Player currentPlayer = getPlayer(playerInfo.getName());

        int index = players.size();
        if (currentPlayer != null) {
            index = players.indexOf(currentPlayer);
            removePlayer(currentPlayer);
        }

        int minScore = getPlayersMinScore();
        final PlayerScores playerScores = gameType.getPlayerScores(minScore);
        final InformationCollector informationCollector = new InformationCollector(playerScores);

        Game game = gameType.newGame(informationCollector);

        Player player = new Player(playerInfo.getName(), playerInfo.getCallbackUrl(), playerScores, informationCollector);
        players.add(player);
        games.add(game);

        return player;
    }

    private int getPlayersMinScore() {
        int result = 0;
        for (Player player : players) {
            result = Math.min(player.getScore(), result);
        }
        return result;
    }

    @Override
    public void nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            for (Game game : games) {
                if (game.isGameOver()) {
                    game.newGame();
                }
                game.tick();
            }

            HashMap<Player, PlayerData> map = new HashMap<Player, PlayerData>();
            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);

                Player player = players.get(i);

                map.put(player, new PlayerData(gameType.getBoardSize(),
                        game.getPlots(),
                        player.getScore(),
                        game.getMaxScore(),
                        game.getCurrentScore(),
                        player.getCurrentLevel() + 1,
                        player.getMessage()));
            }

            screenSender.sendUpdates(map);

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                Game game = games.get(i);
                try {
                    playerController.requestControl(player, game.getJoystick(), game.getBoardAsString());
                } catch (IOException e) {
                    logger.error("Unable to send control request to player " + player.getName() +
                            " URL: " + player.getCallbackUrl(), e);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void savePlayerGame(String name) {
        lock.readLock().lock();
        try {
            Player player = getPlayer(name);
            if (player != null) {
//                saver.saveGame(player); // TODO implement me
            }
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

    @Override
    public void loadPlayerGame(String name) {
        lock.writeLock().lock();
        try {
//            Player.PlayerBuilder builder = saver.loadGame(name); // TODO implement me
//            if (builder != null) {
//                register(builder, null);
//            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Player> getPlayers() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(players);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void removePlayerByName(String name) {
        lock.writeLock().lock();
        try {
            removePlayer(findPlayer(name));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
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

    @Override
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

    @Override
    public boolean alreadyRegistered(String playerName) {
        lock.readLock().lock();
        try {
            return findPlayer(playerName) != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
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

    @Override
    public void removeAll() {
        lock.writeLock().lock();
        try {
            players.clear();
            games.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<Game> getBoards() {
        return games;
    }

    @Override
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

    @Override
    public void removePlayerByIp(String ip) {
        lock.writeLock().lock();
        try {
            int index = players.indexOf(findPlayerByIp(ip));
            if (index < 0) return;
            players.remove(index);
            games.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getBoardSize() {
        return gameType.getBoardSize();
    }

    @Override
    public List<PlayerInfo> getPlayersGames() {
        List<PlayerInfo> result = new LinkedList<PlayerInfo>();
        for (Player player : players) {
            result.add(new PlayerInfo(player));
        }

//        List<String> savedList = saver.getSavedList();  // TODO implement me
        List<String> savedList = new LinkedList<String>();
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
