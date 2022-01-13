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
import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class BoardService {

    private final PlayerGameSaver playerGameSaver;
    private final Registration registration;

    private final Map<String, Map<String, Integer>> leaderboardsCache;

    @Autowired
    public BoardService(PlayerGameSaver playerGameSaver, Registration registration) {
        this.playerGameSaver = playerGameSaver;
        this.registration = registration;
        leaderboardsCache = new ConcurrentHashMap<>();
    }

    public void savePlayerForGame(Player player) {
        String playerName = player.getReadableName();
        String game = player.getGame();

        if (!leaderboardsCache.containsKey(game)) {
            leaderboardsCache.put(game, new TreeMap<>());
        }
        leaderboardsCache.get(game).put(playerName, (int) player.getScore());
        sortPlayersByScore(game);
    }

    public void updateLeaderboardScore(String name, String game, long score) {
        leaderboardsCache.get(game).replace(name, (int) score);
        sortPlayersByScore(game);
    }

    public Map<Integer, Map.Entry<String, Integer>> getPlayersForGame(String game) {
        if (leaderboardsCache.get(game) == null) {
            leaderboardsCache.put(game, getUserNamesById(game));
        }
        return sortPlayersByScoreAndCountThem(game);
    }

    public void removePlayerFromGame(String name, String game) {
        leaderboardsCache.get(game).remove(name);
    }


    private void sortPlayersByScore(String game) {
        leaderboardsCache.replace(game, leaderboardsCache.get(game).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
//        return leaderboardsCache.get(game).entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<Integer, Map.Entry<String, Integer>> sortPlayersByScoreAndCountThem(String game) {
        Map<Integer, Map.Entry<String, Integer>> numeratedLeaderboard = new HashMap<>();
        sortPlayersByScore(game);

        int counter = 1;
        for (Map.Entry<String,Integer> entry: leaderboardsCache.get(game).entrySet()) {
            numeratedLeaderboard.put(counter, entry);
            counter++;
        }

        return numeratedLeaderboard;
    }

    private Map<String, Integer> getUserNamesById(String game) {
        Map<String, Integer> leaderboard = playerGameSaver.getPlayerIdAndScoresForGame(game);
        List<Registration.User> allUsers = registration.getUsers();

        Map<String, Integer> usersForUi = new HashMap<>();
        allUsers.stream()
                .filter(user -> leaderboard.containsKey(user.getId()))
                .forEach(user -> usersForUi.put(user.getReadableName(), leaderboard.get(user.getId())));

        return usersForUi;
    }
}
