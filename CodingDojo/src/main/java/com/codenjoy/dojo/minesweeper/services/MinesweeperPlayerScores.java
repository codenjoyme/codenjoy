package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 11:44 PM
 */
public class MinesweeperPlayerScores implements PlayerScores {

    private final Parameter<Integer> gameOverPenalty;
    private final Parameter<Integer> destroyedPenalty;
    private final Parameter<Integer> destroyedForgotPenalty;
    private final Parameter<Integer> winScore;
    private final Parameter<Integer> clearBoardScore;

    private volatile int score;
    private volatile int destroyed;

    public MinesweeperPlayerScores(int startScore, Settings settings) {
        this.score = startScore;
        destroyed = 0;

        gameOverPenalty = settings.addEditBox("Game over penalty").type(Integer.class).def(15);
        destroyedPenalty = settings.addEditBox("Forgot penalty").type(Integer.class).def(5);
        destroyedForgotPenalty = settings.addEditBox("Destoyed forgot penalty").type(Integer.class).def(2);
        winScore = settings.addEditBox("Win score").type(Integer.class).def(300);
        clearBoardScore = settings.addEditBox("Clear board score").type(Integer.class).def(1);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public void event(Object event) {
        if (event.equals(MinesweeperEvents.DESTROY_MINE)) {
            onDestroyMine();
        } else if (event.equals(MinesweeperEvents.FORGET_CHARGE)) {
            onForgotCharge();
        } else if (event.equals(MinesweeperEvents.KILL_ON_MINE)) {
            onKillOnMine();
        } else if (event.equals(MinesweeperEvents.NO_MORE_CHARGE)) {
            onNoMoreCharge();
        } else if (event.equals(MinesweeperEvents.WIN)) {
            onWin();
        } else if (event.equals(MinesweeperEvents.CLEAN_BOARD)) {
            onClearBoard();
        }
        score = Math.max(0, score);
    }

    private void onClearBoard() {
        score += clearBoardScore.getValue();
    }

    private void onWin() {
        score += winScore.getValue();
    }

    private void onNoMoreCharge() {
        onKillOnMine();
    }

    private void onDestroyMine() {
        destroyed++;
        score += destroyed;
    }

    private void onForgotCharge() {
        score -= destroyedPenalty.getValue();
        destroyed -= destroyedForgotPenalty.getValue();
        destroyed = Math.max(0, destroyed);
    }

    private void onKillOnMine() {
        score -= gameOverPenalty.getValue();
        destroyed = 0;
    }
}
