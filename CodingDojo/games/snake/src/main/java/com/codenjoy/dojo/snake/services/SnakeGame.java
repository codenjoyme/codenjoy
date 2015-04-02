package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.snake.model.*;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.RandomArtifactGenerator;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:41 PM
 */
public class SnakeGame implements GameType {

    private Settings settings;
    private Parameter<Integer> boardSize;

    public SnakeGame() {
        this.settings = new SettingsImpl();

        boardSize = settings.addEditBox("Board size").type(Integer.class).def(15);
        new SnakePlayerScores(0, settings);  // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new SnakePlayerScores(score, settings);
    }

    @Override
    public Game newGame(final EventListener listener, PrinterFactory factory) {
        return new Snake(new RandomArtifactGenerator(), new SnakeFactory() {
            @Override
            public Hero create(int x, int y) {
                return new SnakeEvented(listener, x, y);
            }
        }, new BasicWalls(boardSize.getValue()), boardSize.getValue(), factory);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }

    @Override
    public String name() {
        return "snake";
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
        // TODO implement me
    }

}
