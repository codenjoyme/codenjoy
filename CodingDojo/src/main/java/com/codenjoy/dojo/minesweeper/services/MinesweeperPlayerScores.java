package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;

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

    public MinesweeperPlayerScores(int startScore, SettingsImpl parameters) {
        this.score = startScore;
        destroyed = 0;
        this.score = startScore;
        gameOverPenalty = parameters.addEditBox("Game over penalty").type(Integer.class).def(15);
        destroyedPenalty = parameters.addEditBox("Forgot penalty").type(Integer.class).def(5);
        destroyedForgotPenalty = parameters.addEditBox("Destoyed forgot penalty").type(Integer.class).def(2);
        winScore = parameters.addEditBox("Win score").type(Integer.class).def(300);
        clearBoardScore = parameters.addEditBox("Clear board score").type(Integer.class).def(1);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int clear() { // TODO test me
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
        score = Math.max(0, score);
        destroyed = Math.max(0, destroyed);
    }

    private void onKillOnMine() {
        score -= gameOverPenalty.getValue();
        score = Math.max(0, score);
        destroyed = 0;
    }
}
