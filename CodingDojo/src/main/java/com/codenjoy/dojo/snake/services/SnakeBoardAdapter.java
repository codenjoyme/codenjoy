package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GuiPlotColorDecoder;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.snake.model.PlotColor;
import com.codenjoy.dojo.snake.model.SnakePrinter;
import com.codenjoy.dojo.snake.model.BoardImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:45 PM
 */
public class SnakeBoardAdapter implements Game {  // TODO remove me
    private BoardImpl board;
    private GuiPlotColorDecoder decoder;

    public SnakeBoardAdapter(BoardImpl board) {
        this.board = board;
        this.decoder = new GuiPlotColorDecoder(PlotColor.values());
    }

    @Override
    public Joystick getJoystick() {
        return board.getSnake();
    }

    @Override
    public int getMaxScore() {
        return board.getMaxLength();
    }

    @Override
    public int getCurrentScore() {
        return board.getSnake().getLength();
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    public void newGame() {
        board.newGame();
    }

    @Override
    public String getBoardAsString() {
        return board.toString();
    }

    @Override
    public void tick() {
        board.tact();
    }
}
