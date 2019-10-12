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


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SemifinalTest extends AbstractPlayerGamesTest {

    private Semifinal semifinal;
    private int timeout;

    @Before
    public void setup() {
        timeout = 3;
        semifinal = new Semifinal();
        semifinal.setEnabled(true);
        semifinal.setPercentage(true);
        semifinal.setLimit(50);
        semifinal.setResetBoard(false);
        semifinal.setTimeout(timeout);
        semifinal.playerGames = playerGames;
        semifinal.clean();
    }

    @Test
    public void shouldDoNothing_whenDisabled() {
        // given
        semifinal.setEnabled(false);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(80);
        Player player3 = createPlayerWithScore(60);
        Player player4 = createPlayerWithScore(40);
        Player player5 = createPlayerWithScore(20);

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
        semifinal.setPercentage(true);
        semifinal.setLimit(50);

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
    public void shouldCut30PercentUsers_whenNotAccurateCut() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(30);

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
    public void shouldCut1PercentUsers_whenNotAccurateCut() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(1);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut1PercentUsers_whenNotAccurateCut_caseTwoPlayers() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(1);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(50);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldCut20PercentUsers_whenAccurateCut() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(20);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);
    }

    @Test
    public void shouldCut20PercentUsers_whenAccurateCut_mixedScoreOrder() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(20);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player8);
    }

    @Test
    public void shouldCutOnly3Users_from10() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(3);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);
    }

    @Test
    public void shouldCutOnly3Users_from3() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(3);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3);
    }

    @Test
    public void shouldCutOnly3Users_from1() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(3);

        Player player1 = createPlayerWithScore(100);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1);
    }

    @Test
    public void shouldLeaveLastUser() {
        // given
        semifinal.setPercentage(true);
        semifinal.setLimit(50);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(50);

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
    public void shouldCleanScoresAfterCut_whenSetResetBoard() {
        // given
        semifinal.setResetBoard(true);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2);

        verify(player1.getScores()).clear();
        verify(playerGames.get(0).getGame().getField()).clearScore();

        verify(player2.getScores()).clear();
        verify(playerGames.get(1).getGame().getField()).clearScore();
    }

    @Test
    public void shouldDontCleanScoresAfterCut_whenIsNotSetResetBoard() {
        // given
        semifinal.setResetBoard(false);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(90);
        Player player3 = createPlayerWithScore(80);
        Player player4 = createPlayerWithScore(70);

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
                        .map(Player::getName)
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
        semifinal.setPercentage(true);
        semifinal.setLimit(50);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7);
    }

    @Test
    public void shouldDontCutPlayers_whenSameScore_caseNotPercentage() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(4);

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

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4, player5, player6, player7);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutOne() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(1);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutTwo() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(2);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutExactSame() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(4);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }

    @Test
    public void shouldDontCutPlayers_whenAllScoresAreSame_cutMoreThanPlayers() {
        // given
        semifinal.setPercentage(false);
        semifinal.setLimit(10);

        Player player1 = createPlayerWithScore(100);
        Player player2 = createPlayerWithScore(100);
        Player player3 = createPlayerWithScore(100);
        Player player4 = createPlayerWithScore(100);

        // when
        ticksTillTimeout();

        // then
        assertActive(player1, player2, player3, player4);
    }
}
