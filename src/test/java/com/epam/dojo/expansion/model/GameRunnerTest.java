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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.lobby.NoPlayerLobby;
import com.epam.dojo.expansion.model.lobby.PlayerLobby;
import com.epam.dojo.expansion.services.GameRunner;
import com.epam.dojo.expansion.services.PrinterData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.function.BiConsumer;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.epam.dojo.expansion.model.AbstractSinglePlayersTest.*;
import static com.epam.dojo.expansion.services.SettingsWrapper.data;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 15.02.14.
 */
public class GameRunnerTest extends AbstractGameRunnerTest {

    @Test
    public void shouldCreateSixPlayersInTwoDifferentRooms() {
        givenLevels();

        createNewGame(LEVEL1); // LEVEL1
        createNewGame(0); // first free room
        createNewGame(0); // first free room
        createNewGame(0); // first free room
        createNewGame(LEVEL2);
        createNewGame(0); // first free room

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
    public void shouldNextTwoPlayersGoToSecondRoom() {
        shouldCreateSixPlayersInTwoDifferentRooms();

        createNewGame(0); // first free room
        createNewGame(0); // first free room

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
    public void shouldWhenOneUserShouldResetLevelThenGoToAnotherFreeRoom() {
        shouldCreateSixPlayersInTwoDifferentRooms();

        game(PLAYER1).getJoystick().act(0); // player want to leave room
        gotoFreeRoom(1); // select free room with index 1 (PLAYER5, PLAYER6)
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
                "--♣---\n" +
                "------\n";
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);
        assertL(level2, PLAYER1);
        assertE(forces2, PLAYER1);

    }

    @Test
    public void shouldWhenOneUserShouldResetLevelThenGoToAnotherFreeRoom_caseSameRoom() {
        shouldCreateSixPlayersInTwoDifferentRooms();

        game(PLAYER1).getJoystick().act(0); // player want to leave room
        gotoFreeRoom(0); // select free room with index 0 (PLAYER2, PLAYER3, PLAYER4)
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
    public void shouldNewUserCanGoToAnyFreeRoomAtThisMoment_oneOneRoom() {
        shouldWhenOneUserShouldResetLevelThenGoToAnotherFreeRoom();

        createNewGame(0); // first free room (PLAYER2, PLAYER3, PLAYER4)

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
        assertL(level1, PLAYER7);
        assertE(forces1, PLAYER7);

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
        assertL(level2, PLAYER5);
        assertE(forces2, PLAYER5);
        assertL(level2, PLAYER6);
        assertE(forces2, PLAYER6);
        assertL(level2, PLAYER1);
        assertE(forces2, PLAYER1);
    }

    @Test
    public void shouldNewUserCanGoToAnyFreeRoomAtThisMoment_anotherOneRoom() {
        shouldWhenOneUserShouldResetLevelThenGoToAnotherFreeRoom();

        createNewGame(1); // second free room (PLAYER5, PLAYER6, PLAYER1)

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
        assertL(level2, PLAYER1);
        assertE(forces2, PLAYER1);
        assertL(level2, PLAYER7);
        assertE(forces2, PLAYER7);
    }

    @Test
    public void shouldNewUserCanGoToAnyFreeRoomAtThisMoment_caseWhenBaseIsBusyInFRoomWith3Players() {
        shouldWhenOneUserShouldResetLevelThenGoToAnotherFreeRoom();

        goTimes(PLAYER2, pt(4, 4), 4).left();

        createNewGame(0); // first free room is (PLAYER5, PLAYER6, PLAYER1)
        // because base in first room is busy by another player

        String level1 =
                "╔════┐\n" +
                "║1..2│\n" +
                "║....│\n" +
                "║....│\n" +
                "║4..3│\n" +
                "└────┘\n";
        String forces1 =
                "------\n" +
                "-♦♦♦♦-\n" +
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
        assertL(level2, PLAYER1);
        assertE(forces2, PLAYER1);
        assertL(level2, PLAYER7);
        assertE(forces2, PLAYER7);
    }

    @Test
    public void shouldResetAllUsersAfterRoundTicksIsUp() {
        int old = data.roundTicks();
        try {
            data.roundTicks(10);
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

            for (int i = 0; i < 9; i++) {
                tickAll();
            }

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
        } finally {
            data.roundTicks(old);
        }
    }

}
