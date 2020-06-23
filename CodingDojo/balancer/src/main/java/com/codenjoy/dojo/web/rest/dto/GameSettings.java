package com.codenjoy.dojo.web.rest.dto;

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
import com.codenjoy.dojo.services.entity.server.SimpleNamedParameter;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.LinkedList;
import java.util.List;

public class GameSettings {

    public static final String GAME_IS_MULTIPLE_OR_DISPOSABLE = "[Game] Is multiple or disposable";
    public static final String GAME_PLAYERS_PER_ROOM_FOR_DISPOSABLE = "[Game] Players per room for disposable";
    public static final String SCORE_KILL_WALL_SCORE = "[Score] Kill wall score";
    public static final String SCORE_KILL_MEAT_CHOPPER_SCORE = "[Score] Kill meat chopper score";
    public static final String SCORE_KILL_OTHER_HERO_SCORE = "[Score] Kill other hero score";
    public static final String SCORE_CATCH_PERK_SCORE = "[Score] Catch perk score";
    public static final String SCORE_YOUR_HEROES_DEATH_PENALTY = "[Score] Your hero's death penalty";
    public static final String SCORE_ROUNDS_WIN_ROUND_SCORE = "[Score][Rounds] Win round score";
    public static final String LEVEL_BOMBS_COUNT = "[Level] Bombs count";
    public static final String LEVEL_BIG_BADABOOM = "[Level] Blast activate bomb";
    public static final String LEVEL_BOMB_POWER = "[Level] Bomb power";
    public static final String LEVEL_BOARD_SIZE = "[Level] Board size";
    public static final String LEVEL_DESTROY_WALL_COUNT = "[Level] Destroy wall count";
    public static final String LEVEL_MEAT_CHOPPERS_COUNT = "[Level] Meat choppers count";
    public static final String ROUNDS_TIME_PER_ROUND = "[Rounds] Time per Round";
    public static final String ROUNDS_TIME_FOR_WINNER = "[Rounds] Time for Winner";
    public static final String ROUNDS_TIME_BEFORE_START_ROUND = "[Rounds] Time before start Round";
    public static final String ROUNDS_ROUNDS_PER_MATCH = "[Rounds] Rounds per Match";
    public static final String ROUNDS_MIN_TICKS_FOR_WIN = "[Rounds] Min ticks for win";
    public static final String PERKS_PERKS_DROP_RATIO_IN = "[Perks] Perks drop ratio in %";
    public static final String PERKS_PERKS_PICK_TIMEOUT = "[Perks] Perks pick timeout";
    public static final String PERKS_BOMB_BLAST_RADIUS_INCREASE = "[Perks] Bomb blast radius increase";
    public static final String PERKS_BOMB_COUNT_INCREASE = "[Perks] Bomb count increase";
    public static final String PERKS_BOMB_COUNT_EFFECT_TIMEOUT = "[Perks] Bomb count effect timeout";
    public static final String PERKS_BOMB_IMMUNE_EFFECT_TIMEOUT = "[Perks] Bomb immune effect timeout";
    public static final String PERKS_NUMBER_OF_BOMB_REMOTE_CONTROLS = "[Perks] Number of Bomb remote controls (how many times player can use it)";
    public static final String PERKS_BOMB_BLAST_RADIUS_INCREASE_EFFECT_TIMEOUT = "[Perks] Bomb blast radius increase effect timeout";
    public static final String GAME_ROUNDS_ENABLED = "[Game][Rounds] Enabled";
    public static final String SEMIFINAL_TIMEOUT = "Semifinal timeout";
    public static final String SEMIFINAL_PERCENTAGE = "Semifinal percentage";
    public static final String SEMIFINAL_LIMIT = "Semifinal limit";
    public static final String SEMIFINAL_ENABLED = "Semifinal enabled";
    public static final String SEMIFINAL_RESET_BOARD = "Semifinal reset board";
    public static final String SEMIFINAL_SHUFFLE_BOARD = "Semifinal shuffle board";

    private final List<PParameter> parameters;

    public GameSettings() {
        parameters = new LinkedList<>();
    }

    public GameSettings(PParameters parameters) {
        this.parameters = parameters.getParameters();
    }

    public Boolean isMultiple() {
        return getBoolean(GAME_IS_MULTIPLE_OR_DISPOSABLE);
    }

    public Boolean isBigBadaboom() {
        return getBoolean(LEVEL_BIG_BADABOOM);
    }

    public Integer getPlayersPerRoom() {
        return getInteger(GAME_PLAYERS_PER_ROOM_FOR_DISPOSABLE);
    }

    public Integer getKillWallScore() {
        return getInteger(SCORE_KILL_WALL_SCORE);
    }

    public Integer getKillMeatChopperScore() {
        return getInteger(SCORE_KILL_MEAT_CHOPPER_SCORE);
    }

    public Integer getKillOtherHeroScore() {
        return getInteger(SCORE_KILL_OTHER_HERO_SCORE);
    }

    public Integer getCatchPerkScore() {
        return getInteger(SCORE_CATCH_PERK_SCORE);
    }

    public Integer getYourHeroesDeathPenalty() {
        return getInteger(SCORE_YOUR_HEROES_DEATH_PENALTY);
    }

    public Integer getWinRoundScore() {
        return getInteger(SCORE_ROUNDS_WIN_ROUND_SCORE);
    }

    public Integer getBombsCount() {
        return getInteger(LEVEL_BOMBS_COUNT);
    }

    public Integer getBombPower() {
        return getInteger(LEVEL_BOMB_POWER);
    }

    public Integer getBoardSize() {
        return getInteger(LEVEL_BOARD_SIZE);
    }

    public Integer getDestroyWallCount() {
        return getInteger(LEVEL_DESTROY_WALL_COUNT);
    }

    public Integer getMeetChoppersCount() {
        return getInteger(LEVEL_MEAT_CHOPPERS_COUNT);
    }

    public Integer getTimePerRound() {
        return getInteger(ROUNDS_TIME_PER_ROUND);
    }

    public Integer getTimeForWinner() {
        return getInteger(ROUNDS_TIME_FOR_WINNER);
    }

    public Integer getTimeBeforeStartRound() {
        return getInteger(ROUNDS_TIME_BEFORE_START_ROUND);
    }

    public Integer getRoundsPerMatch() {
        return getInteger(ROUNDS_ROUNDS_PER_MATCH);
    }

    public Integer getMinTicksForWin() {
        return getInteger(ROUNDS_MIN_TICKS_FOR_WIN);
    }

    public Integer getPerksDropRatio() {
        return getInteger(PERKS_PERKS_DROP_RATIO_IN);
    }

    public Integer getPerksPickTimeout() {
        return getInteger(PERKS_PERKS_PICK_TIMEOUT);
    }

    public Integer getPerksBombBlastRadiusIncrease() {
        return getInteger(PERKS_BOMB_BLAST_RADIUS_INCREASE);
    }

    public Integer getPerksBombCountIncrease() {
        return getInteger(PERKS_BOMB_COUNT_INCREASE);
    }

    public Integer getPerksBombCountEffectTimeout() {
        return getInteger(PERKS_BOMB_COUNT_EFFECT_TIMEOUT);
    }

    public Integer getPerksBombImmuneEffectTimeout() {
        return getInteger(PERKS_BOMB_IMMUNE_EFFECT_TIMEOUT);
    }

    public Integer getPerksNumberOfBombRemoteControl() {
        return getInteger(PERKS_NUMBER_OF_BOMB_REMOTE_CONTROLS);
    }

    public Integer getPerksBombBlastRadiusIncreaseEffectTimeout() {
        return getInteger(PERKS_BOMB_BLAST_RADIUS_INCREASE_EFFECT_TIMEOUT);
    }

    public Boolean isRoundsEnabled() {
        return getBoolean(GAME_ROUNDS_ENABLED);
    }

    public Integer getSemifinalTimeout() {
        return getInteger(SEMIFINAL_TIMEOUT);
    }

    public Boolean getSemifinalPercentage() {
        return getBoolean(SEMIFINAL_PERCENTAGE);
    }

    public Integer getSemifinalLimit() {
        return getInteger(SEMIFINAL_LIMIT);
    }

    public Boolean isSemifinalEnabled() {
        return getBoolean(SEMIFINAL_ENABLED);
    }

    public Boolean isSemifinalResetBoard() {
        return getBoolean(SEMIFINAL_RESET_BOARD);
    }

    public Boolean isSemifinalShuffleBoard() {
        return getBoolean(SEMIFINAL_SHUFFLE_BOARD);
    }
    
    public void setMultiple(Boolean input) {
        add(GAME_IS_MULTIPLE_OR_DISPOSABLE, input);
    }

    public void setBigBadaboom(Boolean input) {
        add(LEVEL_BIG_BADABOOM, input);
    }

    public void setPlayersPerRoom(Integer input) {
        add(GAME_PLAYERS_PER_ROOM_FOR_DISPOSABLE, input);
    }

    public void setKillWallScore(Integer input) {
        add(SCORE_KILL_WALL_SCORE, input);
    }

    public void setKillMeatChopperScore(Integer input) {
        add(SCORE_KILL_MEAT_CHOPPER_SCORE, input);
    }

    public void setKillOtherHeroScore(Integer input) {
        add(SCORE_KILL_OTHER_HERO_SCORE, input);
    }

    public void setCatchPerkScore(Integer input) {
        add(SCORE_CATCH_PERK_SCORE, input);
    }

    public void setYourHeroesDeathPenalty(Integer input) {
        add(SCORE_YOUR_HEROES_DEATH_PENALTY, input);
    }

    public void setWinRoundScore(Integer input) {
        add(SCORE_ROUNDS_WIN_ROUND_SCORE, input);
    }

    public void setBombsCount(Integer input) {
        add(LEVEL_BOMBS_COUNT, input);
    }

    public void setBombPower(Integer input) {
        add(LEVEL_BOMB_POWER, input);
    }

    public void setBoardSize(Integer input) {
        add(LEVEL_BOARD_SIZE, input);
    }

    public void setDestroyWallCount(Integer input) {
        add(LEVEL_DESTROY_WALL_COUNT, input);
    }

    public void setMeetChoppersCount(Integer input) {
        add(LEVEL_MEAT_CHOPPERS_COUNT, input);
    }

    public void setTimePerRound(Integer input) {
        add(ROUNDS_TIME_PER_ROUND, input);
    }

    public void setTimeForWinner(Integer input) {
        add(ROUNDS_TIME_FOR_WINNER, input);
    }

    public void setTimeBeforeStartRound(Integer input) {
        add(ROUNDS_TIME_BEFORE_START_ROUND, input);
    }

    public void setRoundsPerMatch(Integer input) {
        add(ROUNDS_ROUNDS_PER_MATCH, input);
    }

    public void setMinTicksForWin(Integer input) {
        add(ROUNDS_MIN_TICKS_FOR_WIN, input);
    }

    public void setPerksDropRatio(Integer input) {
        add(PERKS_PERKS_DROP_RATIO_IN, input);
    }

    public void setPerksPickTimeout(Integer input) {
        add(PERKS_PERKS_PICK_TIMEOUT, input);
    }

    public void setPerksBombBlastRadiusIncrease(Integer input) {
        add(PERKS_BOMB_BLAST_RADIUS_INCREASE, input);
    }

    public void setPerksBombCountIncrease(Integer input) {
        add(PERKS_BOMB_COUNT_INCREASE, input);
    }

    private void add(String name, Object value) {
        parameters.add(new PParameter(new SimpleNamedParameter(name, value)));
    }

    public void setPerksBombCountEffectTimeout(Integer input) {
        add(PERKS_BOMB_COUNT_EFFECT_TIMEOUT, input);;
    }

    public void setPerksBombImmuneEffectTimeout(Integer input) {
        add(PERKS_BOMB_IMMUNE_EFFECT_TIMEOUT, input);
    }

    public void setPerksNumberOfBombRemoteControl(Integer input) {
        add(PERKS_NUMBER_OF_BOMB_REMOTE_CONTROLS, input);
    }

    public void setPerksBombBlastRadiusIncreaseEffectTimeout(Integer input) {
        add(PERKS_BOMB_BLAST_RADIUS_INCREASE_EFFECT_TIMEOUT, input);
    }

    public void setRoundsEnabled(Boolean input) {
        add(GAME_ROUNDS_ENABLED, input);
    }

    public void setSemifinalTimeout(Integer input) {
        add(SEMIFINAL_TIMEOUT, input);
    }

    public void getSemifinalPercentage(Boolean input) {
        add(SEMIFINAL_PERCENTAGE, input);
    }

    public void setSemifinalLimit(Integer input) {
        add(SEMIFINAL_LIMIT, input);
    }

    public void setSemifinalEnabled(Boolean input) {
        add(SEMIFINAL_ENABLED, input);
    }

    public void setSemifinalResetBoard(Boolean input) {
        add(SEMIFINAL_RESET_BOARD, input);
    }

    public void setSemifinalShuffleBoard(Boolean input) {
        add(SEMIFINAL_SHUFFLE_BOARD, input);
    }

    public void setSemifinalPercentage(Boolean input) {
        add(SEMIFINAL_PERCENTAGE, input);
    }

    private Parameter<Object> find(String name, List<PParameter> input) {
        return input.stream()
                .filter(parameter -> parameter.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parameter name not found: " + name))
                .build();
    }

    public void update(List<PParameter> parameters) {
        update(parameters, GAME_IS_MULTIPLE_OR_DISPOSABLE);
        update(parameters, LEVEL_BIG_BADABOOM);
        update(parameters, GAME_PLAYERS_PER_ROOM_FOR_DISPOSABLE);
        update(parameters, SCORE_KILL_WALL_SCORE);
        update(parameters, SCORE_KILL_MEAT_CHOPPER_SCORE);
        update(parameters, SCORE_KILL_OTHER_HERO_SCORE);
        update(parameters, SCORE_CATCH_PERK_SCORE);
        update(parameters, SCORE_YOUR_HEROES_DEATH_PENALTY);
        update(parameters, SCORE_ROUNDS_WIN_ROUND_SCORE);
        update(parameters, LEVEL_BOMBS_COUNT);
        update(parameters, LEVEL_BOMB_POWER);
        update(parameters, LEVEL_BOARD_SIZE);
        update(parameters, LEVEL_DESTROY_WALL_COUNT);
        update(parameters, LEVEL_MEAT_CHOPPERS_COUNT);
        update(parameters, ROUNDS_TIME_PER_ROUND);
        update(parameters, ROUNDS_TIME_FOR_WINNER);
        update(parameters, ROUNDS_TIME_BEFORE_START_ROUND);
        update(parameters, ROUNDS_ROUNDS_PER_MATCH);
        update(parameters, ROUNDS_MIN_TICKS_FOR_WIN);
        update(parameters, PERKS_PERKS_DROP_RATIO_IN);
        update(parameters, PERKS_PERKS_PICK_TIMEOUT);
        update(parameters, PERKS_BOMB_BLAST_RADIUS_INCREASE);
        update(parameters, PERKS_BOMB_COUNT_INCREASE);
        update(parameters, PERKS_BOMB_COUNT_EFFECT_TIMEOUT);
        update(parameters, PERKS_BOMB_IMMUNE_EFFECT_TIMEOUT);
        update(parameters, PERKS_NUMBER_OF_BOMB_REMOTE_CONTROLS);
        update(parameters, PERKS_BOMB_BLAST_RADIUS_INCREASE_EFFECT_TIMEOUT);
        update(parameters, GAME_ROUNDS_ENABLED);
        update(parameters, SEMIFINAL_TIMEOUT);
        update(parameters, SEMIFINAL_PERCENTAGE);
        update(parameters, SEMIFINAL_LIMIT);
        update(parameters, SEMIFINAL_ENABLED);
        update(parameters, SEMIFINAL_RESET_BOARD);
        update(parameters, SEMIFINAL_SHUFFLE_BOARD);
    }

    private void update(List<PParameter> input, String name) {
        try {
            Object value = find(name, parameters).getValue();
            find(name, input).update(value);
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }

    private Boolean getBoolean(String name) {
        return (Boolean) find(name, parameters).getValue();
    }
    
    private Integer getInteger(String name) {
        return (Integer) find(name, parameters).getValue();
    }

    public PParameters parameters() {
        return new PParameters(){{
            setParameters(parameters);
        }};
    }
}
