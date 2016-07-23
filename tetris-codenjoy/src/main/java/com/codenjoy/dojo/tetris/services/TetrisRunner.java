package com.codenjoy.dojo.tetris.services;

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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.tetris.client.ai.ApofigSolver;
import com.codenjoy.dojo.tetris.model.*;

import java.util.Arrays;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see TetrisRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class TetrisRunner implements GameType {

    private final Settings settings;
    private final PlayerFigures queue;
    private final Levels levels;
    private TetrisPlayerScores scores;

    public TetrisRunner() {
        settings = new SettingsImpl();;
        Parameter<?> gameLevelsSelect = settings.addSelect("gameLevels", Arrays.<Object>asList("AllFigureLevels"));
        gameLevelsSelect.select(0);
        queue = new PlayerFigures();
        levels = getLevels(queue);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        scores = new TetrisPlayerScores(score);
        scores.levelChanged(levels.getCurrentLevelNumber(), levels.getCurrentLevel());
        return scores;
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        TetrisGlass glass = new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT, scores, levels);
        final TetrisGame game = new TetrisGame(queue, glass);
        game.newGame();
        return game;
    }

    private Levels getLevels(PlayerFigures queue) {
        String levelName = (String) settings.getParameter("gameLevels").getValue();
        return new LevelsFactory().getGameLevels(queue, levelName);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(TetrisGame.GLASS_HEIGHT);
    }

    @Override
    public String name() {
        return "tetris";
    }

    @Override
    public Enum[] getPlots() {
        return PlotColor.values();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoard() {
        return true;
    }

    @Override
    public boolean newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
