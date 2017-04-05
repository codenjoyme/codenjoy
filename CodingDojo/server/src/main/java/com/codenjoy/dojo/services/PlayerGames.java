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


import com.codenjoy.dojo.services.hero.HeroData;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlayerGames implements Iterable<PlayerGame>, Tickable {

    private static Logger logger = LoggerFactory.getLogger(PlayerGames.class);

    public static final int TICKS_FOR_REMOVE = 60*30; // 15 минут без игры - дисквалификация
    private List<PlayerGame> playerGames = new LinkedList<PlayerGame>();

    public PlayerGames() {}
    public PlayerGames(Statistics statistics) { // TODO
        this.statistics = statistics;
    }

    private @Autowired Statistics statistics;

    public void remove(Player player) {
        int index = playerGames.indexOf(player);
        if (index == -1) return;
        playerGames.remove(index).remove();
    }

    public PlayerGame get(String playerName) {
        for (PlayerGame playerGame : playerGames) {
            if (playerGame.getPlayer().getName().equals(playerName)) {
                return playerGame;
            }
        }
        return NullPlayerGame.INSTANCE;
    }

    public void add(Player player, Game game, PlayerController controller) {
        PlayerSpy spy = statistics.newPlayer(player);

        LazyJoystick joystick = new LazyJoystick(game, spy);
        controller.registerPlayerTransport(player, joystick);
        playerGames.add(new PlayerGame(player, game, controller, joystick));
    }

    public boolean isEmpty() {
        return playerGames.isEmpty();
    }

    @Override
    public Iterator<PlayerGame> iterator() {
        return playerGames.iterator();
    }

    public List<Player> players() {
        List<Player> result = new ArrayList<Player>(playerGames.size());

        for (PlayerGame playerGame : playerGames) {
            result.add(playerGame.getPlayer());
        }

        return result;
    }

    public int size() {
        return playerGames.size();
    }

    public void clear() {
        for (PlayerGame playerGame : playerGames) {
            playerGame.remove();
        }
        playerGames.clear();
    }

    public List<PlayerGame> getAll(String gameType) {
        List<PlayerGame> result = new LinkedList<PlayerGame>();

        for (PlayerGame playerGame : playerGames) {
            if (playerGame.getPlayer().getGameName().equals(gameType)) {
                result.add(playerGame);
            }
        }

        return result;
    }

    public List<GameType> getGameTypes() {
        List<GameType> result = new LinkedList<GameType>();

        for (PlayerGame playerGame : playerGames) {
            GameType gameType = playerGame.getPlayer().getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }

    private void quietTick(Tickable tickable) {
        try {
            tickable.tick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
//        long time = System.currentTimeMillis();

        for (PlayerGame playerGame : playerGames) {
            quietTick(playerGame);
        }

        quietTick(statistics);

//        removeNotActivePlayers();

        for (final PlayerGame playerGame : playerGames) {
            final Game game = playerGame.getGame();
            if (game.isGameOver()) {
                quietTick(new Tickable() {
                    @Override
                    public void tick() {
                        game.newGame();
                    }
                });
            }
        }

        List<GameType> gameTypes = getGameTypes();  // TODO потестить еще отдельно
        for (GameType gameType : gameTypes) {
            List<PlayerGame> games = getAll(gameType.name());
            if (gameType.isSingleBoard()) {
                if (!games.isEmpty()) {
                    quietTick(games.iterator().next().getGame());
                }
            } else {
                for (PlayerGame playerGame : games) {
                    quietTick(playerGame.getGame());
                }
            }
        }

//        if (logger.isDebugEnabled()) {
//            time = System.currentTimeMillis() - time;
//            logger.debug("PlayerGames.tick() is {} ms", time);
//        }
    }

    private void removeNotActivePlayers() {
        for (Player player : statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, TICKS_FOR_REMOVE)) {
            remove(player);
        }
    }

    // TODO test me
    public Map<String, GameData> getGamesDataMap() {
        Map<String, GameData> additionalData = new HashMap<>();
        for (GameType gameType : getGameTypes()) {
            int boardSize = gameType.getBoardSize().getValue();
            GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameType.getPlots());
            JSONObject scores = getScoresJSON(gameType.name());
            JSONObject heroesData = getCoordinatesJSON(gameType.name());

            additionalData.put(gameType.name(), new GameData(boardSize, decoder, scores, heroesData));
        }
        return additionalData;
    }

    // TODO test me
    private JSONObject getCoordinatesJSON(String gameType) {
        JSONObject result = new JSONObject();
        for (PlayerGame playerGame : getAll(gameType)) {
            Player player = playerGame.getPlayer();
            Game game = playerGame.getGame();
            HeroData data = game.getHero();
            result.put(player.getName(), new JSONObject(data));
        }
        return result;
    }

    // TODO test me
    private JSONObject getScoresJSON(String gameType) {
        JSONObject scores = new JSONObject();
        for (PlayerGame playerGame : getAll(gameType)) {
            Player player = playerGame.getPlayer();
            scores.put(player.getName(), player.getScore());
        }
        return scores;
    }
}
