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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.ai.AISolver;
import com.codenjoy.dojo.tetris.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class GameRunner extends AbstractGameType {

    private Settings settings;

    private Parameter<String> gameLevels;
    private Parameter<Integer> glassSize;

    public GameRunner() {
        settings = new SettingsImpl();;
        gameLevels = settings.addSelect("Game Levels", Arrays.asList("AllFigureLevels")).type(String.class);
        gameLevels.select(0);
        glassSize = settings.addEditBox("Glass Size").type(Integer.class).def(20);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame() {
        PlayerFigures queue = new PlayerFigures();
        Levels levels = getLevels(queue);
        // TODO не понятно что делать с этим levels
        return new TetrisGame(queue, glassSize.getValue());
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
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
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
