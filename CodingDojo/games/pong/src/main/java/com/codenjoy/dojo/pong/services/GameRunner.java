package com.codenjoy.dojo.pong.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.pong.client.ai.PongSolver;
import com.codenjoy.dojo.pong.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;
    private Pong game;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
        level = new LevelImpl(
                "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "------------------------------" +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                o             " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "------------------------------" +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              " +
                        "                              ");
    }

    private Pong newGame() {
        return new Pong(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (!SINGLE || game == null) {
            game = newGame();
        }

        Game game = new Single(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "pong";
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
        return SINGLE;
    }

    @Override
    public void newAI(String aiName) {
        PongSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    }
}
