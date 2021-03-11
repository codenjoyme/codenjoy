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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
public class Spreader {

    private Multimap<String, GameRoom> rooms = LinkedHashMultimap.create();

    public GameField fieldFor(GamePlayer player, String room,
                              MultiplayerType type,
                              int roomSize, int level,
                              Supplier<GameField> field)
    {
        room = type.getRoom(room, level);
        GameRoom gameRoom = null;
        if (type.shouldTryFindUnfilled(level)) {
            gameRoom = findUnfilled(room);
        }

        if (gameRoom == null) {
            gameRoom = new GameRoom(field.get(), roomSize, type.isDisposable());
            add(room, gameRoom);
        }

        return gameRoom.join(player);
    }

    private void add(String room, GameRoom gameRoom) {
        rooms.get(room).add(gameRoom);
    }

    private GameRoom findUnfilled(String room) {
        Collection<GameRoom> rooms = rooms(room);
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.stream()
                .filter(GameRoom::isFree)
                .findFirst()
                .orElse(null);
    }

    private Collection<GameRoom> rooms(String room) {
        return rooms.get(room);
    }

    /**
     * @param player Игрок который покидает борду
     * @return Все игроки, что так же покинут эту борду в случае если им
     * оставаться на борде не имеет смысла
     */
    public List<GamePlayer> remove(GamePlayer player) {
        List<GameRoom> rooms = roomsFor(player);

        List<GamePlayer> removed = rooms.stream()
                .flatMap(room -> room.remove(player).stream())
                .collect(toList());

        rooms.forEach(this::removeIfEmpty);

        return removed;
    }

    private void removeIfEmpty(GameRoom room) {
        if (!room.isEmpty()) return;

        rooms.entries().stream()
                .filter(entry -> entry.getValue() == room)
                .map(entry -> entry.getKey())
                .distinct()
                .forEach(key -> rooms.remove(key, room));
    }

    private List<GameRoom> roomsFor(GamePlayer player) {
        return rooms.values().stream()
                    .filter(room -> room.contains(player))
                    .collect(toList());
    }

    private List<GameRoom> roomsFor(GameField field) {
        return rooms.values().stream()
                .filter(room -> room.isFor(field))
                .collect(toList());
    }

    public boolean contains(GamePlayer player) {
        return !roomsFor(player).isEmpty();
    }

    public boolean isRoomStaffed(GameField field) {
        if (field == null) {
            log.warn("Почему-то комната для поля == null");
        }

        List<GameRoom> rooms = roomsFor(field);
        if (rooms.size() != 1) {
            log.warn("Почему-то комната для поля не одна: " + rooms.size());
            return true;
        }
        return rooms.get(0).isStuffed();
    }

    public Multimap<String, GameRoom> rooms() {
        return rooms;
    }

}
