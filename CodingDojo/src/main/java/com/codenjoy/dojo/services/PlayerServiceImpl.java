package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.screen.ScreenSender;
import org.json.JSONObject;
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

    private List<Player> players = new LinkedList<Player>();
    private List<Game> games = new ArrayList<Game>();
    private List<PlayerController> controllers = new ArrayList<PlayerController>();

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Autowired private ScreenSender<ScreenRecipient, PlayerData> screenSender;
    @Autowired private PlayerControllerFactory playerControllerFactory;
    @Autowired private GameService gameService;
    @Autowired private ChatService chatService;
    @Autowired private AutoSaver autoSaver;

    @Override
    public Player register(String name, String password, String callbackUrl) {
        lock.writeLock().lock();
        try {
            return register(new Player.PlayerBuilder(name, password, callbackUrl, getPlayersMinScore(), Protocol.WS.name()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void removePlayer(Player player) {
        int index = players.indexOf(player);
        if (index < 0) return;
        players.remove(index);
        Game game = games.remove(index);
        removeController(player, index);
        game.destroy();
    }

    private void removeController(Player player, int index) {
        PlayerController controller = controllers.remove(index);
        if (controller != null) {
            controller.unregisterPlayerTransport(player);
        }
    }

    @Override
    public Player register(Player.PlayerBuilder playerBuilder) {
        Player player = playerBuilder.getPlayer(gameService.getSelectedGame());

        Player currentPlayer = getPlayer(player.getName());

        if (currentPlayer != null) {
            players.indexOf(currentPlayer);
            removePlayer(currentPlayer);
        }

        players.add(player);
        Game game = playerBuilder.getGame();
        games.add(game);

        createController(player, game);

        return player;
    }

    private void createController(Player player, Game game) {
        PlayerController controller = playerControllerFactory.get(player.getProtocol());
        controllers.add(controller);
        controller.registerPlayerTransport(player, game.getJoystick());
    }

    private int getPlayersMinScore() {
        int result = 0;
        for (Player player : players) {
            result = Math.min(player.getScore(), result);
        }
        return result;
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            autoSaver.tick();

            if (games.size() == 0 || players.size() == 0) {
                return;
            }

            processGames();

            HashMap<ScreenRecipient, PlayerData> map = new HashMap<ScreenRecipient, PlayerData>();

            String chatLog = chatService.getChatLog();
            int boardSize = gameService.getSelectedGame().getBoardSize().getValue();

            String scores = getScoresJSON();

            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                Player player = players.get(i);

                // TODO передавать размер поля (и чат) не каждому плееру отдельно, а всем сразу
                map.put(player, new PlayerData(boardSize,
                        gameService.getDecoder().encode(game.getBoardAsString()),
                        player.getScore(),
                        game.getMaxScore(),
                        game.getCurrentScore(),
                        player.getCurrentLevel() + 1,
                        player.getMessage(),
                        chatLog,
                        scores));
            }

            screenSender.sendUpdates(map);

            for (int index = 0; index < players.size(); index++) {
                Player player = players.get(index);
                Game game = games.get(index);
                try {
                    String board = game.getBoardAsString().replace("\n", "");

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Sent for player '%s' board \n%s", player, board));
                    }

                    controllers.get(index).requestControl(player, board);
                } catch (IOException e) {
                    logger.error("Unable to send control request to player " + player.getName() +
                            " URL: " + player.getCallbackUrl(), e);
                }
            }
        } catch (Error e) {
            e.printStackTrace();
            logger.error("nextStepForAllGames throws", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void processGames() {
        boolean singleBoard = gameService.getSelectedGame().isSingleBoardGame();
        for (Game game : games) {
            if (game.isGameOver()) {
                game.newGame();
            }
            if (!singleBoard) {
                game.tick();
            }
        }
        if (singleBoard && games.size() > 0) {
            games.get(0).tick();
        }
    }

    private String getScoresJSON() {
        JSONObject scores = new JSONObject();
        for (int i = 0; i < games.size(); i++) {
            Player player = players.get(i);
            Game game = games.get(i);

            scores.put(player.getName(), player.getScore());
        }
        return scores.toString();
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
    public List<Player> getAll() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(players);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(String name) {
        lock.writeLock().lock();
        try {
            removePlayer(get(name));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Player player) {
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
    public void updateAll(List<PlayerInfo> players) {
        lock.writeLock().lock();
        try {
            if (players == null) {
                return;
            }
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
    public boolean contains(String name) {
        lock.readLock().lock();
        try {
            return get(name) != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Player get(String name) {
        lock.readLock().lock();
        try {
            for (Player player : players) {
                if (player.getName().equals(name)) {
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
            for (Player player : players.toArray(new Player[0])) {
                removePlayer(player);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    void clean() {  // TODO для тестов
        players.clear();
        controllers.clear();
        games.clear();
    }

    @Override
    public Player getByIp(String ip) {
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
    public void removeByIp(String ip) {
        lock.writeLock().lock();
        try {
            int index = players.indexOf(getByIp(ip));
            if (index < 0) return;
            players.remove(index);
            games.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Joystick getJoystick(String name) {
        lock.writeLock().lock();
        try {
            int index = players.indexOf(get(name));
            if (index < 0) {
                return new NullJoystick();
            }
            return games.get(index).getJoystick();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void cleanAllScores() {   // TODO test me
        lock.writeLock().lock();
        try {
            for (Player player : players) {
                player.clearScore();
            }
            for (Game game : games) {
                game.newGame();
            }
            for (Game game : games) {
                game.clearScore();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String getByCode(String code) {
        lock.readLock().lock();
        try {
            if (code == null) return null;
            for (Player player : players) {
                if (player.getCode().equals(code)) {
                    return player.getName();
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String getRandom() {
        lock.readLock().lock();
        try {
            if (players.size() == 0) return null;
            if (get("apofig") != null) return "apofig";
            if (get("admin") != null) return "admin";
            return players.iterator().next().getName();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean login(String name, String password) {
        lock.readLock().lock();
        try {
            Player player = get(name);
            return (player != null && player.itsMe(password));
        } finally {
            lock.readLock().unlock();
        }
    }


}
