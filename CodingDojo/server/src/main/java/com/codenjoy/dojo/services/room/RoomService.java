package com.codenjoy.dojo.services.room;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.RoomGameType;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class RoomService {

    private Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    public boolean isActive(String room) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return false;
        }

        return rooms.get(room).getActive();
    }

    public boolean exists(String room) {
        return !Validator.isEmpty(room)
                && rooms.containsKey(room);
    }

    public RoomState state(String room) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return null;
        }

        return new RoomState(rooms.get(room));
    }

    public void setActive(String room, boolean value) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return;
        }
        rooms.get(room).setActive(value);
    }

    public GameType create(String room, GameType gameType) {
        if (exists(room)) {
            return rooms.get(room).getType();
        }

        RoomGameType decorator = new RoomGameType(gameType);
        RoomState state = new RoomState(room, decorator, true);
        rooms.put(room, state);

        return decorator;
    }

    public Settings settings(String room) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return null;
        }
        return rooms.get(room).getType().getSettings();
    }

    public void removeAll() {
        rooms.clear();
    }

    public List<String> names() {
        return rooms.values().stream()
                .sorted(Comparator.comparing(o -> o.getType().name() + o.getName()))
                .map(roomState -> roomState.getName())
                .collect(toList());
    }

    public String game(String room) {
        return gameType(room).name();
    }

    public GameType gameType(String room) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return null;
        }
        return state(room).getType();
    }

    public List<GameRooms> gameRooms() {
        return rooms.values().stream()
                .collect(groupingBy(roomState -> roomState.getType().name(),
                        mapping(roomState -> roomState.getName(), toList())))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> new GameRooms(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
