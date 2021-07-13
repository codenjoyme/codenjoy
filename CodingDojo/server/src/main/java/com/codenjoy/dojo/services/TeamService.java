package com.codenjoy.dojo.services;

import com.codenjoy.dojo.web.rest.pojo.PTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<Integer, List<String>> playersByTeam = playerGames.stream()
                .collect(Collectors.groupingBy(
                        pg -> pg.getGame().getPlayer().getTeamId(),
                        mapping(PlayerGame::getPlayerId, toList())));
        playersByTeam.forEach((t, p) -> teams.add(new PTeam(t, p)));
        return teams;
    }

    public void distributePlayersByTeam(@RequestBody List<PTeam> teams) {
        for (PTeam team : teams) {
            Integer teamId = team.getTeamId();
            for (String playerId : team.getPlayers()) {
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
}
