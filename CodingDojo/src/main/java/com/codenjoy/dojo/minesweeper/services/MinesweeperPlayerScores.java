package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.GameLevel;
import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 11:44 PM
 */
public class MinesweeperPlayerScores implements PlayerScores {  // TODO test me

    public static final int GAME_OVER_PENALTY = 15;
    public static final int FORGOT_PENALTY = 5;

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
        } else if (name.equals(MinesweeperEvents.KILL_NO_MINE.name())) {
            onKillOnMine();
        } else if (name.equals(MinesweeperEvents.NO_MORE_CHARGE.name())) {
            onNoMoreCharge();
        }
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
        destroyed -= 10;
        if (score < 0) {
            score = 0;
        }
    }

    private void onKillOnMine() {
        score -= GAME_OVER_PENALTY;
        if (score < 0) {
            score = 0;
        }
        destroyed = 0;
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
