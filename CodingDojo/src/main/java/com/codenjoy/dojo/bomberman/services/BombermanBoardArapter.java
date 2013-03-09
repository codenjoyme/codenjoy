package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.BoardEvented;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Plot;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:24 PM
 */
public class BombermanBoardArapter implements Game {
    private BoardEvented board;

    public BombermanBoardArapter(BoardEvented board) {
        this.board = board;
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                board.getBomberman().down();
            }

            @Override
            public void up() {
                board.getBomberman().up();
            }

            @Override
            public void left() {
                board.getBomberman().left();
            }

            @Override
            public void right() {
                board.getBomberman().right();
            }

            @Override
            public void act() {
                board.getBomberman().bomb();
            }
        };
    }

    @Override
    public int getMaxScore() {
        return 0;
    }

    @Override
    public int getCurrentScore() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    public void newGame() {
        board = new BombermanGame().createNewGame(board.getEventListener());
    }

    @Override
    public String getBoardAsString() {
        return null;
    }

    @Override
    public List<Plot> getPlots() {
        return null;
    }

    @Override
    public void tick() {
        board.newGame();
    }
}
