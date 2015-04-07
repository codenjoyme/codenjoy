package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.*;

public class Single implements Game {

    private Printer printer;
    private Player player;
    private Moebius game;

    public Single(Moebius game, EventListener listener, PrinterFactory factory) {
        this.game = game;
        this.player = new Player(listener);
        this.printer = factory.getPrinter(game.reader(), player);
    }

    @Override
    public Joystick getJoystick() {
        return game.getJoystick();
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
        // do nothing
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
