package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class MinesweeperPlayerScoresTest {
    private PlayerScores scores;
    private SettingsImpl parameters = new SettingsImpl();

    public void minesweeperDestroyMine() {
        scores.event(MinesweeperEvents.DESTROY_MINE.name());
    }

    public void minesweeperForgetCharge() {
        scores.event(MinesweeperEvents.FORGET_CHARGE.name());
    }

    public void minesweeperKillOnMine() {
        scores.event(MinesweeperEvents.KILL_ON_MINE.name());
    }

    public void minesweeperNoMoreCharge() {
        scores.event(MinesweeperEvents.NO_MORE_CHARGE.name());
    }

    public void minesweeperClearBoard() {
        scores.event(MinesweeperEvents.CLEAN_BOARD.name());
    }

    public void minesweeperWin() {
        scores.event(MinesweeperEvents.WIN.name());
    }

    @Test
    public void shouldCollectScores() {
        scores = new MinesweeperPlayerScores(140, parameters);

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
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperKillOnMine();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterForgotCharge() {
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperForgetCharge();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterNoMoreCharge() {
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperNoMoreCharge();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZeroAfterDead() {
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperDestroyMine();   // +1
        minesweeperKillOnMine();    //-15

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCountAfterForgotCharge() {
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperDestroyMine();   // +1
        minesweeperDestroyMine();   // +2
        minesweeperForgetCharge();    //-5

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZeroAfterManyTimesForgotCharge() {
        scores = new MinesweeperPlayerScores(0, parameters);

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
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperWin();    // +300

        Integer score = parameters.getParameter("Win score").type(Integer.class).getValue();
        assertEquals(score.intValue(), scores.getScore());
    }

    @Test
    public void shouldScore_whenClearBoard() {
        scores = new MinesweeperPlayerScores(0, parameters);

        minesweeperClearBoard();    // +1

        Integer score = parameters.getParameter("Clear board score").type(Integer.class).getValue();
        assertEquals(score.intValue(), scores.getScore());

    }


}
