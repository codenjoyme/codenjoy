package com.codenjoy.dojo.football.services;

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

import java.util.ArrayList;
import java.util.List;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.football.client.ai.DefaultSolver;
import com.codenjoy.dojo.football.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner extends AbstractGameType implements GameType {

	private static final String NUM_OF_PLAYERS = "Num of players";
	private static final String IS_NEED_AI_PARAMETR = "Is need AI";
    private final Level level;
    private Football game;
    private List<Football> games;

    private final Parameter<Integer> needAI;
    private final Parameter<Integer> numberOfPlayers;

    public GameRunner() {
    	games = new ArrayList<Football>();
        numberOfPlayers = settings.addEditBox(NUM_OF_PLAYERS).type(Integer.class).def(2);
        needAI = settings.addEditBox(IS_NEED_AI_PARAMETR).type(Integer.class).def(1);
        new Scores(0, settings);
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┴┴┴┴┴┴┴☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼               ∙              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┬┬┬┬┬┬┬☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Football newGame() {
        return new Football(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        if (getMultiplayerType().isSingle() || game == null) {
            game = newGame();
        } else if (game != null) {
        	if (game.getPlayersCount() >= numberOfPlayers.getValue() ) {
        		games.add(game);
        		game = newGame();
        	}
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
        return "football";
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public boolean newAI(String aiName) {
        boolean result = needAI.getValue() == 1;
        if (result) {
    		DefaultSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
    	}
        return result;
    }
}
