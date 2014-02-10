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
    private final Level level;
    private Sudoku game;

    public SudokuGame() {
        settings = new SettingsImpl();
        new SudokuPlayerScores(0, settings);
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼",

                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼  ?☼? ?☼???☼" +
                "☼ ??☼   ☼???☼" +
                "☼?  ☼???☼? ?☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ ??☼? ?☼?? ☼" +
                "☼ ??☼ ? ☼?? ☼" +
                "☼ ??☼? ?☼?? ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼? ?☼???☼  ?☼" +
                "☼???☼   ☼?? ☼" +
                "☼???☼? ?☼?  ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Sudoku newGame() {
        return new Sudoku(level);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new SudokuPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        game = newGame();

        Game game = new SingleSudoku(this.game, listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
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
}
