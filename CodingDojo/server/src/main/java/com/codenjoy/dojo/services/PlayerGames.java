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


import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.codenjoy.dojo.services.PlayerGame.by;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
public class PlayerGames implements Iterable<PlayerGame>, Tickable {

    private static Logger logger = LoggerFactory.getLogger(PlayerGames.class);

    private List<PlayerGame> playerGames = new LinkedList<>();

    private Consumer<PlayerGame> onAdd;
    private Consumer<PlayerGame> onRemove;
    private ReadWriteLock lock;
    private Spreader spreader = new Spreader();

    public void onAdd(Consumer<PlayerGame> consumer) {
        this.onAdd = consumer;
    }

    public void onRemove(Consumer<PlayerGame> consumer) {
        this.onRemove = consumer;
    }

    public void init(ReadWriteLock lock) {
        this.lock = lock;
    }

    public PlayerGames() {
        lock = new ReentrantReadWriteLock();
    }

    public void remove(Player player) {
        int index = playerGames.indexOf(player);
        if (index == -1) return;
        playerGames.remove(index).remove(onRemove);
    }

    public PlayerGame get(String playerName) {
        return playerGames.stream()
                .filter(pg -> pg.getPlayer().getName().equals(playerName))
                .findFirst()
                .orElse(NullPlayerGame.INSTANCE);
    }

    public PlayerGame add(Player player, PlayerSave save) {
        GameType gameType = player.getGameType();

        GamePlayer gamePlayer = gameType.createPlayer(player.getEventListener(),
                (save == null) ? "" : save.getSave(), player.getName());

        Single single = new Single(gamePlayer,
                gameType.getPrinterFactory(),
                gameType.getMultiplayerType());

        spreader.play(single, gameType);

        Game game = new LockedGame(lock).wrap(single);

        PlayerGame playerGame = new PlayerGame(player, game);
        if (onAdd != null) {
            onAdd.accept(playerGame);
        }
        playerGames.add(playerGame);
        return playerGame;
    }

    public boolean isEmpty() {
        return playerGames.isEmpty();
    }

    @Override
    public Iterator<PlayerGame> iterator() {
        return playerGames.iterator();
    }

    public List<Player> players() {
        return playerGames.stream()
                .map(PlayerGame::getPlayer)
                .collect(toList());
    }

    public int size() {
        return playerGames.size();
    }

    public void clear() {
        players().forEach(this::remove);
    }

    public List<PlayerGame> getAll(String gameType) {
        return playerGames.stream()
                .filter(pg -> pg.getPlayer().getGameName().equals(gameType))
                .collect(toList());
    }

    public List<GameType> getGameTypes() {
        List<GameType> result = new LinkedList<>();

        for (PlayerGame playerGame : playerGames) {
            GameType gameType = playerGame.getPlayer().getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }

    @Override
    public void tick() {
        // по всем джойстикам отправили сообщения играм
        playerGames.forEach(PlayerGame::quietTick);

        // создаем новые игры для тех, кто уже game over
        // TODO если игра игрока многопользовательская то
        // он должен добавиться в новую борду
        // ну и последний играющий игрок на борде так же должен ее покинуть
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            if (game.isGameOver()) {
                quiet(() -> {
                    GameType gameType = getPlayer(game).getGameType();
                    spreader.replay(game, gameType);
                    game.newGame();
                });
            }
        }

        // собираем все уникальные борды
        // независимо от типа игры нам нужно тикнуть все
        playerGames.stream()
                .map(PlayerGame::getField)
                .collect(toSet())
                .forEach(GameField::quietTick);

        // ну и тикаем все GameRunner мало ли кому надо на это подписаться
        getGameTypes().forEach(GameType::quietTick);
    }

    private Player getPlayer(Game game) {
        return playerGames.stream()
                .filter(pg -> pg.equals(by(game)))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getPlayer();
    }

    private void quiet(Runnable runnable) {
        ((Tickable)() -> runnable.run()).quietTick();
    }

    public Map<String, GameData> getGamesDataMap() {
        Map<String, GameData> result = new LinkedHashMap<>();
        for (GameType gameType : getGameTypes()) {
            int boardSize = gameType.getBoardSize().getValue();
            GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameType.getPlots());
            JSONObject scores = getScoresJSON(gameType.name());
            JSONObject heroesData = getCoordinatesJSON(gameType.name());

            result.put(gameType.name(), new GameData(boardSize, decoder, scores, heroesData));
        }
        return result;
    }

    private JSONObject getCoordinatesJSON(String gameType) {
        List<PlayerGame> playerGames = getAll(gameType);

        Map<Player, List<Player>> playersMap = new HashMap<>();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            Game game = playerGame.getGame();
            HeroData heroData = game.getHero();
            List<Game> gamesGroup = heroData.playersGroup();
            List<Player> playersGroup = new LinkedList<>();
            if (gamesGroup == null) {
                playersGroup.add(player);
            } else {
                for (Game game2 : gamesGroup) {
                    int index = playerGames.indexOf(by(game2));
                    if (index != -1) {
                        playersGroup.add(playerGames.get(index).getPlayer());
                    } else {
                        // TODO этого не должн случиться, но лучше порефакторить
                        throw new IllegalStateException("Игрок не в группе");
                    }
                }
            }
            playersMap.put(player, playersGroup);
        }

        Map<String, JSONObject> heroesData = new HashMap<>();
        for (PlayerGame playerGame : playerGames) {
            heroesData.put(playerGame.getPlayer().getName(),
                    new JSONObject(playerGame.getGame().getHero()));
        }

        JSONObject result = new JSONObject();
        for (Map.Entry<Player, List<Player>> entry : playersMap.entrySet()) {
            Player player1 = entry.getKey();

            JSONObject map = new JSONObject();
            result.put(player1.getName(), map);

            for (Player player2 : entry.getValue()) {
                String name = player2.getName();
                map.put(name, heroesData.get(name));
            }
        }
        return result;
    }

    private JSONObject getScoresJSON(String gameType) {
        JSONObject result = new JSONObject();
        for (PlayerGame playerGame : getAll(gameType)) {
            Player player = playerGame.getPlayer();
            result.put(player.getName(), player.getScore());
        }
        return result;
    }

    private static class Room {
        private GameField field;
        private int count;
        private List<GamePlayer> players = new LinkedList<>();

        private Room(GameField field, int count) {
            this.field = field;
            this.count = count;
        }

        private GameField getField(GamePlayer player) {
            players.add(player);
            return field;
        }

        public boolean isFree() {
            return players.size() < count;
        }

        public boolean contains(GamePlayer player) {
            return players.stream()
                    .filter(p -> p.equals(player))
                    .count() != 0;
        }
    }

    private class Spreader {

        private Map<String, List<Room>> rooms = new HashMap<>();

        public GameField getField(GamePlayer player, String gameType, int count, Supplier<GameField> supplier) {
            Room room = findUnfilled(gameType);
            if (room == null) {
                room = new Room(supplier.get(), count);
                add(gameType, room);
            }

            GameField field = room.getField(player);
            return field;
        }

        private void add(String gameType, Room room) {
            List<Room> rooms = getRooms(gameType);
            rooms.add(room);
        }

        private Room findUnfilled(String gameType) {
            List<Room> rooms = getRooms(gameType);
            if (rooms.isEmpty()) {
                return null;
            }
            return rooms.stream()
                    .filter(Room::isFree)
                    .findFirst()
                    .orElse(null);
        }

        private List<Room> getRooms(String gameType) {
            List<Room> result = rooms.get(gameType);
            if (result == null) {
                rooms.put(gameType, result = new LinkedList<>());
            }
            return result;
        }

        public void remove(Game game, GameType gameType) {
            List<Room> roomList = rooms.get(gameType.name());
            GamePlayer player = game.getPlayer();
            Room room = roomList.stream()
                    .filter(r -> r.contains(player))
                    .findFirst()
                    .orElse(null);

            roomList.remove(room);

            List<GamePlayer> players = room.players;
            players.remove(player);
            players.forEach(p -> play(game, gameType));
        }

        public void play(Game game, GameType gameType) {
            GameField field = spreader.getField(game.getPlayer(),
                    gameType.name(),
                    gameType.getMultiplayerType().getCount(),
                    gameType::createGame);

            game.on(field);
        }

        public void replay(Game game, GameType gameType) {
            remove(game, gameType);
            play(game, gameType);
        }
    }
}
