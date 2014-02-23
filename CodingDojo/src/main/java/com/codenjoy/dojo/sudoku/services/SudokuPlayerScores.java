package com.codenjoy.dojo.sudoku.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class SudokuPlayerScores implements PlayerScores {

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> failPenalty;
    private final Parameter<Integer> successScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public SudokuPlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(1000);
        failPenalty = settings.addEditBox("Fail penalty").type(Integer.class).def(10);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(500);
        successScore = settings.addEditBox("Success score").type(Integer.class).def(10);
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(SudokuEvents.WIN)) {
            score += winScore.getValue();
        } else if (event.equals(SudokuEvents.FAIL)) {
            score -= failPenalty.getValue();
        } else if (event.equals(SudokuEvents.SUCCESS)) {
            score += successScore.getValue();
        } else if (event.equals(SudokuEvents.LOOSE)) {
            score -= loosePenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
