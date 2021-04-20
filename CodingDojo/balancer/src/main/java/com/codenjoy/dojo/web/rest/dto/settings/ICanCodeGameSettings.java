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

public class ICanCodeGameSettings extends AbstractSettings {

    public static final String WIN_SCORE = "Win score";
    public static final String GOLD_SCORE = "Gold score";
    public static final String KILL_ZOMBIE_SCORE = "Kill zombie score";
    public static final String KILL_HERO_SCORE = "Kill hero score";
    public static final String ENABLE_SCORE_FOR_KILL = "Enable score for kill";
    public static final String LOSE_PENALTY = "Lose penalty";
    public static final String IS_TRAINING_MODE = "Is training mode";
    public static final String ROOM_SIZE = "Room size";
    public static final String PERK_DROP_RATIO = "Perk drop ratio";
    public static final String PERK_AVAILABILITY = "Perk availability";
    public static final String PERK_ACTIVITY = "Perk activity";
    public static final String GUN_RECHARGE = "Heroes gun recharge";
    public static final String DEATH_RAY_PERK_RANGE = "Death-Ray perk range";
    public static final String GUN_SHOT_QUEUE = "Heroes gun need to relax after a series of shots";
    public static final String GUN_REST_TIME =  "Heroes gun rest time(ticks)";

    public ICanCodeGameSettings(PParameters parameters) {
        super(parameters);
    }

    public Integer getRoomSize() {
        return getInteger(ROOM_SIZE);
    }

    public Integer getLosePenalty() {
        return getInteger(LOSE_PENALTY);
    }

    public Integer getKillHeroScore() {
        return getInteger(KILL_HERO_SCORE);
    }

    public Integer getKillZombieScore() {
        return getInteger(KILL_ZOMBIE_SCORE);
    }

    public Integer getGoldScore() {
        return getInteger(GOLD_SCORE);
    }

    public Integer getWinScore() {
        return getInteger(WIN_SCORE);
    }

    public Boolean isTrainingMode() {
        return getBoolean(IS_TRAINING_MODE);
    }

    public Boolean isEnableKillScore() {
        return getBoolean(ENABLE_SCORE_FOR_KILL);
    }

    public Integer getPerkDropRatio() {
        return getInteger(PERK_DROP_RATIO);
    }

    public Integer getPerkAvailability() {
        return getInteger(PERK_AVAILABILITY);
    }

    public Integer getPerkActivity() {
        return getInteger(PERK_ACTIVITY);
    }

    public Integer getGunRecharge() {
        return getInteger(GUN_RECHARGE);
    }

    public Integer getDeathRayRange() {
        return getInteger(DEATH_RAY_PERK_RANGE);
    }

    public Integer getGunShotQueue() {
        return getInteger(GUN_SHOT_QUEUE);
    }

    public Integer getGunRestTime() {
        return getInteger(GUN_REST_TIME);
    }

    public void setRoomSize(Integer input) {
        add(ROOM_SIZE, input);
    }

    public void setLosePenalty(Integer input) {
        add(LOSE_PENALTY, input);
    }

    public void setKillHeroScore(Integer input) {
        add(KILL_HERO_SCORE, input);
    }

    public void setKillZombieScore(Integer input) {
        add(KILL_ZOMBIE_SCORE, input);
    }

    public void setGoldScore(Integer input) {
        add(GOLD_SCORE, input);
    }

    public void setWinScore(Integer input) {
        add(WIN_SCORE, input);
    }

    public void setTrainingMode(Boolean input) {
        add(IS_TRAINING_MODE, input);
    }

    public void setEnableKillScore(Boolean input) {
        add(ENABLE_SCORE_FOR_KILL, input);
    }

    public void setPerkDropRatio(Integer input) {
        add(PERK_DROP_RATIO, input);
    }

    public void setPerkAvailability(Integer input) {
        add(PERK_AVAILABILITY, input);
    }

    public void setPerkActivity(Integer input) {
        add(PERK_ACTIVITY, input);
    }

    public void setGunRecharge(Integer input) {
        add(GUN_RECHARGE, input);
    }

    public void setDeathRayRange(Integer input) {
        add(DEATH_RAY_PERK_RANGE, input);
    }

    public void setGunShotQueue(Integer input) {
        add(GUN_SHOT_QUEUE, input);
    }

    public void setGunRestTime(Integer input) {
        add(GUN_REST_TIME, input);
    }

    @Override
    public void update(List<PParameter> parameters) {
        update(parameters, WIN_SCORE);
        update(parameters, GOLD_SCORE);
        update(parameters, KILL_ZOMBIE_SCORE);
        update(parameters, KILL_HERO_SCORE);
        update(parameters, ENABLE_SCORE_FOR_KILL);
        update(parameters, LOSE_PENALTY);
        update(parameters, IS_TRAINING_MODE);
        update(parameters, ROOM_SIZE);
        update(parameters, PERK_DROP_RATIO);
        update(parameters, PERK_AVAILABILITY);
        update(parameters, PERK_ACTIVITY);
        update(parameters, GUN_RECHARGE);
        update(parameters, DEATH_RAY_PERK_RANGE);
        update(parameters, GUN_SHOT_QUEUE);
        update(parameters, GUN_REST_TIME);
    }
}
