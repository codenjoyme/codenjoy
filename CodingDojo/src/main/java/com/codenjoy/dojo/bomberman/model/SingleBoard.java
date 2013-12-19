package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.*;

import java.util.List;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 21:43
 */
public class SingleBoard implements Game, IBoard {

    private final Ticker ticker;
    private Player player;
    private Board board;

    private BombermanPrinter printer;
    private final LazyJoystick joystick;

    public SingleBoard(Board board, Ticker ticker, EventListener listener) {
        this.board = board;
        this.ticker = ticker;
        player = new Player(listener);
        board.add(player);
        printer = new BombermanPrinter(board, player);
        this.joystick = new LazyJoystick();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Joystick getJoystick() {
        return joystick;
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
        joystick.setJoystick(player.getBomberman());
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public void destroy() {
        board.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public void tick() {
        if (ticker.collectTicks()) return;

        board.tick();
    }

    @Override
    public int size() {
        return board.size();
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
    public List<Bomb> getBombs(MyBomberman bomberman) {
        return board.getBombs(bomberman);
    }

    @Override
    public Walls getWalls() {
        return board.getWalls();
    }

    @Override
    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        return board.isBarrier(x, y, isWithMeatChopper);
    }

    @Override
    public void add(Player player) {
        board.add(player);
    }

    @Override
    public void remove(Player player) {
        board.remove(player);
    }

    @Override
    public List<Point> getBlasts() {
        return board.getBlasts();
    }

    @Override
    public void drop(Bomb bomb) {

    }
}
