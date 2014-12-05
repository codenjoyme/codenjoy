package com.codenjoy.dojo.collapse.services;

import com.codenjoy.dojo.sudoku.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.model.SingleSudoku;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class CollapseGame implements GameType {

    private final Settings settings;

    public CollapseGame() {
        settings = new SettingsImpl();
        new CollapsePlayerScores(0, settings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new CollapsePlayerScores(score, settings);
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
        return "collapse";
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
