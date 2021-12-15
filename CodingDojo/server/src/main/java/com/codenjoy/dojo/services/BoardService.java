package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class BoardService {

    private final PlayerGameSaver playerGameSaver;

    private final Map<String, Map<String, Integer>> leaderboards;

    @Autowired
    public BoardService(PlayerGameSaver playerGameSaver) {
        this.playerGameSaver = playerGameSaver;
        leaderboards = new ConcurrentHashMap<>();
    }

    public void savePlayerForGame(Player player) {
        String playerName = player.getReadableName();
        String game = player.getGame();
        if (!leaderboards.containsKey(game)) {
            leaderboards.put(game, new TreeMap<>());
        }
        leaderboards.get(game).put(playerName, (int) player.getScore());
        leaderboards.replace(game, sortPlayersByScore(game));
    }

    public void updateLeaderboardScore(String name, String game, long score) {
        leaderboards.get(game).replace(name, (int) score);
        leaderboards.replace(game, sortPlayersByScore(game));
    }

    public Map<String, Integer> getPlayersForGame(String game) {
        if (leaderboards.get(game) == null) {
            leaderboards.put(game, playerGameSaver.getPlayersForGame(game));
        }
        return sortPlayersByScore(game);
    }

    public void removePlayerFromGame(String name, String game) {
        leaderboards.get(game).remove(name);
    }


    private Map<String, Integer> sortPlayersByScore(String game) {
        return leaderboards.get(game).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
