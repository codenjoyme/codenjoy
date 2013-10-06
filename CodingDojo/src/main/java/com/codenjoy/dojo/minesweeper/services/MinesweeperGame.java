package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.minesweeper.model.BoardImpl;
import com.codenjoy.dojo.minesweeper.model.PlotColor;
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

    private final SettingsImpl parameters;

    private Parameter<Integer> boardSize;
    private Parameter<Integer> minesOnBoard;
    private Parameter<Integer> charge;

    public MinesweeperGame () {
        this.parameters = new SettingsImpl();
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new MinesweeperPlayerScores(score, parameters);
    }

    @Override
    public Game newGame(EventListener listener) {
        boardSize = parameters.addEditBox("Board size").type(Integer.class).def(15);
        minesOnBoard = parameters.addEditBox("Mines on board").type(Integer.class).def(30);
        charge = parameters.addEditBox("Charge").type(Integer.class).def(100);

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
        return PlotColor.values();
    }

    @Override
    public Settings getSettings() {
        return parameters;
    }
}
