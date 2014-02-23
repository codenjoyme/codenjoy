package com.codenjoy.dojo.rubicscube.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class RubicsCubePlayerScores implements PlayerScores {

    private final Parameter<Integer> failPenalty;
    private final Parameter<Integer> successScore;

    private volatile int score;

    public RubicsCubePlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        failPenalty = settings.addEditBox("Fail penalty").type(Integer.class).def(500);
        successScore = settings.addEditBox("Success score").type(Integer.class).def(1000);
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
        if (event.equals(RubicsCubeEvents.FAIL)) {
            score -= failPenalty.getValue();
        } else if (event.equals(RubicsCubeEvents.SUCCESS)) {
            score += successScore.getValue();
        }
        score = Math.max(0, score);
    }
}
