package com.codenjoy.dojo.services.controller;

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
import static org.mockito.Matchers.eq;
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
    public void shouldOnResponse_whenOnResponse_caseAllPlayersScreen() {
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
                "  'player1':{\n" +
                "    'board':'some_board1',\n" +
                "    'boardSize':10,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'hero':'herodata1'\n" +
                "    },\n" +
                "    'info':'some_info1',\n" +
                "    'score':134,\n" +
                "    'scores':{\n" +
                "      'score':'data1'\n" +
                "    }\n" +
                "  },\n" +
                "  'player2':{\n" +
                "    'board':'some_board2',\n" +
                "    'boardSize':12,\n" +
                "    'gameName':'game',\n" +
                "    'heroesData':{\n" +
                "      'hero':'herodata2'\n" +
                "    },\n" +
                "    'info':'some_info2',\n" +
                "    'score':546,\n" +
                "    'scores':{\n" +
                "      'score':'data2'\n" +
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
                "      'hero':'herodata2'\n" +
                "    },\n" +
                "    'info':'some_info2',\n" +
                "    'score':546,\n" +
                "    'scores':{\n" +
                "      'score':'data2'\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonUtils.prettyPrint(result));
    }

    private Map<Player, PlayerData> getDummyPlayers() {
        Map<Player, PlayerData> map = new HashMap<>();

        Player player1 = new Player("player1");
        player1.setGameName("game");
        map.put(player1, new PlayerData(10, "some_board1", "game",
                134, "some_info1", new JSONObject("{'score':'data1'}"),
                new JSONObject("{'hero':'herodata1'}")));

        Player player2 = new Player("player2");
        player2.setGameName("game");
        map.put(player2, new PlayerData(12, "some_board2", "game",
                546, "some_info2", new JSONObject("{'score':'data2'}"),
                new JSONObject("{'hero':'herodata2'}")));

        Player player3 = new Player("player3");
        player3.setGameName("other_game");
        map.put(player3, new PlayerData(14, "some_board3", "other_game",
                235, "some_info3", new JSONObject("{'score':'data3'}"),
                new JSONObject("{'hero':'herodata3'}")));
        return map;
    }

    private Function verifySetFilterFor() {
        ArgumentCaptor<Function> result = ArgumentCaptor.forClass(Function.class);
        verify(transport).setFilterFor(eq(socket), result.capture());
        return result.getValue();
    }

}