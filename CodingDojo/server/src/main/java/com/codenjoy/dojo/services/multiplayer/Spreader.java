package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class Spreader {

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

    public List<GamePlayer> remove(Game game) {
        List<GamePlayer> removed = new LinkedList<>();

        GamePlayer player = game.getPlayer();
        List<Room> playerRooms = roomsFor(player);

        removed.add(player);

        playerRooms.forEach(room -> {
            List<GamePlayer> players = room.getPlayers();
            players.remove(player);

            if (players.isEmpty()) {
                rooms.remove(room);
            }
            if (players.size() == 1) {
                GamePlayer lastPlayer = players.iterator().next();

                removed.add(lastPlayer);

                rooms.remove(room);
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

    public void play(Game game, GameType gameType) {
        GameField field = getField(game.getPlayer(),
                gameType.name(),
                gameType.getMultiplayerType().getCount(),
                gameType::createGame);

        game.on(field);
    }

    public boolean contains(Game game) {
        return !roomsFor(game.getPlayer()).isEmpty();
    }
}
