package com.codenjoy.dojo.startandjump.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.startandjump.client.ai.VladKvadratSolver;
import com.codenjoy.dojo.startandjump.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner implements GameType {

    public final static boolean SINGLE = false;
    private final Settings settings;
    private final Level level;
    private StartAndJump game;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
        level = new LevelImpl(
                        "####################" +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        " =                  " +
                        "                    " +
                        "☺            ==  ===" +
                        " =        =         " +
                        " =  ==== = ==       " +
                        "####################");
    }

    private StartAndJump newGame() {
        return new StartAndJump(new RandomDice(), level);
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
        return "startandjump";
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
    public boolean newAI(String aiName) {
        VladKvadratSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
