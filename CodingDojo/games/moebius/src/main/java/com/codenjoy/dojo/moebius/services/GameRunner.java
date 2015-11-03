package com.codenjoy.dojo.moebius.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.moebius.client.ai.ApofigSolver;
import com.codenjoy.dojo.moebius.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.apache.commons.lang.StringUtils;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner implements GameType {

    private final Settings settings;
    private final Level level;
    private final Parameter<Integer> size;

    public GameRunner() {
        settings = new SettingsImpl();
        new Scores(0, settings);
        size = settings.addEditBox("Size").type(Integer.class).def(15);

        String map = buildMap(size.getValue());
        level = new LevelImpl(map);
    }

    private String buildMap(int size) {
        StringBuilder board = new StringBuilder();
        board.append(pad(size, '╔', '═', '╗'));
        for (int y = 1; y < size - 1; y++) {
            board.append(pad(size, '║', ' ', '║'));
        }
        board.append(pad(size, '╚', '═', '╝'));
        return board.toString();
    }

    private String pad(int len, char left, char middle, char right) {
        return left + StringUtils.rightPad("", len - 2, middle) + right;
    }

    private Moebius newGame(EventListener listener) {
        return new Moebius(level, new RandomDice(), listener);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        Moebius moebius = newGame(listener);
        return new Single(moebius, listener, factory);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "moebius";
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
    public void newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
    }
}
