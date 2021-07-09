package com.codenjoy.dojo.web.controller;

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

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/team")
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@Slf4j
@RequiredArgsConstructor
public class TeamController {

    private final PlayerGames playerGames;
    private final GameSaver gameSaver;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamPlayers {

        private Integer teamId;
        private List<String> playersIds;
    }

    @GetMapping
    public List<TeamPlayers> getTeamsPlayersInfo() {
        List<TeamPlayers> teamsPlayers = new ArrayList<>();
        Map<Integer, List<String>> playersByTeam = playerGames.stream()
                .collect(Collectors.groupingBy(
                        pg -> pg.getGame().getPlayer().getTeamId(),
                        mapping(PlayerGame::getPlayerId, toList())));
        playersByTeam.forEach((t, ps) -> teamsPlayers.add(new TeamPlayers(t, ps)));
        return teamsPlayers;
    }

    @PutMapping
    public void distributePlayersByTeam(@RequestBody List<TeamPlayers> teamsPlayers) {
        for (TeamPlayers teamPlayers : teamsPlayers) {
            Integer teamId = teamPlayers.getTeamId();
            for (String playerId : teamPlayers.getPlayersIds()) {
                playerGames.stream()
                        .filter(pg -> pg.getPlayerId().equals(playerId))
                        .map(PlayerGame::getGame)
                        .map(Game::getPlayer)
                        .findFirst()
                        .ifPresentOrElse(gamePlayer -> gamePlayer.setTeamId(teamId),
                                () -> log.warn("playerId {} has not been found", playerId));
            }
        }
    }

    @PutMapping("/random")
    public void distributePlayersRandomly(@RequestParam Integer teamCapacity,
                                          @RequestBody List<String> playersIds) {
        List<TeamPlayers> teamPlayersToDistribute = new ArrayList<>();

        int newTeamsAmount = playersIds.size() / teamCapacity;
        if (playersIds.size() % teamCapacity != 0) {
            newTeamsAmount += 1;
        }

        Set<Integer> teamIds = getTeamsPlayersInfo().stream()
                .map(TeamPlayers::getTeamId)
                .collect(Collectors.toSet());

        for (int i = 0; i < newTeamsAmount; i++) {
            int newTeamId = 0;
            do {
                newTeamId++;
            } while (teamIds.contains(newTeamId));
            List<String> players = playersIds.subList(i * newTeamsAmount, newTeamsAmount);
            teamPlayersToDistribute.add(new TeamPlayers(newTeamId, players));
        }
        distributePlayersByTeam(teamPlayersToDistribute);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamScore {

        private TeamPlayers teamPlayers;
        private long score;

        public Integer getTeamId() {
            return teamPlayers.getTeamId();
        }
    }

    @GetMapping("/score")
    public List<TeamScore> calculateAllTeamsScores() {
        List<TeamScore> teamsScores = new ArrayList<>();
        for (TeamPlayers teamsPlayer : getTeamsPlayersInfo()) {
            Integer teamId = teamsPlayer.getTeamId();
            List<String> playersIds = teamsPlayer.playersIds;
            long score = gameSaver.loadAll(playersIds).stream()
                    .filter(ps -> ps.getScore() instanceof Number)
                    .mapToLong(ps -> (long) ps.getScore())
                    .sum();
            teamsScores.add(new TeamScore(new TeamPlayers(teamId, playersIds), score));
        }
        return teamsScores;
    }

    @GetMapping("/score/{teamId}")
    public TeamScore calculateTeamScores(@PathVariable int teamId) {
        return calculateAllTeamsScores().stream()
                .filter(teamScore -> teamScore.getTeamId() == teamId)
                .findFirst().orElseThrow();
    }
}
