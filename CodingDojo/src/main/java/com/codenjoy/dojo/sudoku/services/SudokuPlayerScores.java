package com.codenjoy.dojo.sudoku.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class SudokuPlayerScores implements PlayerScores {

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public SudokuPlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);
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
        } else if (event.equals(SudokuEvents.LOOSE)) {
            score -= loosePenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
