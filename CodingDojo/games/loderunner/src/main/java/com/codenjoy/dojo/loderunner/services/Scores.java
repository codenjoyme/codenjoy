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

    public static final String HERO_KILL_PENALTY = "Kill hero penalty";
    public static final String ENEMY_KILL_SCORE = "Kill enemy score";

    public static final String SUICIDE_PENALTY = "SuicidePenalty";

    public static final String SHADOW_TICKS = "Shadow ticks";
    public static final String SHADOW_PILLS = "Shadow pills";

    public static final String PORTALS = "Portals";
    public static final String PORTAL_TICKS = "Portal ticks";

    public static final String GOLD_SERIES_INCREMENT_SCORE = "Get next gold increment score";

    public static final String GOLD_COUNT_YELLOW = "Yellow gold count";
    public static final String GOLD_COUNT_GREEN = "Green gold count";
    public static final String GOLD_COUNT_RED = "Red gold count";

    public static final String GOLD_SCORE_YELLOW = "Yellow gold score";
    public static final String GOLD_SCORE_GREEN = "Green gold score";
    public static final String GOLD_SCORE_RED = "Red gold score";

    public static final String ENEMIES_COUNT = "Enemies count";

    public static final String MAP_PATH = "Custom map path";

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

        killHeroPenalty = settings.addEditBox(HERO_KILL_PENALTY).type(Integer.class).def(1);
        killEnemyScore = settings.addEditBox(ENEMY_KILL_SCORE).type(Integer.class).def(10);
        forNextGoldIncScore = settings.addEditBox(GOLD_SERIES_INCREMENT_SCORE).type(Integer.class).def(1);
        shadowPillsCount = settings.addEditBox(SHADOW_PILLS).type(Integer.class).def(0);
        shadowPillActiveTicks = settings.addEditBox(SHADOW_TICKS).type(Integer.class).def(15);
        suicidePenalty = settings.addEditBox(SUICIDE_PENALTY).type(Integer.class).def(10);
        portalsCount = settings.addEditBox(PORTALS).type(Integer.class).def(0);
        portalsActiveTicks = settings.addEditBox(PORTAL_TICKS).type(Integer.class).def(10);

        greenTypeGoldCount = settings.addEditBox(GOLD_COUNT_GREEN).type(Integer.class).def(0); // 25
        yellowTypeGoldCount = settings.addEditBox(GOLD_COUNT_YELLOW).type(Integer.class).def(20);
        redTypeGoldCount = settings.addEditBox(GOLD_COUNT_RED).type(Integer.class).def(0); // 15

        greenTypeGoldWeight = settings.addEditBox(GOLD_SCORE_GREEN).type(Integer.class).def(1);
        yellowTypeGoldWeight = settings.addEditBox(GOLD_SCORE_YELLOW).type(Integer.class).def(2);
        redTypeGoldWeight = settings.addEditBox(GOLD_SCORE_RED).type(Integer.class).def(5);

        numberOfEnemies = settings.addEditBox(ENEMIES_COUNT).type(Integer.class).def(5);

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
