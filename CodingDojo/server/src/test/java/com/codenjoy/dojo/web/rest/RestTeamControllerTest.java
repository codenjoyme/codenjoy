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

import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;

@Import(RestTeamControllerTest.ContextConfiguration.class)
public class RestTeamControllerTest extends AbstractRestControllerTest {

    @Autowired
    private PlayerGames playerGames;

    @Before
    public void setUp() {
        super.setUp();
        playerService.removeAll();
        roomService.removeAll();
        registration.removeAll();

        register("room1", 0, "player1");
        register("room1", 0, "player2");
        register("room1", 1, "player3");
        register("room1", 2, "player4");
        register("room1", 2, "player5");
        register("room1", 2, "player6");
        register("room2", 1, "player7");

        asAdmin();
    }

    private void register(String room, int teamId, String id) {
        PlayerGame registeredPlayer = register(id, "ip", room, "first");
        registeredPlayer.getGame().getPlayer().setTeamId(teamId);
    }

    @Test
    public void getTeamInfo() {
        String expected = "" +
                "[{\"teamId\":0,\"players\":[\"player1\",\"player2\"]}," +
                "{\"teamId\":1,\"players\":[\"player3\"]}," +
                "{\"teamId\":2,\"players\":[\"player4\",\"player5\",\"player6\"]}]";
        assertEquals(expected, get("/rest/team/room/room1"));
    }

    @Test
    public void distributePlayersByTeam() {
        post(202, "/rest/team/room/room1",
                        "[\n" +
                        "    {\n" +
                        "        \"teamId\": 10,\n" +
                        "        \"players\": [\n" +
                        "            \"player1\",\n" +
                        "            \"player3\",\n" +
                        "            \"player5\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"teamId\": 20,\n" +
                        "        \"players\": [\n" +
                        "            \"player2\",\n" +
                        "            \"player4\",\n" +
                        "            \"player6\"" +
                        "        ]\n" +
                        "    }\n" +
                        "]");
        assertEquals("[room `room1`, teamId `10`: player1]" +
                "[room `room1`, teamId `10`: player3]" +
                "[room `room1`, teamId `10`: player5]" +
                "[room `room1`, teamId `20`: player2]" +
                "[room `room1`, teamId `20`: player4]" +
                "[room `room1`, teamId `20`: player6]" +
                "[room `room2`, teamId `1`: player7]", playerGamesString());
    }

    private String playerGamesString() {
        return playerGames.all().stream()
                .map(pg -> String.format("[room `%s`, teamId `%d`: %s]",
                        pg.getRoom(),
                        pg.getGame().getPlayer().getTeamId(),
                        pg.getPlayerId()))
                .sorted()
                .reduce(String::concat)
                .orElse(StringUtils.EMPTY);
    }
}
