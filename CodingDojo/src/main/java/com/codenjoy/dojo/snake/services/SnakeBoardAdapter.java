package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.snake.model.SnakePrinterImpl;
import com.codenjoy.dojo.snake.model.BoardImpl;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:45 PM
 */
public class SnakeBoardAdapter implements Game {
    private BoardImpl board;
    private SnakePrinterImpl printer;

    public SnakeBoardAdapter(BoardImpl board) {
        this.board = board;
        printer = new SnakePrinterImpl();
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
        return printer.print(board);
    }

    @Override
    public List<Plot> getPlots() {
        return new SnakePlotsBuilder(board).get();    // TODO fixme
    }

    @Override
    public void tick() {
        board.tact();
    }
}
