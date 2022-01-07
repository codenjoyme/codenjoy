package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

@Component
public class DealsView {

    @Autowired
    protected Deals service;

    public Map<String, GameData> getGamesDataMap() {
        Map<String, GuiPlotColorDecoder> decoders = getDecoders();
        Map<String, List<String>> groupsMap = getGroupsMap();
        Map<String, Object> scores = getScores();
        Map<String, Integer> teams = getTeams();
        Map<String, HeroData> coordinates = getCoordinates();
        Map<String, String> readableNames = getReadableNames();

        return service.all().stream()
                .collect(toMap(
                        deal -> deal.getPlayer().getId(),
                        deal -> {
                            GameType<Settings> gameType = deal.getGameType();
                            String player = deal.getPlayer().getId();
                            List<String> group = groupsMap.get(player);

                            return new GameData(
                                    gameType.getBoardSize(gameType.getSettings()).getValue(),
                                    decoders.get(gameType.name()),
                                    filterByGroup(scores, group),
                                    filterByGroup(teams, group),
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
                .collect(toMap(deal -> deal.getPlayer().getId(),
                        deal -> deal.getGame().getHero()));
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
        return getGroupBy(Deal::getRoom);
    }

    public List<List<String>> getGroupsByField() {
        return getGroupBy(Deal::getField);
    }

    private List<List<String>> getGroupBy(Function<Deal, Object> function) {
        return service.all().stream()
                    .filter(deal -> Objects.nonNull(function.apply(deal)))
                    .collect(groupingBy(function))
                    .values().stream()
                    .map(group -> group.stream()
                            .map(deal -> deal.getPlayer().getId())
                            .collect(toList()))
                    .collect(toList());
    }

    public Map<String, Object> getScores() {
        return service.all().stream()
                .collect(toMap(deal -> deal.getPlayer().getId(),
                        deal -> deal.getPlayer().getScore()));
    }

    public Map<String, Integer> getTeams() {
        return service.all().stream()
                .collect(toMap(deal -> deal.getPlayer().getId(),
                        deal -> deal.getPlayer().getTeamId()));
    }

    public List<PScoresOf> getScoresForGame(String game) {
        return scoresFor(deal -> deal.getPlayer().getGame().equals(game));
    }

    private List<PScoresOf> scoresFor(Predicate<Deal> predicate) {
        return service.all().stream()
                .filter(predicate)
                .map(deal -> new PScoresOf(deal))
                .collect(toList());
    }

    public List<PScoresOf> getScoresForRoom(String room) {
        return scoresFor(deal -> deal.getRoom().equals(room));
    }

    public Map<String, String> getReadableNames() {
        return service.all().stream()
                .collect(toMap(deal -> deal.getPlayer().getId(),
                        deal -> deal.getPlayer().getNotNullReadableName()));
    }
}
