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

import com.codenjoy.dojo.services.nullobj.NullDeal;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final Deals deals;
    private final SaveService saveService;

    public List<PTeam> getTeamInfo(String room) {
        return deals.getAll(Deals.withRoom(room)).stream()
                .collect(Collectors.groupingBy(
                        Deal::getTeamId,
                        mapping(Deal::getPlayerId, toList())))
                .entrySet().stream()
                .map(PTeam::new)
                .collect(toList());
    }

    public void distributePlayersByTeam(String room, List<PTeam> teams) {
        for (PTeam team : teams) {
            for (String id : team.getPlayers()) {
                Deal game = deals.get(id);
                if (game == NullDeal.INSTANCE) {
                    log.warn("Player with id '{}' has not been found", id);
                }
                if (room.equals(game.getRoom())) {
                    game.setTeamId(team.getTeamId());
                    // TODO #3d4w надо подумать и поменять всех участников комнаты что там остались
                    deals.reload(id, Sweeper.on().lastAlone());
                }
            }
        }
        saveService.saveAll();
    }
}
