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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class RoomService {

    private Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    public boolean isActive(String room) {
        return state(room)
                .map(RoomState::isActive)
                .orElse(false);
    }

    public boolean isOpened(String room) {
        if (!exists(room)) {
            // комната которой не существует должна быть открыта для регистрации
            // иначе не создастся первый пользователь и не создаст тут комнату
            return true;
        }

        return rooms.get(room).isOpened();
    }

    public boolean exists(String room) {
        return !Validator.isEmpty(room)
                && rooms.containsKey(room);
    }

    public Optional<RoomState> state(String room) {
        if (!exists(room)) {
            log.warn("Room '{}' not found", room);
            return Optional.empty();
        }

        return Optional.of(rooms.get(room));
    }

    public void setActive(String room, boolean value) {
        state(room)
                .ifPresent(state -> state.setActive(value));
    }

    public void setOpened(String room, boolean value) {
        state(room)
                .ifPresent(state -> state.setOpened(value));
    }

    public GameType create(String room, GameType gameType) {
        if (exists(room)) {
            return rooms.get(room).getType();
        }

        RoomGameType decorator = new RoomGameType(gameType);
        RoomState state = new RoomState(room, decorator, true, true, 0);
        rooms.put(room, state);
        return decorator;
    }

    public Settings settings(String room) {
        return (Settings) state(room)
                .map(RoomState::getType)
                .map(GameType::getSettings)
                .orElse(null);
    }

    public GameType gameType(String room) {
        return state(room)
                .map(RoomState::getType)
                .orElse(null);
    }

    public void removeAll() {
        rooms.clear();
    }

    public List<String> names() {
        return allSorted()
                .map(roomState -> roomState.getName())
                .collect(toList());
    }

    private Stream<RoomState> allSorted() {
        return rooms.values().stream()
                .sorted(Comparator.comparing(o -> o.getType().name() + o.getName()));
    }

    public String game(String room) {
        return state(room)
                .map(RoomState::getType)
                .map(GameType::name)
                .orElse(null);
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

    public void resetTick(String room) {
        state(room)
                .ifPresent(RoomState::resetTick);
    }

    public void tick(String room) {
        state(room)
                .ifPresent(RoomState::tick);
    }

    public Collection<RoomState> all() {
        return allSorted()
                .collect(toList());
    }

    public int getTick(String room) {
        return state(room)
                .map(RoomState::getTick)
                .orElse(-1);
    }
}
