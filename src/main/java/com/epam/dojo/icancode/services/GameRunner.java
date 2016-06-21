package com.epam.dojo.icancode.services;

import com.epam.dojo.icancode.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner implements GameType {

    public final static boolean SINGLE = false;
    private final Settings settings;
    private String level;
    private ICanCode game;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
    }

    private ICanCode newGame() {
        return new ICanCode(Levels.collect(), new RandomDice());
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
        return v(Levels.size());
    }

    @Override
    public String name() {
        return "icancode";
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
        // do nothing
        //return false;
    }
}
