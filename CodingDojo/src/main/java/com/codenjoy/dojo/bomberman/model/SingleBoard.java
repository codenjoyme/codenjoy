package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.snake.model.Snake;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 21:43
 */
public class SingleBoard implements Game, IBoard {

    private Player player;
    private Board board;

    private BombermanPrinter printer;

    public SingleBoard(Board board, EventListener listener) {
        this.board = board;
        player = new Player(listener);
        board.add(player);
        initPrinter(board);
    }

    private void initPrinter(final Board board) {
        printer = new BombermanPrinter(new IBoard() {
            @Override
            public int size() {
                return board.size();
            }

            @Override
            public Bomberman getBomberman() {
                return player.getBomberman();
            }

            @Override
            public List<Bomberman> getBombermans() {
                return board.getBombermans();
            }

            @Override
            public List<Bomb> getBombs() {
                return board.getBombs();
            }

            @Override
            public Walls getWalls() {
                return board.getWalls();
            }

            @Override
            public List<IPoint> getBlasts() {
                return board.getBlasts();
            }
        });
    }

    @Override
    public Joystick getJoystick() {
        return player.getBomberman();
    }

    @Override
    public int getMaxScore() {
        return player.getMaxScore();
    }

    @Override
    public int getCurrentScore() {
        return player.getScore();
    }

    @Override
    public boolean isGameOver() {
        return !player.getBomberman().isAlive();
    }

    @Override
    public void newGame() {
        player.newGame(board, board.getSettings().getLevel());
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public void tick() {
        board.tick();
    }

    @Override
    public int size() {
        return board.size();
    }

    @Override
    public Bomberman getBomberman() {
        return player.getBomberman();
    }

    @Override
    public List<Bomberman> getBombermans() {
        return board.getBombermans();
    }

    @Override
    public List<Bomb> getBombs() {
        return board.getBombs();
    }

    @Override
    public Walls getWalls() {
        return board.getWalls();
    }

    @Override
    public List<IPoint> getBlasts() {
        return board.getBlasts();
    }
}
