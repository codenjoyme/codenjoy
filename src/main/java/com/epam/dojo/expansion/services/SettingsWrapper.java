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
import com.epam.dojo.expansion.model.levels.Levels;

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
    private final Parameter<Integer> roundTicks;
    private final Parameter<Integer> initialForce;
    private final Parameter<Integer> goldScore;
    private final Parameter<Integer> regionsScores;
    private final Parameter<Integer> boardSize;
    private final Parameter<Boolean> waitingOthers;
    private final Parameter<Boolean> shufflePlayers;
    private final Parameter<Boolean> lobbyEnable;
    private final List<Parameter<String>> levels;
    private final int total;
    private final Parameter<Integer> leaveForceCount;

    public static SettingsWrapper setup(Settings settings) {
        return new SettingsWrapper(settings);
    }

    public static SettingsWrapper setup() {
        return setup(new SettingsImpl());
    }

    private SettingsWrapper(Settings settings) {
        data = this;

        levels = new LinkedList<>();
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);
        winScore = settings.addEditBox("Win multiple score").type(Integer.class).def(1);
        roundTicks = settings.addEditBox("Ticks per round").type(Integer.class).def(600);
        leaveForceCount = settings.addEditBox("Leave forces count").type(Integer.class).def(0);
        initialForce = settings.addEditBox("Initial forces count").type(Integer.class).def(10);
        increasePerTick = settings.addEditBox("Increase forces per tick count").type(Integer.class).def(10);
        goldScore = settings.addEditBox("Increase forces gold score").type(Integer.class).def(1);
        regionsScores = settings.addEditBox("Total count territories is occupied by you increase force score").type(Integer.class).def(10);
        waitingOthers = settings.addEditBox("Waiting others").type(Boolean.class).def(false);
        shufflePlayers = settings.addEditBox("Shuffle players after lobby").type(Boolean.class).def(true);
        lobbyEnable = settings.addEditBox("Lobby enable").type(Boolean.class).def(true);
        levels.add(settings.addEditBox("Multiple level 1").type(String.class).def(MULTI1));
        levels.add(settings.addEditBox("Multiple level 2").type(String.class).def(MULTI2));
        levels.add(settings.addEditBox("Multiple level 3").type(String.class).def(MULTI3));
        levels.add(settings.addEditBox("Multiple level 4").type(String.class).def(MULTI4));
        total = Levels.collectSingle(boardSize()).get().size();
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
        return total;
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

    public boolean lobbyEnable() {
        return lobbyEnable.getValue();
    }

    public boolean roundLimitedInTime() {
        return roundTicks() != UNLIMITED;
    }

    public int winScore() {
        return winScore.getValue();
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

}
