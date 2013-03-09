package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.GameLevel;
import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:20 PM
 */
public class BombermanPlayerScores implements PlayerScores {

    public static final int KILL_WALL = 10;
    public static final int KILL_MEAT_CHOPPER = 100;

    private volatile int score;


    public BombermanPlayerScores(int startScore) {
        this.score = startScore;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(String name) {
        if (name.equals(BombermanEvents.KILL_MEAT_CHOPPER.name())) {  // TODO fixme
            score += KILL_MEAT_CHOPPER;
        } else if (name.equals(BombermanEvents.KILL_DESTROY_WALL.name())) {
            score += KILL_WALL;
        }
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
