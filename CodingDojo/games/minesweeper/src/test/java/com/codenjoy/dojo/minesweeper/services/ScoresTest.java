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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoresTest {
    private PlayerScores scores;
    private SettingsImpl parameters = new SettingsImpl();

    public void minesweeperDestroyMine() {
        scores.event(Events.DESTROY_MINE);
    }

    public void minesweeperForgetCharge() {
        scores.event(Events.FORGET_CHARGE);
    }

    public void minesweeperKillOnMine() {
        scores.event(Events.KILL_ON_MINE);
    }

    public void minesweeperNoMoreCharge() {
        scores.event(Events.NO_MORE_CHARGE);
    }

    public void minesweeperClearBoard() {
        scores.event(Events.CLEAN_BOARD);
    }

    public void minesweeperWin() {
        scores.event(Events.WIN);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, parameters);

        minesweeperDestroyMine();  //+1
        minesweeperDestroyMine();  //+2
        minesweeperDestroyMine();  //+3
        minesweeperDestroyMine();  //+4

        minesweeperForgetCharge();  //-5

        minesweeperNoMoreCharge();  //-15

        minesweeperKillOnMine();    //-15

        Integer gameover = parameters.getParameter("Game over penalty").type(Integer.class).getValue();
        Integer forgot = parameters.getParameter("Forgot penalty").type(Integer.class).getValue();
        assertEquals(140 + 1 + 2 + 3 + 4 - forgot - 2* gameover,
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new Scores(0, parameters);

        minesweeperKillOnMine();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterForgotCharge() {
        scores = new Scores(0, parameters);

        minesweeperForgetCharge();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterNoMoreCharge() {
        scores = new Scores(0, parameters);

        minesweeperNoMoreCharge();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZeroAfterDead() {
        scores = new Scores(0, parameters);

        minesweeperDestroyMine();   // +1
        minesweeperKillOnMine();    //-15

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCountAfterForgotCharge() {
        scores = new Scores(0, parameters);

        minesweeperDestroyMine();   // +1
        minesweeperDestroyMine();   // +2
        minesweeperForgetCharge();    //-5

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZeroAfterManyTimesForgotCharge() {
        scores = new Scores(0, parameters);

        minesweeperDestroyMine();   // +1
        minesweeperDestroyMine();   // +2
        minesweeperForgetCharge();    //-5
        minesweeperForgetCharge();    //-5
        minesweeperForgetCharge();    //-5

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldScore_whenWin() {
        scores = new Scores(0, parameters);

        minesweeperWin();    // +300

        Integer score = parameters.getParameter("Win score").type(Integer.class).getValue();
        assertEquals(score, scores.getScore());
    }

    @Test
    public void shouldScore_whenClearBoard() {
        scores = new Scores(0, parameters);

        minesweeperClearBoard();    // +1

        Integer score = parameters.getParameter("Clear board score").type(Integer.class).getValue();
        assertEquals(score, scores.getScore());

    }

    @Test
    public void shouldClearScore() {
        scores = new Scores(0, parameters);

        minesweeperClearBoard();    // +1

        scores.clear();

        assertEquals(0, scores.getScore());
    }



}
