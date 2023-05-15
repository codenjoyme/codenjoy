package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.helper.ChatDealsUtils;
import com.codenjoy.dojo.services.info.Information;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.multiplayer.FieldService;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.room.RoomService;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.utils.TestUtils.mockGameType;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO try @SpringBootTest
public class ActionLoggerTest {

    private ActionLogger logger;
    private ExecutorService executor;
    private RoomService roomService;
    private Deals deals;
    private long time;

    @Before
    public void setup() {
        String dbFile = "target/logs.db" + new Random().nextInt();
        logger = new ActionLogger(
                    new SqliteConnectionThreadPoolFactory(false, dbFile,
                            new ContextPathGetter() {
                                @Override
                                public String getContext() {
                                    return "context";
                                }
                            }))
        {
            {
                // пригодится, когда будем дожидаться завершения сохранения
                ActionLoggerTest.this.executor = executor;
            }

            @Override
            protected long now() {
                // кроме того, что вернет время еще и увеличит его на 1 для следующих сейвов
                return ++time;
            }
        };
        logger.setTicks(1);
        time = 100;

        deals = new Deals(){{
            ActionLoggerTest.this.roomService = this.roomService = mock(RoomService.class);
            this.spreader = new Spreader(){{
                fields = mock(FieldService.class);
            }};
        }};
        ChatDealsUtils.setupChat(deals, null);
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
        log(deals);
        waitFor();

        // then
        assertAllLogs(
                "[BoardLog(time=101, playerId=player1, game=game1, room=room1, score=123, board=player1Board:101, message=null, command=[]), \n" +
                "BoardLog(time=101, playerId=player2, game=game2, room=room2, score=234, board=player2Board:101, message=null, command=[])]");
    }

    private void allRoomsAreActive() {
        when(roomService.isActive(anyString())).thenReturn(true);
    }

    private void thisRoomIsNotActive(String room) {
        when(roomService.isActive(room)).thenReturn(false);
    }

    @SneakyThrows
    private void log(Deals deals) {
        logger.log(deals);
    }

    /**
     * Так как логгирование происходит в отдельном потоке, то нам надо дождаться завершения его
     * перед тем как пойдем проверять результаты работы метода логгирования
     */
    @SneakyThrows
    private void waitFor() {
        executor.shutdown();
        if (!executor.isTerminated()) {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void shouldNotLog_whenNotEnabled() {
        // given
        givenPlayers();

        // when
        log(deals);
        waitFor();

        // then
        assertAllLogs("[]");
    }

    private void givenPlayers() {
        addPlayer(deals, "player1Board", 123, "player1", "room1", "game1");
        addPlayer(deals, "player2Board", 234, "player2", "room2", "game2");
    }

    private void addPlayer(Deals deals, String board, int scoreValue, String id, String room, String game) {
        PlayerScores score = getScore(scoreValue);
        Player player = new Player(id, "127.0.0.1", mockGameType(game), score, null);
        player.setInfo(mock(Information.class));

        TestUtils.Env env = TestUtils.getDeal(deals, player, room,
                inv -> {
                    GameField field = mock(GameField.class);
                    when(field.reader()).thenReturn(mock(BoardReader.class));
                    return field;
                },
                MultiplayerType.SINGLE,
                null,
                // борда будет меняться после каждого сохранения, так чтобы мы могли видеть эту разницу в assert
                parameters -> board + ":" + time);
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
        log(deals);
        waitFor();

        // then
        assertAllLogs("[BoardLog(time=101, playerId=player1, game=game1, room=room1, score=123, board=player1Board:101, message=null, command=[])]");
    }

    @Test
    public void shouldGetLastTime_whenSeveralSaves() {
        // given
        logger.resume();
        givenPlayers();

        // when
        log(deals); // time = 101
        log(deals); // time = 102
        log(deals); // time = 103
        log(deals); // time = 104
        waitFor();

        // then
        assertAllLogs(
                "[BoardLog(time=101, playerId=player1, game=game1, room=room1, score=123, board=player1Board:101, message=null, command=[]), \n" +
                "BoardLog(time=101, playerId=player2, game=game2, room=room2, score=234, board=player2Board:101, message=null, command=[]), \n" +
                "BoardLog(time=102, playerId=player1, game=game1, room=room1, score=123, board=player1Board:102, message=null, command=[]), \n" +
                "BoardLog(time=102, playerId=player2, game=game2, room=room2, score=234, board=player2Board:102, message=null, command=[]), \n" +
                "BoardLog(time=103, playerId=player1, game=game1, room=room1, score=123, board=player1Board:103, message=null, command=[]), \n" +
                "BoardLog(time=103, playerId=player2, game=game2, room=room2, score=234, board=player2Board:103, message=null, command=[]), \n" +
                "BoardLog(time=104, playerId=player1, game=game1, room=room1, score=123, board=player1Board:104, message=null, command=[]), \n" +
                "BoardLog(time=104, playerId=player2, game=game2, room=room2, score=234, board=player2Board:104, message=null, command=[])]");

        assertEquals(104,
                logger.getLastTime("player1"));

        assertEquals(104,
                logger.getLastTime("player2"));
    }

    public void assertAllLogs(String expected) {
        assertEquals(expected, split(logger.getAll(), ", \nBoardLog"));
    }

    @Test
    public void shouldGetBoardLogsFor() {
        // given
        logger.resume();
        givenPlayers();

        // when
        log(deals); // time = 101
        log(deals); // time = 102
        log(deals); // time = 103
        log(deals); // time = 104
        log(deals); // time = 105
        log(deals); // time = 106
        log(deals); // time = 107
        log(deals); // time = 108
        log(deals); // time = 109
        log(deals); // time = 110
        log(deals); // time = 111
        waitFor();

        // then
        assertLogs("BoardLog(time=101, playerId=player1, game=game1, room=room1, score=123, board=player1Board:101, message=null, command=[])\n" +
                   "BoardLog(time=102, playerId=player1, game=game1, room=room1, score=123, board=player1Board:102, message=null, command=[])\n" +
                   "BoardLog(time=103, playerId=player1, game=game1, room=room1, score=123, board=player1Board:103, message=null, command=[])\n" +
                   "BoardLog(time=104, playerId=player1, game=game1, room=room1, score=123, board=player1Board:104, message=null, command=[])\n" +

                   "BoardLog(time=105, playerId=player1, game=game1, room=room1, score=123, board=player1Board:105, message=null, command=[])\n" +

                   "BoardLog(time=106, playerId=player1, game=game1, room=room1, score=123, board=player1Board:106, message=null, command=[])\n" +
                   "BoardLog(time=107, playerId=player1, game=game1, room=room1, score=123, board=player1Board:107, message=null, command=[])\n" +
                   "BoardLog(time=108, playerId=player1, game=game1, room=room1, score=123, board=player1Board:108, message=null, command=[])\n" +
                   "BoardLog(time=109, playerId=player1, game=game1, room=room1, score=123, board=player1Board:109, message=null, command=[])",
                logger.getBoardLogsFor("player1", "room1", 105, 4));

        assertLogs("BoardLog(time=103, playerId=player2, game=game2, room=room2, score=234, board=player2Board:103, message=null, command=[])\n" +
                   "BoardLog(time=104, playerId=player2, game=game2, room=room2, score=234, board=player2Board:104, message=null, command=[])\n" +

                   "BoardLog(time=105, playerId=player2, game=game2, room=room2, score=234, board=player2Board:105, message=null, command=[])\n" +

                   "BoardLog(time=106, playerId=player2, game=game2, room=room2, score=234, board=player2Board:106, message=null, command=[])\n" +
                   "BoardLog(time=107, playerId=player2, game=game2, room=room2, score=234, board=player2Board:107, message=null, command=[])",
                logger.getBoardLogsFor("player2", "room2", 105, 2));

        assertLogs("BoardLog(time=101, playerId=player2, game=game2, room=room2, score=234, board=player2Board:101, message=null, command=[])\n" +

                    "BoardLog(time=102, playerId=player2, game=game2, room=room2, score=234, board=player2Board:102, message=null, command=[])\n" +

                    "BoardLog(time=103, playerId=player2, game=game2, room=room2, score=234, board=player2Board:103, message=null, command=[])\n" +
                    "BoardLog(time=104, playerId=player2, game=game2, room=room2, score=234, board=player2Board:104, message=null, command=[])\n" +
                    "BoardLog(time=105, playerId=player2, game=game2, room=room2, score=234, board=player2Board:105, message=null, command=[])\n" +
                    "BoardLog(time=106, playerId=player2, game=game2, room=room2, score=234, board=player2Board:106, message=null, command=[])",
                logger.getBoardLogsFor("player2", "room2", 102, 4));

        assertLogs("BoardLog(time=106, playerId=player2, game=game2, room=room2, score=234, board=player2Board:106, message=null, command=[])\n" +
                    "BoardLog(time=107, playerId=player2, game=game2, room=room2, score=234, board=player2Board:107, message=null, command=[])\n" +
                    "BoardLog(time=108, playerId=player2, game=game2, room=room2, score=234, board=player2Board:108, message=null, command=[])\n" +

                    "BoardLog(time=109, playerId=player2, game=game2, room=room2, score=234, board=player2Board:109, message=null, command=[])\n" +

                    "BoardLog(time=110, playerId=player2, game=game2, room=room2, score=234, board=player2Board:110, message=null, command=[])\n" +
                    "BoardLog(time=111, playerId=player2, game=game2, room=room2, score=234, board=player2Board:111, message=null, command=[])",
                logger.getBoardLogsFor("player2", "room2", 109, 3));
    }

    private void assertLogs(String expected, List<BoardLog> logs) {
        assertEquals(expected,
                logs.stream()
                        .map(BoardLog::toString)
                        .collect(joining("\n")));
    }
}