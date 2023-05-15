package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Player;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
public class Spreader {

    private FieldService fields;

    public Spreader(FieldService fields) {
        this.fields = fields;
    }

    private Multimap<String, GameRoom> rooms = LinkedHashMultimap.create();

    public GameField fieldFor(Deal deal, String room,
                              MultiplayerType type,
                              int roomSize, int level,
                              Supplier<GameField> getField)
    {
        room = type.getRoom(room, level);
        GameRoom gameRoom = null;
        if (type.shouldTryFindUnfilled(level)) {
            gameRoom = findUnfilled(deal, room);
        }

        if (gameRoom == null) {
            GameField field = getField.get();
            fields.register(field);
            gameRoom = new GameRoom(room, field, roomSize, type.isDisposable());
            add(room, gameRoom);
        }

        GameField field = gameRoom.join(deal);
        deal.chat().postField("Player joined the field", room);
        return field;
    }

    private void add(String room, GameRoom gameRoom) {
        rooms.get(room).add(gameRoom);
    }

    private GameRoom findUnfilled(Deal deal, String room) {
        return rooms.get(room).stream()
                .filter(r -> r.isAvailable(deal))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param deal Игрок который покидает борду
     * @return Все игроки, что так же покинут эту борду в случае если им
     * оставаться на борде не имеет смысла
     */
    public List<Deal> remove(Deal deal, Sweeper sweeper) {
        Optional<GameRoom> optional = roomFor(deal);

        if (!optional.isPresent()) {
            return Arrays.asList();
        }

        GameRoom room = optional.get();

        deal.chat().postField("Player left the field", deal.getRoom());
        deal.getGame().close();

        List<Deal> removed = room.remove(deal, sweeper);

        removeIfEmpty(room);

        return removed;
    }

    private void removeIfEmpty(GameRoom room) {
        if (!room.isEmpty()) return;

        fields.remove(room.field());

        // TODO попробовать это решить иначе
        rooms.entries().stream()
                .filter(entry -> entry.getValue() == room)
                .map(Map.Entry::getKey)
                .distinct()
                .forEach(key -> rooms.remove(key, room));
    }

    private Optional<GameRoom> roomFor(Deal deal) {
        return rooms.values().stream()
                .filter(room -> room.containsDeal(deal))
                .findFirst();
    }

    private Optional<GameRoom> roomFor(GameField field) {
        return rooms.values().stream()
                .filter(room -> room.isFor(field))
                .findFirst();
    }

    public List<Player> players(String roomName) {
        return rooms.values().stream()
                .filter(room -> isSame(room.name(), roomName))
                .flatMap(room -> room.deals().stream())
                .map(Deal::getPlayer)
                .collect(toList());
    }

    public List<Player> players(int fieldId) {
        return rooms.values().stream()
                .filter(room -> fields.id(room.field()) == fieldId)
                .flatMap(room -> room.deals().stream())
                .map(Deal::getPlayer)
                .collect(toList());
    }

    public boolean contains(Deal deal) {
        return !roomFor(deal).isEmpty();
    }

    public boolean isRoomStaffed(GameField field) {
        if (field == null) {
            throw new IllegalArgumentException("Field is null");
        }

        return roomFor(field)
                .orElseThrow(() -> new IllegalStateException(
                        "There is no room for field: " + field.hashCode()))
                .isStuffed();
    }

    public Multimap<String, GameRoom> rooms() {
        return rooms;
    }

    public Optional<GameRoom> gameRoom(String room, String playerId) {
        return rooms.keys().stream()
                .filter(key -> isSame(key, room))
                .flatMap(key -> rooms.get(key).stream())
                .filter(r -> r.containsPlayer(playerId))
                .findFirst();
    }

    // так надо потому, что в rooms обычно комнаты будут 'room',
    // но для levels типов уровней будут 'room[N]', где N уровень
    // TODO #7D4 быть может стоит как-то 'иначее' называть комнаты для levels?
    private boolean isSame(String key, String room) {
        return key.equals(room) || key.startsWith(room + "[");
    }

}
