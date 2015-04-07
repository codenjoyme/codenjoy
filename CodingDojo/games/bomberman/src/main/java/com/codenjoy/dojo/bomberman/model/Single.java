package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 21:43
 */
public class Single implements Game {

    private Player player;
    private Bomberman game;

    private Printer printer;

    public Single(Bomberman game, EventListener listener, PrinterFactory factory) {
        this.game = game;
        player = new Player(listener);
        printer = factory.getPrinter(game.reader(), player);
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
        return player.getBomberman();
    }

    @Override
    public void tick() {
        game.tick();
    }

}
