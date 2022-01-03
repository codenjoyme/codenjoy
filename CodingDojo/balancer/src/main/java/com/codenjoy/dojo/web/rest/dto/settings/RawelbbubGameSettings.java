package com.codenjoy.dojo.web.rest.dto.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.entity.server.PParameter;
import com.codenjoy.dojo.services.entity.server.PParameters;

import java.util.List;

public class RawelbbubGameSettings extends AbstractSettings {

    public static final String AI_TICKS_PER_SHOOT = "[Game] Ticks until the next AI Tank shoot";
    public static final String TANK_TICKS_PER_SHOOT = "[Game] Ticks until the next Tank shoot";
    public static final String SLIPPERINESS = "[Game] Value of tank sliding on ice";
    public static final String PENALTY_WALKING_ON_FISHNET = "[Game] Penalty time when walking on water";
    public static final String SHOW_MY_TANK_UNDER_TREE = "[Game] Show my tank under tree";

    public static final String SPAWN_AI_PRIZE = "[Prize] Count spawn for AI Tank with prize";
    public static final String KILL_HITS_AI_PRIZE = "[Prize] Hits to kill AI Tank with prize";
    public static final String PRIZE_ON_FIELD = "[Prize] The period of prize validity on the field after the appearance";
    public static final String PRIZE_WORKING = "[Prize] Working time of the prize after catch up";
    public static final String AI_PRIZE_LIMIT = "[Prize] The total number of prize tanks and prizes on the board";

    public static final String KILL_YOUR_TANK_PENALTY = "[Score] Kill your tank penalty";
    public static final String KILL_OTHER_HERO_TANK_SCORE = "[Score] Kill other hero tank score";
    public static final String KILL_OTHER_AI_TANK_SCORE = "[Score] Kill other AI tank score";

    public Boolean isShowMyTankUnderTree() {
        return getBoolean(SHOW_MY_TANK_UNDER_TREE);
    }

    public Integer getKillYourTankPenalty() {
        return getInteger(KILL_YOUR_TANK_PENALTY);
    }

    public Integer getKillOtherHeroTankScore() {
        return getInteger(KILL_OTHER_HERO_TANK_SCORE);
    }

    public Integer getKillOtherAiTankScore() {
        return getInteger(KILL_OTHER_AI_TANK_SCORE);
    }

    public Integer getSpawnAiPrize() {
        return getInteger(SPAWN_AI_PRIZE);
    }

    public Integer getKillHitsAiPrize() {
        return getInteger(KILL_HITS_AI_PRIZE);
    }

    public Integer getPrizeOnField() {
        return getInteger(PRIZE_ON_FIELD);
    }

    public Integer getPrizeWorking() {
        return getInteger(PRIZE_WORKING);
    }

    public Integer getAiTicksPerShoot() {
        return getInteger(AI_TICKS_PER_SHOOT);
    }

    public Integer getTankTicksPerShoot() {
        return getInteger(TANK_TICKS_PER_SHOOT);
    }

    public Integer getSlipperiness() {
        return getInteger(SLIPPERINESS);
    }

    public Integer getAiPrizeLimit() {
        return getInteger(AI_PRIZE_LIMIT);
    }

    public Integer getPenaltyWalkingOnWater() {
        return getInteger(PENALTY_WALKING_ON_FISHNET);
    }

    public void setShowMyTankUnderTree(Boolean input) {
        add(SHOW_MY_TANK_UNDER_TREE, input);
    }

    public void setKillYourTankPenalty(Integer input) {
        add(KILL_YOUR_TANK_PENALTY, input);
    }

    public void setKillOtherHeroTankScore(Integer input) {
        add(KILL_OTHER_HERO_TANK_SCORE, input);
    }

    public void setKillOtherAiTankScore(Integer input) {
        add(KILL_OTHER_AI_TANK_SCORE, input);
    }

    public void setSpawnAiPrize(Integer input) {
        add(SPAWN_AI_PRIZE, input);
    }

    public void setKillHitsAiPrize(Integer input) {
        add(KILL_HITS_AI_PRIZE, input);
    }

    public void setPrizeOnField(Integer input) {
        add(PRIZE_ON_FIELD, input);
    }

    public void setPrizeWorking(Integer input) {
        add(PRIZE_WORKING, input);
    }

    public void setAiTicksPerShoot(Integer input) {
        add(AI_TICKS_PER_SHOOT, input);
    }

    public void setTankTicksPerShoot(Integer input) {
        add(TANK_TICKS_PER_SHOOT, input);
    }

    public void setSlipperiness(Integer input) {
        add(SLIPPERINESS, input);
    }

    public void setAiPrizeLimit(Integer input) {
        add(AI_PRIZE_LIMIT, input);
    }

    public void setPenaltyWalkingOnWater(Integer input) {
        add(PENALTY_WALKING_ON_FISHNET, input);
    }


    public RawelbbubGameSettings(PParameters parameters) {
        super(parameters);
    }

    @Override
    public void update(List<PParameter> parameters) {
        update(parameters, KILL_YOUR_TANK_PENALTY);
        update(parameters, KILL_OTHER_HERO_TANK_SCORE);
        update(parameters, KILL_OTHER_AI_TANK_SCORE);
        update(parameters, SPAWN_AI_PRIZE);
        update(parameters, KILL_HITS_AI_PRIZE);
        update(parameters, PRIZE_ON_FIELD);
        update(parameters, PRIZE_WORKING);
        update(parameters, AI_TICKS_PER_SHOOT);
        update(parameters, TANK_TICKS_PER_SHOOT);
        update(parameters, SLIPPERINESS);
        update(parameters, AI_PRIZE_LIMIT);
        update(parameters, PENALTY_WALKING_ON_FISHNET);
        update(parameters, SHOW_MY_TANK_UNDER_TREE);
    }
}
