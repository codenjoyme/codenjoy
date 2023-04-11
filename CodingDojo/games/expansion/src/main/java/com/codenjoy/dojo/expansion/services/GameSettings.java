package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
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


import com.codenjoy.dojo.expansion.model.attack.Attack;
import com.codenjoy.dojo.expansion.model.attack.DefenderHasAdvantageAttack;
import com.codenjoy.dojo.expansion.model.attack.OneByOneAttack;
import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.PropertiesKey;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.expansion.services.GameRunner.GAME_NAME;
import static com.codenjoy.dojo.expansion.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.toArray;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public static final int UNLIMITED = -1;

    public enum Keys implements PropertiesKey {
        
        BOARD_SIZE,
        SINGLE_TRAINING_MODE,
        WAITING_OTHERS,
        SHUFFLE_PLAYERS_AFTER_LOBBY,
        WIN_ON_MULTIPLE_SCORE,
        DRAW_ON_MULTIPLE_SCORE,
        TICKS_PER_ROUND,
        LEAVE_FORCES,
        INITIAL_FORCES,
        INCREASE_FORCES_PER_TICK,
        INCREASE_FORCES_GOLD_SCORE,
        REGION_SCORE,
        DEFENDER_HAS_ADVANTAGE,
        DEFENDER_ATTACK_ADVANTAGE,
        COMMAND,
        GAME_LOGGING_ENABLE,
        DELAY_REPLAY,
        MULTIPLE_LEVEL,
        MULTIPLE_LEVEL_SIZE,
        SINGLE_LEVEL,
        SINGLE_LEVEL_SIZE;

        private String key;

        Keys() {
            this.key = key(GAME_NAME);
        }
        
        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        integer(BOARD_SIZE, 20);

        bool(SINGLE_TRAINING_MODE, false);

        integer(WIN_ON_MULTIPLE_SCORE, 4);
        integer(DRAW_ON_MULTIPLE_SCORE, 1);
        integer(TICKS_PER_ROUND, 600);

        integer(LEAVE_FORCES, 0);
        integer(INITIAL_FORCES, 10);
        integer(INCREASE_FORCES_PER_TICK, 10);
        integer(INCREASE_FORCES_GOLD_SCORE, 1);
        integer(REGION_SCORE, 10);

        bool(DEFENDER_HAS_ADVANTAGE, true);
        real(DEFENDER_ATTACK_ADVANTAGE, 1.3);

        string(COMMAND, "");
        bool(GAME_LOGGING_ENABLE, false);
        bool(DELAY_REPLAY, false);

        integer(MULTIPLE_LEVEL_SIZE, Levels.MULTI.size());
        for (int index = 0; index < Levels.MULTI.size(); index++) {
            String name = Levels.MULTI.get(index);
            int finalIndex = index;
            string(() -> MULTIPLE_LEVEL(finalIndex), name);
        }

        integer(SINGLE_LEVEL_SIZE, Levels.SINGLE.size());
        for (int index = 0; index < Levels.SINGLE.size(); index++) {
            String name = Levels.SINGLE.get(index);
            int finalIndex = index;
            string(() -> SINGLE_LEVEL(finalIndex), name);
        }
    }

    private String MULTIPLE_LEVEL(int index) {
        return getKey(index, MULTIPLE_LEVEL.key());
    }

    @NotNull
    private String getKey(int index, String key) {
        return key + " " + (index + 1);
    }

    private String SINGLE_LEVEL(int index) {
        return getKey(index, SINGLE_LEVEL.key());
    }

    public List<String> multipleLevels() {
        return IntStream.range(0, integer(MULTIPLE_LEVEL_SIZE))
                .mapToObj(index -> string(() -> MULTIPLE_LEVEL(index)))
                .collect(toList());
    }

    public List<String> singleLevels() {
        return IntStream.range(0, integer(SINGLE_LEVEL_SIZE))
                .mapToObj(index -> string(() -> SINGLE_LEVEL(index)))
                .collect(toList());
    }

    public int boardSize() {
        return integer(BOARD_SIZE);
    }

    public int leaveForceCount() {
        return integer(LEAVE_FORCES);
    }

    public int initialForce() {
        return integer(INITIAL_FORCES);
    }

    public int increasePerTick() {
        return integer(INCREASE_FORCES_PER_TICK);
    }

    public int regionsScores() {
        return integer(REGION_SCORE);
    }

    public int goldScore() {
        return integer(INCREASE_FORCES_GOLD_SCORE);
    }

    public int roundTicks() {
        return integer(TICKS_PER_ROUND);
    }

    public boolean gameLoggingEnable() {
        return bool(GAME_LOGGING_ENABLE);
    }

    public boolean roundLimitedInTime() {
        return roundTicks() != UNLIMITED;
    }

    public int winScore() {
        return integer(WIN_ON_MULTIPLE_SCORE);
    }

    public int drawScore() {
        return integer(DRAW_ON_MULTIPLE_SCORE);
    }

    public double defenderAdvantage() {
        return real(DEFENDER_ATTACK_ADVANTAGE);
    }

    public boolean defenderHasAdvantage() {
        return bool(DEFENDER_HAS_ADVANTAGE);
    }

    public boolean singleTrainingMode() {
        return bool(SINGLE_TRAINING_MODE);
    }

    public String command() {
        return string(COMMAND);
    }

    private final DefenderHasAdvantageAttack DEFENDER_HAS_ADVANTAGE_ATTACK = new DefenderHasAdvantageAttack(this);
    private final OneByOneAttack ONE_BY_ONE_ATTACK = new OneByOneAttack(this);

    public Attack attack() {
        return defenderHasAdvantage() ? DEFENDER_HAS_ADVANTAGE_ATTACK : ONE_BY_ONE_ATTACK;
    }

    public boolean delayReplay() {
        return bool(DELAY_REPLAY);
    }

    // setters for testing

    public GameSettings leaveForceCount(int value) {
        integer(LEAVE_FORCES, value);
        return this;
    }

    public GameSettings goldScore(int value) {
        integer(INCREASE_FORCES_GOLD_SCORE, value);
        return this;
    }

    public GameSettings regionsScores(int value) {
        integer(REGION_SCORE, value);
        return this;
    }

    public GameSettings roundUnlimited() {
        return roundTicks(UNLIMITED);
    }

    public GameSettings roundTicks(int value) {
        integer(TICKS_PER_ROUND, value);
        return this;
    }

    public GameSettings boardSize(int value) {
        integer(BOARD_SIZE, value);
        return this;
    }

    public GameSettings defenderAdvantage(double value) {
        real(DEFENDER_ATTACK_ADVANTAGE, value);
        return this;
    }

    public GameSettings defenderHasAdvantage(boolean value) {
        bool(DEFENDER_HAS_ADVANTAGE, value);
        return this;
    }

    public GameSettings command(String value) {
        string(COMMAND, value);
        return this;
    }

    public GameSettings gameLoggingEnable(boolean value) {
        bool(GAME_LOGGING_ENABLE, value);
        return this;
    }

    public GameSettings delayReplay(boolean value) {
        bool(DELAY_REPLAY, value);
        return this;
    }

    public GameSettings singleTrainingMode(boolean value) {
        bool(SINGLE_TRAINING_MODE, value);
        return this;
    }

    public void cleanMulti(int afterIndex) {
        int index = 0;
        for (Parameter parameter : toArray(new Parameter[0])) {
            if (index > afterIndex) {
                removeParameter(parameter.getName());
            }
            index++;
        }
    }

    public void multi(String... maps) {
        for (int i = 0; i < maps.length; i++) {
            updateLevels("MULTI", MULTIPLE_LEVEL.key(), i, maps[i]);
        }
        integer(MULTIPLE_LEVEL_SIZE, maps.length);
    }

    public void single(String... maps) {
        for (int i = 0; i < maps.length; i++) {
            updateLevels("SINGLE", SINGLE_LEVEL.key(), i, maps[i]);
        }
        integer(SINGLE_LEVEL_SIZE, maps.length);
    }

    public void updateLevels(String type, String description,
                                    int index, String value) {
        String name = type + index + "_TEST";
        Parameter<?> parameter = getParameter(getKey(index, description), () -> null);
        if (parameter != null) {
            parameter.update(name);
        }
        Levels.put(name, value);
        int size = (int) Math.sqrt(value.length());
        if (size < boardSize()) {
            boardSize(size);
        }
    }


}
