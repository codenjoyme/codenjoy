package com.codenjoy.dojo.collapse.services;

import com.codenjoy.dojo.collapse.model.SingleCollapse;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.model.Elements;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class CollapseGame implements GameType {

    private final Settings settings;
    private final Parameter<Integer> size;

    public CollapseGame() {
        settings = new SettingsImpl();
        new CollapsePlayerScores(0, settings);
        size = settings.addEditBox("Field size").type(Integer.class).def(15);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new CollapsePlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleCollapse(listener, settings);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return size;
    }

    @Override
    public String gameName() {
        return "collapse";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getGameSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoardGame() {
        return false;
    }

    @Override
    public void newAI(String aiName) {
        // TODO implment me
    }
}
