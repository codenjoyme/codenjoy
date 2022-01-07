package com.codenjoy.dojo.web.rest.dto.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

    // TODO тут настройки могут не соответствовать реальным, у codenjoy есть апи на этот счет, можно отсюда это все убирать
    public static final String AI_TICKS_PER_SHOOT = "[Game] Ticks until the next AI shoot";
    public static final String HERO_TICKS_PER_SHOOT = "[Game] Ticks until the next hero shoot";
    public static final String OIL_SLIPPERINESS = "[Game] The amount of leaked oil. The more - the more naughty the submarine.";
    public static final String PENALTY_WALKING_ON_FISHNET = "[Game] Penalty time when walking on fishnet";
    public static final String SHOW_MY_HERO_UNDER_SEAWEED = "[Game] Show my tank under seaweed";
    public static final String ICEBERG_REGENERATE_TIME = "[Game] Iceberg regenerate time";
    public static final String TICKS_STUCK_BY_FISHNET = "[Game] Ticks AI gets stuck by fishnet";
    public static final String COUNT_AIS = "[Game] Count AIs on the board";
    public static final String TURN_MODE = "[Game] Turn mode";

    public static final String SPAWN_AI_PRIZE = "[Prize] Count spawn for AI with prize";
    public static final String KILL_HITS_AI_PRIZE = "[Prize] Hits to kill AI with prize";
    public static final String PRIZE_ON_FIELD = "[Prize] The period of prize validity on the field after the appearance";
    public static final String PRIZE_WORKING = "[Prize] Working time of the prize after catch up";
    public static final String AI_PRIZE_LIMIT = "[Prize] The total number of prize AI and prizes on the board";
    public static final String PRIZE_SPRITE_CHANGE_TICKS = "[Prize] Prize sprite changes every ticks";

    public static final String CHANCE_IMMORTALITY = "[Chance] Prize immortality";
    public static final String CHANCE_BREAKING_BAD = "[Chance] Prize breaking bad";
    public static final String CHANCE_WALKING_ON_FISHNET = "[Chance] Prize walking on fishnet";
    public static final String CHANCE_VISIBILITY = "[Chance] Prize visibility";
    public static final String CHANCE_NO_SLIDING = "[Chance] Prize no sliding";

    public static final String HERO_DIED_PENALTY = "[Score] Kill your hero penalty";
    public static final String KILL_OTHER_HERO_SCORE = "[Score] Kill other hero score";
    public static final String KILL_AI_SCORE = "[Score] Kill other AI score";

    public Integer getAiTicksPerShoot() {
        return getInteger(AI_TICKS_PER_SHOOT);
    }

    public Integer getHeroTicksPerShoot() {
        return getInteger(HERO_TICKS_PER_SHOOT);
    }

    public Integer getOilSlipperiness() {
        return getInteger(OIL_SLIPPERINESS);
    }

    public Integer getPenaltyWalkingOnFishnet() {
        return getInteger(PENALTY_WALKING_ON_FISHNET);
    }

    public Boolean isShowMyHeroUnderSeaweed() {
        return getBoolean(SHOW_MY_HERO_UNDER_SEAWEED);
    }

    public Integer getIcebergRegenerateTime() {
        return getInteger(ICEBERG_REGENERATE_TIME);
    }

    public Integer getTicksStuckByFishnet() {
        return getInteger(TICKS_STUCK_BY_FISHNET);
    }

    public Integer getCountAis() {
        return getInteger(COUNT_AIS);
    }

    public Integer getTurnMode() {
        return getInteger(TURN_MODE);
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

    public Integer getAiPrizeLimit() {
        return getInteger(AI_PRIZE_LIMIT);
    }

    public Integer getPrizeSpriteChangeTicks() {
        return getInteger(PRIZE_SPRITE_CHANGE_TICKS);
    }

    public Integer getChanceImmortality() {
        return getInteger(CHANCE_IMMORTALITY);
    }

    public Integer getChanceBreakingBad() {
        return getInteger(CHANCE_BREAKING_BAD);
    }

    public Integer getChanceWalkingOnFishnet() {
        return getInteger(CHANCE_WALKING_ON_FISHNET);
    }

    public Integer getChanceVisibility() {
        return getInteger(CHANCE_VISIBILITY);
    }

    public Integer getChanceNoSliding() {
        return getInteger(CHANCE_NO_SLIDING);
    }

    public Integer getHeroDiedPenalty() {
        return getInteger(HERO_DIED_PENALTY);
    }

    public Integer getKillOtherHeroScore() {
        return getInteger(KILL_OTHER_HERO_SCORE);
    }

    public Integer getKillAiScore() {
        return getInteger(KILL_AI_SCORE);
    }
    
    // ---------

    public void setAiTicksPerShoot(Integer input) {
        add(AI_TICKS_PER_SHOOT, input);
    }

    public void setHeroTicksPerShoot(Integer input) {
        add(HERO_TICKS_PER_SHOOT, input);
    }

    public void setOilSlipperiness(Integer input) {
        add(OIL_SLIPPERINESS, input);
    }

    public void setPenaltyWalkingOnFishnet(Integer input) {
        add(PENALTY_WALKING_ON_FISHNET, input);
    }

    public void setShowMyHeroUnderSeaweed(Boolean input) {
        add(SHOW_MY_HERO_UNDER_SEAWEED, input);
    }

    public void setIcebergRegenerateTime(Integer input) {
        add(ICEBERG_REGENERATE_TIME, input);
    }

    public void setTicksStuckByFishnet(Integer input) {
        add(TICKS_STUCK_BY_FISHNET, input);
    }
    
    public void setCountAis(Integer input) {
        add(COUNT_AIS, input);
    }

    public void setTurnMode(Integer input) {
        add(TURN_MODE, input);
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

    public void setAiPrizeLimit(Integer input) {
        add(AI_PRIZE_LIMIT, input);
    }

    public void setPrizeSpriteChangeTicks(Integer input) {
        add(PRIZE_SPRITE_CHANGE_TICKS, input);
    }

    public void setChanceImmortality(Integer input) {
        add(CHANCE_IMMORTALITY, input);
    }

    public void setChanceBreakingBad(Integer input) {
        add(CHANCE_BREAKING_BAD, input);
    }

    public void setChanceWalkingOnFishnet(Integer input) {
        add(CHANCE_WALKING_ON_FISHNET, input);
    }

    public void setChanceVisibility(Integer input) {
        add(CHANCE_VISIBILITY, input);
    }

    public void setChanceNoSliding(Integer input) {
        add(CHANCE_NO_SLIDING, input);
    }

    public void setHeroDiedPenalty(Integer input) {
        add(HERO_DIED_PENALTY, input);
    }

    public void setKillOtherHeroScore(Integer input) {
        add(KILL_OTHER_HERO_SCORE, input);
    }

    public void setKillAiScore(Integer input) {
        add(KILL_AI_SCORE, input);
    }

    public RawelbbubGameSettings(PParameters parameters) {
        super(parameters);
    }

    @Override
    public void update(List<PParameter> parameters) {
        update(parameters, AI_TICKS_PER_SHOOT);
        update(parameters, HERO_TICKS_PER_SHOOT);
        update(parameters, OIL_SLIPPERINESS);
        update(parameters, PENALTY_WALKING_ON_FISHNET);
        update(parameters, SHOW_MY_HERO_UNDER_SEAWEED);
        update(parameters, ICEBERG_REGENERATE_TIME);
        update(parameters, TICKS_STUCK_BY_FISHNET);
        update(parameters, COUNT_AIS);
        update(parameters, SPAWN_AI_PRIZE);
        update(parameters, KILL_HITS_AI_PRIZE);
        update(parameters, PRIZE_ON_FIELD);
        update(parameters, PRIZE_WORKING);
        update(parameters, AI_PRIZE_LIMIT);
        update(parameters, PRIZE_SPRITE_CHANGE_TICKS);
        update(parameters, CHANCE_IMMORTALITY);
        update(parameters, CHANCE_BREAKING_BAD);
        update(parameters, CHANCE_WALKING_ON_FISHNET);
        update(parameters, CHANCE_VISIBILITY);
        update(parameters, CHANCE_NO_SLIDING);
        update(parameters, HERO_DIED_PENALTY);
        update(parameters, KILL_OTHER_HERO_SCORE);
        update(parameters, KILL_AI_SCORE);
    }
}
