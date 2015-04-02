package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.08.13
 * Time: 19:52
 */
public class SingleBattlecity implements Game {    // TODO test me

    private Player player;
    private Battlecity game;
    private Printer printer;

    public SingleBattlecity(Battlecity game, EventListener listener, PrinterFactory factory, Dice dice) {
        this.game = game;
        this.player = new Player(listener, dice);
        this.printer = factory.getPrinter(game.reader(), player);
    }

    @Override
    public Joystick getJoystick() {
        return player.getTank();
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
        return !player.getTank().isAlive();
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
        return player.getTank();
    }

    @Override
    public void tick() {
        game.tick();
    }

    public Player getPlayer() {
        return player;
    }
}
