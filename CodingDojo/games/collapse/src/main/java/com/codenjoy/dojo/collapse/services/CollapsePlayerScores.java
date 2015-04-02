package com.codenjoy.dojo.collapse.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class CollapsePlayerScores implements PlayerScores {

    private final Parameter<Integer> successScore;

    private volatile int score;

    public CollapsePlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        successScore = settings.addEditBox("Success score").type(Integer.class).def(1);
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
            int count = ((CollapseEvents) event).getCount();
            int inc = 0;
            for (int i = 1; i <= count; i++) {
                inc += i*successScore.getValue();
            }
            score += inc;
        }
        score = Math.max(0, score);
    }
}
