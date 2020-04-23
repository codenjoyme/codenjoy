package com.codenjoy.dojo.services.dao;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActionLoggerTest {

    private static ActionLogger logger;
    private RoomService roomService;
    private PlayerGames playerGames;
    private long time;

    @Before
    public void setup() {
        String dbFile = "target/logs.db" + new Random().nextInt();
        logger = new ActionLogger(
                    new SqliteConnectionThreadPoolFactory(dbFile,
                            new ContextPathGetter() {
                                @Override
                                public String getContext() {
                                    return "context";
                                }
                            }))
        {
            @Override
            protected long now() {
                // кроме того, что вернет время еще и увеличит его на 1 для следующих сейвов
                return time++;
            }
        };
        logger.setTicks(1);
        this.time = 100;

        playerGames = new PlayerGames(){{
            ActionLoggerTest.this.roomService = this.roomService = mock(RoomService.class);
        }};
        allRoomsAreActive();
    }


    @After
    public void tearDown() {
        logger.removeDatabase();
    }

    @Test
    public void shouldLog_whenEnabled() {
        // given
        logger.resume();
        allRoomsAreActive();
        givenPlayers();

        // when
        log(playerGames);

        // then
        assertSaved("[BoardLog(time=100, playerId=player1, gameType=game1, score=123, board=board1, command=[]), " +
                "BoardLog(time=100, playerId=player2, gameType=game2, score=234, board=board2, command=[])]");
    }

    private void assertSaved(String expected) {
        assertEquals(expected,
                logger.getAll().toString());
    }

    private void allRoomsAreActive() {
        when(roomService.isActive(anyString())).thenReturn(true);
    }

    private void thisRoomIsNotActive(String room) {
        when(roomService.isActive(room)).thenReturn(false);
    }

    @SneakyThrows
    private void log(PlayerGames playerGames) {
        logger.log(playerGames);

        Thread.sleep(1000); // потому что сохранение в базу делается асинхронно и надо подождать
    }

    @Test
    public void shouldNotLog_whenNotEnabled() {
        // given
        givenPlayers();

        // when
        log(playerGames);

        // then
        assertSaved("[]");
    }

    private void givenPlayers() {
        addPlayer(playerGames, "board1", 123, "player1", "room1", "game1");
        addPlayer(playerGames, "board2", 234, "player2", "room2", "game2");
    }

    private void addPlayer(PlayerGames playerGames, String board, int scoreValue, String id, String roomName, String gameName) {
        PlayerScores score = getScore(scoreValue);
        Player player = new Player(id, "127.0.0.1", PlayerTest.mockGameType(gameName), score, null);
        player.setEventListener(mock(InformationCollector.class));

        TestUtils.Env env = TestUtils.getPlayerGame(playerGames, player, roomName,
                inv -> {
                    GameField field = mock(GameField.class);
                    when(field.reader()).thenReturn(mock(BoardReader.class));
                    return field;
                },
                MultiplayerType.SINGLE,
                null,
                parameters -> board);
    }

    private PlayerScores getScore(int value) {
        PlayerScores result = mock(PlayerScores.class);
        when(result.getScore()).thenReturn(value);
        return result;
    }

    // есть несколько игроков из разных комнат, одна из которых находится на паузе -
    // так вот те ребята, что на паузе просто не будут сейвиться в базе и все
    @Test
    public void shouldLog_whenEnabled_onlyForActiveRooms() {
        // given
        logger.resume();
        thisRoomIsNotActive("room2");
        givenPlayers();

        // when
        log(playerGames);

        // then
        assertSaved("[BoardLog(time=100, playerId=player1, gameType=game1, score=123, board=board1, command=[])]");
    }

    @Test
    public void shouldGetLastTime_whenSeveralSaves() {
        // given
        logger.resume();
        givenPlayers();

        // when
        log(playerGames); // time = 100
        log(playerGames); // time = 101
        log(playerGames); // time = 102
        log(playerGames); // time = 103

        // then
        assertEquals(103,
                logger.getLastTime("player1"));

        assertEquals(103,
                logger.getLastTime("player2"));
    }

}