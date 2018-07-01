package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.multiplayer.MultiplayerService;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.screen.ScreenSender;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerServiceImpl implements PlayerService {

    private static Logger logger = DLoggerFactory.getLogger(PlayerServiceImpl.class);
    public static String BOT_EMAIL_SUFFIX = "-super-ai@codenjoy.com";

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Map<Player, String> cacheBoards = new HashMap<Player, String>();
    private boolean registration = true;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Autowired
    private PlayerGames playerGames;

    @Autowired
    private MultiplayerService multiplayer;

    @Autowired
    private ScreenSender<ScreenRecipient, ScreenData> screenSender;

    @Autowired
    @Qualifier("playerController")
    private PlayerController playerController;

    @Autowired
    @Qualifier("screenController")
    private PlayerController screenController;

    @Autowired
    private GameService gameService;

    @Autowired
    private AutoSaver autoSaver;

    @Autowired
    private ActionLogger actionLogger;

    @Value("${autoSaverEnable}")
    private boolean autoSaverEnable;

    @Override
    public Player register(String name, String callbackUrl, String gameName) {
        lock.writeLock().lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Registered user {} in game {}", name, gameName);
            }

            if (!registration) {
                return NullPlayer.INSTANCE;
            }

            registerAIFor(name, gameName);

            Player player = register(new PlayerSave(name, callbackUrl, gameName, 0, null));

            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void reloadAI(String name) {
        lock.writeLock().lock();
        try {
            Player player = get(name);
            playerGames.remove(player);
            registerAI(player.getGameName(), player.getGameType(), name);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void registerAIFor(String forPlayer, String gameName) {
        if (forPlayer.endsWith(BOT_EMAIL_SUFFIX)) return;

        GameType gameType = gameService.getGame(gameName);

        // если в эту игру ai еще не играет
        String aiName = gameName + BOT_EMAIL_SUFFIX;
        PlayerGame playerGame = playerGames.get(aiName);

        if (playerGame instanceof NullPlayerGame) {
            registerAI(gameName, gameType, aiName);
        }
    }

    private void registerAI(String gameName, GameType gameType, String aiName) {
        if (gameType.newAI(aiName)) {
            Player player = register(PlayerSave.get(aiName, "127.0.0.1", gameName, 0, null));
        }
    }

    @Override
    public Player register(PlayerSave save) {
        String name = save.getName();
        String gameName = save.getGameName();

        GameType gameType = gameService.getGame(gameName);
        if (name.endsWith(BOT_EMAIL_SUFFIX)) {
            gameType.newAI(name);
        }

        Player player = get(save.getName());

        boolean newPlayer = (player instanceof NullPlayer) || !gameName.equals(player.getGameName());
        if (newPlayer) {
            playerGames.remove(player);

            // TODO .playerWantsToPlay(name, callbackUrl, score, data, gameType);

            PlayerGame playerGame = playerWantsToPlay(gameType, save);

            player = playerGame.getPlayer();

            if (logger.isDebugEnabled()) {
                logger.info("Player {} starting new game {}", save.getName(), playerGame.getGame());
            }
        } else {
          // do nothing
        }

        return player;
    }

    private PlayerGame playerWantsToPlay(GameType gameType, PlayerSave save) {
        PlayerScores playerScores = gameType.getPlayerScores(save.getScore());
        InformationCollector informationCollector = new InformationCollector(playerScores);

        Player player = new Player(save.getName(), save.getCallbackUrl(),
                gameType, playerScores, informationCollector);

        Game game = gameType.newGame(informationCollector, printer, save.getSave(), save.getName());
        return playerGames.add(player, game, playerController, screenController);
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("==================================================================================");
                logger.info("PlayerService.tick() starts");
            }

            long time = System.currentTimeMillis();

            if (autoSaverEnable) {
                autoSaver.tick();
            }

            playerGames.tick();
            sendScreenUpdates();
            requestControls();
            actionLogger.log(playerGames);

            if (logger.isDebugEnabled()) {
                time = System.currentTimeMillis() - time;
                logger.debug("PlayerService.tick() for all {} games is {} ms",
                        playerGames.size(), time);
            }

            if (playerGames.isEmpty()) {
                return;
            }
        } catch (Error e) {
            e.printStackTrace();
            logger.error("PlayerService.tick() throws", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void requestControls() {
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            try {
                String board = cacheBoards.get(player);
                playerController.requestControl(player, board);
            } catch (IOException e) {
                logger.error("Unable to send control request to player " + player.getName() +
                        " URL: " + player.getCallbackUrl(), e);
            }
        }
    }

    private void sendScreenUpdates() {
        Map<ScreenRecipient, ScreenData> map = buildScreenData();
        sendScreenForAsync(map);
        sendScreenForWebSockets(map);
    }

    private Map<ScreenRecipient, ScreenData> buildScreenData() {
        Map<ScreenRecipient, ScreenData> map = new HashMap<>();
        cacheBoards.clear();

        Map<String, GameData> gameDataMap = playerGames.getGamesDataMap();
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            Player player = playerGame.getPlayer();
            try {
                GameType gameType = player.getGameType();
                GameData gameData = gameDataMap.get(gameType.name());

                // TODO вот например для бомбера всем отдаются одни и те же борды, отличие только в паре спрайтов
                Object board = game.getBoardAsString(); // TODO дольше всего строчка выполняется, прооптимизировать!

                GuiPlotColorDecoder decoder = gameData.getDecoder();
                cacheBoards.put(player, decoder.encodeForClient(board));
                Object encoded = decoder.encodeForBrowser(board);

                map.put(player, new PlayerData(gameData.getBoardSize(),
                        encoded,
                        gameType.name(),
                        player.getScore(),
                        game.getMaxScore(),
                        game.getCurrentScore(),
                        player.getMessage(),
                        gameData.getScores(),
                        gameData.getHeroesData()));
            } catch (Exception e) {
                logger.error("Unable to send screen updates to player " + player.getName() +
                        " URL: " + player.getCallbackUrl(), e);
                e.printStackTrace();
            }
        }

        return map;
    }

    private void sendScreenForAsync(Map<ScreenRecipient, ScreenData> map) {
        screenSender.sendUpdates(map);
    }

    private void sendScreenForWebSockets(Map<ScreenRecipient, ScreenData> map) {
        try {
            screenController.requestControlToAll(map);
        } catch (Exception e) {
            logger.error("Unable to send screen updates to all players", e);
            e.printStackTrace();
        }
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
    public List<Player> getAll(String gameName) {
        lock.writeLock().lock();
        try {
            return private_getAll(gameName);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<Player> private_getAll(String gameName) {
        List<Player> result = new LinkedList<Player>();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            if (player.getGameName().equals(gameName)) {
                result.add(player);
            }
        }
        return result;
    }

    @Override
    public void remove(String name) {
        lock.writeLock().lock();
        try {
            Player player = get(name);

            if (logger.isDebugEnabled()) {
                logger.debug("Unregistered user {} from game {}",
                        player.getName(), player.getGameName());
            }

            playerGames.remove(player);
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
            return get(name) != NullPlayer.INSTANCE;
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
    public void closeRegistration() {
        registration = false;
    }

    @Override
    public boolean isRegistrationOpened() {
        return registration;
    }

    @Override
    public void openRegistration() {
        registration = true;
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
    public Player getRandom(String gameType) {
        lock.readLock().lock();
        try {
            if (playerGames.isEmpty()) return NullPlayer.INSTANCE;

            if (gameType == null) {
                return playerGames.iterator().next().getPlayer();
            }

            Iterator<Player> iterator = private_getAll(gameType).iterator();
            if (!iterator.hasNext()) return NullPlayer.INSTANCE;
            return iterator.next();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public GameType getAnyGameWithPlayers() {
        lock.readLock().lock();
        try {
            if (playerGames.isEmpty()) return NullGameType.INSTANCE;

            return playerGames.iterator().next().getPlayer().getGameType();
        } finally {
            lock.readLock().unlock();
        }
    }
}
