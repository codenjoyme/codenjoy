package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.GameLevel;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:20 PM
 */
public class BombermanPlayerScores implements PlayerScores {    // TODO тест ми :)

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
    public void event(String name) {
        if (name.equals(BombermanEvents.KILL_BOMBERMAN.name())) {  // TODO сделать хорошо!
            score += killBomerman.getValue();
            if (score < 0) {
                score = 0;
            }
        } else if (name.equals(BombermanEvents.KILL_OTHER_BOMBERMAN.name())) {
            score += killOtherBomberman.getValue();
        } else if (name.equals(BombermanEvents.KILL_MEAT_CHOPPER.name())) {
            score += killMeatChopper.getValue();
        } else if (name.equals(BombermanEvents.KILL_DESTROY_WALL.name())) {
            score += killWall.getValue();
        }
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
