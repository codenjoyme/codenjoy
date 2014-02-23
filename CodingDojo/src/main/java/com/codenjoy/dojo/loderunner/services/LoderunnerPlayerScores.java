package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:44 PM
 */
public class LoderunnerPlayerScores implements PlayerScores {

    private final Parameter<Integer> killHeroPenalty;
    private final Parameter<Integer> killEnemyScore;
    private final Parameter<Integer> getGoldScore;

    private volatile int score;

    public LoderunnerPlayerScores(int startScore, Settings settings) {
        this.score = startScore;

        killHeroPenalty = settings.addEditBox("Kill hero penalty").type(Integer.class).def(30);
        killEnemyScore = settings.addEditBox("Kill enemy score").type(Integer.class).def(100);
        getGoldScore = settings.addEditBox("Get gold score").type(Integer.class).def(10);
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
        if (event.equals(LoderunnerEvents.GET_GOLD)) {
            score += getGoldScore.getValue();
        } else if (event.equals(LoderunnerEvents.KILL_ENEMY)) {
            score += killEnemyScore.getValue();
        } else if (event.equals(LoderunnerEvents.KILL_HERO)) {
            score -= killHeroPenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
