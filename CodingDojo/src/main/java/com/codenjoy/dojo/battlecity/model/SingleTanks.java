package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.08.13
 * Time: 19:52
 */
public class SingleTanks implements Game {    // TODO test me

    private Player player;
    private Tanks tanks;
    private LazyJoystick joystick;
    private Printer printer;
    private Ticker ticker;

    public SingleTanks(Tanks tanks, Ticker ticker, EventListener listener, Dice dice) {
        this.tanks = tanks;
        this.ticker = ticker;
        this.player = new Player(listener, dice);
        this.joystick = new LazyJoystick();
        this.printer = new Printer(tanks.getSize(), new BattlecityPrinter(tanks, player));
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
        return !player.getTank().isAlive();
    }

    @Override
    public void newGame() {
        tanks.newGame(player);
        joystick.setJoystick(player.getTank());
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        tanks.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public void tick() {
        if (ticker.collectTicks()) return;

        tanks.tick();
    }

    public Player getPlayer() {
        return player;
    }
}
