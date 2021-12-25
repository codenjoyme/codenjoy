package com.codenjoy.dojo.minesweeper.services;

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


import com.codenjoy.dojo.minesweeper.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.minesweeper.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void destroyMine() {
        scores.event(Event.DESTROY_MINE);
    }

    public void forgotCharge() {
        scores.event(Event.FORGET_CHARGE);
    }

    public void killOnMine() {
        scores.event(Event.KILL_ON_MINE);
    }

    public void noMoreCharge() {
        scores.event(Event.NO_MORE_CHARGE);
    }

    public void clearBoard() {
        scores.event(Event.CLEAN_BOARD);
    }

    public void minesweeperWin() {
        scores.event(Event.WIN);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    @Test
    public void shouldCollectScores() {
        givenScores(140);

        destroyMine();
        destroyMine();
        destroyMine();
        destroyMine();

        forgotCharge();

        noMoreCharge();

        killOnMine();

        clearBoard();
        clearBoard();

        minesweeperWin();

        assertEquals(140
                        + 1 + 2 + 3 + 4
                        + settings.integer(DESTROYED_FORGOT_PENALTY)
                        + 2 * settings.integer(GAME_OVER_PENALTY)
                        + 2 * settings.integer(CLEAR_BOARD_SCORE)
                        + settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        givenScores(0);

        killOnMine();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterForgotCharge() {
        givenScores(0);

        forgotCharge();

        assertEquals(0, scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldStillZeroAfterNoMoreCharge() {
        givenScores(0);

        noMoreCharge();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZeroAfterDead() {
        givenScores(0);

        destroyMine();
        killOnMine();

        destroyMine();

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCountAfterForgotCharge() {
        settings.integer(DESTROYED_PENALTY, 2);

        givenScores(0);

        destroyMine();
        destroyMine();
        destroyMine();
        destroyMine();
        destroyMine();

        forgotCharge();

        destroyMine();
        destroyMine();
        destroyMine();

        assertEquals(1 + 2 + 3 + 4 + 5
                + settings.integer(DESTROYED_FORGOT_PENALTY)
                + 4 + 5 + 6, scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZeroAfterManyTimesForgotCharge() {
        givenScores(0);

        destroyMine();
        destroyMine();

        forgotCharge();
        forgotCharge();
        forgotCharge();

        destroyMine();

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldScore_whenWin() {
        givenScores(0);

        minesweeperWin();

        assertEquals(settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldScore_whenClearBoard() {
        givenScores(0);

        clearBoard();

        assertEquals(settings.integer(CLEAR_BOARD_SCORE),
                scores.getScore());

    }

    @Test
    public void shouldClearScore() {
        givenScores(0);

        clearBoard();

        scores.clear();

        assertEquals(0, scores.getScore());
    }
}