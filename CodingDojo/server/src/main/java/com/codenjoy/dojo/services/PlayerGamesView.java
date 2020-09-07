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
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

@Component
public class PlayerGamesView {

    @Autowired
    protected PlayerGames service;

    public Map<String, GameData> getGamesDataMap() {
        Map<String, GuiPlotColorDecoder> decoders = getDecoders();
        Map<String, List<String>> groupsMap = getGroupsMap();
        Map<String, Object> scores = getScores();
        Map<String, HeroData> coordinates = getCoordinates();
        Map<String, String> readableNames = getReadableNames();

        return service.all().stream()
                .collect(toMap(
                        pg -> pg.getPlayer().getId(),
                        pg -> {
                            GameType gameType = pg.getGameType();
                            String player = pg.getPlayer().getId();
                            List<String> group = groupsMap.get(player);

                            return new GameData(
                                    gameType.getBoardSize().getValue(),
                                    decoders.get(gameType.name()),
                                    filterByGroup(scores, group),
                                    group,
                                    filterByGroup(coordinates, group),
                                    filterByGroup(readableNames, group));
                        }));
    }

    protected Map<String, GuiPlotColorDecoder> getDecoders() {
        return service.getGameTypes().stream()
                .collect(toMap(type -> type.name(),
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
                .collect(toMap(pg -> pg.getPlayer().getId(),
                        pg -> pg.getGame().getHero()));
    }

    public Map<String, List<String>> getGroupsMap() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (List<String> group : getGroupsByField()) {
            for (String player : group) {
                if (result.containsKey(player)) {
                    continue;
                }
                result.put(player, group);
            }
        }
        return result;
    }

    public List<List<String>> getGroupsByRooms() {
        return getGroupBy(PlayerGame::getRoomName);
    }

    public List<List<String>> getGroupsByField() {
        return getGroupBy(PlayerGame::getField);
    }

    private List<List<String>> getGroupBy(Function<PlayerGame, Object> function) {
        return service.all().stream()
                    .collect(groupingBy(function))
                    .values().stream()
                    .map(group -> group.stream()
                            .map(pg -> pg.getPlayer().getId())
                            .collect(toList()))
                    .collect(toList());
    }

    public Map<String, Object> getScores() {
        return service.all().stream()
                .collect(toMap(pg -> pg.getPlayer().getId(),
                        pg -> pg.getPlayer().getScore()));
    }

    public List<PScoresOf> getScoresForGame(String gameName) {
        return scoresFor(pg -> pg.getPlayer().getGameName().equals(gameName));
    }

    private List<PScoresOf> scoresFor(Predicate<PlayerGame> predicate) {
        return service.all().stream()
                .filter(predicate)
                .map(pg -> new PScoresOf(pg))
                .collect(toList());
    }

    public List<PScoresOf> getScoresForRoom(String roomName) {
        return scoresFor(pg -> pg.getRoomName().equals(roomName));
    }

    public Map<String, String> getReadableNames() {
        return service.all().stream()
                .collect(toMap(pg -> pg.getPlayer().getId(),
                        pg -> pg.getPlayer().getNotNullReadableName()));
    }
}
