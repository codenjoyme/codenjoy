package com.codenjoy.dojo.web.rest;

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

import com.codenjoy.dojo.config.ThreeGamesConfiguration;
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.mocks.*;
import com.codenjoy.dojo.services.multiplayer.GameRoom;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.json.JSONArray;
import org.junit.Before;
import org.springframework.context.annotation.Import;

import java.util.*;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static com.codenjoy.dojo.utils.JsonUtils.toStringSorted;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.joining;

@Import(ThreeGamesConfiguration.class)
public abstract class AbstractTeamControllerTest extends AbstractRestControllerTest  {

    public static final String game = "third";
    public static final String ip = "ip";
    public static final String room = "test";

    protected ThirdGameSettings settings;
    protected ThirdGameType type;

    @Before
    public void setUp() {
        super.setUp();

        login.removeAll();
        rooms.removeAll();

        // when changing teams, the current state is
        // saved to the database (there is a TeamId).
        // It should also be deleted between test runs.
        saves.removeAllSaves();

        // this is how we get the room settings
        type = (ThirdGameType) games.getGameType(game);
        settings = (ThirdGameSettings) rooms.create(room, type).getSettings();

        // We want to remember every field ever created,
        // in order to count the indices. So there we want to delete it.
        type.clear();

        login.asAdmin();

        // for all tests
        settings.playersAndTeamsPerRoom(4, 2);
    }

    public void givenPl(PTeam... teams) {
        for (PTeam team : teams) {
            for (String playerId : team.getPlayers()) {
                login.register(playerId, ip, room, game);
            }
        }
        teamService.distributePlayersByTeam(room, Arrays.asList(teams));
        removeDeadFields();
    }

    /**
     * Reset uninformative indexes that
     * could have been created during the
     * registration process, changing commands.
     */
    private void removeDeadFields() {
        type.fields().clear();
        type.fields().addAll((List)deals.rooms().values().stream()
                .map(room -> room.field())
                .distinct()
                .collect(toList()));
    }

    public void asrtTms(String expected) {
        String actual = deals.all().stream()
                .collect(groupingBy(Deal::getTeamId, TreeMap::new, toSet()))
                .entrySet().stream()
                .map(entry -> {
                    Integer teamId = entry.getKey();
                    String players = entry.getValue().stream()
                            .map(Deal::getPlayerId)
                            .sorted()
                            .collect(joining(", "));
                    return String.format("[%d: %s]", teamId, players);
                })
                .collect(joining("\n"));
        assertEquals(expected, actual);
    }

    public void asrtFld(String expected) {
        assertEquals(expected, getFieldAndTeamInfo());
    }

    public String getFieldAndTeamInfo() {
        Collection<GameRoom> rooms = deals.rooms().get(room);

        Map<String, Integer> playersTeams = deals.all().stream().
                collect(toMap(deal -> deal.getPlayerId(),
                        deal -> deal.getTeamId()));

        List<String> lines = type.fields().stream()
                .map(field -> {
                    String players = rooms.stream()
                            .filter(room -> room.field() == field)
                            .flatMap(room -> room.deals().stream())
                            .map(Deal::getPlayerId)
                            .map(id -> String.format("%s(t%s)", id, playersTeams.get(id)))
                            .collect(joining(", "));
                    return String.format("[f%s: %s]", type.fields().indexOf(field), players);
                })
                .collect(toList());

        removeFirstNulls(lines);

        return lines.stream().collect(joining("\n"));
    }

    private void removeFirstNulls(List<String> lines) {
        final String EMPTY = ": ]";

        for (int index = 0; index < lines.size() - 1; index++) {
            if (lines.get(index).contains(EMPTY)
                    && lines.get(index + 1).contains(EMPTY))
            {
                lines.remove(index);
                index--;
            } else {
                break;
            }
        }
    }

    public void callGet(PTeam... teams) {
        String expected = new JSONArray(Arrays.asList(teams)).toString();
        String actual = new JSONArray(get("/rest/team/room/" + room)).toString();
        assertEquals(expected, actual);
    }

    public void callPost(PTeam... teams) {
        post(202, "/rest/team/room/" + room,
                toStringSorted(Arrays.asList(teams)));
    }
}
