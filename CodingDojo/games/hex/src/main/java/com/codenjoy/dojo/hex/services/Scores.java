package com.codenjoy.dojo.hex.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(10);
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
        Event hexEvent = (Event) event;

        if (hexEvent.getType() == Event.EventEnum.WIN) {
            score += winScore.getValue()*hexEvent.getCount();
        } else if (hexEvent.getType() == Event.EventEnum.LOOSE) {
            score -= loosePenalty.getValue()*hexEvent.getCount();
        }
        score = Math.max(0, score);
    }
}
