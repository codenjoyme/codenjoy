package com.codenjoy.dojo.a2048.services;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.a2048.client.ai.ApofigDirectionSolver;
import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class A2048Game implements GameType {

    private Level level;
    private A2048 game;

    public A2048Game() {
        level = new LevelImpl();
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new A2048PlayerScores(score);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        game = new A2048(level, new RandomDice());

        Game game = new SingleA2048(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.size());
    }

    @Override
    public String name() {
        return "a2048";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return level.getSettings();
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
