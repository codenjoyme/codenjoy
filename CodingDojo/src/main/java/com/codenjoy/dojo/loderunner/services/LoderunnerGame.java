package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class LoderunnerGame implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;

    public LoderunnerGame() {
        settings = new SettingsImpl();
        new LoderunnerPlayerScores(0, settings);  // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new LoderunnerPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        return null;  // TODO implement me
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return null; // TODO implement me
    }

    @Override
    public String gameName() {
        return "loderunner";
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
