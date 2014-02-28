package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.*;

public class SingleHex implements Game {

    private Printer printer;
    private Player player;
    private Hex Hex;

    public SingleHex(Hex Hex, EventListener listener) {
        this.Hex = Hex;
        this.player = new Player(listener);
        this.printer = new Printer(Hex.getSize(), new HexPrinter(Hex, player));
    }

    @Override
    public Joystick getJoystick() {
        return player.getHero();
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
        return !player.hero.isAlive();
    }

    @Override
    public void newGame() {
        Hex.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        Hex.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public Point getHero() {
        return player.getHero();
    }

    @Override
    public void tick() {
        Hex.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
