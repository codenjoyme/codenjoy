package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.console.BombermanPrinter;
import com.codenjoy.dojo.bomberman.console.Printer;
import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.BoardEvented;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.services.playerdata.PlotsBuilder;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:24 PM
 */
public class BombermanBoardArapter implements Game {
    private BoardEvented board;
    private Printer printer;
    private PlotsBuilder builder;

    public BombermanBoardArapter(BoardEvented board) {
        this.board = board;
        printer = new BombermanPrinter(board.size());
        builder = new BombermanPlotsBuilder(board);
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
        return 13;  //TODO fixme
    }

    @Override
    public int getCurrentScore() {
        return 14; //TODO fixme
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    public void newGame() {
        BombermanBoardArapter game = new BombermanGame().createNewGame(board.getEventListener());
        board = game.board;
        printer = game.printer;
        builder = game.builder;
    }

    @Override
    public String getBoardAsString() {
        return printer.print(board);
    }

    @Override
    public List<Plot> getPlots() {
        return builder.get();
    }

    @Override
    public void tick() {
        board.tact();
    }
}
