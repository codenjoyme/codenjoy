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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Component
public class PlayerGamesView {

    @Autowired
    protected PlayerGames service;

    public Map<String, GameData> getGamesDataMap() {
        Map<String, GameData> result = new LinkedHashMap<>();

        Map<GameType, GuiPlotColorDecoder> decoders = getDecoders();
        Map<String, List<String>> groupsMap = getGroupsMap();
        Map<String, Object> allScores = getScores();
        Map<String, HeroData> allCoordinates = getCoordinates();

        for (PlayerGame playerGame : service) {
            String player = playerGame.getPlayer().getName();
            GameType gameType = playerGame.getGameType();
            int boardSize = gameType.getBoardSize().getValue();
            GuiPlotColorDecoder decoder = decoders.get(gameType);
            List<String> group = groupsMap.get(player);
            Map<String, Object> scores = filterByGroup(allScores, group);
            Map<String, HeroData> coordinates = filterByGroup(allCoordinates, group);

            result.put(player, new GameData(boardSize, decoder,
                    scores, group, coordinates));
        }

        return result;
    }

    private Map<GameType, GuiPlotColorDecoder> getDecoders() {
        Map<GameType, GuiPlotColorDecoder> result = new HashMap<>();
        for (GameType gameType : service.getGameTypes()) {
            GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameType.getPlots());
            result.put(gameType, decoder);
        }
        return result;
    }

    private <K, V> Map<K, V> filterByGroup(Map<K, V> map, List<String> group) {
        return map.entrySet().stream()
                .filter(entry -> group.contains(entry.getKey()))
                .collect(toMap(entry -> entry.getKey(),
                        entry -> entry.getValue()));
    }

    private Map<String, HeroData> getCoordinates() {
        Map<String, HeroData> result = new HashMap<>();
        for (PlayerGame playerGame : service) {
            result.put(playerGame.getPlayer().getName(),
                    playerGame.getGame().getHero());
        }
        return result;
    }

    private Map<String, List<String>> getGroupsMap() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (List<String> group : getGroups()) {
            for (String player : group) {
                result.put(player, group);
            }
        }
        return result;
    }

    private List<List<String>> getGroups() {
        return service.all().stream()
                    .collect(groupingBy(PlayerGame::getField))
                    .values().stream()
                    .map(group -> group.stream()
                            .map(pg -> pg.getPlayer().getName())
                            .collect(toList()))
                    .collect(toList());
    }

    private Map<String, Object> getScores() {
        Map<String, Object> result = new HashMap<>();
        for (PlayerGame playerGame : service.all()) {
            Player player = playerGame.getPlayer();
            result.put(player.getName(), player.getScore());
        }
        return result;
    }
}
