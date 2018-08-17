package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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

    public void remove(Game game, GameType gameType) {
        List<Room> roomList = rooms.get(gameType.name());
        GamePlayer player = game.getPlayer();
        Room room = roomList.stream()
                .filter(r -> r.contains(player))
                .findFirst()
                .orElse(null);

        if (room == null) {
            return;
        }

        roomList.remove(room);

        List<GamePlayer> players = room.getPlayers();
        players.remove(player);
        players.forEach(p -> replay(game, gameType));
    }

    public void play(Game game, GameType gameType) {
        GameField field = getField(game.getPlayer(),
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
