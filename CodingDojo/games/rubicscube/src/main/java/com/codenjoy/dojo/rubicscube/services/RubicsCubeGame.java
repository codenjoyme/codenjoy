package com.codenjoy.dojo.rubicscube.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.rubicscube.client.Board;
import com.codenjoy.dojo.rubicscube.client.ai.ApofigDirectionSolver;
import com.codenjoy.dojo.rubicscube.model.Elements;
import com.codenjoy.dojo.rubicscube.model.RandomCommand;
import com.codenjoy.dojo.rubicscube.model.RubicsCube;
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
    public Game newGame(EventListener listener, PrinterFactory factory) {
        RubicsCube rubicsCube = new RubicsCube(new RandomCommand(new RandomDice()));

        Game game = new SingleRubicsCube(rubicsCube, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(12);
    }

    @Override
    public String name() {
        return "rubicscube";
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
        ApofigDirectionSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    }
}
