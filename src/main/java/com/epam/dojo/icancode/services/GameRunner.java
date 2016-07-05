package com.epam.dojo.icancode.services;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ICanCode;
import com.epam.dojo.icancode.model.Single;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner implements GameType {

    private final Settings settings;
    private ICanCode multiple;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
        multiple = new ICanCode(Levels.collectMultiple(), new RandomDice(), ICanCode.MULTIPLE);
    }

    private ICanCode newSingleGame() {
        return new ICanCode(Levels.collectSingle(), new RandomDice(), ICanCode.SINGLE);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        Game single = new Single(newSingleGame(), multiple, listener, factory);
        single.newGame();
        return single;
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
        return false;
    }

    @Override
    public boolean newAI(String aiName) {
        // do nothing
        return false;
    }
}
