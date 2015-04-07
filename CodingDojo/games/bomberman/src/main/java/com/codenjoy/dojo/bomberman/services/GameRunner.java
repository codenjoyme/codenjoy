package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.client.ai.ApofigSolver;
import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Single;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:18 PM
 */
public class GameRunner implements GameType {

    public static final String GAME_NAME = "bomberman";
    private final Settings settings;
    private GameSettings gameSettings;
    private Bomberman board;

    public GameRunner() {
        settings = new SettingsImpl();
        gameSettings = new OptionGameSettings(settings);
        new Scores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    private Bomberman newGame() {
        return new Bomberman(gameSettings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (board == null) {
            board = newGame();
        }
        Game game = new Single(board, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return gameSettings.getBoardSize();
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
    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoard() {
        return true;
    }

    @Override
    public void newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    }
}
