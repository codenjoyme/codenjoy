package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class LoderunnerGame implements GameType {

    public final static boolean SINGLE = true;

    public LoderunnerGame() {
        // TODO implement me
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new LoderunnerPlayerScores(score);
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
    public Settings getSettings() {
        return new NullSettings();  // TODO implement me
    }

    @Override
    public boolean isSingleBoardGame() {
        return SINGLE;
    }
}
