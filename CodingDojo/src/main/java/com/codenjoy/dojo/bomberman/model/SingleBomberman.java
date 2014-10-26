package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 21:43
 */
public class SingleBomberman implements Game {

    private Player player;
    private Bomberman board;

    private Printer printer;

    public SingleBomberman(final Bomberman board, EventListener listener) {
        this.board = board;
        player = new Player(listener);

        printer = new Printer(board.size(),
                new Printer.GamePrinterImpl<Elements, Player>(board.reader(), player, Elements.EMPTY.ch()));
    }

    public Player getPlayer() {
        return player;
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
        board.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
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
    public Point getHero() {
        return player.getBomberman();
    }

    @Override
    public void tick() {
        board.tick();
    }

}
