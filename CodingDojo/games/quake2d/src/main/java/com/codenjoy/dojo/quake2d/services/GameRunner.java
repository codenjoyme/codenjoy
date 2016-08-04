package com.codenjoy.dojo.quake2d.services;

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
import com.codenjoy.dojo.quake2d.client.ai.BotSolver;
import com.codenjoy.dojo.quake2d.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner extends AbstractGameType implements GameType {

    public final static boolean SINGLE = true;
    private final Level level;
    private Sample game;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                  ☼☼☼☼      ☼" +
                "☼                  ☼☼☼☼      ☼" +
                "☼    ☼☼☼☼          ☼☼☼☼      ☼" +
                "☼    ☼☼☼☼          ☼☼☼☼      ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼           ☼☼☼☼             ☼" +
                "☼           ☼☼☼☼             ☼" +
                "☼           ☼☼☼☼             ☼" +
                "☼           ☼☼☼☼             ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                   ☼☼☼☼     ☼" +
                "☼                   ☼☼☼☼     ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼    ☼☼☼☼                    ☼" +
                "☼                ☼☼☼☼        ☼" +
                "☼                ☼☼☼☼        ☼" +
                "☼                ☼☼☼☼        ☼" +
                "☼       ☺        ☼☼☼☼        ☼" +
                "☼                            ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Sample newGame() {
        return new Sample(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save) {
        if (!SINGLE || game == null) {
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
        return "quake2d";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public boolean isSingleBoard() {
        return SINGLE;
    }

    @Override
    public boolean newAI(String aiName) {
        BotSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
