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
    public void event(String name) {
        if (name.equals(BattlecityEvents.KILL_YOUR_TANK.name())) {  // TODO сделать хорошо!
            score += KILL_YOUR_TANK;
            if (score < 0) {
                score = 0;
            }
        } else if (name.equals(BattlecityEvents.KILL_OTHER_TANK.name())) {
            score += KILL_OTHER_TANK;
        }
    }
}
