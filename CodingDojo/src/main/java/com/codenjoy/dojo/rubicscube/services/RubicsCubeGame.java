package com.codenjoy.dojo.rubicscube.services;

import com.codenjoy.dojo.rubicscube.model.Elements;
import com.codenjoy.dojo.rubicscube.model.SingleRubicsCube;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class RubicsCubeGame implements GameType {

    private final Settings settings;

    public RubicsCubeGame() {
        settings = new SettingsImpl();
        new RubicsCubePlayerScores(0, settings);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new RubicsCubePlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleRubicsCube(listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(12);
    }

    @Override
    public String gameName() {
        return "rubicscube";
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
