package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:20 PM
 */
public class BombermanPlayerScores implements PlayerScores {

    private final Parameter<Integer> killWallScore;
    private final Parameter<Integer> killMeatChopperScore;
    private final Parameter<Integer> killOtherBombermanScore;
    private final Parameter<Integer> killBomermanPenalty;

    private volatile int score;

    public BombermanPlayerScores(int startScore, Settings settings) {
        this.score = startScore;
        killWallScore = settings.addEditBox("Kill wall score").type(Integer.class).def(10);
        killMeatChopperScore = settings.addEditBox("Kill meat chopper score").type(Integer.class).def(100);
        killOtherBombermanScore = settings.addEditBox("Kill other bomberman score").type(Integer.class).def(1000);
        killBomermanPenalty = settings.addEditBox("Kill your bomberman penalty").type(Integer.class).def(50);
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
        if (event.equals(BombermanEvents.KILL_BOMBERMAN)) {
            score -= killBomermanPenalty.getValue();
        } else if (event.equals(BombermanEvents.KILL_OTHER_BOMBERMAN)) {
            score += killOtherBombermanScore.getValue();
        } else if (event.equals(BombermanEvents.KILL_MEAT_CHOPPER)) {
            score += killMeatChopperScore.getValue();
        } else if (event.equals(BombermanEvents.KILL_DESTROY_WALL)) {
            score += killWallScore.getValue();
        }
        score = Math.max(0, score);
    }
}
