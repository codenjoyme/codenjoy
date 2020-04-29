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
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerGamesViewTest {

    private PlayerGamesView playerGamesView;
    private PlayerGames playerGames;
    private List<Player> players;
    private List<Game> games;
    private List<Controller> controllers;
    private List<GameType> gameTypes;
    private List<HeroData> heroesData;
    private List<GamePlayer> gamePlayers;

    @Before
    public void setup() {
        playerGames = new PlayerGames();
        playerGamesView = new PlayerGamesView();
        playerGamesView.service = playerGames;

        players = new LinkedList<>();
        games = new LinkedList<>();
        controllers = new LinkedList<>();
        gameTypes = new LinkedList<>();
        heroesData = new LinkedList<>();
        gamePlayers = new LinkedList<>();
    }

    @Test
    public void testGetGamesDataMap_usersInSameGroup() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, GameData> dataMap = playerGamesView.getGamesDataMap();

        // then
        String expectedGroup = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}," +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'group':['user1','user2','user3','user4']," +
                    "'readableNames':{'user1':'readable_user1','user2':'readable_user2','user3':'readable_user3','user4':'readable_user4'}" +
                "},'scores':{'user1':123,'user2':234,'user3':345,'user4':456}}";

        assertEquals(expectedGroup, toString(dataMap.get("user1")));
    }

    private void givenUsersInSameGroup() {
        GameField field = mock(GameField.class); // same group
        GameType gameType = addNewGameType("gameName1", 1234, inv -> field);

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
        Map<String, GameData> dataMap = playerGamesView.getGamesDataMap();

        // then
        String expectedGroup1 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'group':['user1','user2']," +
                    "'readableNames':{'user1':'readable_user1','user2':'readable_user2'}" +
                "},'scores':{'user1':123,'user2':234}}";

        assertEquals(expectedGroup1, toString(dataMap.get("user1")));
        assertEquals(expectedGroup1, toString(dataMap.get("user2")));

        String expectedGroup2 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'group':['user3','user4']," +
                    "'readableNames':{'user3':'readable_user3','user4':'readable_user4'}" +
                "},'scores':{'user3':345,'user4':456}}";

        assertEquals(expectedGroup2, toString(dataMap.get("user3")));
        assertEquals(expectedGroup2, toString(dataMap.get("user4")));
    }

    private <T> Map<String, T> sortKeys(Map<String, T> map) {
        return new TreeMap<>(map);
    }

    @Test
    public void testGetGroupsMap_usersInSameGroup() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, List<String>> map = playerGamesView.getGroupsMap();

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
        Map<String, List<String>> map = playerGamesView.getGroupsMap();

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
        List<List<String>> groups = playerGamesView.getGroupsByField();

        // then
        assertEquals("[[user1, user2, user3, user4]]", // all together
                groups.toString());
    }

    @Test
    public void testGetGroupsByField_usersInSeveralGroups() {
        // given
        givenUsersInSeveralGroups_inOneGeneralGameRoom();

        // when
        List<List<String>> groups = playerGamesView.getGroupsByField();

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
        GameType gameType = addNewGameType("gameName1", 1234, inv -> fields.remove(0));

        // комната будет у всех одна, общая игровая gameName1
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
        GameType gameType = addNewGameType("gameName1", 1234, inv -> fields.remove(0));

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
        List<List<String>> groups = playerGamesView.getGroupsByRooms();

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
        List<List<String>> groups = playerGamesView.getGroupsByRooms();

        // then
        assertEquals("[[user1, user2, user3, user4]]", // all together
                groups.toString());
    }

    @Test
    public void testGetScoresForGame() {
        // given
        testGetGamesDataMap_singleGames();

        // юзера которые не должны войти в запрос
        GameType gameType2 = addNewGameType("gameName2", 1234, inv -> mock(GameField.class));
        addNewPlayer(gameType2, 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType2, 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        List<PScoresOf> scores = playerGamesView.getScoresForGame("gameName1");

        // then
        assertEquals("[" +
                    "{'score':123,'name':'readable_user1','id':'user1'}," +
                    "{'score':234,'name':'readable_user2','id':'user2'}," +
                    "{'score':345,'name':'readable_user3','id':'user3'}," +
                    "{'score':456,'name':'readable_user4','id':'user4'}]",
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
        GameType gameType = addNewGameType("gameName1", 1234, inv -> fields.remove(0));

        addNewPlayer(gameType, "room1", 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, "room1", 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, "room1", 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, "room1", 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        // юзера которые не должны войти в запрос
        addNewPlayer(gameType, "room2", 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType, "room2", 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        List<PScoresOf> scores = playerGamesView.getScoresForRoom("room1");

        // then
        assertEquals("[{'score':123,'name':'readable_user1','id':'user1'}," +
                        "{'score':234,'name':'readable_user2','id':'user2'}," +
                        "{'score':345,'name':'readable_user3','id':'user3'}," +
                        "{'score':456,'name':'readable_user4','id':'user4'}]",
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
        GameType gameType = addNewGameType("gameName1", 1234, inv -> fields.remove(0));

        addNewPlayer(gameType, "room1", 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, "room1", 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, "room1", 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, "room1", 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        addNewPlayer(gameType, "room2", 678, getHeroData(23, pt(5, 6), "data8"));
        addNewPlayer(gameType, "room2", 789, getHeroData(24, pt(8, 7), "data9"));

        // when
        Map<String, Object> scores = playerGamesView.getScores();

        // then
        assertEquals("{user1=123, user2=234, user3=345, user4=456, user5=678, user6=789}",
                sortKeys(scores).toString());
    }

    @Test
    public void testGetGamesDataMap_singleGames() {
        // given
        // separate groups
        GameType gameType = addNewGameType("gameName1", 1234, inv -> mock(GameField.class));

        addNewPlayer(gameType, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        // when
        Map<String, GameData> dataMap = playerGamesView.getGamesDataMap();

        // then
        String expectedGroup1 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}" +
                    "}," +
                    "'group':['user1']," +
                    "'readableNames':{'user1':'readable_user1'}" +
                "},'scores':{'user1':123}}";

        assertEquals(expectedGroup1, toString(dataMap.get("user1")));

        String expectedGroup2 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'group':['user2']," +
                    "'readableNames':{'user2':'readable_user2'}" +
                "},'scores':{'user2':234}}";

        assertEquals(expectedGroup2, toString(dataMap.get("user2")));

        String expectedGroup3 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}" +
                    "}," +
                    "'group':['user3']," +
                    "'readableNames':{'user3':'readable_user3'}" +
                "},'scores':{'user3':345}}";

        assertEquals(expectedGroup3, toString(dataMap.get("user3")));

        String expectedGroup4 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'group':['user4']," +
                    "'readableNames':{'user4':'readable_user4'}" +
                "},'scores':{'user4':456}}";

        assertEquals(expectedGroup4, toString(dataMap.get("user4")));
    }

    @Test
    public void testGetReadableNames() {
        // given
        givenUsersInSameGroup();

        // when
        Map<String, String> names = playerGamesView.getReadableNames();

        // then
        assertEquals("{user1=readable_user1, user2=readable_user2, " +
                        "user3=readable_user3, user4=readable_user4}",
                sortKeys(names).toString());

    }

    private String toString(GameData gameData) {
        return JsonUtils.clean(JsonUtils.toStringSorted(gameData));
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

    private GameType addNewGameType(String gameName, int boardSize, Answer<Object> fieldSupplier) {
        GameType result = mock(GameType.class);
        when(result.getBoardSize()).thenReturn(new SimpleParameter<>(boardSize));
        when(result.name()).thenReturn(gameName);
        when(result.getMultiplayerType()).thenReturn(MultiplayerType.SINGLE);
        when(result.getPrinterFactory()).thenReturn(mock(PrinterFactory.class));
        when(result.createPlayer(any(EventListener.class), anyString()))
                .thenAnswer(inv -> gamePlayers.get(gamePlayers.size() - 1));
        when(result.createGame(anyInt())).thenAnswer(fieldSupplier);
        gameTypes.add(result);
        return result;
    }

    private PlayerGame addNewPlayer(GameType gameType, int scores, HeroData heroData) {
        return addNewPlayer(gameType, gameType.name(), scores, heroData);
    }

    private PlayerGame addNewPlayer(GameType gameType, String roomName, int scores, HeroData heroData) {
        PlayerScores gameScore = mock(PlayerScores.class);
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
        PlayerGame playerGame = playerGames.add(player, roomName, null);
        games.add(playerGame.getGame());
        return playerGame;
    }

    private String getNextName() {
        return "user" + (players.size() + 1);
    }

    enum Elements1 implements CharElements {
        A('a'),
        B('b'),
        C('c');

        private final char ch;

        Elements1(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }
    }

    enum Elements2 implements CharElements {
        ONE('1'),
        TWO('2'),
        THREE('3'),
        FOUR('4');

        private final char ch;

        Elements2(char ch) {
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
        GameType gameType1 = addNewGameType("gameName1", 1234, inv -> mock(GameField.class));
        when(gameType1.getPlots()).thenReturn(Elements1.values());

        GameType gameType2 = addNewGameType("gameName2", 1234, inv -> mock(GameField.class));
        when(gameType2.getPlots()).thenReturn(Elements2.values());

        addNewPlayer(gameType1, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType2, 234, getHeroData(11, pt(3, 4), "data2"));

        // when then
        assertEquals("{gameName1=[abc -> ABC], " +
                        "gameName2=[1234 -> ABCD]}",
                playerGamesView.getDecoders().toString());
    }
}
