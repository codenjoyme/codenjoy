package com.codenjoy.dojo.web.rest;

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

        register(0, "player1");
        register(0, "player2");
        register(1, "player3");
        register(2, "player4");
        register(2, "player5");
        register(2, "player6");

        asAdmin();
    }

    private void register(int teamId, String id) {
        PlayerGame registeredPlayer = register(id, "ip", "validRoom", "first");
        registeredPlayer.getGame().getPlayer().setTeamId(teamId);
    }

    @Test
    public void getTeamInfo() {
        String expected = "" +
                "[{\"teamId\":0,\"players\":[\"player1\",\"player2\"]}," +
                "{\"teamId\":1,\"players\":[\"player3\"]}," +
                "{\"teamId\":2,\"players\":[\"player4\",\"player5\",\"player6\"]}]";
        assertEquals(expected, get("/rest/team"));
    }

    @Test
    public void distributePlayersByTeam() {
        post(202, "/rest/team",
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
        String expected = "[10: player1][10: player3][10: player5][20: player2][20: player4][20: player6]";
        assertEquals(expected, playerGamesString());
    }

    private String playerGamesString() {
        return playerGames.all().stream()
                .map(pg -> String.format("[%d: %s]",
                        pg.getGame().getPlayer().getTeamId(), pg.getPlayerId()))
                .sorted()
                .reduce(String::concat)
                .orElse(StringUtils.EMPTY);
    }
}