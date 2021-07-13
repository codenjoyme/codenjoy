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

import com.codenjoy.dojo.web.rest.pojo.PTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final PlayerGames playerGames;

    public List<PTeam> getTeamInfo() {
        List<PTeam> teams = new ArrayList<>();
        Map<Integer, List<PlayerGame>> playersByTeam = playerGames.all().stream()
                .collect(Collectors.groupingBy(
                        pg -> pg.getGame().getPlayer().getTeamId(),
                        mapping(Function.identity(), toList())));
        for (Map.Entry<Integer, List<PlayerGame>> entry : playersByTeam.entrySet()) {
            Integer teamId = entry.getKey();
            Map<String, List<String>> playersByRoom = entry.getValue().stream()
                    .collect(Collectors.groupingBy(PlayerGame::getRoom,
                            mapping(PlayerGame::getPlayerId, toList())));
            playersByRoom.forEach((room, players) -> teams.add(new PTeam(room, teamId, players)));
        }
        return teams;
    }

    public void distributePlayersByTeam(@RequestBody List<PTeam> teams) {
        for (PTeam team : teams) {
            String room = team.getRoom();
            int teamId = team.getTeamId();
            for (String playerId : team.getPlayers()) {
                playerGames.stream()
                        .filter(pg -> room.equals(team.getRoom()))
                        .filter(pg -> playerId.equals(pg.getPlayerId()))
                        .map(PlayerGame::getGame)
                        .map(Game::getPlayer)
                        .findFirst()
                        .ifPresentOrElse(gamePlayer -> gamePlayer.setTeamId(teamId),
                                () -> log.warn("playerId {} has not been found", playerId));
            }
        }
    }
}
