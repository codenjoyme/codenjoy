package com.codenjoy.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.nullobj.NullJoystick;
import com.codenjoy.dojo.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Baglai on 2017-09-22.
 */
public class ReplayGameTest {

    @Test
    public void testIsReplayGame() {
        assertEquals(true, ReplayGame.isReplay("{'startFromTick':0,'replayName':'game-E@1e16c0aa-1','playerName':'P@57bc27f5'}"));

        assertEquals(false, ReplayGame.isReplay("{'total':10,'current':0,'lastPassed':9,'multiple':true}"));
        assertEquals(false, ReplayGame.isReplay(""));
        assertEquals(false, ReplayGame.isReplay(null));
    }

    @Test
    public void shouldCreateLoggerReader() {
        final String[] actualReplayName = {null};
        final String[] actualPlayerName = {null};

        ReplayGame game = new ReplayGame(new JSONObject("{'startFromTick':0,'replayName':'game-E@1e16c0aa-1','playerId':'P@57bc27f5'}")){
            @Override
            protected LoggerReader getLoggerReader(String replayName, String playerId) {
                actualReplayName[0] = replayName;
                actualPlayerName[0] = playerId;
                return null;
            }
        };
        assertEquals("game-E@1e16c0aa-1", actualReplayName[0]);
        assertEquals("P@57bc27f5", actualPlayerName[0]);
    }

    @Test
    public void shouldNullJoystick() {
        ReplayGame game = new ReplayGame(new JSONObject("{'startFromTick':0,'replayName':'game-E@1e16c0aa-1','playerId':'P@57bc27f5'}"));
        assertEquals(NullJoystick.INSTANCE, game.getJoystick());
    }

    @Test
    public void shouldIsGameOverFalse() {
        ReplayGame game = new ReplayGame(new JSONObject("{'startFromTick':0,'replayName':'game-E@1e16c0aa-1','playerId':'P@57bc27f5'}"));
        assertEquals(false, game.isGameOver());
    }


    private JSONObject lobbyBoard;
    private Map<String, JSONObject> lobbyLastActions;
    private Map<String, JSONObject> allBasePosition;
    private Point lobbyPt;
    private List<JSONObject> currentActions;
    private List<Map<String, JSONObject>> lastActions;
    private List<Point> basePositions;
    private List<JSONObject> boards;

    @Before
    public void setup() {
        SettingsWrapper.setup().delayReplay(true);

        currentActions = new LinkedList<>();
        lastActions = new LinkedList<>();
        basePositions = new LinkedList<>();
        boards = new LinkedList<>();

        lobbyBoard = new JSONObject("{'lb':0}");
        lobbyLastActions = new HashMap<String, JSONObject>(){{
                put("user1", new JSONObject("{'loca':10}"));
                put("user2", new JSONObject("{'loca':20}"));
        }};
        allBasePosition = new HashMap<String, JSONObject>(){{
            put("user1", new JSONObject("{'abp':30}"));
            put("user2", new JSONObject("{'abp':40}"));
        }};
        lobbyPt = pt(-1, -1);

        // It is important that there are different objects, but not their contents
        for (int i = 0; i < 4; i++) {
            currentActions.add(new JSONObject("{'ca':" + i + "}"));
            int finalI = i;
            lastActions.add(new HashMap<String, JSONObject>(){{
                    put("user1", new JSONObject("{'oca':" + (10 + finalI) + "}"));
                    put("user2", new JSONObject("{'oca':" + (20 + finalI) + "}"));
                }});
            basePositions.add(pt(i, i));
            boards.add(new JSONObject("{'b':" + i + "}"));
        }
    }

    @Test
    public void shouldProcessAllData() {
        // given
        ReplayGame game = createGame(0);

        // replay not started, tick=-1 and we are at "replays lobby"
        assertAtLobby(game);

        // clearScore, tick = 0
        game.clearScore();
        assertTick(game, 0);

        // tick++ = 1
        game.tick();
        assertTick(game, 1);

        // tick++ = 2
        game.tick();
        assertTick(game, 2);

        // tick++ = 3
        game.tick();
        assertTick(game, 3);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);

        // then restart again
        // clearScore, tick = 0
        game.clearScore();
        assertTick(game, 0);

        // tick++ = 1
        game.tick();
        assertTick(game, 1);

        // tick++ = 2
        game.tick();
        assertTick(game, 2);

        // tick++ = 3
        game.tick();
        assertTick(game, 3);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);
    }

    @Test
    public void shouldProcessAllData_startFromOther() {
        // given
        ReplayGame game = createGame(2);

        // replay not started, tick=-1 and we are at "replays lobby"
        assertAtLobby(game);

        // clearScore, tick = 2
        game.clearScore();
        assertTick(game, 2);

        // tick++ = 3
        game.tick();
        assertTick(game, 3);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);

        // then restart again
        // clearScore, tick = 2
        game.clearScore();
        assertTick(game, 2);

        // tick++ = 3
        game.tick();
        assertTick(game, 3);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);
    }

    @Test
    public void shouldNotTickWhenNotStart() {
        // given
        ReplayGame game = createGame(0);

        // replay not started, tick=-1 and we are at "replays lobby"
        assertAtLobby(game);

        // new game, tick = -1, because need clearScore()
        game.tick();
        assertAtLobby(game);
    }

    @Test
    public void shouldNotTickWhenNoMoreTicks() {
        // given
        ReplayGame game = createGame(3);

        // replay not started, tick=-1 and we are at "replays lobby"
        assertAtLobby(game);

        // clearScore, tick = 3
        game.clearScore();
        assertTick(game, 3);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);

        // tick++ is out of, so we go to lobby
        game.tick();
        assertAtLobby(game);
    }

    @Test
    public void shouldNotTickWhenNoMoreTicks_whenDelayReplay() {
        boolean old = data.delayReplay();
        try {
            data.delayReplay(false);

            // given
            ReplayGame game = createGame(3);

            // without clearScore, tick = 3
            // game.clearScore();
            assertTick(game, 3);

            // tick++ is out of, so we go to lobby
            game.tick();
            assertAtLobby(game);

            // tick++ is out of, so we go to lobby
            game.tick();
            assertAtLobby(game);
        } finally {
            data.delayReplay(old);
        }
    }

    @NotNull
    private ReplayGame createGame(int startFrom) {
        return new ReplayGame(new JSONObject("{'startFromTick':" + startFrom + ",'replayName':'game-E@1e16c0aa-1','playerId':'P@57bc27f5'}")){
                @Override
                protected LoggerReader getLoggerReader(String replayName, String playerId) {
                    return new LoggerReader() {
                        private boolean isOutOf(int tick) {
                            return tick < 0 || tick >= boards.size();
                        }

                        @Override
                        public Map<String, JSONObject> getAllLastActions(int tick) {
                            if (isOutOf(tick)) {
                                return lobbyLastActions;
                            }
                            return lastActions.get(tick);
                        }

                        @Override
                        public Map<String, JSONObject> getAllBasePositions() {
                            return allBasePosition;
                        }

                        @Override
                        public Point getBasePosition(int tick) {
                            if (isOutOf(tick)) {
                                return lobbyPt;
                            }
                            return basePositions.get(tick);
                        }

                        @Override
                        public JSONObject getBoard(int tick) {
                            if (isOutOf(tick)) {
                                return lobbyBoard;
                            }
                            return boards.get(tick);
                        }

                        @Override
                        public int size() {
                            return boards.size();
                        }
                    };
                }
            };
    }

    private void assertTick(ReplayGame game, int tick) {
        assertEquals(false, game.noMoreTicks());

        assertEquals("{'b':" + tick + "}",
                JsonUtils.cleanSorted(game.getBoardAsString()));

        assertEquals("{'allLastActions':{'user1':{'oca':1" + tick + "},'user2':{'oca':2" + tick + "}}," +
                        "'heroesData':{'user1':{'abp':30},'user2':{'abp':40}}}",
                JsonUtils.cleanSorted(game.getHero().getAdditionalData()));

        assertEquals(pt(tick, tick).toString(),
                game.getHero().getCoordinate().toString());
    }

    private void assertAtLobby(ReplayGame game) {
        assertEquals(true, game.noMoreTicks());

        assertEquals("{'lb':0}",
                JsonUtils.cleanSorted(game.getBoardAsString()));

        assertEquals("{'allLastActions':{'user1':{'loca':10},'user2':{'loca':20}}," +
                        "'heroesData':{'user1':{'abp':30},'user2':{'abp':40}}}",
                JsonUtils.cleanSorted(game.getHero().getAdditionalData()));

        assertEquals("[-1,-1]",
                game.getHero().getCoordinate().toString());
    }
}


