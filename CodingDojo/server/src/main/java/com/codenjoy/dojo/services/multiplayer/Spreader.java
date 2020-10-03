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
import lombok.extern.java.Log;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Log
public class Spreader {

    private Multimap<String, Room> rooms = LinkedHashMultimap.create();

    public GameField fieldFor(GamePlayer player, String roomName,
                              MultiplayerType type,
                              int roomSize, int levelNumber,
                              Supplier<GameField> field)
    {
        roomName = type.getRoomName(roomName, levelNumber);
        Room room = null;
        if (type.shouldTryFindUnfilled(levelNumber)) {
            room = findUnfilled(roomName);
        }

        if (room == null) {
            room = new Room(field.get(), roomSize, type.isDisposable());
            add(roomName, room);
        }

        return room.join(player);
    }

    private void add(String roomName, Room room) {
        rooms.get(roomName).add(room);
    }

    private Room findUnfilled(String roomName) {
        Collection<Room> rooms = rooms(roomName);
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.stream()
                .filter(Room::isFree)
                .findFirst()
                .orElse(null);
    }

    private Collection<Room> rooms(String roomName) {
        return rooms.get(roomName);
    }

    /**
     * @param player Игрок который покидает борду
     * @return Все игроки, что так же покинут эту борду в случае если им
     * оставаться на борде не имеет смысла
     */
    public List<GamePlayer> remove(GamePlayer player) {
        List<Room> rooms = roomsFor(player);

        List<GamePlayer> removed = rooms.stream()
                .flatMap(room -> room.remove(player).stream())
                .collect(toList());

        rooms.forEach(this::removeIfEmpty);

        return removed;
    }

    private void removeIfEmpty(Room room) {
        if (!room.isEmpty()) return;

        rooms.entries().stream()
                .filter(entry -> entry.getValue() == room)
                .map(entry -> entry.getKey())
                .distinct()
                .forEach(key -> rooms.remove(key, room));
    }

    private List<Room> roomsFor(GamePlayer player) {
        return rooms.values().stream()
                    .filter(room -> room.contains(player))
                    .collect(toList());
    }

    private List<Room> roomsFor(GameField field) {
        return rooms.values().stream()
                .filter(room -> room.isFor(field))
                .collect(toList());
    }

    public boolean contains(GamePlayer player) {
        return !roomsFor(player).isEmpty();
    }

    public boolean isRoomStaffed(GameField field) {
        if (field == null) {
            log.warning("Почему-то комната для поля == null");
        }

        List<Room> rooms = roomsFor(field);
        if (rooms.size() != 1) {
            log.warning("Почему-то комната для поля не одна: " + rooms.size());
            return true;
        }
        return rooms.get(0).isStuffed();
    }

    public Multimap<String, Room> rooms() {
        return rooms;
    }

}
