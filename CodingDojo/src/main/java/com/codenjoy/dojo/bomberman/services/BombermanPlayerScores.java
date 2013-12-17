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

    private final Parameter<Integer> killWall;
    private final Parameter<Integer> killMeatChopper;
    private final Parameter<Integer> killOtherBomberman;
    private final Parameter<Integer> killBomerman;

    private volatile int score;

    public BombermanPlayerScores(int startScore, Settings settings) {
        this.score = startScore;
        killWall = settings.addEditBox("Kill wall score").type(Integer.class).def(10);
        killMeatChopper = settings.addEditBox("Kill meat chopper score").type(Integer.class).def(100);
        killOtherBomberman = settings.addEditBox("Kill other bomberman score").type(Integer.class).def(1000);
        killBomerman = settings.addEditBox("Kill your bomberman score").type(Integer.class).def(-50);
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
        if (event.equals(BombermanEvents.KILL_BOMBERMAN)) {  // TODO сделать хорошо!
            score += killBomerman.getValue();
            if (score < 0) {
                score = 0;
            }
        } else if (event.equals(BombermanEvents.KILL_OTHER_BOMBERMAN)) {
            score += killOtherBomberman.getValue();
        } else if (event.equals(BombermanEvents.KILL_MEAT_CHOPPER)) {
            score += killMeatChopper.getValue();
        } else if (event.equals(BombermanEvents.KILL_DESTROY_WALL)) {
            score += killWall.getValue();
        }
    }
}
