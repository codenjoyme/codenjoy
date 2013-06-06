package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.GameLevel;
import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 11:44 PM
 */
public class MinesweeperPlayerScores implements PlayerScores {

    public static final int GAME_OVER_PENALTY = 15;
    public static final int FORGOT_PENALTY = 5;
    public static final int DESTROYED_FORGOT_PENALTY = 2;
    public static final int WIN_SCORE = 300;
    public static final int CLEAR_BOARD_SCORE = 1;

    private volatile int score;
    private volatile int destroyed;

    public MinesweeperPlayerScores(int startScore) {
        this.score = startScore;
        destroyed = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(String name) {
        if (name.equals(MinesweeperEvents.DESTROY_MINE.name())) {
            onDestroyMine();
        } else if (name.equals(MinesweeperEvents.FORGET_CHARGE.name())) {
            onForgotCharge();
        } else if (name.equals(MinesweeperEvents.KILL_ON_MINE.name())) {
            onKillOnMine();
        } else if (name.equals(MinesweeperEvents.NO_MORE_CHARGE.name())) {
            onNoMoreCharge();
        } else if (name.equals(MinesweeperEvents.WIN.name())) {
            onWin();
        } else if (name.equals(MinesweeperEvents.CLEAN_BOARD.name())) {
            onClearBoard();
        }
    }

    private void onClearBoard() {
        score += CLEAR_BOARD_SCORE;
    }

    private void onWin() {
        score += WIN_SCORE;
    }

    private void onNoMoreCharge() {
        onKillOnMine();
    }

    private void onDestroyMine() {
        destroyed++;
        score += destroyed;
    }

    private void onForgotCharge() {
        score -= FORGOT_PENALTY;
        destroyed -= DESTROYED_FORGOT_PENALTY;
        score = Math.max(0, score);
        destroyed = Math.max(0, destroyed);
    }

    private void onKillOnMine() {
        score -= GAME_OVER_PENALTY;
        score = Math.max(0, score);
        destroyed = 0;
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
