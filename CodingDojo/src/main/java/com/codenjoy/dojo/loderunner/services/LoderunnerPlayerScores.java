package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:44 PM
 */
public class LoderunnerPlayerScores implements PlayerScores {    // TODO тест ми :)

    public static final int GET_GOLD = 10;
    public static final int KILL_ENEMY = 100;
    public static final int KILL_HERO = -30;

    private volatile int score;

    public LoderunnerPlayerScores(int startScore) {
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
        if (event.equals(LoderunnerEvents.GET_GOLD)) {  // TODO сделать хорошо!
            score += GET_GOLD;
        } else if (event.equals(LoderunnerEvents.KILL_ENEMY)) {
            score += KILL_ENEMY;
        } else if (event.equals(LoderunnerEvents.KILL_HERO)) {
            score += KILL_HERO;
        }

        if (score < 0) {
            score = 0;
        }
    }
}
