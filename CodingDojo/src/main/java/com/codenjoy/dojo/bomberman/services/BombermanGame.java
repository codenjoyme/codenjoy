package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:18 PM
 */
public class BombermanGame implements GameType {

    public static final String GAME_NAME = "bomberman";
    private final Settings parameters;
    private GameSettings settings;
    private Board board;

    public BombermanGame() {
        parameters = new SettingsImpl();
        settings = new OptionGameSettings(parameters);
        board = new Board(settings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BombermanPlayerScores(score, parameters);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleBoard(board, listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return settings.getBoardSize();
    }

    @Override
    public String gameName() {
        return GAME_NAME;
    }

    @Override
    public Object[] getPlots() {
        return PlotColor.values();
    }

    @Override
    public Settings getSettings() {
        return parameters;
    }

    @Override
    public boolean isSingleBoardGame() {
        return true;
    }
}
