package com.codenjoy.dojo.services.multiplayer;

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

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class Spreader {

    private Map<String, List<Room>> rooms = new HashMap<>();

    public GameField getField(GamePlayer player, String gameType,
                              MultiplayerType type,
                              int roomSize, int levelNumber,
                              Supplier<GameField> supplier)
    {
        Room room = null;
        if (!type.isTraining() || type.isLastLevel(levelNumber)) {
            room = findUnfilled(gameType);
        }
        if (room == null) {
            room = new Room(supplier.get(), roomSize, type.isDisposable());
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

    /**
     * @param game Игра что покидает борду
     * @return Все пллера, что так же покинут эту борду в случае если им
     * оставаться на борде не имеет смысла
     */
    public List<GamePlayer> remove(Game game) {
        List<GamePlayer> removed = new LinkedList<>();

        GamePlayer player = game.getPlayer();
        List<Room> playerRooms = roomsFor(player);

        playerRooms.forEach(room -> {
            List<GamePlayer> players = room.getPlayers();
            players.remove(player);

            if (players.size() == 1) { // TODO ##1 тут может не надо выходить если тип игры MULTIPLAYER
                GamePlayer lastPlayer = players.iterator().next();
                if (!lastPlayer.wantToStay()) {
                    removed.add(lastPlayer);
                    players.remove(lastPlayer);
                }
            }
            if (players.isEmpty()) {
                rooms.values().forEach(it -> it.remove(room));
            }
        });

        return removed;
    }

    private List<Room> roomsFor(GamePlayer player) {
        return allRooms().stream()
                    .filter(r -> r.contains(player))
                    .collect(toList());
    }

    private List<Room> allRooms() {
        return rooms.values().stream()
                .flatMap(List::stream)
                .collect(toList());
    }

    public void play(Game game, GameType gameType, JSONObject save) {
        game.close();

        MultiplayerType type = gameType.getMultiplayerType();
        int roomSize = type.loadProgress(game, save);
        LevelProgress progress = game.getProgress();
        int levelNumber = progress.getCurrent();
        GameField field = getField(game.getPlayer(),
                gameType.name(),
                type,
                roomSize,
                levelNumber,
                () -> {
                    game.getPlayer().setProgress(progress);
                    return gameType.createGame(levelNumber);
                });

        game.on(field);

        game.newGame();
        if (save != null && !save.keySet().isEmpty()) {
            game.loadSave(save);
        }
    }

    public boolean contains(Game game) {
        return !roomsFor(game.getPlayer()).isEmpty();
    }

    public boolean isRoomStaffed(GameField field) {
        List<Room> rooms = allRooms().stream()
                .filter(r -> r.isFor(field))
                .collect(toList());
        if (rooms.size() != 1) {
            throw new IllegalArgumentException("Почему-то комната для поля не одна: " + rooms.size());
        }
        return rooms.get(0).isStuffed();
    }
}
