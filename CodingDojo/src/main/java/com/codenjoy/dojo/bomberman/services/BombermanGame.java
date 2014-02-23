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
    private final Settings settings;
    private GameSettings gameSettings;
    private Board board;

    public BombermanGame() {
        settings = new SettingsImpl();
        gameSettings = new OptionGameSettings(settings);
        new BombermanPlayerScores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    private Board newGame() {
        return new Board(gameSettings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BombermanPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        if (board == null) {
            board = newGame();
        }
        Game game = new SingleBoard(board, listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return gameSettings.getBoardSize();
    }

    @Override
    public String gameName() {
        return GAME_NAME;
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getGameSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoardGame() {
        return true;
    }
}
