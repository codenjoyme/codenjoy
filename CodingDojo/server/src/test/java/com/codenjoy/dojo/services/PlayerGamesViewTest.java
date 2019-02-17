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
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import com.codenjoy.dojo.utils.JsonUtils;
import org.fest.reflect.core.Reflection;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
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
        GameField field = mock(GameField.class); // same group
        GameType gameType = addNewGameType("gameName1", 1234, inv -> field);

        addNewPlayer(gameType, 123, getHeroDataForAllPlayers(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroDataForAllPlayers(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroDataForAllPlayers(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroDataForAllPlayers(13, pt(7, 8), Arrays.asList("data3, data4")));

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
                    "'readableNames':{'user1':'user1','user2':'user2','user3':'user3','user4':'user4'}" +
                "},'scores':{'user1':123,'user2':234,'user3':345,'user4':456}}";

        assertEquals(expectedGroup, toString(dataMap.get("user1")));
    }

    @Test
    public void testGetGamesDataMap_usersInGroup() {
        // given
        GameField field1 = mock(GameField.class);
        GameField field2 = mock(GameField.class);
        List<GameField> fields = new LinkedList<>(Arrays.asList(field1, field1, field2, field2));
        GameType gameType = addNewGameType("gameName1", 1234, inv -> fields.remove(0));

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
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'group':['user1','user2']," +
                    "'readableNames':{'user1':'user1','user2':'user2'}" +
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
                    "'readableNames':{'user3':'user3','user4':'user4'}" +
                "},'scores':{'user3':345,'user4':456}}";

        assertEquals(expectedGroup2, toString(dataMap.get("user3")));
        assertEquals(expectedGroup2, toString(dataMap.get("user4")));
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
                    "'readableNames':{'user1':'user1'}" +
                "},'scores':{'user1':123}}";

        assertEquals(expectedGroup1, toString(dataMap.get("user1")));

        String expectedGroup2 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'group':['user2']," +
                    "'readableNames':{'user2':'user2'}" +
                "},'scores':{'user2':234}}";

        assertEquals(expectedGroup2, toString(dataMap.get("user2")));

        String expectedGroup3 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}" +
                    "}," +
                    "'group':['user3']," +
                    "'readableNames':{'user3':'user3'}" +
                "},'scores':{'user3':345}}";

        assertEquals(expectedGroup3, toString(dataMap.get("user3")));

        String expectedGroup4 = "{'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'coordinates':{" +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'group':['user4']," +
                    "'readableNames':{'user4':'user4'}" +
                "},'scores':{'user4':456}}";

        assertEquals(expectedGroup4, toString(dataMap.get("user4")));
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
        PlayerScores gameScore = mock(PlayerScores.class);
        when(gameScore.getScore()).thenReturn(scores);

        GamePlayer gamePlayer = mock(GamePlayer.class);
        when(gamePlayer.getHeroData()).thenReturn(heroData);
        gamePlayers.add(gamePlayer);
        heroesData.add(heroData);

        Player player = new Player(getNextName(), "http://" + getNextName() + ".com:8080", gameType, gameScore, null);
        players.add(player);

        Controller controller = mock(Controller.class);
        controllers.add(controller);
        PlayerGame playerGame = playerGames.add(player, null);
        games.add(playerGame.getGame());
        return playerGame;
    }

    private String getNextName() {
        return "user" + (players.size() + 1);
    }
}
