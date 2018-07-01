package com.codenjoy.dojo.moebius.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.moebius.client.ai.ApofigSolver;
import com.codenjoy.dojo.moebius.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.apache.commons.lang.StringUtils;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;
    private final Parameter<Integer> size;

    public GameRunner() {
        new Scores(0, settings);
        size = settings.addEditBox("Size").type(Integer.class).def(15);

        String map = buildMap(size.getValue());
        level = new LevelImpl(map);
    }

    private String buildMap(int size) {
        StringBuilder board = new StringBuilder();
        board.append(pad(size, '╔', '═', '╗'));
        for (int y = 1; y < size - 1; y++) {
            board.append(pad(size, '║', ' ', '║'));
        }
        board.append(pad(size, '╚', '═', '╝'));
        return board.toString();
    }

    private String pad(int len, char left, char middle, char right) {
        return left + StringUtils.rightPad("", len - 2, middle) + right;
    }

    private Moebius newGame(EventListener listener) {
        return new Moebius(level, new RandomDice(), listener);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        Moebius moebius = newGame(listener);
        return new Single(moebius, listener, factory);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "moebius";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }

    @Override
    public boolean newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
