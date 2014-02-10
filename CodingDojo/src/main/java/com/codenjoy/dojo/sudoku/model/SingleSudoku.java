package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.*;

public class SingleSudoku implements Game {

    private Printer printer;
    private Player player;
    private Sudoku sudoku;

    public SingleSudoku(Sudoku sudoku, EventListener listener) {
        this.sudoku = sudoku;
        this.player = new Player(listener);
        this.printer = new Printer(sudoku.getSize(), new SudokuPrinter(sudoku, player));
    }

    @Override
    public Joystick getJoystick() {
        return player.getJoystick();
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
        return sudoku.isGameOver();
    }

    @Override
    public void newGame() {
        sudoku.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        sudoku.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public Point getHero() {
        return PointImpl.pt(-1, -1);
    }

    @Override
    public void tick() {
        sudoku.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
