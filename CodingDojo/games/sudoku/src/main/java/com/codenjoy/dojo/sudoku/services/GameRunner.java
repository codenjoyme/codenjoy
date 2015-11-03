package com.codenjoy.dojo.sudoku.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.client.ai.ApofigSolver;
import com.codenjoy.dojo.sudoku.model.*;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner implements GameType {

    private final Settings settings;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        LevelBuilder builder = new LevelBuilder(40, new RandomDice());
        builder.build();
        Level level = new LevelImpl(builder.getBoard(), builder.getMask());
        Sudoku sudoku = new Sudoku(level);

        Game game = new Single(sudoku, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(9 + 4);
    }

    @Override
    public String name() {
        return "sudoku";
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
        return false;
    }

    @Override
    public void newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
    }
}
