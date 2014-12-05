package com.codenjoy.dojo.collapse.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class CollapsePlayerScores implements PlayerScores {

    private final Parameter<Integer> successScore;
    private final Parameter<Integer> newGamePenalty;

    private volatile int score;

    public CollapsePlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        successScore = settings.addEditBox("Success score").type(Integer.class).def(10);
        newGamePenalty = settings.addEditBox("New game penalty").type(Integer.class).def(500);
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
        if (event.equals(CollapseEvents.SUCCESS)) {
            score += successScore.getValue();
        } else if (event.equals(CollapseEvents.NEW_GAME)) {
            score -= newGamePenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
