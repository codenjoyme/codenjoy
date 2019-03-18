package com.codenjoy.dojo.services.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.transport.ws.ResponseHandler;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ScreenResponseHandlerTest {

    private PlayerTransport transport;
    private Player player;
    private ResponseHandler handler;
    private PlayerSocket socket;

    @Before
    public void setup() throws Exception {
        transport = mock(PlayerTransport.class);
        player = new Player();
        handler = new ScreenResponseHandler(transport, player);
    }

    @Test
    public void shouldOnResponse_whenOnResponse_caseAllPlayersScreen_distinctByGroups() {
        // given

        // when
        handler.onResponse(socket,
                "{'name':getScreen, 'allPlayersScreen':true, " +
                        "'players':[], 'gameName':'game'}");

        // then
        Function function = verifySetFilterFor();

        Map<Player, PlayerData> map = getDummyPlayers();

        // when
        JSONObject result = (JSONObject)function.apply(map);

        // then
        assertEquals("{\n" +
                "  'player2':{\n" +
                "    'board':'some_board2',\n" +
                "    'boardSize':12,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates1',\n" +
                "      'group':[\n" +
                "        'player1',\n" +
                "        'player2'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info2',\n" +
                "    'score':546,\n" +
                "    'scores':{\n" +
                "      'player1':100,\n" +
                "      'player2':200\n" +
                "    }\n" +
                "  },\n" +
                "  'player4':{\n" +
                "    'board':'some_board4',\n" +
                "    'boardSize':45,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates4',\n" +
                "      'group':[\n" +
                "        'player4'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info4',\n" +
                "    'score':765,\n" +
                "    'scores':{\n" +
                "      'player4':400\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonUtils.prettyPrint(result));
    }

    @Test
    public void shouldOnResponse_whenOnResponse_caseAllPlayersScreen_noGroupsAllSelected() {
        // given

        // when
        handler.onResponse(socket,
                "{'name':getScreen, 'allPlayersScreen':true, " +
                        "'players':[], 'gameName':'game'}");

        // then
        Function function = verifySetFilterFor();

        Map<Player, PlayerData> map = getDummyPlayers();
        map.get(new Player("player1")).getHeroesData().getJSONArray("group").remove(1);
        map.get(new Player("player1")).getScores().remove("player2");

        map.get(new Player("player2")).getHeroesData().getJSONArray("group").remove(0);
        map.get(new Player("player2")).getScores().remove("player1");

        // when
        JSONObject result = (JSONObject)function.apply(map);

        // then
        assertEquals("{\n" +
                "  'player1':{\n" +
                "    'board':'some_board1',\n" +
                "    'boardSize':10,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates1',\n" +
                "      'group':[\n" +
                "        'player1'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info1',\n" +
                "    'score':134,\n" +
                "    'scores':{\n" +
                "      'player1':100\n" +
                "    }\n" +
                "  },\n" +
                "  'player2':{\n" +
                "    'board':'some_board2',\n" +
                "    'boardSize':12,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates1',\n" +
                "      'group':[\n" +
                "        'player2'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info2',\n" +
                "    'score':546,\n" +
                "    'scores':{\n" +
                "      'player2':200\n" +
                "    }\n" +
                "  },\n" +
                "  'player4':{\n" +
                "    'board':'some_board4',\n" +
                "    'boardSize':45,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates4',\n" +
                "      'group':[\n" +
                "        'player4'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info4',\n" +
                "    'score':765,\n" +
                "    'scores':{\n" +
                "      'player4':400\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonUtils.prettyPrint(result));
    }

    @Test
    public void shouldOnResponse_whenOnResponse_caseSelectedPlayer_fromOtherGame() {
        // given

        // when
        handler.onResponse(socket,
                "{'name':getScreen, 'allPlayersScreen':false, " +
                        "'players':['player3'], 'gameName':'other_game'}");

        // then
        Function function = verifySetFilterFor();

        Map<Player, PlayerData> map = getDummyPlayers();

        // when
        JSONObject result = (JSONObject)function.apply(map);

        // then
        assertEquals("{\n" +
                "  'player3':{\n" +
                "    'board':'some_board3',\n" +
                "    'boardSize':14,\n" +
                "    'gameName':'other_game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates1',\n" +
                "      'group':[\n" +
                "        'player3'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info3',\n" +
                "    'score':235,\n" +
                "    'scores':{\n" +
                "      'player3':300\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonUtils.prettyPrint(result));
    }

    @Test
    public void shouldOnResponse_whenOnResponse_caseSelectedPlayer() {
        // given

        // when
        handler.onResponse(socket,
                "{'name':getScreen, 'allPlayersScreen':false, " +
                        "'players':['player2'], 'gameName':'game'}");

        // then
        Function function = verifySetFilterFor();

        Map<Player, PlayerData> map = getDummyPlayers();

        // when
        JSONObject result = (JSONObject)function.apply(map);

        // then
        assertEquals("{\n" +
                "  'player2':{\n" +
                "    'board':'some_board2',\n" +
                "    'boardSize':12,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'coordinates':'coordinates1',\n" +
                "      'group':[\n" +
                "        'player1',\n" +
                "        'player2'\n" +
                "      ]\n" +
                "    },\n" +
                "    'info':'some_info2',\n" +
                "    'score':546,\n" +
                "    'scores':{\n" +
                "      'player1':100,\n" +
                "      'player2':200\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonUtils.prettyPrint(result));
    }

    private Map<Player, PlayerData> getDummyPlayers() {
        Map<Player, PlayerData> map = new HashMap<>();

        Player player1 = new Player("player1");
        player1.setGameName("game");
        map.put(player1, new PlayerData(10, "some_board1", "game",
                134, "some_info1", new JSONObject("{'player1':100,'player2':200}"),
                new JSONObject("{'coordinates':'coordinates1','group':['player1','player2']}")));

        Player player2 = new Player("player2");
        player2.setGameName("game");
        map.put(player2, new PlayerData(12, "some_board2", "game",
                546, "some_info2", new JSONObject("{'player1':100,'player2':200}"),
                new JSONObject("{'coordinates':'coordinates1','group':['player1','player2']}")));

        Player player4 = new Player("player4");
        player4.setGameName("game");
        map.put(player4, new PlayerData(45, "some_board4", "game",
                765, "some_info4", new JSONObject("{'player4':400}"),
                new JSONObject("{'coordinates':'coordinates4','group':['player4']}")));

        Player player3 = new Player("player3");
        player3.setGameName("other_game");
        map.put(player3, new PlayerData(14, "some_board3", "other_game",
                235, "some_info3", new JSONObject("{'player3':300}"),
                new JSONObject("{'coordinates':'coordinates1','group':['player3']}")));

        return map;
    }

    private Function verifySetFilterFor() {
        ArgumentCaptor<Function> result = ArgumentCaptor.forClass(Function.class);
        verify(transport).setFilterFor(eq(socket), result.capture());
        return result.getValue();
    }

}