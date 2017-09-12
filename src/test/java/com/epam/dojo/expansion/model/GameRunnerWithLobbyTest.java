package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.epam.dojo.expansion.services.SettingsWrapper;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.epam.dojo.expansion.model.AbstractSinglePlayersTest.*;
import static com.epam.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Sanja on 15.02.14.
 */
public class GameRunnerWithLobbyTest extends AbstractGameRunnerTest {

    @Override
    public void setup() {
        super.setup();

        SettingsWrapper.data
                .lobbyEnable(true)
                .lobbyCapacity(-1)
                .shufflePlayers(false);
    }

    public static final String LOBBY_LEVEL =
            "╔══════════════════┐\n" +
            "║..................│\n" +
            "║..................│\n" +
            "║..................│\n" +
            "║..B....BBB..BBB...│\n" +
            "║..B....B.B..B.B...│\n" +
            "║..B....B.B..BBB...│\n" +
            "║..B....B.B..B.B...│\n" +
            "║..BBB..BBB..BBB...│\n" +
            "║..................│\n" +
            "║..................│\n" +
            "║........BBB..B.B..│\n" +
            "║........B.B..B.B..│\n" +
            "║........BBB...B...│\n" +
            "║........B.B...B...│\n" +
            "║........BBB...B...│\n" +
            "║..................│\n" +
            "║..................│\n" +
            "║..................│\n" +
            "└──────────────────┘\n";
    public static final String LOBBY_FORCES =
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n" +
            "--------------------\n";

    @Test
    public void shouldCreateSixPlayersInTwoDifferentRooms() {
        givenLevels();

        createNewGame();
        createNewGame();
        createNewGame();
        createNewGame();
        createNewGame();
        createNewGame();

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);
        assertL(LOBBY_LEVEL, PLAYER2);
        assertE(LOBBY_FORCES, PLAYER2);
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);
        assertL(LOBBY_LEVEL, PLAYER4);
        assertE(LOBBY_FORCES, PLAYER4);
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);
        assertL(LOBBY_LEVEL, PLAYER6);
        assertE(LOBBY_FORCES, PLAYER6);

        // when
        levelOrFreeRoom(LEVEL1,  // LEVEL1
                0, // first free room
                0, // first free room
                0, // first free room
                LEVEL2,
                0); // first free room

        tickAll();

        // then
        String level1 =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces1 =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "-♠--♣-\n" +
                "------\n";
        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);

        String level2 =
                "╔════┐\n" +
                "║..1.│\n" +
                "║4...│\n" +
                "║...2│\n" +
                "║.3..│\n" +
                "└────┘\n";
        String forces2 =
                "------\n" +
                "---♥--\n" +
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n";
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);
    }

    @Test
    public void shouldNextTwoPlayersGoToLobby() {
        shouldCreateSixPlayersInTwoDifferentRooms();

        createNewGame();
        createNewGame();

        // first free room
        tickAll();

        String level2 =
                "╔════┐\n" +
                "║..1.│\n" +
                "║4...│\n" +
                "║...2│\n" +
                "║.3..│\n" +
                "└────┘\n";
        String forces2 =
                "------\n" +
                "---♥--\n" +
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n";
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER7);
        assertE(LOBBY_FORCES, PLAYER7);
        assertL(LOBBY_LEVEL, PLAYER8);
        assertE(LOBBY_FORCES, PLAYER8);
    }

    @Test
    public void shouldWhenOneUserShouldResetLevelThenGoToLobby() {
        shouldCreateSixPlayersInTwoDifferentRooms();

        game(PLAYER1).getJoystick().act(0); // player want to leave room
        tickAll();

        String level1 =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces1 =
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n" +
                "-♠--♣-\n" +
                "------\n";
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);

        String level2 =
                "╔════┐\n" +
                "║..1.│\n" +
                "║4...│\n" +
                "║...2│\n" +
                "║.3..│\n" +
                "└────┘\n";
        String forces2 =
                "------\n" +
                "---♥--\n" +
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n";
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);

        // when
        tickAll();

        // then nothing changed
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);

        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);
    }

    @Test
    public void shouldNewUserCanGoToLobby() {
        shouldWhenOneUserShouldResetLevelThenGoToLobby();

        createNewGame();

        String level1 =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces1 =
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n" +
                "-♠--♣-\n" +
                "------\n";
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);

        String level2 =
                "╔════┐\n" +
                "║..1.│\n" +
                "║4...│\n" +
                "║...2│\n" +
                "║.3..│\n" +
                "└────┘\n";
        String forces2 =
                "------\n" +
                "---♥--\n" +
                "------\n" +
                "----♦-\n" +
                "------\n" +
                "------\n";
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);
        assertL(LOBBY_LEVEL, PLAYER7);
        assertE(LOBBY_FORCES, PLAYER7);
    }

    @Test
    public void shouldResetAllUsersAfterRoundTicksIsUp() {
        int old = data.roundTicks();
        try {
            int ROUND_TICKS = 10;
            data.roundTicks(ROUND_TICKS);
            shouldCreateSixPlayersInTwoDifferentRooms();

            destroy(PLAYER1);
            destroy(PLAYER2);

            String level1 =
                    "╔════┐\n" +
                    "║1..2│\n" +
                    "║....│\n" +
                    "║....│\n" +
                    "║4..3│\n" +
                    "└────┘\n";
            String forces1 =
                    "------\n" +
                    "------\n" +
                    "------\n" +
                    "------\n" +
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);

            String level2 =
                    "╔════┐\n" +
                    "║..1.│\n" +
                    "║4...│\n" +
                    "║...2│\n" +
                    "║.3..│\n" +
                    "└────┘\n";
            String forces2 =
                    "------\n" +
                    "---♥--\n" +
                    "------\n" +
                    "----♦-\n" +
                    "------\n" +
                    "------\n";
            assertL(level2, PLAYER5);
            assertE(forces2, PLAYER5);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);

            for (int i = 0; i < ROUND_TICKS - 1; i++) {
                tickAll();
            }

            assertL(LOBBY_LEVEL, PLAYER3);
            assertE(LOBBY_FORCES, PLAYER3);
            assertL(LOBBY_LEVEL, PLAYER4);
            assertE(LOBBY_FORCES, PLAYER4);
            assertL(LOBBY_LEVEL, PLAYER5);
            assertE(LOBBY_FORCES, PLAYER5);
            assertL(LOBBY_LEVEL, PLAYER6);
            assertE(LOBBY_FORCES, PLAYER6);

            gotoFreeRoom(1, // PLAYER3
                    0, // PLAYER4
                    0, // PLAYER5
                    1); // PLAYER6
            tickAll();

            level1 =
                    "╔════┐\n" +
                    "║1..2│\n" +
                    "║....│\n" +
                    "║....│\n" +
                    "║4..3│\n" +
                    "└────┘\n";
            forces1 =
                    "------\n" +
                    "-♥--♦-\n" +
                    "------\n" +
                    "------\n" +
                    "------\n" +
                    "------\n";
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level1, PLAYER5);
            assertE(forces1, PLAYER5);

            level2 =
                    "╔════┐\n" +
                    "║..1.│\n" +
                    "║4...│\n" +
                    "║...2│\n" +
                    "║.3..│\n" +
                    "└────┘\n";
            forces2 =
                    "------\n" +
                    "---♥--\n" +
                    "------\n" +
                    "----♦-\n" +
                    "------\n" +
                    "------\n";
            assertL(level2, PLAYER3);
            assertE(forces2, PLAYER3);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);

        } finally {
            data.roundTicks(old);
        }
    }

    @Test
    public void shouldPutFirstPlayerToLobby() {
        givenLevels();

        // when
        createNewGame();
        tickAll();

        // then
        assertL(LOBBY_LEVEL, PLAYER1);

        // when
        createNewGame();
        tickAll();

        // then
        String level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
    }

    @Test
    public void shouldAllNFirstPlayersWillBeOnLobby_whenSetLobbyCapacity() {
        int old = data.lobbyCapacity();
        try {
            data.lobbyCapacity(6);
            givenLevels();

            // when
            createNewGame();
            tickAll();

            // then
            assertL(LOBBY_LEVEL, PLAYER1);

            // when
            createNewGame();
            tickAll();

            // then
            assertL(LOBBY_LEVEL, PLAYER1);
            assertL(LOBBY_LEVEL, PLAYER2);

            // when
            for (int i = 3; i <= 5; i++) {
                createNewGame();
                tickAll();
            }

            // then
            assertL(LOBBY_LEVEL, PLAYER1);
            assertL(LOBBY_LEVEL, PLAYER2);
            assertL(LOBBY_LEVEL, PLAYER3);
            assertL(LOBBY_LEVEL, PLAYER4);
            assertL(LOBBY_LEVEL, PLAYER5);

            // when
            createNewGame();
            tickAll();

            // then
            String level1 =
                    "╔════┐\n" +
                    "║1..2│\n" +
                    "║....│\n" +
                    "║....│\n" +
                    "║4..3│\n" +
                    "└────┘\n";
            String forces1 =
                    "------\n" +
                    "-♥--♦-\n" +
                    "------\n" +
                    "------\n" +
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level1, PLAYER1);
            assertE(forces1, PLAYER1);
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);

            String level2 =
                    "╔════┐\n" +
                    "║..1.│\n" +
                    "║4...│\n" +
                    "║...2│\n" +
                    "║.3..│\n" +
                    "└────┘\n";
            String forces2 =
                    "------\n" +
                    "---♥--\n" +
                    "------\n" +
                    "----♦-\n" +
                    "------\n" +
                    "------\n";
            assertL(level2, PLAYER5);
            assertE(forces2, PLAYER5);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);

            // when
            createNewGame();
            tickAll();

            // then
            assertL(level1, PLAYER1);
            assertE(forces1, PLAYER1);
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level2, PLAYER5);
            assertE(forces2, PLAYER5);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);

            assertL(LOBBY_LEVEL, PLAYER7);
            assertE(LOBBY_FORCES, PLAYER7);
        } finally {
            data.lobbyCapacity(old);
        }
    }

    @Test
    public void shouldFillAllLevelsThenStartAgain() {
        int old = data.lobbyCapacity();
        try {
            data.lobbyCapacity(33);
            givenLevels();

            // when
            for (int i = 0; i < 34; i++) {
                createNewGame();
                tickAll();
            }

            // then
            String level1 =
                    "╔════┐\n" +
                    "║1..2│\n" +
                    "║....│\n" +
                    "║....│\n" +
                    "║4..3│\n" +
                    "└────┘\n";
            String forces1 =
                    "------\n" +
                    "-♥--♦-\n" +
                    "------\n" +
                    "------\n" +
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level1, PLAYER1);
            assertE(forces1, PLAYER1);
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);

            String level2 =
                    "╔════┐\n" +
                    "║..1.│\n" +
                    "║4...│\n" +
                    "║...2│\n" +
                    "║.3..│\n" +
                    "└────┘\n";
            String forces2 =
                    "------\n" +
                    "---♥--\n" +
                    "-♠----\n" +
                    "----♦-\n" +
                    "--♣---\n" +
                    "------\n";
            assertL(level2, PLAYER5);
            assertE(forces2, PLAYER5);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);
            assertL(level2, PLAYER7);
            assertE(forces2, PLAYER7);
            assertL(level2, PLAYER8);
            assertE(forces2, PLAYER8);

            String level3 =
                    "╔════┐\n" +
                    "║.1..│\n" +
                    "║...2│\n" +
                    "║4...│\n" +
                    "║..3.│\n" +
                    "└────┘\n";
            String forces3 =
                    "------\n" +
                    "--♥---\n" +
                    "----♦-\n" +
                    "-♠----\n" +
                    "---♣--\n" +
                    "------\n";
            assertL(level3, PLAYER9);
            assertE(forces3, PLAYER9);
            assertL(level3, PLAYER10);
            assertE(forces3, PLAYER10);
            assertL(level3, PLAYER11);
            assertE(forces3, PLAYER11);
            assertL(level3, PLAYER12);
            assertE(forces3, PLAYER12);

            String level4 =
                    "╔════┐\n" +
                    "║....│\n" +
                    "║.12.│\n" +
                    "║.43.│\n" +
                    "║....│\n" +
                    "└────┘\n";
            String forces4 =
                    "------\n" +
                    "------\n" +
                    "--♥♦--\n" +
                    "--♠♣--\n" +
                    "------\n" +
                    "------\n";
            assertL(level4, PLAYER13);
            assertE(forces4, PLAYER13);
            assertL(level4, PLAYER14);
            assertE(forces4, PLAYER14);
            assertL(level4, PLAYER15);
            assertE(forces4, PLAYER15);
            assertL(level4, PLAYER16);
            assertE(forces4, PLAYER16);

            String level5 = level1;
            String forces5 = forces1;
            assertL(level5, PLAYER17);
            assertE(forces5, PLAYER17);
            assertL(level5, PLAYER18);
            assertE(forces5, PLAYER18);
            assertL(level5, PLAYER19);
            assertE(forces5, PLAYER19);
            assertL(level5, PLAYER20);
            assertE(forces5, PLAYER20);

            String level6 = level2;
            String forces6 = forces2;
            assertL(level6, PLAYER21);
            assertE(forces6, PLAYER21);
            assertL(level6, PLAYER22);
            assertE(forces6, PLAYER22);
            assertL(level6, PLAYER23);
            assertE(forces6, PLAYER23);
            assertL(level6, PLAYER24);
            assertE(forces6, PLAYER24);

            String level7 = level3;
            String forces7 = forces3;
            assertL(level7, PLAYER25);
            assertE(forces7, PLAYER25);
            assertL(level7, PLAYER26);
            assertE(forces7, PLAYER26);
            assertL(level7, PLAYER27);
            assertE(forces7, PLAYER27);
            assertL(level7, PLAYER28);
            assertE(forces7, PLAYER28);

            String level8 = level4;
            String forces8 = forces4;
            assertL(level8, PLAYER29);
            assertE(forces8, PLAYER29);
            assertL(level8, PLAYER30);
            assertE(forces8, PLAYER30);
            assertL(level8, PLAYER31);
            assertE(forces8, PLAYER31);
            assertL(level8, PLAYER32);
            assertE(forces8, PLAYER32);

            String level9 = level1;
            String forces9 =
                    "------\n" +
                    "-♥----\n" +
                    "------\n" +
                    "------\n" +
                    "------\n" +
                    "------\n";
            assertL(level9, PLAYER33);
            assertE(forces9, PLAYER33);
        } finally {
            data.lobbyCapacity(old);
        }
    }

    @Test
    public void shouldPlayersCanGoAfterLobby() {
        shouldPutFirstPlayerToLobby();

        String level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);

        // when
        goTimes(PLAYER1, pt(1, 4), 2).down();
        goTimes(PLAYER2, pt(4, 4), 2).left();

        // then
        level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        forces =
                "------\n" +
                "-♥♦♦♦-\n" +
                "-♥----\n" +
                "-♥----\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);

    }

    @Test
    public void shouldPlayersCantGoOnLobby() {
        givenLevels();

        // when
        createNewGame();
        tickAll();

        // then
        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);

        // when
        goTimes(PLAYER1, pt(1, 4), 2).down();

        // then
        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);
    }

    @Test
    public void shouldNextOnePlayersCantGoBecauseOfLobby() {
        shouldPlayersCanGoAfterLobby();

        // when
        createNewGame();
        tickAll();

        // then
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);

        // when
        goTimes(PLAYER3, pt(1, 4), 2).down();

        // then
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);
    }

    @Test
    public void shouldNextOnePlayersCantGoBecauseOfLobby_butOtherCanDo() {
        shouldNextOnePlayersCantGoBecauseOfLobby();

        String level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces =
                "------\n" +
                "-♥♦♦♦-\n" +
                "-♥----\n" +
                "-♥----\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);

        // when
        goTimes(PLAYER1, pt(1, 2), 1).down();
        goTimes(PLAYER2, pt(2, 4), 1).down();

        level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        forces =
                "------\n" +
                "-♥♦♦♦-\n" +
                "-♥♦---\n" +
                "-♥----\n" +
                "-♥----\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);
    }

    @Test
    public void shouldRenewPlayerOnLobbyWhenCurrentFinished() {
        shouldPutFirstPlayerToLobby();

        String level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);

        goTimes(PLAYER1, pt(1, 4), 2).right();

        createNewGame();
        tickAll();

        forces =
                "------\n" +
                "-♥♥♥♦-\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";
        String forcesCount =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A00100100A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertF(forcesCount, PLAYER1);

        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertF(forcesCount, PLAYER2);

        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);

        // when
        for (int i = 0; i < 10; i++) { // because of 0A armies we should attack with 01
            goTimes(PLAYER1, pt(3, 4), 1).right();
        }

        forces =
                "------\n" +
                "-♥♥♥--\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";

        forcesCount =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A001001-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertF(forcesCount, PLAYER1);

        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertF(forcesCount, PLAYER2);

        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);

        // when
        tickAll();

        // then
        forces =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "----♣-\n" +
                "------\n";

        forcesCount =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertF(forcesCount, PLAYER1);

        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertF(forcesCount, PLAYER2);

        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertF(forcesCount, PLAYER3);
    }

    @Test
    public void shouldRenewFromLobbyWhenAnotherRemovedFromGame() {
        givenLevels();

        // when
        createNewGame();
        tickAll();

        destroy(PLAYER1);

        // when
        createNewGame();
        tickAll();

        // then
        assertL(LOBBY_LEVEL, PLAYER2);
        assertE(LOBBY_FORCES, PLAYER2);

        // when
        createNewGame();
        tickAll();

        // then
        String level =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces =
                "------\n" +
                "-♥--♦-\n" +
                "------\n" +
                "------\n" +
                "------\n" +
                "------\n";
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
    }
}

