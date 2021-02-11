package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.expansion.model.AbstractSinglePlayersTest.*;
import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static org.junit.Assert.assertEquals;

/**
 * Created by Sanja on 15.02.14.
 */
@Ignore("TODO: пофиксить создание игры")
public class GameRunnerWithLobbyTest extends AbstractGameRunnerTest {

    @Override
    public void setup() {
        super.setup();

        SettingsWrapper.data
//                .lobbyEnable(true)
//                .lobbyCapacity(4)
                .defenderHasAdvantage(false)
                .shufflePlayers(false);
    }

    public static final String LOBBY_LEVEL =
            "                    \n" +
            "                    \n" +
            "   O     OO  OOO    \n" +
            "   O    O  O O  O   \n" +
            "   O    O  O O  O   \n" +
            "   O    O  O OOO    \n" +
            "   O    O  O O  O   \n" +
            "   O    O  O O  O   \n" +
            "   OOOO  OO  OOO    \n" +
            "                    \n" +
            "                    \n" +
            "         OOO  O  O  \n" +
            "         O  O O  O  \n" +
            "    $    O  O O  O  \n" +
            "   $O$   OOO   OOO  \n" +
            "    $    O  O    O  \n" +
            "         O  O   O   \n" +
            "         OOO  OO    \n" +
            "                    \n" +
            "                    \n";
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
    public void shouldCreateEightPlayersInTwoDifferentRooms() {
        givenLevels();

        createNewGame();
        createNewGame();
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
        assertL(LOBBY_LEVEL, PLAYER7);
        assertE(LOBBY_FORCES, PLAYER7);
        assertL(LOBBY_LEVEL, PLAYER8);
        assertE(LOBBY_FORCES, PLAYER8);

        // when
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
    }

    @Test
    public void shouldCreateSixPlayersInTwoDifferentRooms_twoWillBeOnLobby() {
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

        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);
        assertL(LOBBY_LEVEL, PLAYER6);
        assertE(LOBBY_FORCES, PLAYER6);
    }

    @Test
    public void shouldCreateSixPlayersInTwoDifferentRooms_twoWillBeOnLobby_thenCreateTwoAndAllStartsGame() {
        shouldCreateSixPlayersInTwoDifferentRooms_twoWillBeOnLobby();

        // when
        // total users = 8
        createNewGame();
        createNewGame();

        tickAll();

        // then
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
    }

    @Test
    public void shouldNextTwoPlayersGoToLobby() {
        shouldCreateEightPlayersInTwoDifferentRooms();

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

        assertL(LOBBY_LEVEL, PLAYER9);
        assertE(LOBBY_FORCES, PLAYER9);
        assertL(LOBBY_LEVEL, PLAYER10);
        assertE(LOBBY_FORCES, PLAYER10);
    }

    @Test
    public void shouldWhenOneUserShouldResetLevelThenGoToLobby() {
        shouldCreateEightPlayersInTwoDifferentRooms();

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
        assertL(level2, PLAYER7);
        assertE(forces2, PLAYER7);
        assertL(level2, PLAYER8);
        assertE(forces2, PLAYER8);

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

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);
        assertL(LOBBY_LEVEL, PLAYER9);
        assertE(LOBBY_FORCES, PLAYER9);
    }

    @Test
    public void shouldResetAllUsersAfterRoundTicksIsUp() {
        int old = data.roundTicks();
        try {
            int ROUND_TICKS = 10;
            data.roundTicks(ROUND_TICKS);
            shouldCreateEightPlayersInTwoDifferentRooms();

            destroy(PLAYER1);
            destroy(PLAYER2);
            destroy(PLAYER7);
            destroy(PLAYER8);

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

            assertEquals(0, getRound(PLAYER3));
            assertEquals(0, getRound(PLAYER4));
            assertEquals(0, getRound(PLAYER5));
            assertEquals(0, getRound(PLAYER6));

            for (int i = 0; i < ROUND_TICKS - 1; i++) {
                tickAll();
            }

            assertEquals(9, getRound(PLAYER3));
            assertEquals(9, getRound(PLAYER4));
            assertEquals(9, getRound(PLAYER5));
            assertEquals(9, getRound(PLAYER6));

            level1 =
                    "╔════┐\n" +
                    "║1..2│\n" +
                    "║....│\n" +
                    "║....│\n" +
                    "║4..3│\n" +
                    "└────┘\n";
            forces1 =
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
            assertL(level2, PLAYER5);
            assertE(forces2, PLAYER5);
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);

            tickAll();

            assertEquals(0, getRound(PLAYER3));
            assertEquals(0, getRound(PLAYER4));
            assertEquals(0, getRound(PLAYER5));
            assertEquals(0, getRound(PLAYER6));

            level1 =
                    "╔════┐\n" +
                    "║.1..│\n" +
                    "║...2│\n" +
                    "║4...│\n" +
                    "║..3.│\n" +
                    "└────┘\n";
            forces1 =
                    "------\n" +
                    "--♥---\n" +
                    "----♦-\n" +
                    "-♠----\n" +
                    "---♣--\n" +
                    "------\n";
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level1, PLAYER5);
            assertE(forces1, PLAYER5);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER6);
            assertE(forces1, PLAYER6);

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
        createNewGame();
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
                "-♠--♣-\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);
    }

    @Test
    public void shouldAllNFirstPlayersWillBeOnLobby_whenSetLobbyCapacity() {
//        int old = data.lobbyCapacity();
        try {
            final int CAPACITY = 8;
//            data.lobbyCapacity(CAPACITY);
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
            for (int i = 3; i <= CAPACITY - 1; i++) {
                createNewGame();
                tickAll();
            }

            // then
            assertL(LOBBY_LEVEL, PLAYER1);
            assertL(LOBBY_LEVEL, PLAYER2);
            assertL(LOBBY_LEVEL, PLAYER3);
            assertL(LOBBY_LEVEL, PLAYER4);
            assertL(LOBBY_LEVEL, PLAYER5);
            assertL(LOBBY_LEVEL, PLAYER6);
            assertL(LOBBY_LEVEL, PLAYER7);

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
            assertL(level2, PLAYER7);
            assertE(forces2, PLAYER7);
            assertL(level2, PLAYER8);
            assertE(forces2, PLAYER8);

            assertL(LOBBY_LEVEL, PLAYER9);
            assertE(LOBBY_FORCES, PLAYER9);
        } finally {
//            data.lobbyCapacity(old);
        }
    }

    @Test
    public void shouldFillAllLevelsThenStartAgain() {
//        int old = data.lobbyCapacity();
        try {
            final int CAPACITY = 36;
//            data.lobbyCapacity(CAPACITY);
            givenLevels();

            // when
            for (int i = 0; i < CAPACITY + 1; i++) {
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
                    "-♥--♦-\n" +
                    "------\n" +
                    "------\n" +
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level9, PLAYER33);
            assertE(forces9, PLAYER33);
            assertL(level9, PLAYER34);
            assertE(forces9, PLAYER34);
            assertL(level9, PLAYER35);
            assertE(forces9, PLAYER35);
            assertL(level9, PLAYER36);
            assertE(forces9, PLAYER36);
        } finally {
//            data.lobbyCapacity(old);
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
                "-♠--♣-\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);

        // when
        goTimes(PLAYER1, pt(1, 4), 2).down();
        goTimes(PLAYER2, pt(4, 4), 2).left();
        goTimes(PLAYER3, pt(4, 1), 2).up();
        goTimes(PLAYER4, pt(1, 1), 2).right();

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
                "-♥--♣-\n" +
                "-♥--♣-\n" +
                "-♠♠♠♣-\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);
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
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);

        // when
        goTimes(PLAYER5, pt(1, 4), 2).down();

        // then
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);
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
                "-♥--♣-\n" +
                "-♥--♣-\n" +
                "-♠♠♠♣-\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);

        // when
        goTimes(PLAYER1, pt(1, 2), 1).right();
        goTimes(PLAYER2, pt(2, 4), 1).down();
        goTimes(PLAYER3, pt(4, 3), 1).left();
        goTimes(PLAYER4, pt(3, 1), 1).up();

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
                "-♥♦♣♣-\n" +
                "-♥♥♠♣-\n" +
                "-♠♠♠♣-\n" +
                "------\n";
        assertL(level, PLAYER1);
        assertE(forces, PLAYER1);
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);
    }

    @Test
    public void shouldRenewPlayerOnLobbyWhenCurrentFinished() {
        shouldPutFirstPlayerToLobby();

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

        goTimes(PLAYER1, pt(1, 4), 2).right();

        createNewGame();
        createNewGame();
        createNewGame();
        tickAll();

        forces1 =
                "------\n" +
                "-♥♥♥♦-\n" +
                "------\n" +
                "------\n" +
                "-♠--♣-\n" +
                "------\n";
        String forcesCount1 =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A00100100A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertF(forcesCount1, PLAYER1);

        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertF(forcesCount1, PLAYER2);

        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertF(forcesCount1, PLAYER3);

        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);
        assertF(forcesCount1, PLAYER4);

        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);

        assertL(LOBBY_LEVEL, PLAYER6);
        assertE(LOBBY_FORCES, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER7);
        assertE(LOBBY_FORCES, PLAYER7);

        // when
        for (int i = 0; i < 10; i++) { // because of 0A armies we should attack with 01
            goTimes(PLAYER1, pt(3, 4), 1).right();
        }

        forces1 =
                "------\n" +
                "-♥♥♥--\n" +
                "------\n" +
                "------\n" +
                "-♠--♣-\n" +
                "------\n";

        forcesCount1 =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A001001-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertF(forcesCount1, PLAYER1);

        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertF(forcesCount1, PLAYER2);

        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertF(forcesCount1, PLAYER3);

        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);
        assertF(forcesCount1, PLAYER4);

        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);

        assertL(LOBBY_LEVEL, PLAYER6);
        assertE(LOBBY_FORCES, PLAYER6);

        assertL(LOBBY_LEVEL, PLAYER7);
        assertE(LOBBY_FORCES, PLAYER7);

        // when
        // all players want to leave room
        game(PLAYER3).getJoystick().act(0);
        tickAll();

        // then
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

        String forcesCount2 =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#00A-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertF(forcesCount2, PLAYER5);

        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);
        assertF(forcesCount2, PLAYER6);

        assertL(level2, PLAYER7);
        assertE(forces2, PLAYER7);
        assertF(forcesCount2, PLAYER7);

        assertL(level2, PLAYER3);
        assertE(forces2, PLAYER3);
        assertF(forcesCount2, PLAYER3);

        forces1 =
                "------\n" +
                "-♥♥♥--\n" +
                "------\n" +
                "------\n" +
                "-♠----\n" +
                "------\n";

        forcesCount1 =
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A001001-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n";

        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertF(forcesCount1, PLAYER1);

        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertF(forcesCount1, PLAYER2);

        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);
        assertF(forcesCount1, PLAYER4);

        // then
        game(PLAYER1).getJoystick().act(0);
        game(PLAYER2).getJoystick().act(0);
        game(PLAYER4).getJoystick().act(0);
        tickAll();

        assertL(LOBBY_LEVEL, PLAYER1);
        assertE(LOBBY_FORCES, PLAYER1);

        assertL(LOBBY_LEVEL, PLAYER2);
        assertE(LOBBY_FORCES, PLAYER2);

        assertL(LOBBY_LEVEL, PLAYER4);
        assertE(LOBBY_FORCES, PLAYER4);
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
        createNewGame();
        createNewGame();
        tickAll();

        // then
        assertL(LOBBY_LEVEL, PLAYER2);
        assertE(LOBBY_FORCES, PLAYER2);
        assertL(LOBBY_LEVEL, PLAYER3);
        assertE(LOBBY_FORCES, PLAYER3);
        assertL(LOBBY_LEVEL, PLAYER4);
        assertE(LOBBY_FORCES, PLAYER4);

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
                "-♠--♣-\n" +
                "------\n";
        assertL(level, PLAYER2);
        assertE(forces, PLAYER2);
        assertL(level, PLAYER3);
        assertE(forces, PLAYER3);
        assertL(level, PLAYER4);
        assertE(forces, PLAYER4);
        assertL(level, PLAYER5);
        assertE(forces, PLAYER5);
    }

    @Test
    public void shouldNotResetOtherPlayersWhenLobbyPlayersWillStart() {
        givenLevels();

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

        // when
        tickAll();
        destroy(PLAYER3);
        destroy(PLAYER4);

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
                "------\n" +
                "------\n";
        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);

        // when
        createNewGame();

        // then
        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);

        // when
        goTimes(PLAYER1, pt(1, 4), 2).down();
        goTimes(PLAYER2, pt(4, 4), 2).left();
        tickAll();

        // then
        forces1 =
                "------\n" +
                "-♥♦♦♦-\n" +
                "-♥----\n" +
                "-♥----\n" +
                "------\n" +
                "------\n";
        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);

        // when
        createNewGame();
        createNewGame();
        createNewGame();
        tickAll();
        destroy(PLAYER7);
        destroy(PLAYER8);

        // then
        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);

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
    public void shouldCopyAllLobbyStateToAnotherDuringChangeSettingsIfSomebodyAtLobby() {
        final int LOBBY_CAPACITY = 8;
        final int ROUNDS = 10;

        boolean old1 = data.waitingOthers();
//        int old2 = data.lobbyCapacity();
        int old3 = data.roundTicks();
        try {
            data.waitingOthers(true);
//            data.lobbyCapacity(LOBBY_CAPACITY);
            data.roundTicks(ROUNDS);

            givenLevels();

            for (int i = 0; i < LOBBY_CAPACITY; i++) {
                createNewGame();
            }

            // when then
            // first tick so all go to lobby and start new game
            tickAll();

            // second tick is for round = 1
            tickAll();

            // remove player and wait till all goes to lobby again
            destroy(PLAYER1);
            for (int i = 0; i < ROUNDS; i++) {
                tickAll();
            }

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
            assertL(LOBBY_LEVEL, PLAYER7);
            assertE(LOBBY_FORCES, PLAYER7);
            assertL(LOBBY_LEVEL, PLAYER8);
            assertE(LOBBY_FORCES, PLAYER8);

            // when then
            // try to leave lobby but cant do it
            tickAll();

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
            assertL(LOBBY_LEVEL, PLAYER7);
            assertE(LOBBY_FORCES, PLAYER7);
            assertL(LOBBY_LEVEL, PLAYER8);
            assertE(LOBBY_FORCES, PLAYER8);

            // when
            // change some settings so lobby recreates
//            data.lobbyCapacity(LOBBY_CAPACITY - 1); // just simulation
//            data.lobbyCapacity(LOBBY_CAPACITY);

            // when then
            // create new player so count players = 8 = current lobby capacity
            createNewGame();

            tickAll();

            // all players on boards (if lobby doesn't copy state - then it will be LOBBY for all)
            // we starts from LEVEL1 because factory reset also
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
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level1, PLAYER5);
            assertE(forces1, PLAYER5);

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
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);
            assertL(level2, PLAYER7);
            assertE(forces2, PLAYER7);
            assertL(level2, PLAYER8);
            assertE(forces2, PLAYER8);
            assertL(level2, PLAYER9);
            assertE(forces2, PLAYER9);
        } finally {
            data.waitingOthers(old1);
//            data.lobbyCapacity(old2);
            data.roundTicks(old3);
        }
    }

    @Test
    public void shouldLetThemGoFromLobbyIfDisableLobbyDuringTheGame() {
        final int LOBBY_CAPACITY = 8;
        final int ROUNDS = 10;

        boolean old1 = data.waitingOthers();
//        int old2 = data.lobbyCapacity();
        int old3 = data.roundTicks();
//        boolean old4 = data.lobbyEnable();
        try {
//            data.lobbyEnable(true);
            data.waitingOthers(true);
//            data.lobbyCapacity(LOBBY_CAPACITY);
            data.roundTicks(ROUNDS);

            givenLevels();

            for (int i = 0; i < LOBBY_CAPACITY; i++) {
                createNewGame();
            }

            // when then
            // first tick so all go to lobby and start new game
            tickAll();

            // second tick is for round = 1
            tickAll();

            // remove player and wait till all goes to lobby again
            destroy(PLAYER1);
            for (int i = 0; i < ROUNDS; i++) {
                tickAll();
            }

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
            assertL(LOBBY_LEVEL, PLAYER7);
            assertE(LOBBY_FORCES, PLAYER7);
            assertL(LOBBY_LEVEL, PLAYER8);
            assertE(LOBBY_FORCES, PLAYER8);

            // when then
            // try to leave lobby but cant do it
            tickAll();

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
            assertL(LOBBY_LEVEL, PLAYER7);
            assertE(LOBBY_FORCES, PLAYER7);
            assertL(LOBBY_LEVEL, PLAYER8);
            assertE(LOBBY_FORCES, PLAYER8);

            // when
            // disable lobby with players inside
//            data.lobbyEnable(false);

            tickAll();

            // then
            // all players on boards (if lobby doesn't letThemGo during change settings there sill be lobby for all)
            // we starts from LEVEL1 because factory reset also
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
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level1, PLAYER5);
            assertE(forces1, PLAYER5);

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
                    "--♣---\n" +
                    "------\n";
            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);
            assertL(level2, PLAYER7);
            assertE(forces2, PLAYER7);
            assertL(level2, PLAYER8);
            assertE(forces2, PLAYER8);

            // then
            // anybody from second room cant go
            goTimes(PLAYER6, pt(3, 4), 2).down();
            tickAll();

            assertL(level2, PLAYER6);
            assertE(forces2, PLAYER6);
            assertL(level2, PLAYER7);
            assertE(forces2, PLAYER7);
            assertL(level2, PLAYER8);
            assertE(forces2, PLAYER8);

            // but from first room - can
            goTimes(PLAYER2, pt(1, 4), 2).down();
            tickAll();

            forces1 =
                    "------\n" +
                    "-♥--♦-\n" +
                    "-♥----\n" +
                    "-♥----\n" +
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level1, PLAYER2);
            assertE(forces1, PLAYER2);
            assertL(level1, PLAYER3);
            assertE(forces1, PLAYER3);
            assertL(level1, PLAYER4);
            assertE(forces1, PLAYER4);
            assertL(level1, PLAYER5);
            assertE(forces1, PLAYER5);

        } finally {
            data.waitingOthers(old1);
//            data.lobbyCapacity(old2);
            data.roundTicks(old3);
//            data.lobbyEnable(old4);
        }
    }

    @Test
    public void shouldRunCommandLobbyFromSettings_enoughPlayers() {
//        int old = data.lobbyCapacity();
        try {
//            data.lobbyCapacity(7);

            givenLevels();

            createNewGame();
            createNewGame();
            createNewGame();
            createNewGame();
            createNewGame();

            // when then
            // first tick so all go to lobby and start new game
            tickAll();

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

            // when
            data.command("lobby.letThemGo()");
            tickAll();

            // then
            // clear settings
            assertEquals("", data.command());

            // all players go to boards
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
                    "-♠--♣-\n" +
                    "------\n";
            assertL(level, PLAYER1);
            assertE(forces, PLAYER1);
            assertL(level, PLAYER2);
            assertE(forces, PLAYER2);
            assertL(level, PLAYER3);
            assertE(forces, PLAYER3);
            assertL(level, PLAYER4);
            assertE(forces, PLAYER4);

            assertL(LOBBY_LEVEL, PLAYER5);
            assertE(LOBBY_FORCES, PLAYER5);
        } finally {
//            data.lobbyCapacity(old);
        }
    }

    @Test
    public void shouldRunCommandLobbyFromSettings_notEnoughPlayers() {
//        int old = data.lobbyCapacity();
        try {
//            data.lobbyCapacity(7);

            givenLevels();

            createNewGame();
            createNewGame();
            createNewGame();

            // when then
            // first tick so all go to lobby and start new game
            tickAll();

            assertL(LOBBY_LEVEL, PLAYER1);
            assertE(LOBBY_FORCES, PLAYER1);
            assertL(LOBBY_LEVEL, PLAYER2);
            assertE(LOBBY_FORCES, PLAYER2);
            assertL(LOBBY_LEVEL, PLAYER3);
            assertE(LOBBY_FORCES, PLAYER3);

            // when
            data.command("lobby.letThemGo()");
            tickAll();

            // then
            // clear settings
            assertEquals("", data.command());

            // all players go to boards
            assertL(LOBBY_LEVEL, PLAYER1);
            assertE(LOBBY_FORCES, PLAYER1);
            assertL(LOBBY_LEVEL, PLAYER2);
            assertE(LOBBY_FORCES, PLAYER2);
            assertL(LOBBY_LEVEL, PLAYER3);
            assertE(LOBBY_FORCES, PLAYER3);
        } finally {
//            data.lobbyCapacity(old);
        }
    }

    @Test
    public void shouldClearPlayersScoresWhenTheyOnLobby() {
        shouldCreateSixPlayersInTwoDifferentRooms_twoWillBeOnLobby();

        for (int player = PLAYER1; player <= PLAYER6; player++) {
            game(player).newGame();
        }

        tickAll();

        // then
        String level1 =
                "╔════┐\n" +
                "║..1.│\n" +
                "║4...│\n" +
                "║...2│\n" +
                "║.3..│\n" +
                "└────┘\n";
        String forces1 =
                "------\n" +
                "---♥--\n" +
                "-♠----\n" +
                "----♦-\n" +
                "--♣---\n" +
                "------\n";
        assertL(level1, PLAYER1);
        assertE(forces1, PLAYER1);
        assertL(level1, PLAYER2);
        assertE(forces1, PLAYER2);
        assertL(level1, PLAYER3);
        assertE(forces1, PLAYER3);
        assertL(level1, PLAYER4);
        assertE(forces1, PLAYER4);

        assertL(LOBBY_LEVEL, PLAYER5);
        assertE(LOBBY_FORCES, PLAYER5);
        assertL(LOBBY_LEVEL, PLAYER6);
        assertE(LOBBY_FORCES, PLAYER6);
    }
}


