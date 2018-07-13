package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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


import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.lock.LockedGameTest;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import com.codenjoy.dojo.utils.JsonUtils;
import org.fest.reflect.core.Reflection;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerGamesAdditionalDataTest {

    private PlayerGames playerGames;
    private List<Player> players;
    private List<Game> games;
    private List<PlayerController> controllers;
    private List<GameType> gameTypes;
    private List<HeroData> heroesData;

    @Before
    public void setup() {
        Statistics statistics = mock(Statistics.class);
        playerGames = new PlayerGames(statistics);
        players = new LinkedList<>();
        games = new LinkedList<>();
        controllers = new LinkedList<>();
        gameTypes = new LinkedList<>();
        heroesData = new LinkedList<>();
    }

    public static class DummyGame implements Game {

        private HeroData heroData;

        public DummyGame(HeroData heroData) {
            this.heroData = heroData;
        }

        @Override
        public Joystick getJoystick() {
            return null;
        }

        @Override
        public int getMaxScore() {
            return 0;
        }

        @Override
        public int getCurrentScore() {
            return 0;
        }

        @Override
        public boolean isGameOver() {
            return false;
        }

        @Override
        public void newGame() {

        }

        @Override
        public Object getBoardAsString() {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public void clearScore() {

        }

        @Override
        public HeroData getHero() {
            return heroData;
        }

        @Override
        public String getSave() {
            return null;
        }

        @Override
        public GamePlayer getPlayer() {
            return null;
        }

        @Override
        public GameField getField() {
            return null;
        }

        @Override
        public void tick() {

        }
    }

    @Test
    public void testGetGamesDataMap_usersInSameGroup() {
        // given
        GameType gameType = addNewGameType("gameName1", 1234);

        addNewPlayer(gameType, 123, getHeroDataForAllPlayers(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroDataForAllPlayers(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroDataForAllPlayers(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroDataForAllPlayers(13, pt(7, 8), Arrays.asList("data3, data4")));

        // when
        Map<String, GameData> dataMap = playerGames.getGamesDataMap();

        // then
        assertEquals("{gameName1={'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'user1':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}," +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'user2':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}," +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'user3':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}," +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'user4':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}," +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}" +
                "},'scores':{'user1':123,'user2':234,'user3':345,'user4':456}}}",
                toString(dataMap));
    }

    @Test
    public void testGetGamesDataMap_usersInGroup() {
        // given
        GameType gameType = addNewGameType("gameName1", 1234);

        addNewPlayer(gameType, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        setHeroesGroup(heroesData.get(0), Arrays.asList(games.get(0), games.get(1)));
        setHeroesGroup(heroesData.get(1), Arrays.asList(games.get(0), games.get(1)));
        setHeroesGroup(heroesData.get(2), Arrays.asList(games.get(2), games.get(3)));
        setHeroesGroup(heroesData.get(3), Arrays.asList(games.get(2), games.get(3)));

        // when
        Map<String, GameData> dataMap = playerGames.getGamesDataMap();

        // then
        assertEquals("{gameName1={'boardSize':1234,'decoder':{}," +
                "'heroesData':{" +
                    "'user1':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'user2':{" +
                        "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}," +
                        "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                    "}," +
                    "'user3':{" +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}," +
                    "'user4':{" +
                        "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}," +
                        "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                    "}" +
                "},'scores':{'user1':123,'user2':234,'user3':345,'user4':456}}}",
                toString(dataMap));
    }

    @Test
    public void testGetGamesDataMap_singleGames() {
        // given
        GameType gameType = addNewGameType("gameName1", 1234);

        addNewPlayer(gameType, 123, getHeroData(10, pt(1, 2), "data1"));
        addNewPlayer(gameType, 234, getHeroData(11, pt(3, 4), "data2"));
        addNewPlayer(gameType, 345, getHeroData(12, pt(5, 6), new JSONObject("{'key':'value'}")));
        addNewPlayer(gameType, 456, getHeroData(13, pt(7, 8), Arrays.asList("data3, data4")));

        // heroData.playersGroup == null
        assertEquals(null, heroesData.get(0).playersGroup());
        assertEquals(null, heroesData.get(1).playersGroup());
        assertEquals(null, heroesData.get(2).playersGroup());
        assertEquals(null, heroesData.get(3).playersGroup());

        // when
        Map<String, GameData> dataMap = playerGames.getGamesDataMap();

        // then
        assertEquals("{gameName1={'boardSize':1234,'decoder':{}," +
                        "'heroesData':{" +
                            "'user1':{" +
                                "'user1':{'additionalData':'data1','coordinate':{'x':1,'y':2},'level':10,'multiplayer':false}" +
                            "}," +
                            "'user2':{" +
                                "'user2':{'additionalData':'data2','coordinate':{'x':3,'y':4},'level':11,'multiplayer':false}" +
                            "}," +
                            "'user3':{" +
                                "'user3':{'additionalData':{'key':'value'},'coordinate':{'x':5,'y':6},'level':12,'multiplayer':false}" +
                            "}," +
                            "'user4':{" +
                                "'user4':{'additionalData':['data3, data4'],'coordinate':{'x':7,'y':8},'level':13,'multiplayer':false}" +
                            "}" +
                        "},'scores':{'user1':123,'user2':234,'user3':345,'user4':456}}}",
                toString(dataMap));
    }

    private String toString(Map<String, GameData> dataMap) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, GameData> entry : dataMap.entrySet()) {
            result.put(entry.getKey(), JsonUtils.clean(JsonUtils.toStringSorted(entry.getValue())));
        }
        return result.toString();
    }

    private HeroData getHeroDataForAllPlayers(int level, Point coordinate, Object additionalData) {
        HeroData result = getHeroData(level, coordinate, additionalData);
        setHeroesGroup(result, games);
        return result;
    }

    private void setHeroesGroup(HeroData result, List<Game> games) {
        Reflection.field("playersGroup").ofType(Object.class).in(result).set(games);
    }

    private HeroData getHeroData(int level, Point coordinate, Object additionalData) {
        HeroData result = new HeroDataImpl(level, coordinate,
                MultiplayerType.SINGLE.isMultiplayer());
        Reflection.field("additionalData").ofType(Object.class).in(result).set(additionalData);
        heroesData.add(result);
        return result;
    }

    private GameType addNewGameType(String gameName, int boardSize) {
        GameType result = mock(GameType.class);
        when(result.getBoardSize()).thenReturn(new SimpleParameter<>(boardSize));
        when(result.name()).thenReturn(gameName);
        gameTypes.add(result);
        return result;
    }

    private PlayerGame addNewPlayer(GameType gameType, int scores, HeroData heroData) {
        PlayerScores gameScore = mock(PlayerScores.class);
        when(gameScore.getScore()).thenReturn(scores);

        Player player = new Player(getNextName(), "http://" + getNextName() + ".com:8080", gameType, gameScore, null);
        players.add(player);

        Game game = LockedGameTest.getLockedGame().wrap(new DummyGame(heroData));
        games.add(game);

        PlayerController controller = mock(PlayerController.class);
        controllers.add(controller);
        return playerGames.add(player, game);
    }

    private String getNextName() {
        return "user" + (players.size() + 1);
    }
}
