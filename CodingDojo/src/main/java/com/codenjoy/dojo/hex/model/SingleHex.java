package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class SingleHex implements Game {

    private Printer printer;
    private Player player;
    private Hex hex;

    public SingleHex(Hex Hex, EventListener listener) {
        this.hex = Hex;
        this.player = new Player(listener, hex);
        this.printer = new Printer(Hex.getSize(), new HexPrinter(Hex, player));
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
        return !player.isAlive();
    }

    @Override
    public void newGame() {
        hex.newGame(player);
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        hex.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public Point getHero() {
        return pt(-1, -1); // TODO
    }

    @Override
    public void tick() {
        hex.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
