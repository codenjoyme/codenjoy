package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import org.fest.reflect.core.Reflection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.utils.JsonUtils.prettyPrint;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DealsViewTest {

    private DealsView dealsView;
    private Deals deals;
    private List<Player> players;
    private List<Game> games;
    private List<Controller> controllers;
    private List<GameType> gameTypes;
    private List<HeroData> heroesData;
    private List<GamePlayer> gamePlayers;
    private List<PlayerScores> playerScores;

    @Before
    public void setup() {
        deals = new Deals();
        deals.spreader = new Spreader();
        dealsView = new DealsView();
        dealsView.service = deals;

        players = new LinkedList<>();
        games = new LinkedList<>();
        controllers = new LinkedList<>();
        gameTypes = new LinkedList<>();
        heroesData = new LinkedList<>();
        gamePlayers = new LinkedList<>();
        playerScores = new LinkedList<>();
    }

    @Test
    public void testGetGamesDataMap_usersInSameGroup() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, GameData> dataMap = dealsView.getGamesDataMap();

        // then
        String expectedGroup = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user1':{\n" +
                "      'additionalData':'data1',\n" +
                "      'coordinate':{\n" +
                "        'x':1,\n" +
                "        'y':2\n" +
                "      },\n" +
                "      'level':10,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user2':{\n" +
                "      'additionalData':'data2',\n" +
                "      'coordinate':{\n" +
                "        'x':3,\n" +
                "        'y':4\n" +
                "      },\n" +
                "      'level':11,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user3':{\n" +
                "      'additionalData':{\n" +
                "        'key':'value'\n" +
                "      },\n" +
                "      'coordinate':{\n" +
                "        'x':5,\n" +
                "        'y':6\n" +
                "      },\n" +
                "      'level':12,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user4':{\n" +
                "      'additionalData':[\n" +
                "        'data3, data4'\n" +
                "      ],\n" +
                "      'coordinate':{\n" +
                "        'x':7,\n" +
                "        'y':8\n" +
                "      },\n" +
                "      'level':13,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user1',\n" +
                "    'user2',\n" +
                "    'user3',\n" +
                "    'user4'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user1':'readable_user1',\n" +
                "    'user2':'readable_user2',\n" +
                "    'user3':'readable_user3',\n" +
                "    'user4':'readable_user4'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user1':123,\n" +
                "    'user2':234,\n" +
                "    'user3':345,\n" +
                "    'user4':456\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user1':0,\n" +
                "    'user2':0,\n" +
                "    'user3':0,\n" +
                "    'user4':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup, prettyPrint(dataMap.get("user1")));
    }

    @Test
    public void testGetGamesDataMap_usersInSameGroup_butDifferentTeams() {
        // given
        testGetGamesDataMap_usersInSameGroup();

        deals.setTeam("user1", 1);
        deals.setTeam("user2", 2);
        deals.setTeam("user3", 1);
        deals.setTeam("user4", 2);

        // when
        Map<String, GameData> dataMap = dealsView.getGamesDataMap();

        // then
        String expectedGroup = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user1':{\n" +
                "      'additionalData':'data1',\n" +
                "      'coordinate':{\n" +
                "        'x':1,\n" +
                "        'y':2\n" +
                "      },\n" +
                "      'level':10,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user2':{\n" +
                "      'additionalData':'data2',\n" +
                "      'coordinate':{\n" +
                "        'x':3,\n" +
                "        'y':4\n" +
                "      },\n" +
                "      'level':11,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user3':{\n" +
                "      'additionalData':{\n" +
                "        'key':'value'\n" +
                "      },\n" +
                "      'coordinate':{\n" +
                "        'x':5,\n" +
                "        'y':6\n" +
                "      },\n" +
                "      'level':12,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user4':{\n" +
                "      'additionalData':[\n" +
                "        'data3, data4'\n" +
                "      ],\n" +
                "      'coordinate':{\n" +
                "        'x':7,\n" +
                "        'y':8\n" +
                "      },\n" +
                "      'level':13,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user1',\n" +
                "    'user2',\n" +
                "    'user3',\n" +
                "    'user4'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user1':'readable_user1',\n" +
                "    'user2':'readable_user2',\n" +
                "    'user3':'readable_user3',\n" +
                "    'user4':'readable_user4'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user1':123,\n" +
                "    'user2':234,\n" +
                "    'user3':345,\n" +
                "    'user4':456\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user1':1,\n" +
                "    'user2':2,\n" +
                "    'user3':1,\n" +
                "    'user4':2\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup, prettyPrint(dataMap.get("user1")));
    }

    private void givenUsersInSameGroup() {
        GameField field = mock(GameField.class); // same group
        GameType gameType = addNewGameType("game1", 1234, inv -> field);

        addNewPlayer(gameType, 123, getHeroDataForAllPlayers(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroDataForAllPlayers(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroDataForAllPlayers(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroDataForAllPlayers(13, pt(7, 8), Arrays.asList("data3, data4")));
    }

    @Test
    public void testGetGamesDataMap_usersInSeveralGroups() {
        // given
        givenUsersInSeveralGroups_inOneGeneralGameRoom();

        // when
        Map<String, GameData> dataMap = dealsView.getGamesDataMap();

        // then
        String expectedGroup1 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user1':{\n" +
                "      'additionalData':'data1',\n" +
                "      'coordinate':{\n" +
                "        'x':1,\n" +
                "        'y':2\n" +
                "      },\n" +
                "      'level':10,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user2':{\n" +
                "      'additionalData':'data2',\n" +
                "      'coordinate':{\n" +
                "        'x':3,\n" +
                "        'y':4\n" +
                "      },\n" +
                "      'level':11,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user1',\n" +
                "    'user2'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user1':'readable_user1',\n" +
                "    'user2':'readable_user2'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user1':123,\n" +
                "    'user2':234\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user1':0,\n" +
                "    'user2':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup1, prettyPrint(dataMap.get("user1")));
        assertEquals(expectedGroup1, prettyPrint(dataMap.get("user2")));

        String expectedGroup2 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user3':{\n" +
                "      'additionalData':{\n" +
                "        'key':'value'\n" +
                "      },\n" +
                "      'coordinate':{\n" +
                "        'x':5,\n" +
                "        'y':6\n" +
                "      },\n" +
                "      'level':12,\n" +
                "      'multiplayer':false\n" +
                "    },\n" +
                "    'user4':{\n" +
                "      'additionalData':[\n" +
                "        'data3, data4'\n" +
                "      ],\n" +
                "      'coordinate':{\n" +
                "        'x':7,\n" +
                "        'y':8\n" +
                "      },\n" +
                "      'level':13,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user3',\n" +
                "    'user4'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user3':'readable_user3',\n" +
                "    'user4':'readable_user4'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user3':345,\n" +
                "    'user4':456\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user3':0,\n" +
                "    'user4':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup2, prettyPrint(dataMap.get("user3")));
        assertEquals(expectedGroup2, prettyPrint(dataMap.get("user4")));
    }

    private <T> Map<String, T> sortKeys(Map<String, T> map) {
        return new TreeMap<>(map);
    }

    @Test
    public void testGetGroupsMap_usersInSameGroup() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, List<String>> map = dealsView.getGroupsMap();

        // then
        assertEquals("{user1=[user1, user2, user3, user4], " + // all together
                        "user2=[user1, user2, user3, user4], " +
                        "user3=[user1, user2, user3, user4], " +
                        "user4=[user1, user2, user3, user4]}",
                sortKeys(map).toString());
    }

    @Test
    public void testGetGroupsMap_usersInSeveralGroups() {
        // given
        givenUsersInSeveralGroups_inOneGeneralGameRoom();

        // when
        Map<String, List<String>> map = dealsView.getGroupsMap();

        // then
        assertEquals("{user1=[user1, user2], " + // group 1
                        "user2=[user1, user2], " +
                        "user3=[user3, user4], " +       // group 2
                        "user4=[user3, user4]}",
                sortKeys(map).toString());
    }

    @Test
    public void testGetGroupsByField_usersInSameGroup() {
        // given
        givenUsersInSameGroup();

        // when
        List<List<String>> groups = dealsView.getGroupsByField();

        // then
        assertEquals("[[user1, user2, user3, user4]]", // all together
                groups.toString());
    }

    @Test
    public void testGetGroupsByField_usersInSeveralGroups() {
        // given
        givenUsersInSeveralGroups_inOneGeneralGameRoom();

        // when
        List<List<String>> groups = dealsView.getGroupsByField();

        // then
        groups.forEach(list -> Collections.sort(list));
        Collections.sort(groups, Comparator.comparing(Object::toString));

        assertEquals("[[user1, user2], " +   // group 1
                        "[user3, user4]]",   // group 2
                groups.toString());
    }

    private void givenUsersInSeveralGroups_inOneGeneralGameRoom() {
        GameField field1 = mock(GameField.class);
        GameField field2 = mock(GameField.class);
        List<GameField> fields = new LinkedList<GameField>(){{
            addAll(Arrays.asList(field1, field1));
            addAll(Arrays.asList(field2, field2));
        }};
        GameType gameType = addNewGameType("game1", 1234, inv -> fields.remove(0));

        // комната будет у всех одна, общая игровая game1
        // но сама игра говорит, что fields будут у них не общие
        addNewPlayer(gameType, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));
    }

    private void givenUsersInSeveralGroups_withDifferentRooms() {
        GameField field1 = mock(GameField.class);
        GameField field2 = mock(GameField.class);
        List<GameField> fields = new LinkedList<GameField>(){{
            addAll(Arrays.asList(field1, field1));
            addAll(Arrays.asList(field2, field2));
        }};
        GameType gameType = addNewGameType("game1", 1234, inv -> fields.remove(0));

        // отличия от givenUsersInSeveralGroups метода только в явно указанных комнатах тут
        // симулируем тут две комнаты для одной игры
        addNewPlayer(gameType, "room1", 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, "room1", 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, "room2", 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, "room2", 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));
    }

    @Test
    public void testGetGroupsByRooms_usersInSeveralGroups_separatedByRooms() {
        // given
        givenUsersInSeveralGroups_withDifferentRooms();

        // when
        List<List<String>> groups = dealsView.getGroupsByRooms();

        // then
        assertEquals("[[user1, user2], " +   // group 1
                        "[user3, user4]]",   // group 2
                groups.toString());
    }

    @Test
    public void testGetGroupsByRooms_usersInSeveralGroups_inOneGeneralGameRoom() {
        // given
        givenUsersInSeveralGroups_inOneGeneralGameRoom();

        // when
        List<List<String>> groups = dealsView.getGroupsByRooms();

        // then
        assertEquals("[[user1, user2, user3, user4]]", // all together
                groups.toString());
    }

    @Test
    public void testGetScoresForGame() {
        // given
        testGetGamesDataMap_singleGames();

        // юзера которые не должны войти в запрос
        GameType gameType2 = addNewGameType("game2", 1234, inv -> mock(GameField.class));
        addNewPlayer(gameType2, 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType2, 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        List<PScoresOf> scores = dealsView.getScoresForGame("game1");

        // then
        assertEquals("[\n" +
                        "  {\n" +
                        "    'score':123,\n" +
                        "    'teamId':0,\n" +
                        "    'name':'readable_user1',\n" +
                        "    'id':'user1'\n" +
                        "  },\n" +
                        "  {\n" +
                        "    'score':234,\n" +
                        "    'teamId':0,\n" +
                        "    'name':'readable_user2',\n" +
                        "    'id':'user2'\n" +
                        "  },\n" +
                        "  {\n" +
                        "    'score':345,\n" +
                        "    'teamId':0,\n" +
                        "    'name':'readable_user3',\n" +
                        "    'id':'user3'\n" +
                        "  },\n" +
                        "  {\n" +
                        "    'score':456,\n" +
                        "    'teamId':0,\n" +
                        "    'name':'readable_user4',\n" +
                        "    'id':'user4'\n" +
                        "  }\n" +
                        "]",
                prettyPrint(new JSONArray(scores)));
    }

    @Test
    public void testGetScoresForGame_caseJsonScore() {
        // given
        testGetScoresForGame();

        // переопределяем скоры игроков, так чтобы они были уже в json зашиты
        assertEquals(6, playerScores.size());
        for (int index = 0; index < players.size(); index++) {
            Player player = players.get(index);
            PlayerScores score = playerScores.get(index);
            JSONObject json = new JSONObject() {{
                // + 1000 чтобы быть точно уверенными, что мы тут были
                put("score", (Integer)player.getScore() + 1000);
                put("data", "something");
            }};
            when(score.getScore()).thenReturn(json);
        }

        // when
        List<PScoresOf> scores = dealsView.getScoresForGame("game1");

        // then
        assertEquals("[{'score':1123,'teamId':0,'name':'readable_user1','id':'user1'}," +
                        "{'score':1234,'teamId':0,'name':'readable_user2','id':'user2'}," +
                        "{'score':1345,'teamId':0,'name':'readable_user3','id':'user3'}," +
                        "{'score':1456,'teamId':0,'name':'readable_user4','id':'user4'}]",
                JsonUtils.clean(JsonUtils.toStringSorted(new JSONArray(scores))));
    }

    @Test
    public void testGetScoresForRoom() {
        // given
        GameField field1 = mock(GameField.class);
        GameField field2 = mock(GameField.class);
        List<GameField> fields = new LinkedList<GameField>(){{
            addAll(Arrays.asList(field1, field1, field1, field1));
            addAll(Arrays.asList(field2, field2));
        }};
        GameType gameType = addNewGameType("game1", 1234, inv -> fields.remove(0));

        addNewPlayer(gameType, "room1", 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, "room1", 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, "room1", 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, "room1", 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        // юзера которые не должны войти в запрос
        addNewPlayer(gameType, "room2", 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType, "room2", 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        List<PScoresOf> scores = dealsView.getScoresForRoom("room1");

        // then
        assertEquals("[{'score':123,'teamId':0,'name':'readable_user1','id':'user1'}," +
                        "{'score':234,'teamId':0,'name':'readable_user2','id':'user2'}," +
                        "{'score':345,'teamId':0,'name':'readable_user3','id':'user3'}," +
                        "{'score':456,'teamId':0,'name':'readable_user4','id':'user4'}]",
                JsonUtils.clean(JsonUtils.toStringSorted(new JSONArray(scores))));
    }

    @Test
    public void testGetScores() {
        // given
        GameField field1 = mock(GameField.class);
        GameField field2 = mock(GameField.class);
        List<GameField> fields = new LinkedList<GameField>(){{
            addAll(Arrays.asList(field1, field1, field1, field1));
            addAll(Arrays.asList(field2, field2));
        }};
        GameType gameType = addNewGameType("game1", 1234, inv -> fields.remove(0));

        addNewPlayer(gameType, "room1", 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, "room1", 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, "room1", 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, "room1", 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        addNewPlayer(gameType, "room2", 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType, "room2", 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        Map<String, Object> scores = dealsView.getScores();

        // then
        assertEquals("{user1=123, user2=234, user3=345, user4=456, user5=678, user6=789}",
                sortKeys(scores).toString());
    }

    @Test
    public void testGetGamesDataMap_singleGames() {
        // given
        // separate groups
        GameType gameType = addNewGameType("game1", 1234, inv -> mock(GameField.class));

        addNewPlayer(gameType, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        // when
        Map<String, GameData> dataMap = dealsView.getGamesDataMap();

        // then
        String expectedGroup1 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user1':{\n" +
                "      'additionalData':'data1',\n" +
                "      'coordinate':{\n" +
                "        'x':1,\n" +
                "        'y':2\n" +
                "      },\n" +
                "      'level':10,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user1'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user1':'readable_user1'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user1':123\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user1':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup1, prettyPrint(dataMap.get("user1")));

        String expectedGroup2 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user2':{\n" +
                "      'additionalData':'data2',\n" +
                "      'coordinate':{\n" +
                "        'x':3,\n" +
                "        'y':4\n" +
                "      },\n" +
                "      'level':11,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user2'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user2':'readable_user2'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user2':234\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user2':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup2, prettyPrint(dataMap.get("user2")));

        String expectedGroup3 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user3':{\n" +
                "      'additionalData':{\n" +
                "        'key':'value'\n" +
                "      },\n" +
                "      'coordinate':{\n" +
                "        'x':5,\n" +
                "        'y':6\n" +
                "      },\n" +
                "      'level':12,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user3'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user3':'readable_user3'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user3':345\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user3':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup3, prettyPrint(dataMap.get("user3")));

        String expectedGroup4 = "{\n" +
                "  'boardSize':1234,\n" +
                "  'coordinates':{\n" +
                "    'user4':{\n" +
                "      'additionalData':[\n" +
                "        'data3, data4'\n" +
                "      ],\n" +
                "      'coordinate':{\n" +
                "        'x':7,\n" +
                "        'y':8\n" +
                "      },\n" +
                "      'level':13,\n" +
                "      'multiplayer':false\n" +
                "    }\n" +
                "  },\n" +
                "  'decoder':{\n" +
                "    \n" +
                "  },\n" +
                "  'group':[\n" +
                "    'user4'\n" +
                "  ],\n" +
                "  'readableNames':{\n" +
                "    'user4':'readable_user4'\n" +
                "  },\n" +
                "  'scores':{\n" +
                "    'user4':456\n" +
                "  },\n" +
                "  'teams':{\n" +
                "    'user4':0\n" +
                "  }\n" +
                "}";

        assertEquals(expectedGroup4, prettyPrint(dataMap.get("user4")));
    }

    @Test
    public void testGetReadableNames() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, String> names = dealsView.getReadableNames();

        // then
        assertEquals("{user1=readable_user1, user2=readable_user2, " +
                        "user3=readable_user3, user4=readable_user4}",
                sortKeys(names).toString());

    }

    private HeroData getHeroDataForAllPlayers(int level, Point coordinate, Object additionalData) {
        return getHeroData(level, coordinate, additionalData);
    }

    private HeroData getHeroData(int level, Point coordinate, Object additionalData) {
        HeroData result = new HeroDataImpl(level, coordinate,
                MultiplayerType.SINGLE.isMultiplayer());
        Reflection.field("additionalData").ofType(Object.class).in(result).set(additionalData);
        return result;
    }

    private GameType addNewGameType(String game, int boardSize, Answer<Object> fieldSupplier) {
        GameType result = mock(GameType.class);
        when(result.getBoardSize(any())).thenReturn(new SimpleParameter<>(boardSize));
        when(result.name()).thenReturn(game);
        when(result.getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);
        when(result.getPrinterFactory()).thenReturn(mock(PrinterFactory.class));
        when(result.createPlayer(any(EventListener.class), anyInt(), anyString(), any()))
                .thenAnswer(inv -> gamePlayers.get(gamePlayers.size() - 1));
        when(result.createGame(anyInt(), any())).thenAnswer(fieldSupplier);
        gameTypes.add(result);
        return result;
    }

    private Deal addNewPlayer(GameType gameType, int scores, HeroData heroData) {
        return addNewPlayer(gameType, gameType.name(), scores, heroData);
    }

    private Deal addNewPlayer(GameType gameType, String room, int scores, HeroData heroData) {
        PlayerScores gameScore = mock(PlayerScores.class);
        playerScores.add(gameScore);
        when(gameScore.getScore()).thenReturn(scores);

        GamePlayer gamePlayer = mock(GamePlayer.class);
        when(gamePlayer.getHeroData()).thenReturn(heroData);
        gamePlayers.add(gamePlayer);
        heroesData.add(heroData);

        String name = getNextName();
        Player player = new Player(name, "http://" + name + ".com:8080", gameType, gameScore, null);
        player.setEventListener(mock(InformationCollector.class));
        player.setReadableName("readable_" + name);
        players.add(player);

        Controller controller = mock(Controller.class);
        controllers.add(controller);
        Deal deal = deals.add(player, room, null);
        games.add(deal.getGame());
        return deal;
    }

    private String getNextName() {
        return "user" + (players.size() + 1);
    }

    enum Element1 implements CharElement {
        A('a'),
        B('b'),
        C('c');

        private final char ch;

        Element1(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }
    }

    enum Element2 implements CharElement {
        ONE('1'),
        TWO('2'),
        THREE('3'),
        FOUR('4');

        private final char ch;

        Element2(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }
    }

    @Test
    public void shouldGetDecoders() {
        // given
        GameType gameType1 = addNewGameType("game1", 1234, inv -> mock(GameField.class));
        when(gameType1.getPlots()).thenReturn(Element1.values());

        GameType gameType2 = addNewGameType("game2", 1234, inv -> mock(GameField.class));
        when(gameType2.getPlots()).thenReturn(Element2.values());

        addNewPlayer(gameType1, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType2, 234, getHeroData(11, pt(3, 4), "data2"));

        // when then
        assertEquals("{game1=[abc -> ABC], " +
                        "game2=[1234 -> ABCD]}",
                dealsView.getDecoders().toString());
    }
}
