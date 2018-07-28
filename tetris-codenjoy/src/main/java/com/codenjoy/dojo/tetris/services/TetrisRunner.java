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
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.tetris.client.ai.ApofigSolver;
import com.codenjoy.dojo.tetris.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class TetrisRunner extends AbstractGameType {

    private Settings settings;
    private PlayerFigures queue;
    private Levels levels;
    private TetrisPlayerScores scores;

    private Parameter<String> gameLevels;
    private Parameter<Integer> glassSize;

    public TetrisRunner() {
        settings = new SettingsImpl();;
        gameLevels = settings.addSelect("Game Levels", Arrays.asList("AllFigureLevels")).type(String.class);
        gameLevels.select(0);
        glassSize = settings.addEditBox("Glass Size").type(Integer.class).def(20);
        queue = new PlayerFigures();
        levels = getLevels(queue);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        scores = new TetrisPlayerScores((Integer)score);
        scores.levelChanged(levels.getCurrentLevelNumber(), levels.getCurrentLevel());
        return scores;
    }

    @Override
    public GameField createGame() {
        return new TetrisGame(new PlayerFigures(), glassSize.getValue());
    }

    private Levels getLevels(PlayerFigures queue) {
        String levelName = (String) settings.getParameter("gameLevels").getValue();
        return new LevelsFactory().getGameLevels(queue, levelName);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return glassSize;
    }

    @Override
    public String name() {
        return "tetris";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String save, String playerName) {
        return new Player(listener);
    }

    @Override
    public boolean newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL, getDice());
        return true;
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            JSONObject result = new JSONObject();

            JSONArray array = new JSONArray();
            result.put("layers", array);
            Hero hero = player.getHero();
            array.put(hero.getCurrentFigurePlots());
            array.put(hero.getDroppedPlots());

            result.put("currentFigureType", hero.getCurrentFigureType());
            result.put("currentFigurePoint", hero.getCurrentFigurePoint());
            result.put("futureFigures", hero.getFutureFigures());

            return result;
        });
    }
}
