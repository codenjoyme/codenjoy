package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.battlecity.client.Board;
import com.codenjoy.dojo.battlecity.client.ai.ApofigDirectionSolver;
import com.codenjoy.dojo.battlecity.model.Battlecity;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.SingleBattlecity;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.client.WebSocketRunner;
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

    private Battlecity tanks;
    private Level level;

    public BattlecityGame() {
        settings = new SettingsImpl();
        new BattlecityPlayerScores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру

        level = new Level();
    }

    private Battlecity newTank() {
        return new Battlecity(level.size(),
                level.getConstructions(),
                level.getBorders(),
                level.getTanks().toArray(new Tank[0]));
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BattlecityPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (!SINGLE || tanks == null) {
            tanks = newTank();
        }
        Game game = new SingleBattlecity(tanks, listener, factory, new RandomDice());
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.size());
    }

    @Override
    public String name() {
        return "battlecity";
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
        ApofigDirectionSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    }
}
