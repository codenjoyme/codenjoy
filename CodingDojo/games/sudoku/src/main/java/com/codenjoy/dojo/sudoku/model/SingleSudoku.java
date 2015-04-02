package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.*;

public class SingleSudoku implements Game { // TODO потести меня

    private Printer printer;
    private Player player;
    private Sudoku game;

    public SingleSudoku(Sudoku game, EventListener listener, PrinterFactory factory) {
        this.player = new Player(listener);
        this.game = game;
        printer = factory.getPrinter(game.reader(), player);
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
        return game.isGameOver();
    }

    @Override
    public void newGame() {
        game.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public void destroy() {
        game.remove(player);
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
        game.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
