package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:44 PM
 */
public class BattlecityPlayerScores implements PlayerScores {    // TODO тест ми :)

    public static final int KILL_YOUR_TANK = -50;
    public static final int KILL_OTHER_TANK = 100;

    private volatile int score;

    public BattlecityPlayerScores(int startScore) {
        this.score = startScore;
    }

    @Override
    public int clear() { // TODO test me
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event == BattlecityEvents.KILL_YOUR_TANK) {  // TODO сделать хорошо!
            score += KILL_YOUR_TANK;
            if (score < 0) {
                score = 0;
            }
        } else if (event.equals(BattlecityEvents.KILL_OTHER_TANK)) {
            score += KILL_OTHER_TANK;
        }
    }
}
