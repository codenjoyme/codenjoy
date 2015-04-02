package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.minesweeper.model.Minesweeper;
import com.codenjoy.dojo.minesweeper.model.RandomMinesGenerator;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 11:43 PM
 */
public class MinesweeperGame implements GameType {   // TODO test me

    private final Settings settings;

    private Parameter<Integer> boardSize;
    private Parameter<Integer> minesOnBoard;
    private Parameter<Integer> charge;

    public MinesweeperGame () {
        this.settings = new SettingsImpl();

        boardSize = settings.addEditBox("Board size").type(Integer.class).def(15);
        minesOnBoard = settings.addEditBox("Mines on board").type(Integer.class).def(30);
        charge = settings.addEditBox("Charge").type(Integer.class).def(100);

        new MinesweeperPlayerScores(0, settings);  // TODO сеттринги разделены по разным классам, продумать архитектуру
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new MinesweeperPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        Minesweeper board = new Minesweeper(boardSize, minesOnBoard, charge, new RandomMinesGenerator(), listener, factory);
        board.newGame();
        return board;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }

    @Override
    public String name() {
        return "minesweeper";
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
