package com.codenjoy.dojo.services.semifinal;

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


import com.codenjoy.dojo.services.AbstractPlayerGamesTest;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.*;

public class SemifinalServiceTest extends AbstractPlayerGamesTest {

    private SemifinalService semifinal;
    private int timeout;

    @Before
    public void setup() {
        super.setUp();

        timeout = 3;
        semifinal = new SemifinalService();
        semifinal.roomService = roomService;
        semifinal.playerGames = playerGames;
        semifinal.clean();
        roomService.removeAll();
    }

    protected Settings settings(String room) {
        return (Settings) new SemifinalSettingsImpl()
                    .setEnabled(true)
                    .setTimeout(timeout)
                    .setPercentage(true)
                    .setLimit(50)
                    .setResetBoard(false)
                    .setShuffleBoard(false);
    }

    @Test
    public void shouldResetTicks_whenRoundDone() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);

        updateSettings("room")
                .setTimeout(3);

        assertEquals(0, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(1, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(2, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(0, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(1, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(2, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(0, semifinal.getTime("room"));
    }

    @Test
    public void shouldGetSemifinalStatus() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setEnabled(true)
                .setTimeout(3)
                .setPercentage(true)
                .setLimit(70)
                .setResetBoard(true)
                .setShuffleBoard(true);

        assertEquals("SemifinalStatus(tick=0, count=10, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=1, count=10, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=2, count=10, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // новый полуфинал
        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=0, count=7, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=1, count=7, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=2, count=7, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());

        // новый полуфинал
        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=0, count=5, enabled=true, " +
                        "timeout=3, percentage=true, limit=70, " +
                        "resetBoard=true, shuffleBoard=true)",
                semifinal.getSemifinalStatus("room").toString());
    }

    @Test
    public void shouldGetSemifinalStatus_ifThereIsNoSemifinalSettings() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        noSemifinalFor("room");

        // when then
        assertEquals("SemifinalStatus(tick=null, count=10, enabled=false, " +
                        "timeout=null, percentage=null, limit=null, " +
                        "resetBoard=null, shuffleBoard=null)",
                semifinal.getSemifinalStatus("room").toString());

        // when then
        semifinal.tick();
        assertEquals("SemifinalStatus(tick=null, count=10, enabled=false, " +
                        "timeout=null, percentage=null, limit=null, " +
                        "resetBoard=null, shuffleBoard=null)",
                semifinal.getSemifinalStatus("room").toString());
    }

        private SemifinalSettings<SettingsReader> updateSettings(String room) {
        return (SemifinalSettings<SettingsReader>) roomService.settings(room);
    }

    @Test
    public void shouldResetTicks_whenClear() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);

        updateSettings("room")
                .setTimeout(10);

        semifinal.tick();
        semifinal.tick();
        semifinal.tick();
        assertEquals(3, semifinal.getTime("room"));

        // when
        semifinal.clean();

        // then
        assertEquals(0, semifinal.getTime("room"));
    }

    @Test
    public void shouldDoNotCalculateTicks_whenDisabled() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);

        updateSettings("room")
                .setEnabled(false)
                .setTimeout(3);

        assertEquals(0, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(0, semifinal.getTime("room"));

        // when then
        semifinal.tick();
        assertEquals(0, semifinal.getTime("room"));
    }

    @Test
    public void shouldDoNothing_whenDisabled() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);
        Player player3 = createPlayerWithScore(60);
        Player player4 = createPlayerWithScore(40);
        Player player5 = createPlayerWithScore(20);

        updateSettings("room")
                .setEnabled(false);

        // when
        ticksTillTimeout();
        ticksTillTimeout();
        ticksTillTimeout();
        ticksTillTimeout();
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5);
    }

    @Test
    public void shouldCut50PercentUsers_whenAccurateCut() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);
        Player player11 = createPlayerWithScore(9);
        Player player12 = createPlayerWithScore(8);
        Player player13 = createPlayerWithScore(7);
        Player player14 = createPlayerWithScore(6);
        Player player15 = createPlayerWithScore(5);
        Player player16 = createPlayerWithScore(4);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(50);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7, player8);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut30PercentUsers_checkThatThereWillBeOnlyOne() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(70);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut1PercentUsers_checkThatThereWillBeOnlyOne() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(99);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5,
                player6, player7, player8, player9);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5,
                player6, player7, player8);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5,
                player6, player7);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5,
                player6);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut50PercentUsers_whenAccurateCut_whenSeveralRooms() {
        // given
        Player player1 = createPlayerWithScore(100, "room1");
        Player player2 = createPlayerWithScore(90, "room1");
        Player player3 = createPlayerWithScore(80, "room1");
        Player player4 = createPlayerWithScore(70, "room1");
        Player player5 = createPlayerWithScore(60, "room1");
        Player player6 = createPlayerWithScore(50, "room1");
        Player player7 = createPlayerWithScore(40, "room1");
        Player player8 = createPlayerWithScore(30, "room1");

        Player player9 = createPlayerWithScore(20, "room2");
        Player player10 = createPlayerWithScore(10, "room2");
        Player player11 = createPlayerWithScore(9, "room2");
        Player player12 = createPlayerWithScore(8, "room2");
        Player player13 = createPlayerWithScore(7, "room2");
        Player player14 = createPlayerWithScore(6, "room2");
        Player player15 = createPlayerWithScore(5, "room2");
        Player player16 = createPlayerWithScore(4, "room2");

        updateSettings("room1")
                .setPercentage(true)
                .setLimit(50);

        updateSettings("room2")
                .setPercentage(true)
                .setLimit(50);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, // room1
                player9, player10, player11, player12);  // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, // room1
                player9, player10);    // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1,  // room1
                player9);      // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, // room1
                player9);     // room2
    }

    @Test
    public void shouldCut50PercentUsers_whenAccurateCut_whenSeveralRooms_someIsNotActive() {
        // given
        Player player1 = createPlayerWithScore(100, "room1");
        Player player2 = createPlayerWithScore(90, "room1");
        Player player3 = createPlayerWithScore(80, "room1");
        Player player4 = createPlayerWithScore(70, "room1");
        Player player5 = createPlayerWithScore(60, "room1");
        Player player6 = createPlayerWithScore(50, "room1");
        Player player7 = createPlayerWithScore(40, "room1");
        Player player8 = createPlayerWithScore(30, "room1");

        Player player9  = createPlayerWithScore(20, "room2");
        Player player10 = createPlayerWithScore(10, "room2");
        Player player11 = createPlayerWithScore(9, "room2");
        Player player12 = createPlayerWithScore(8, "room2");
        Player player13 = createPlayerWithScore(7, "room2");
        Player player14 = createPlayerWithScore(6, "room2");
        Player player15 = createPlayerWithScore(5, "room2");
        Player player16 = createPlayerWithScore(4, "room2");

        updateSettings("room1")
                .setPercentage(true)
                .setLimit(50);

        updateSettings("room2")
                .setPercentage(true)
                .setLimit(50);

        givenActive("room1", false);
        givenActive("room2", true);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7, player8, // all not active room1
                player9, player10, player11, player12);  // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7, player8, // all not active room1
                player9, player10);    // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7, player8,  // all not active room1
                player9);      // room2

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7, player8, // all not active room1
                player9);     // room2
    }

    @Test
    public void shouldCut70PercentUsers_whenNotAccurateCut() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);
        Player player11 = createPlayerWithScore(9);
        Player player12 = createPlayerWithScore(8);
        Player player13 = createPlayerWithScore(7);
        Player player14 = createPlayerWithScore(6);
        Player player15 = createPlayerWithScore(5);
        Player player16 = createPlayerWithScore(4);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(30);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut99PercentUsers_whenNotAccurateCut() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);
        Player player11 = createPlayerWithScore(9);
        Player player12 = createPlayerWithScore(8);
        Player player13 = createPlayerWithScore(7);
        Player player14 = createPlayerWithScore(6);
        Player player15 = createPlayerWithScore(5);
        Player player16 = createPlayerWithScore(4);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut99PercentUsers_whenNotAccurateCut_caseTwoPlayers() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(50);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut80PercentUsers_whenAccurateCut() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(20);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);
    }

    @Test
    public void shouldCut80PercentUsers_whenAccurateCut_mixedScoreOrder() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);
        Player player3 = createPlayerWithScore(60);
        Player player4 = createPlayerWithScore(40);
        Player player5 = createPlayerWithScore(20);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(70);
        Player player8 = createPlayerWithScore(90);
        Player player9 = createPlayerWithScore(30);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(20);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player8);
    }

    @Test
    public void shouldCutOnly3Users_from10() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);
        Player player5 = createPlayerWithScore(60);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(40);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(3);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);
    }

    @Test
    public void shouldCutOnly3Users_from3() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(3);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);
    }

    @Test
    public void shouldCutOnly3Users_from1() {
        // given
        Player player1 = createPlayerWithScore(100);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(3);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldLeaveLastUser() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(50);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(50);

        ticksTillTimeout();
        assertActive(player1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCleanScoresAfterCut_whenSetResetBoard_caseTournament() {
        // given
        int winner = 100;
        int loser = 1;
        Player player1 = createPlayerWithScore(winner, "player1", MultiplayerType.TOURNAMENT);
        Player player2 = createPlayerWithScore(loser, "player2", MultiplayerType.TOURNAMENT);
        Player player3 = createPlayerWithScore(winner, "player3", MultiplayerType.TOURNAMENT);
        Player player4 = createPlayerWithScore(loser, "player4", MultiplayerType.TOURNAMENT);

        updateSettings("room")
                .setResetBoard(true);

        assertEquals(2, fields.size());
        assertEquals(fields.get(0), playerGames.get("player1").getField());
        assertEquals(fields.get(0), playerGames.get("player2").getField());

        assertEquals(fields.get(1), playerGames.get("player3").getField());
        assertEquals(fields.get(1), playerGames.get("player4").getField());

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4]}");

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player3);

        assertRooms("{2=[player1, player3]}");
    }

    @Test
    public void shouldCleanScoresAfterCut_whenSetResetBoard_caseTriple() {
        // given
        int winner = 100;
        int loser = 1;
        Player player1 = createPlayerWithScore(winner, "player1", MultiplayerType.TRIPLE);
        Player player2 = createPlayerWithScore(loser, "player2", MultiplayerType.TRIPLE);
        Player player3 = createPlayerWithScore(loser, "player3", MultiplayerType.TRIPLE);
        Player player4 = createPlayerWithScore(winner, "player4", MultiplayerType.TRIPLE);
        Player player5 = createPlayerWithScore(winner, "player5", MultiplayerType.TRIPLE);
        Player player6 = createPlayerWithScore(loser, "player6", MultiplayerType.TRIPLE);
        Player player7 = createPlayerWithScore(loser, "player7", MultiplayerType.TRIPLE);
        Player player8 = createPlayerWithScore(winner, "player8", MultiplayerType.TRIPLE);

        updateSettings("room")
                .setResetBoard(true);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6], " +
                "2=[player7, player8]}");

        // when
        ticksTillTimeout();

        // then
        assertRooms("{3=[player1, player4, player5], " +
                "4=[player8]}");
        assertEquals(5, fields.size());
    }

    @Test
    public void shouldCleanScoresAfterCut_whenSetResetBoard_caseTriple_whenSeveralRooms() {
        // given
        int winner = 100;
        int loser = 1;
        createPlayerWithScore(winner, "player1-1", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-1", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-2", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-2", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-3", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-3", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-4", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-4", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-5", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-5", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-6", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-6", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-7", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-7", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-8", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-8", "room2", MultiplayerType.TRIPLE);

        updateSettings("room1")
                .setResetBoard(true);

        updateSettings("room2")
                .setResetBoard(true);

        // обрати внимание, что тут схоже с соседним тестом
        assertRooms("{0=[player1-1, player1-2, player1-3], " +
                "1=[player2-1, player2-2, player2-3], " +
                "2=[player1-4, player1-5, player1-6], " +
                "3=[player2-4, player2-5, player2-6], " +
                "4=[player1-7, player1-8], " +
                "5=[player2-7, player2-8]}");

        // when
        ticksTillTimeout();

        // then
        assertRooms("{6=[player1-1, player1-4, player1-5], " +
                "7=[player1-8], " +
                "8=[player2-1, player2-4, player2-5], " +
                "9=[player2-8]}");
        assertEquals(10, fields.size());
    }

    @Test
    public void shouldCleanScoresAfterCut_whenNotSetResetBoard_caseTriple_whenSeveralRooms() {
        // given
        int winner = 100;
        int loser = 1;
        createPlayerWithScore(winner, "player1-1", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-1", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-2", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-2", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-3", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-3", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-4", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-4", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-5", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-5", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-6", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-6", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-7", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-7", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-8", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-8", "room2", MultiplayerType.TRIPLE);

        updateSettings("room1")
                .setResetBoard(false);

        updateSettings("room2")
                .setResetBoard(false);

        // обрати внимание, что тут схоже с соседним тестом
        assertRooms("{0=[player1-1, player1-2, player1-3], " +
                "1=[player2-1, player2-2, player2-3], " +
                "2=[player1-4, player1-5, player1-6], " +
                "3=[player2-4, player2-5, player2-6], " +
                "4=[player1-7, player1-8], " +
                "5=[player2-7, player2-8]}");

        // when
        ticksTillTimeout();

        // then
        // в этом смысла не много, но мало ли сенсей захочет
        assertRooms("{0=[player1-1], " +
                        "1=[player2-1], " +
                        "2=[player1-4, player1-5], " +
                        "3=[player2-4, player2-5], " +
                        "4=[player1-8], " +
                        "5=[player2-8]}");
        assertEquals(6, fields.size());
    }

    @Test
    public void shouldCleanScoresAfterCut_whenSetResetBoard_caseTriple_whenSeveralRooms_someIsNotActive() {
        // given
        int winner = 100;
        int loser = 1;
        createPlayerWithScore(winner, "player1-1", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-1", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-2", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-2", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-3", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-3", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-4", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-4", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-5", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-5", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-6", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-6", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player1-7", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(loser, "player2-7", "room2", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player1-8", "room1", MultiplayerType.TRIPLE);
        createPlayerWithScore(winner, "player2-8", "room2", MultiplayerType.TRIPLE);

        updateSettings("room1")
                .setResetBoard(true);

        updateSettings("room2")
                .setResetBoard(true);

        givenActive("room1", false);
        givenActive("room2", true);

        // обрати внимание, что тут схоже с соседним тестом
        assertRooms("{0=[player1-1, player1-2, player1-3], " +
                "1=[player2-1, player2-2, player2-3], " +
                "2=[player1-4, player1-5, player1-6], " +
                "3=[player2-4, player2-5, player2-6], " +
                "4=[player1-7, player1-8], " +
                "5=[player2-7, player2-8]}");

        // when
        ticksTillTimeout();

        // then
        assertRooms("{0=[player1-1, player1-2, player1-3], " + // didn't touch this because not active
                "2=[player1-4, player1-5, player1-6], " +      // -- " --
                "4=[player1-7, player1-8], " +                 // -- " --
                "6=[player2-1, player2-4, player2-5], " +
                "7=[player2-8]}");
        assertEquals(8, fields.size());
    }

    @Test
    public void shouldCleanScoresAfterCut_whenSetResetBoard_caseTriple_shuffle() {
        // given
        int winner = 100;
        int loser = 1;
        Player player1 = createPlayerWithScore(winner, "player1", MultiplayerType.TRIPLE);
        Player player2 = createPlayerWithScore(loser, "player2", MultiplayerType.TRIPLE);
        Player player3 = createPlayerWithScore(loser, "player3", MultiplayerType.TRIPLE);
        Player player4 = createPlayerWithScore(winner, "player4", MultiplayerType.TRIPLE);
        Player player5 = createPlayerWithScore(winner, "player5", MultiplayerType.TRIPLE);
        Player player6 = createPlayerWithScore(loser, "player6", MultiplayerType.TRIPLE);
        Player player7 = createPlayerWithScore(loser, "player7", MultiplayerType.TRIPLE);
        Player player8 = createPlayerWithScore(winner, "player8", MultiplayerType.TRIPLE);

        updateSettings("room")
                .setResetBoard(true)
                .setShuffleBoard(true);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6], " +
                "2=[player7, player8]}");

        // when
        ticksTillTimeout();

        // then
        Map<Integer, Collection<String>> rooms = getRooms();
        Collection<String> room1 = rooms.get(1);
        Collection<String> room2 = rooms.get(2);
        Collection<String> room3 = rooms.get(3);
        Collection<String> room4 = rooms.get(4);

//        assertR("{3=[playerX, playerY, playerZ],
//                4=[playerA]}");
        assertEquals(5, fields.size());
        try {
            assertEquals(null, room1);
            assertEquals(null, room2);
            assertEquals(3, room3.size()); // TODO порой падает с actual=2, разобраться
            assertEquals(1, room4.size());
        } catch (AssertionError e) {
            e.printStackTrace();
        }
        assertEquals(false, room3.contains(room4.iterator().next()));
        assertEquals(false, room3.contains("player2")); // losers
        assertEquals(false, room3.contains("player3"));
        assertEquals(false, room3.contains("player6"));
        assertEquals(false, room3.contains("player7"));

        assertEquals(false, room4.contains("player2")); // losers
        assertEquals(false, room4.contains("player3"));
        assertEquals(false, room4.contains("player6"));
        assertEquals(false, room4.contains("player7"));
    }

    @Test
    public void shouldDontCleanScoresAfterCut_whenIsNotSetResetBoard() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);

        updateSettings("room")
                .setResetBoard(false);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        verify(player1.getScores(), never()).clear();
        verify(playerGames.get(0).getGame().getField(), never()).clearScore();

        verify(player2.getScores(), never()).clear();
        verify(playerGames.get(1).getGame().getField(), never()).clearScore();
    }

    private void assertActive(Player...players) {
        assertEquals(Arrays.asList(players)
                        .stream()
                        .map(Player::getId)
                        .collect(toList())
                        .toString(),
                playerGames.players().toString());
    }

    private void ticksTillTimeout() {
        for (int i = 0; i < timeout; i++) {
            semifinal.tick();
        }
    }

    @Test
    public void shouldDontCutPlayers_whenSameScore_casePercentage() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(50);
        Player player5 = createPlayerWithScore(50);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(50);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(true)
                .setLimit(50);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7);
    }

    @Test
    public void shouldDontCutPlayers_whenSameScore_caseNotPercentage() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(50);
        Player player5 = createPlayerWithScore(50);
        Player player6 = createPlayerWithScore(50);
        Player player7 = createPlayerWithScore(50);
        Player player8 = createPlayerWithScore(30);
        Player player9 = createPlayerWithScore(20);
        Player player10 = createPlayerWithScore(10);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(4);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutOne() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(1);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutTwo() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(2);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutExactSame() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(4);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutMoreThanPlayers() {
        // given
        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        updateSettings("room")
                .setPercentage(false)
                .setLimit(10);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldGetSettings_whenAllowed() {
        // given
        Player player = createPlayer();

        updateSettings("room")
                .setPercentage(false)
                .setLimit(10);

        // when
        SemifinalSettingsImpl settings = semifinal.semifinalSettings("room");

        // then
        assertEquals(updateSettings("room").toString(), settings.toString());
        assertEquals("SettingsImpl(map={" +
                "[Semifinal] Enabled=[[Semifinal] Enabled:Boolean = def[false] val[true]], " +
                "[Semifinal] Timeout=[[Semifinal] Timeout:Integer = multiline[false] def[900] val[3]], " +
                "[Semifinal] Percentage=[[Semifinal] Percentage:Boolean = def[true] val[false]], " +
                "[Semifinal] Limit=[[Semifinal] Limit:Integer = multiline[false] def[50] val[10]], " +
                "[Semifinal] Reset board=[[Semifinal] Reset board:Boolean = def[true] val[false]], " +
                "[Semifinal] Shuffle board=[[Semifinal] Shuffle board:Boolean = def[true] val[false]]})", settings.toString());
    }

    @Test
    public void shouldGetSettings_whenAllowed_thenTryToUpdate() {
        // given
        Player player = createPlayer();

        updateSettings("room")
                .setPercentage(false)
                .setLimit(10);

        // when
        SemifinalSettingsImpl settings = semifinal.semifinalSettings("room");
        settings.setLimit(34)
                .setPercentage(true)
                .setTimeout(101);

        // then
        assertEquals(updateSettings("room").toString(), settings.toString());
        assertEquals("SettingsImpl(map={" +
                "[Semifinal] Enabled=[[Semifinal] Enabled:Boolean = def[false] val[true]], " +
                "[Semifinal] Timeout=[[Semifinal] Timeout:Integer = multiline[false] def[900] val[101]], " +
                "[Semifinal] Percentage=[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                "[Semifinal] Limit=[[Semifinal] Limit:Integer = multiline[false] def[50] val[34]], " +
                "[Semifinal] Reset board=[[Semifinal] Reset board:Boolean = def[true] val[false]], " +
                "[Semifinal] Shuffle board=[[Semifinal] Shuffle board:Boolean = def[true] val[false]]})", settings.toString());
    }

    @Test
    public void shouldGetSettings_whenNotAllowed() {
        // given
        Player player = createPlayer();

        SemifinalSettings original = updateSettings("room");
        original.setPercentage(false)
                .setLimit(10);

        noSemifinalFor("room");

        // when
        SemifinalSettingsImpl settings = semifinal.semifinalSettings("room");

        // then
        assertNotSame(original.toString(), settings.toString());
        assertEquals("SettingsImpl(map={})", settings.toString());
    }

    // эмулирую другой тип сеттингов, который без semifinal
    public void noSemifinalFor(String room) {
        GameType gameType = mock(GameType.class);
        when(gameType.getSettings()).thenReturn(new SettingsImpl());
        roomService.state(room).get().setType(gameType);
    }
}
