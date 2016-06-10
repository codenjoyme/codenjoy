package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class NullGameType implements GameType {

    public static final GameType INSTANCE = new NullGameType();

    private NullGameType() {
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
    public Game newGame(EventListener listener, PrinterFactory factory) {
        throw exception();
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        throw exception();
    }

    @Override
    public String name() {
        throw exception();
    }

    @Override
    public Enum[] getPlots() {
        throw exception();
    }

    @Override
    public Settings getSettings() {
        throw exception();
    }

    @Override
    public boolean isSingleBoard() {
        throw exception();
    }

    @Override
    public boolean newAI(String aiName) {
        // do nothing
        return false;
    }
}
