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
        Map<GameType, GuiPlotColorDecoder> decoders = getDecoders();
        Map<String, List<String>> groupsMap = getGroupsMap();
        Map<String, Object> scores = getScores();
        Map<String, HeroData> coordinates = getCoordinates();
        Map<String, String> readableNames = getReadableNames();

        return service.all().stream()
                .collect(toMap(
                        pg -> pg.getPlayer().getName(),
                        pg -> {
                            GameType gameType = pg.getGameType();
                            String player = pg.getPlayer().getName();
                            List<String> group = groupsMap.get(player);

                            return new GameData(
                                    gameType.getBoardSize().getValue(),
                                    decoders.get(gameType),
                                    filterByGroup(scores, group),
                                    group,
                                    filterByGroup(coordinates, group),
                                    filterByGroup(readableNames, group));
                        }));
    }

    private Map<GameType, GuiPlotColorDecoder> getDecoders() {
        return service.getGameTypes().stream()
                .collect(toMap(type -> type,
                        type -> new GuiPlotColorDecoder(type.getPlots())));
    }

    private <K, V> Map<K, V> filterByGroup(Map<K, V> map, List<String> group) {
        return map.entrySet().stream()
                .filter(entry -> group.contains(entry.getKey()))
                .collect(toMap(entry -> entry.getKey(),
                        entry -> entry.getValue()));
    }

    private Map<String, HeroData> getCoordinates() {
        return service.all().stream()
                .collect(toMap(pg -> pg.getPlayer().getName(),
                        pg -> pg.getGame().getHero()));
    }

    public Map<String, List<String>> getGroupsMap() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (List<String> group : getGroups()) {
            for (String player : group) {
                if (result.containsKey(player)) {
                    continue;
                }
                result.put(player, group);
            }
        }
        return result;
    }

    public List<List<String>> getGroups() {
        return service.all().stream()
                    .collect(groupingBy(PlayerGame::getField))
                    .values().stream()
                    .map(group -> group.stream()
                            .map(pg -> pg.getPlayer().getName())
                            .collect(toList()))
                    .collect(toList());
    }

    public Map<String, Object> getScores() {
        return service.all().stream()
                .collect(toMap(pg -> pg.getPlayer().getName(),
                        pg -> pg.getPlayer().getScore()));
    }

    public Map<String, String> getReadableNames() {
        return service.all().stream()
                .collect(toMap(pg -> pg.getPlayer().getName(),
                        pg -> pg.getPlayer().getNotNullReadableName()));
    }
}
