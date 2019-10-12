package com.codenjoy.dojo.loderunner.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> killHeroPenalty;
    private final Parameter<Integer> killEnemyScore;
    private final Parameter<Integer> forNextGoldIncScore;
    private final Parameter<Integer> yellowTypeGoldCount;
    private final Parameter<Integer> greenTypeGoldCount;
    private final Parameter<Integer> redTypeGoldCount;
    private final Parameter<Integer> yellowTypeGoldWeight;
    private final Parameter<Integer> greenTypeGoldWeight;
    private final Parameter<Integer> redTypeGoldWeight;
    private final Parameter<Integer> shadowPillActiveTicks;
    private final Parameter<Integer> shadowPillsCount;
    private final Parameter<Integer> suicidePenalty;
    private final Parameter<Integer> portalsCount;
    private final Parameter<Integer> portalsActiveTicks;
    private final Parameter<Integer> numberOfEnemies;
    private final Parameter<String> customMapPath;

    private volatile int score;
    private volatile int count;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        killHeroPenalty = settings.addEditBox("Kill hero penalty").type(Integer.class).def(0);
        killEnemyScore = settings.addEditBox("Kill enemy score").type(Integer.class).def(10);
        forNextGoldIncScore = settings.addEditBox("Get next gold increment score").type(Integer.class).def(1);
        shadowPillActiveTicks = settings.addEditBox("Number of ticks that the shadow pill will be active").type(Integer.class).def(15);
        shadowPillsCount = settings.addEditBox("The shadow pills count").type(Integer.class).def(0);
        suicidePenalty = settings.addEditBox("SuicidePenalty").type(Integer.class).def(0);
        portalsCount = settings.addEditBox("The portals count").type(Integer.class).def(0);
        portalsActiveTicks = settings.addEditBox("Number of ticks that the portals will be active").type(Integer.class).def(10);

        yellowTypeGoldCount = settings.addEditBox("yellow type gold count").type(Integer.class).def(20);
        greenTypeGoldCount = settings.addEditBox("green type gold count").type(Integer.class).def(20);
        redTypeGoldCount = settings.addEditBox("red type gold count").type(Integer.class).def(20);

        yellowTypeGoldWeight = settings.addEditBox("yellow type gold weight").type(Integer.class).def(1);
        greenTypeGoldWeight = settings.addEditBox("green type gold weight").type(Integer.class).def(5);
        redTypeGoldWeight = settings.addEditBox("red type gold weight").type(Integer.class).def(10);

        numberOfEnemies = settings.addEditBox("Number of enemies").type(Integer.class).def(5);

        customMapPath = settings.addEditBox("Custom map path").type(String.class).def("");
    }

    @Override
    public int clear() {
        count = 0;
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.GET_YELLOW_GOLD)) {
            score += yellowTypeGoldWeight.getValue() + count;
            count += forNextGoldIncScore.getValue();
        } else if (event.equals(Events.GET_GREEN_GOLD)) {
            score += greenTypeGoldWeight.getValue() + count;
            count += forNextGoldIncScore.getValue();
        } else if (event.equals(Events.GET_RED_GOLD)) {
            score += redTypeGoldWeight.getValue() + count;
            count += forNextGoldIncScore.getValue();
        }
        if (event.equals(Events.KILL_ENEMY)) {
            score += killEnemyScore.getValue();
        } else if (event.equals(Events.KILL_HERO)) {
            count = 0;
            score -= killHeroPenalty.getValue();
        } else if (event.equals(Events.SUICIDE)) {
            score -= suicidePenalty.getValue();
        }
        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
