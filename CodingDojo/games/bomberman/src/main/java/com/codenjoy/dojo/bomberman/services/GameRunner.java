package com.codenjoy.dojo.bomberman.services;

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


import com.codenjoy.dojo.bomberman.client.ai.ApofigSolver;
import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Single;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:18 PM
 */
public class GameRunner extends AbstractGameType implements GameType {

    public static final String GAME_NAME = "bomberman";
    private GameSettings gameSettings;
    private Bomberman board;

    public GameRunner() {
        gameSettings = new OptionGameSettings(settings);
        new Scores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    private Bomberman newGame() {
        return new Bomberman(gameSettings);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        if (board == null) {
            board = newGame();
        }
        Game game = new Single(board, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName, boolean bot) {
        if (board == null) {
            board = newGame();
        }
        Game game = new Single(board, listener, factory, bot);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return board.getLevel().getSize();
    }

    @Override
    public String name() {
        return GAME_NAME;
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public boolean isSingleBoard() {
        return GameMode.SINGLE_MODE;
    }

    @Override
    public boolean newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
