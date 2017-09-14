package com.epam.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.epam.dojo.expansion.model.GameFactory;
import com.epam.dojo.expansion.model.attack.Attack;
import com.epam.dojo.expansion.model.attack.DefenderHasAdvantageAttack;
import com.epam.dojo.expansion.model.attack.OneByOneAttack;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.lobby.NotWaitPlayerLobby;
import com.epam.dojo.expansion.model.lobby.PlayerLobby;
import com.epam.dojo.expansion.model.lobby.WaitForAllPlayerLobby;

import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.model.levels.Levels.*;
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
    private final Parameter<Integer> lobbyCapacity;
    private final Parameter<Boolean> defenderHasAdvantage;
    private final Parameter<Double> defenderAdvantage;
    private final Parameter<Boolean> waitingOthers;
    private final Parameter<Boolean> shufflePlayers;
    private final Parameter<Boolean> lobbyEnable;
    private final Parameter<Boolean> gameLoggingEnable;
    private final List<Parameter<String>> levels;
    private final int totalSingleLevels;
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

        levels = new LinkedList<>();
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);

        waitingOthers = settings.addEditBox("Waiting others").type(Boolean.class).def(true);
        lobbyEnable = settings.addEditBox("Lobby enable (special waiting room)").type(Boolean.class).def(true);
        shufflePlayers = settings.addEditBox("Shuffle players after lobby").type(Boolean.class).def(true);
        lobbyCapacity = settings.addEditBox("Lobby capacity (-1 if wait for all)").type(Integer.class).def(8);

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

        for (int index = 0; index < MULTI.size(); index++) {
            String name = MULTI.get(index);
            levels.add(settings.addEditBox("Multiple level " + (index + 1)).type(String.class).def(name));
        }

        totalSingleLevels = Levels.collectSingle(boardSize()).get().size();
    }

    public int boardSize() {
        return boardSize.getValue();
    }

    public List<String> levels() {
        return levels.stream().map(p -> p.getValue()).collect(toList());
    }

    public boolean waitingOthers() {
        return waitingOthers.getValue();
    }

    public int totalSingleLevels() {
        return totalSingleLevels;
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

    public boolean lobbyEnable() {
        return lobbyEnable.getValue();
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

    public int lobbyCapacity() {
        return lobbyCapacity.getValue();
    }

    public double defenderAdvantage() {
        return defenderAdvantage.getValue();
    }

    public boolean defenderHasAdvantage() {
        return defenderHasAdvantage.getValue();
    }

    public String command() {
        return command.getValue();
    }

    private static final DefenderHasAdvantageAttack DEFENDER_HAS_ADVANTAGE_ATTACK = new DefenderHasAdvantageAttack();
    private static final OneByOneAttack ONE_BY_ONE_ATTACK = new OneByOneAttack();

    public Attack attack() {
        return defenderHasAdvantage() ? DEFENDER_HAS_ADVANTAGE_ATTACK : ONE_BY_ONE_ATTACK;
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

    public SettingsWrapper lobbyEnable(boolean value) {
        lobbyEnable.update(value);
        return this;
    }

    public SettingsWrapper shufflePlayers(boolean value) {
        shufflePlayers.update(value);
        return this;
    }

    public SettingsWrapper lobbyCapacity(int value) {
        lobbyCapacity.update(value);
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

    public static void cleanMulti(int afterIndex) {
        int index = 0;
        for (Parameter parameter : data.levels.toArray(new Parameter[0])) {
            if (index > afterIndex) {
                data.settings.removeParameter(parameter.getName());
                data.levels.remove(data.levels.size() - 1);
            }
            index++;
        }
    }

    public static void multi(int index, String value) {
        String name = "MULTI" + index + "_TEST";
        data.settings.getParameter("Multiple level " + (index + 1)).update(name);
        Levels.put(name, value);
        int size = (int) Math.sqrt(value.length());
        data.boardSize(size);
    }

    public PlayerLobby getPlayerLobby(GameFactory factory) {
        if (data.lobbyEnable()) {
            return new WaitForAllPlayerLobby(factory);
        } else {
            return new NotWaitPlayerLobby(factory);
        }
    }

}
