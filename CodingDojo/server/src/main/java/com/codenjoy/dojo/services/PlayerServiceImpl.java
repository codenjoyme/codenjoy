
package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import org.fest.reflect.core.Reflection;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerServiceImpl implements PlayerService {

    private static Logger logger = DLoggerFactory.getLogger(PlayerServiceImpl.class);
    
    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Map<Player, String> cacheBoards = new HashMap<>();
    private boolean isRegOpened = true;

    @Autowired protected PlayerGames playerGames;
    @Autowired private PlayerGamesView playerGamesView;

    @Autowired
    @Qualifier("playerController")
    protected Controller playerController;

    @Autowired
    @Qualifier("screenController")
    protected Controller screenController;

    @Autowired protected GameService gameService;
    @Autowired protected AutoSaver autoSaver;
    @Autowired protected ActionLogger actionLogger;
    @Autowired protected Registration registration;

    @Value("${game.ai}")
    protected boolean isAINeeded;

    @PostConstruct
    public void init() {
        playerGames.init(lock);
        playerGames.onAdd(playerGame -> {
            Player player = playerGame.getPlayer();
            Joystick joystick = playerGame.getJoystick();
            playerController.registerPlayerTransport(player, joystick);
            screenController.registerPlayerTransport(player, null);
        });
        playerGames.onRemove(playerGame -> {
            Player player = playerGame.getPlayer();
            playerController.unregisterPlayerTransport(player);
            screenController.unregisterPlayerTransport(player);
        });
    }

    @Override
    public Player register(String name, String callbackUrl, String gameName) {
        lock.writeLock().lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Registered user {} in game {}", name, gameName);
            }

            if (!isRegOpened) {
                return NullPlayer.INSTANCE;
            }

            registerAIIfNeeded(name, gameName);

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
            Player player = getPlayer(name);
            registerAI(name, player.getGameType());
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void registerAIIfNeeded(String forPlayer, String gameName) {
        if (forPlayer.endsWith(WebSocketRunner.BOT_EMAIL_SUFFIX)) return;
        if (!isAINeeded) return;

        GameType gameType = gameService.getGame(gameName);

        // если в эту игру ai еще не играет
        String aiName = gameName + WebSocketRunner.BOT_EMAIL_SUFFIX;
        PlayerGame playerGame = playerGames.get(aiName);

        if (playerGame instanceof NullPlayerGame) {
            registerAI(aiName, gameType);
        }
    }

    private void registerAI(String aiName, GameType gameType) {
        Closeable ai = createAI(aiName, gameType);
        if (ai != null) {
            Player player = getPlayer(PlayerSave.get(aiName, "127.0.0.1", gameType.name(), 0, null), gameType);
            player.setAI(ai);
        }
    }

    @Override
    public Player register(PlayerSave playerSave) {
        lock.writeLock().lock();
        try {
            return justRegister(playerSave);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Player justRegister(PlayerSave playerSave) {
        String name = playerSave.getName();
        String gameName = playerSave.getGameName();

        GameType gameType = gameService.getGame(gameName);
        if (gameType instanceof NullGameType) {
            return NullPlayer.INSTANCE;
        }
        Player player = getPlayer(playerSave, gameType);

        if (name.endsWith(WebSocketRunner.BOT_EMAIL_SUFFIX)) {
            Closeable runner = createAI(name, gameType);
            player.setAI(runner);
        }

        return player;
    }

    private Closeable createAI(String aiName, GameType gameType) {
        Class<? extends Solver> ai = gameType.getAI();
        if (ai == null) {
            return null;
        }

        try {
            Solver solver;

            try {
                solver = Reflection.constructor()
                        .withParameterTypes(Dice.class)
                        .in(ai)
                        .newInstance(gameType.getDice());
            } catch (Exception e) {
                solver = Reflection.constructor()
                        .withParameterTypes()
                        .in(ai)
                        .newInstance(); // without dice
            }

            ClientBoard board = Reflection.constructor()
                    .in(gameType.getBoard())
                    .newInstance();

            WebSocketRunner runner = runAI(aiName, solver, board);
            return runner;
        } catch (Exception e) {
            return null;
        }
    }

    protected WebSocketRunner runAI(String aiName, Solver solver, ClientBoard board) {
        return WebSocketRunner.runAI(aiName, solver, board);
    }

    private Player getPlayer(PlayerSave playerSave, GameType gameType) {
        String name = playerSave.getName();
        String gameName = playerSave.getGameName();
        String callbackUrl = playerSave.getCallbackUrl();

        Player player = getPlayer(name);

        boolean newPlayer = (player instanceof NullPlayer) || !gameName.equals(player.getGameName());
        if (newPlayer) {
            playerGames.remove(player);

            PlayerScores playerScores = gameType.getPlayerScores(playerSave.getScore());
            InformationCollector listener = new InformationCollector(playerScores);

            player = new Player(name, callbackUrl,
                    gameType, playerScores, listener);
            player.setEventListener(listener);

            player.setGameType(gameType);
            PlayerGame playerGame = playerGames.add(player, playerSave);

            player = playerGame.getPlayer();

            player.setReadableName(registration.getReadableName(player.getName()));

            if (logger.isDebugEnabled()) {
                logger.debug("Player {} starting new game {}", name, playerGame.getGame());
            }
        } else {
          // do nothing
        }
        return player;
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            long time = 0;
            if (logger.isDebugEnabled()) {
                logger.debug("==================================================================================");
                logger.debug("PlayerService.tick() starts");
                time = System.currentTimeMillis();
            }

            autoSaver.tick();

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
        int requested = 0;

        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            try {
                String board = cacheBoards.get(player);
                // TODO в конце концов если if (pair == null || pair.noSockets()) то ничего не отправляется, и зря гоняли но вроде как из кеша берем, так что проблем быть не должно
                if (playerController.requestControl(player, board)) {
                    requested++;
                }
            } catch (IOException e) {
                logger.error("Unable to send control request to player " + player.getName() +
                        " URL: " + player.getCallbackUrl(), e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("tick().requestControls() {} players", requested);
        }
    }

    private void sendScreenUpdates() {
        Map<ScreenRecipient, ScreenData> map = buildScreenData();
        sendScreenForWebSockets(map);
    }

    private Map<ScreenRecipient, ScreenData> buildScreenData() {
        Map<ScreenRecipient, ScreenData> map = new HashMap<>();
        cacheBoards.clear();

        Map<String, GameData> gameDataMap = playerGamesView.getGamesDataMap();
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            Player player = playerGame.getPlayer();
            try {
                String gameType = playerGame.getGameType().name();
                GameData gameData = gameDataMap.get(player.getName());

                // TODO вот например для бомбера всем отдаются одни и те же борды, отличие только в паре спрайтов
                Object board = game.getBoardAsString(); // TODO дольше всего строчка выполняется, прооптимизировать!

                GuiPlotColorDecoder decoder = gameData.getDecoder();
                cacheBoards.put(player, decoder.encodeForClient(board));
                Object encoded = decoder.encodeForBrowser(board);

                map.put(player, new PlayerData(gameData.getBoardSize(),
                        encoded,
                        gameType,
                        player.getScore(),
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
        lock.readLock().lock();
        try {
            return playerGames.getPlayers(gameName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(String name) {
        lock.writeLock().lock();
        try {
            Player player = getPlayer(name);

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
                PlayerGame playerGame = playerGames.get(index);
                Player playerToUpdate = playerGame.getPlayer();
                Player newPlayer = players.get(index);

                playerToUpdate.setCallbackUrl(newPlayer.getCallbackUrl());
                playerToUpdate.setName(newPlayer.getName());
                playerToUpdate.setReadableName(newPlayer.getReadableName());
                registration.updateReadableName(newPlayer.getName(), newPlayer.getReadableName());

                Game game = playerGame.getGame();
                if (game != null && game.getSave() != null) {
                    String oldSave = game.getSave().toString();
                    String newSave = newPlayer.getData();
                    if (!PlayerSave.isSaveNull(newSave) && !newSave.equals(oldSave)) {
                        playerGames.setLevel(
                                newPlayer.getName(),
                                new JSONObject(newSave));
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean contains(String name) {
        lock.readLock().lock();
        try {
            return getPlayer(name) != NullPlayer.INSTANCE;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Player get(String name) {
        lock.readLock().lock();
        try {
            return getPlayer(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Player getPlayer(String name) {
        return playerGames.get(name).getPlayer();
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
        isRegOpened = false;
    }

    @Override
    public boolean isRegistrationOpened() {
        return isRegOpened;
    }

    @Override
    public void openRegistration() {
        isRegOpened = true;
    }

    @Override
    public void cleanAllScores() {
        lock.writeLock().lock();
        try {
            for (PlayerGame playerGame : playerGames) {
                Game game = playerGame.getGame();
                Player player = playerGame.getPlayer();

                player.clearScore();
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

            Iterator<Player> iterator = playerGames.getPlayers(gameType).iterator();
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

            return playerGames.iterator().next().getGameType();
        } finally {
            lock.readLock().unlock();
        }
    }

}
