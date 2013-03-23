package com.codenjoy.dojo.minesweeper.services;

import com.codenjoy.dojo.minesweeper.model.BoardImpl;
import com.codenjoy.dojo.minesweeper.model.PlotColor;
import com.codenjoy.dojo.minesweeper.model.RandomMinesGenerator;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 11:43 PM
 */
public class MinesweeperGame implements GameType {   // TODO test me

    public static final int BOARD_SIZE = 15;
    public static final int MONES_ON_BOARD = 30;
    public static final int CHARGE = MONES_ON_BOARD*2;

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new MinesweeperPlayerScores(score);
    }

    @Override
    public Game newGame(EventListener informationCollector) {
        BoardImpl board = new BoardImpl(BOARD_SIZE - 2, MONES_ON_BOARD, CHARGE,
                new RandomMinesGenerator(), informationCollector);
        board.newGame();
        return board;
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    @Override
    public String gameName() {
        return "minesweeper";
    }

    @Override
    public Object[] getPlots() {
        return PlotColor.values();
    }
}
