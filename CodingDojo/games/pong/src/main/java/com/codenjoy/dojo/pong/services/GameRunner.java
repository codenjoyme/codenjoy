package com.codenjoy.dojo.pong.services;

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
import com.codenjoy.dojo.pong.client.ai.PongSolver;
import com.codenjoy.dojo.pong.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;
    private Pong game;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "------------------------------" +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                o             " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "------------------------------" +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              " +
                "                              ");
    }

    private Pong newGame() {
        return new Pong(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        if (getMultiplayerType().isSingle() || game == null) {
            game = newGame();
        }

        Game game = new Single(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "pong";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.TOURNAMENT;
    }

    @Override
    public boolean newAI(String aiName) {
        PongSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
