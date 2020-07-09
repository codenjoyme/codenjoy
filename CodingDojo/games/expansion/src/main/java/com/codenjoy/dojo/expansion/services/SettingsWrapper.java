package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksandr_Baglai on 2017-09-08.
 */
public final class SettingsWrapper {

    public static final int UNLIMITED = -1;

    public static SettingsWrapper data;

    private final Parameter<Integer> increasePerTick;
    private final Parameter<Integer> winScore;
    private final Parameter<Integer> drawScore;
    private final Parameter<Integer> roundTicks;
    private final Parameter<Integer> initialForce;
    private final Parameter<Integer> goldScore;
    private final Parameter<Integer> regionsScores;
    private final Parameter<Integer> boardSize;
    private final Parameter<Boolean> defenderHasAdvantage;
    private final Parameter<Boolean> singleTrainingMode;
    private final Parameter<Boolean> delayReplay;
    private final Parameter<Double> defenderAdvantage;
    private final Parameter<Boolean> waitingOthers;
    private final Parameter<Boolean> shufflePlayers;
    private final Parameter<Boolean> gameLoggingEnable;
    private final List<Parameter<String>> multipleLevels;
    private final List<Parameter<String>> singleLevels;
    private final Parameter<Integer> leaveForceCount;
    private final Settings settings;
    private final Parameter<String> command;

    public static SettingsWrapper setup(Settings settings) {
        return new SettingsWrapper(settings);
    }

    // for testing
    public static SettingsWrapper setup() {
        return setup(new SettingsImpl());
    }

    private SettingsWrapper(Settings settings) {
        data = this;
        this.settings = settings;

        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);

        singleTrainingMode = settings.addEditBox("Single training mode").type(Boolean.class).def(false);

        waitingOthers = settings.addEditBox("Waiting others").type(Boolean.class).def(true);
        shufflePlayers = settings.addEditBox("Shuffle players after lobby").type(Boolean.class).def(true);

        winScore = settings.addEditBox("Win on multiple score").type(Integer.class).def(4);
        drawScore = settings.addEditBox("Draw on multiple score").type(Integer.class).def(1);
        roundTicks = settings.addEditBox("Ticks per round").type(Integer.class).def(600);

        leaveForceCount = settings.addEditBox("Leave forces count").type(Integer.class).def(0);
        initialForce = settings.addEditBox("Initial forces count").type(Integer.class).def(10);
        increasePerTick = settings.addEditBox("Increase forces per tick count").type(Integer.class).def(10);
        goldScore = settings.addEditBox("Increase forces gold score").type(Integer.class).def(1);
        regionsScores = settings.addEditBox("Total count territories is occupied by you increase force score").type(Integer.class).def(10);


        defenderHasAdvantage = settings.addEditBox("Defender has advantage").type(Boolean.class).def(true);
        defenderAdvantage = settings.addEditBox("Defender attack advantage").type(Double.class).def(1.3);

        command = settings.addEditBox("Command").type(String.class).def("");
        gameLoggingEnable = settings.addEditBox("Game logging enable").type(Boolean.class).def(false);
        delayReplay = settings.addEditBox("Clean scores to run all replays").type(Boolean.class).def(false);

        multipleLevels = new LinkedList<>();
        for (int index = 0; index < Levels.MULTI.size(); index++) {
            String name = Levels.MULTI.get(index);
            multipleLevels.add(settings.addEditBox("Multiple level " + (index + 1)).type(String.class).def(name));
        }

        singleLevels = new LinkedList<>();
        for (int index = 0; index < Levels.SINGLE.size(); index++) {
            String name = Levels.SINGLE.get(index);
            singleLevels.add(settings.addEditBox("Single level " + (index + 1)).type(String.class).def(name));
        }
    }

    public int boardSize() {
        return boardSize.getValue();
    }

    public List<String> multipleLevels() {
        return multipleLevels.stream().map(p -> p.getValue()).collect(toList());
    }

    public List<String> singleLevels() {
        return singleLevels.stream().map(p -> p.getValue()).collect(toList());
    }

    public boolean waitingOthers() {
        return waitingOthers.getValue();
    }

    public int leaveForceCount() {
        return leaveForceCount.getValue();
    }

    public int initialForce() {
        return initialForce.getValue();
    }

    public int increasePerTick() {
        return increasePerTick.getValue();
    }

    public int regionsScores() {
        return regionsScores.getValue();
    }

    public int goldScore() {
        return goldScore.getValue();
    }

    public int roundTicks() {
        return roundTicks.getValue();
    }

    public boolean shufflePlayers() {
        return shufflePlayers.getValue();
    }

    public boolean gameLoggingEnable() {
        return gameLoggingEnable.getValue();
    }

    public boolean roundLimitedInTime() {
        return roundTicks() != UNLIMITED;
    }

    public int winScore() {
        return winScore.getValue();
    }

    public int drawScore() {
        return drawScore.getValue();
    }

    public double defenderAdvantage() {
        return defenderAdvantage.getValue();
    }

    public boolean defenderHasAdvantage() {
        return defenderHasAdvantage.getValue();
    }

    public boolean singleTrainingMode() {
        return singleTrainingMode.getValue();
    }

    public String command() {
        return command.getValue();
    }

    private static final DefenderHasAdvantageAttack DEFENDER_HAS_ADVANTAGE_ATTACK = new DefenderHasAdvantageAttack();
    private static final OneByOneAttack ONE_BY_ONE_ATTACK = new OneByOneAttack();

    public Attack attack() {
        return defenderHasAdvantage() ? DEFENDER_HAS_ADVANTAGE_ATTACK : ONE_BY_ONE_ATTACK;
    }

    public boolean delayReplay() {
        return delayReplay.getValue();
    }

    // setters for testing

    public SettingsWrapper leaveForceCount(int value) {
        leaveForceCount.update(value);
        return this;
    }

    public SettingsWrapper goldScore(int value) {
        goldScore.update(value);
        return this;
    }

    public SettingsWrapper regionsScores(int value) {
        regionsScores.update(value);
        return this;
    }

    public SettingsWrapper roundUnlimited() {
        return roundTicks(UNLIMITED);
    }

    public SettingsWrapper roundTicks(int value) {
        roundTicks.update(value);
        return this;
    }

    public SettingsWrapper shufflePlayers(boolean value) {
        shufflePlayers.update(value);
        return this;
    }

    public SettingsWrapper waitingOthers(boolean value) {
        waitingOthers.update(value);
        return this;
    }

    public SettingsWrapper boardSize(int value) {
        boardSize.update(value);
        return this;
    }

    public SettingsWrapper defenderAdvantage(double value) {
        defenderAdvantage.update(value);
        return this;
    }

    public SettingsWrapper defenderHasAdvantage(boolean value) {
        defenderHasAdvantage.update(value);
        return this;
    }

    public SettingsWrapper command(String value) {
        command.update(value);
        return this;
    }

    public SettingsWrapper gameLoggingEnable(boolean value) {
        gameLoggingEnable.update(value);
        return this;
    }

    public SettingsWrapper delayReplay(boolean value) {
        delayReplay.update(value);
        return this;
    }

    public SettingsWrapper singleTrainingMode(boolean value) {
        singleTrainingMode.update(value);
        return this;
    }

    public static void cleanMulti(int afterIndex) {
        int index = 0;
        for (Parameter parameter : data.multipleLevels.toArray(new Parameter[0])) {
            if (index > afterIndex) {
                data.settings.removeParameter(parameter.getName());
                data.multipleLevels.remove(data.multipleLevels.size() - 1);
            }
            index++;
        }
    }

    public static void multi(String... maps) {
        for (int i = 0; i < maps.length; i++) {
            updateLevels("MULTI", "Multiple level ", i, maps[i]);
        }
        clean(maps.length - 1, data.multipleLevels);
    }

    private static void clean(int afterIndex, List<Parameter<String>> fromWhere) {
        int index = 0;
        for (Parameter parameter : fromWhere.toArray(new Parameter[0])) {
            if (index > afterIndex) {
                data.settings.removeParameter(parameter.getName());
                fromWhere.remove(fromWhere.size() - 1);
            }
            index++;
        }
    }

    public static void single(String... maps) {
        for (int i = 0; i < maps.length; i++) {
            updateLevels("SINGLE", "Single level ", i, maps[i]);
        }
        clean(maps.length - 1, data.singleLevels);
    }

    public static void updateLevels(String type, String description,
                                    int index, String value) {
        String name = type + index + "_TEST";
        data.settings.getParameter(description + (index + 1)).update(name);
        Levels.put(name, value);
        int size = (int) Math.sqrt(value.length());
        if (size < data.boardSize()) {
            data.boardSize(size);
        }
    }
}
