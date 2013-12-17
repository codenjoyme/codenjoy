package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.minesweeper.model.BoardImpl;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.minesweeper.model.RandomMinesGenerator;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
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
    public Game newGame(EventListener listener) {
        BoardImpl board = new BoardImpl(boardSize, minesOnBoard, charge, new RandomMinesGenerator(), listener);
        board.newGame();
        return board;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }

    @Override
    public String gameName() {
        return "minesweeper";
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
        return false;
    }
}
