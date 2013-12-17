package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.SingleTanks;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.Tanks;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class BattlecityGame implements GameType {

    public final static boolean SINGLE = true;
    private final SettingsImpl settings;

    private Tanks tanks;
    private Level level;

    public BattlecityGame() {
        settings = new SettingsImpl();
        new BattlecityPlayerScores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру

        level = new Level();
    }

    private Tanks newTank() {
        return new Tanks(level.getSize(),
                level.getConstructions(),
                level.getBorders(),
                level.getTanks().toArray(new Tank[0]));
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BattlecityPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        if (!SINGLE || tanks == null) {
            tanks = newTank();
        }
        Game game = new SingleTanks(tanks, listener, new RandomDice());
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String gameName() {
        return "battlecity";
    }

    @Override
    public Object[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getGameSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoardGame() {
        return SINGLE;
    }
}
