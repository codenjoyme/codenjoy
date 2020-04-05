package com.codenjoy.dojo.kata.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
import com.codenjoy.dojo.kata.client.Board;
import com.codenjoy.dojo.kata.client.ai.AISolver;
import com.codenjoy.dojo.kata.model.Kata;
import com.codenjoy.dojo.kata.model.Player;
import com.codenjoy.dojo.kata.model.levels.Level;
import com.codenjoy.dojo.kata.model.levels.LevelsLoader;
import com.codenjoy.dojo.kata.model.levels.LevelsPool;
import com.codenjoy.dojo.kata.model.levels.LevelsPoolImpl;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private List<Level> levels;

    public GameRunner() {
        new Scores(0, settings);
        levels = LevelsLoader.getAlgorithms();
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Kata(getDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(0);
    }

    @Override
    public String name() {
        return "kata";
    }

    @Override
    public CharElements[] getPlots() {
        return new CharElements[0];
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
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        LevelsPool pool = new LevelsPoolImpl(levels);
        return new Player(listener, pool);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader boardReader, Player player) -> {
            JSONObject result = new JSONObject();
            result.put("description", StringEscapeUtils.escapeJava(player.getDescription()));
            result.put("level", player.getLevel());
            result.put("questions", player.getQuestions());
            result.put("nextQuestion", player.getNextQuestion());
            result.put("history", player.getLastHistory());

            return result;
        });
    }
}
