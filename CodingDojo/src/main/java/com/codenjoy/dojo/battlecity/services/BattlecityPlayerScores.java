package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:44 PM
 */
public class BattlecityPlayerScores implements PlayerScores {

    private final Parameter<Integer> killYourTankPenalty;
    private final Parameter<Integer> killOtherTankScore;

    private volatile int score;
    private SettingsImpl parameters;

    public BattlecityPlayerScores(int startScore, SettingsImpl parameters) {
        this.parameters = parameters;
        this.score = startScore;

        killYourTankPenalty = parameters.addEditBox("Kill your tank penalty").type(Integer.class).def(50);
        killOtherTankScore = parameters.addEditBox("Kill other tank score").type(Integer.class).def(100);
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
        if (event.equals(BattlecityEvents.KILL_YOUR_TANK)) {  // TODO сделать хорошо!
            score -= killYourTankPenalty.getValue();
        } else if (event.equals(BattlecityEvents.KILL_OTHER_TANK)) {
            score += killOtherTankScore.getValue();
        }
        if (score < 0) {
            score = 0;
        }
    }
}
