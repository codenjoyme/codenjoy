package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:44 PM
 */
public class Scores implements PlayerScores {

    private final Parameter<Integer> killHeroPenalty;
    private final Parameter<Integer> killEnemyScore;
    private final Parameter<Integer> getGoldScore;
    private final Parameter<Integer> forNextGoldIncScore;

    private volatile int score;
    private volatile int count;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        killHeroPenalty = settings.addEditBox("Kill hero penalty").type(Integer.class).def(0);
        killEnemyScore = settings.addEditBox("Kill enemy score").type(Integer.class).def(100);
        getGoldScore = settings.addEditBox("Get gold score").type(Integer.class).def(1);
        forNextGoldIncScore = settings.addEditBox("Get next gold increment score").type(Integer.class).def(1);
    }

    @Override
    public int clear() {
        count = 0;
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.GET_GOLD)) {
            score += getGoldScore.getValue() + count;
            count += forNextGoldIncScore.getValue();
        } else if (event.equals(Events.KILL_ENEMY)) {
            score += killEnemyScore.getValue();
        } else if (event.equals(Events.KILL_HERO)) {
            count = 0;
            score -= killHeroPenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
