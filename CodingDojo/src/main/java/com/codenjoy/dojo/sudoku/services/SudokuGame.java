package com.codenjoy.dojo.sudoku.services;

import com.codenjoy.dojo.sudoku.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.model.SingleSudoku;
import com.codenjoy.dojo.sudoku.model.Sudoku;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class SudokuGame implements GameType {

    private final Settings settings;

    public SudokuGame() {
        settings = new SettingsImpl();
        new SudokuPlayerScores(0, settings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new SudokuPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleSudoku(listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(9 + 4);
    }

    @Override
    public String gameName() {
        return "sudoku";
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
        return false;
    }

    @Override
    public void newAI(String aiName) {
        // TODO implment me
    }
}
