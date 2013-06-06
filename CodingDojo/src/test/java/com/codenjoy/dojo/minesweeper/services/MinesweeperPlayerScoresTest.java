package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.snake.services.SnakeEvents;
import com.codenjoy.dojo.snake.services.SnakePlayerScores;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class MinesweeperPlayerScoresTest {
    private PlayerScores scores;

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
        scores = new MinesweeperPlayerScores(140);

        minesweeperDestroyMine();  //+1
        minesweeperDestroyMine();  //+2
        minesweeperDestroyMine();  //+3
        minesweeperDestroyMine();  //+4

        minesweeperForgetCharge();  //-5

        minesweeperNoMoreCharge();  //-15

        minesweeperKillOnMine();    //-15

        assertEquals(140 + 1 + 2 + 3 + 4 - MinesweeperPlayerScores.FORGOT_PENALTY - 2*MinesweeperPlayerScores.GAME_OVER_PENALTY,
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperKillOnMine();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterForgotCharge() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperForgetCharge();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterNoMoreCharge() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperNoMoreCharge();    //-15

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZeroAfterDead() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperDestroyMine();   // +1
        minesweeperKillOnMine();    //-15

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCountAfterForgotCharge() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperDestroyMine();   // +1
        minesweeperDestroyMine();   // +2
        minesweeperForgetCharge();    //-5

        minesweeperDestroyMine();   // +1

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZeroAfterManyTimesForgotCharge() {
        scores = new MinesweeperPlayerScores(0);

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
        scores = new MinesweeperPlayerScores(0);

        minesweeperWin();    // +300

        assertEquals(MinesweeperPlayerScores.WIN_SCORE, scores.getScore());
    }

    @Test
    public void shouldScore_whenClearBoard() {
        scores = new MinesweeperPlayerScores(0);

        minesweeperClearBoard();    // +1

        assertEquals(MinesweeperPlayerScores.CLEAR_BOARD_SCORE, scores.getScore());
    }


}
