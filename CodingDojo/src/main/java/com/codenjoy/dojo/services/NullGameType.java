package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: sanja
 * Date: 28.12.13
 * Time: 15:03
 */
public class NullGameType implements GameType {

    NullGameType() {
        // do nothing
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        throw exception();
    }

    private UnsupportedOperationException exception() {
        return new UnsupportedOperationException("Oops! Trying to use NullGameType...");
    }

    @Override
    public Game newGame(EventListener listener) {
        throw exception();
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        throw exception();
    }

    @Override
    public String gameName() {
        throw exception();
    }

    @Override
    public Enum[] getPlots() {
        throw exception();
    }

    @Override
    public Settings getGameSettings() {
        throw exception();
    }

    @Override
    public boolean isSingleBoardGame() {
        throw exception();
    }

    @Override
    public void newAI(String aiName) {
        // do nothing
    }
}
