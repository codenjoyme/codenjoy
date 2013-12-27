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

    private PlayerGames playerGames = new PlayerGames();

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
            return register(new Player.PlayerBuilder(name, password, callbackUrl, 0, Protocol.WS.name()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Player register(Player.PlayerBuilder playerBuilder) {
        Player player = playerBuilder.getPlayer(gameService.getSelectedGame());

        playerGames.remove(get(player.getName()));

        PlayerController controller = playerControllerFactory.get(player.getProtocol());
        Game game = playerBuilder.getGame();
        playerGames.add(player, game, controller);

        return player;
    }

    private void tickGames(PlayerGames playerGames, boolean singleBoard) { // TODO вот блин )
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            if (game.isGameOver()) {
                game.newGame();
            }
            if (!singleBoard) {
                game.tick();
            }
        }
        if (!playerGames.isEmpty() && singleBoard) {
            playerGames.iterator().next().getGame().tick();
        }
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            autoSaver.tick();

            if (playerGames.isEmpty()) {
                return;
            }

            tickGames(playerGames, gameService.getSelectedGame().isSingleBoardGame());

            HashMap<ScreenRecipient, PlayerData> map = new HashMap<ScreenRecipient, PlayerData>();

            String chatLog = chatService.getChatLog();
            int boardSize = gameService.getSelectedGame().getBoardSize().getValue();

            String scores = getScoresJSON();

            for (PlayerGame playerGame : playerGames) {
                Game game = playerGame.getGame();
                Player player = playerGame.getPlayer();

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

            for (PlayerGame playerGame : playerGames) {
                Game game = playerGame.getGame();
                Player player = playerGame.getPlayer();
                PlayerController controller = playerGame.getController();

                try {
                    String board = game.getBoardAsString().replace("\n", "");

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Sent for player '%s' board \n%s", player, board));
                    }

                    controller.requestControl(player, board);
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

    private String getScoresJSON() {
        JSONObject scores = new JSONObject();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();

            scores.put(player.getName(), player.getScore());
        }
        return scores.toString();
    }

    @Override
    public List<Player> getAll() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(playerGames.players());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(String name) {
        lock.writeLock().lock();
        try {
            playerGames.remove(get(name));
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

            if (playerGames.size() != players.size()) {
                throw new IllegalArgumentException("Diff players count");
            }

            for (int index = 0; index < playerGames.size(); index ++) {
                Player playerToUpdate = playerGames.players().get(index);
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
            return get(name) != Player.NULL;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Player get(String name) {
        lock.readLock().lock();
        try {
            return playerGames.get(name).getPlayer();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void removeAll() {
        lock.writeLock().lock();
        try {
            playerGames.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    void clean() {  // TODO для тестов
        playerGames = new PlayerGames();
    }

    @Override
    public Joystick getJoystick(String name) {
        lock.writeLock().lock();
        try {
            return playerGames.get(name).getGame().getJoystick();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void cleanAllScores() {
        lock.writeLock().lock();
        try {
            for (PlayerGame playerGame : playerGames) {
                Game game = playerGame.getGame();
                Player player = playerGame.getPlayer();

                player.clearScore();

                game.newGame();
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
            for (Player player : playerGames.players()) {
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
            if (playerGames.isEmpty()) return null;

            if (get("apofig") != Player.NULL) return "apofig";
            if (get("admin") != Player.NULL) return "admin";

            return playerGames.iterator().next().getPlayer().getName();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean login(String name, String password) {
        lock.readLock().lock();
        try {
            return get(name).itsMe(password);
        } finally {
            lock.readLock().unlock();
        }
    }


}
