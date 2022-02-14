package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.dao.BoardData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoomsAliaser {

    private final GameService gameService;
    private final BoardData boardData;

    private BidiMap<String, String> rooms = new DualLinkedHashBidiMap<>();

    @PostConstruct
    public void init() {
        addAllGames();

        // TODO move to admin settings
//        rooms.put("battlecity-1", "Haifa");
//        rooms.put("battlecity-2", "Jerusalem");
//        rooms.put("battlecity-3", "Co-studying");
//        rooms.put("battlecity-4", "Semi Final");
//        rooms.put("battlecity-5", "Final");
    }

    private void addAllGames() {
        gameService.getGames()
                .forEach(game -> rooms.putIfAbsent(game, game));
        boardData.getAllGames()
                .forEach(game -> rooms.putIfAbsent(game, game));
    }

    public String getAlias(String game) {
        if (!rooms.containsKey(game)) {
            return game;
        }
        return rooms.get(game);
    }

    public void addRoom(String name) {
        rooms.putIfAbsent(name, name);
    }

    public Set<String> alises() {
        return rooms.values();
    }

    public String getGameName(String alias) {
        if (!rooms.containsValue(alias)) {
            return alias;
        }
        return rooms.getKey(alias);
    }

    public Set<String> game() {
        return rooms.keySet();
    }

    public Set<Map.Entry<String, String>> all() {
        return rooms.entrySet();
    }

    // TODO test me
    public void enableGames(List<String> toRemove) {
        addAllGames();
        toRemove.forEach(game -> rooms.remove(game));
    }
}
