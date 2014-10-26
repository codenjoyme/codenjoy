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
    private Printer printer;

    public SingleTanks(Tanks tanks, EventListener listener, Dice dice) {
        this.tanks = tanks;
        this.player = new Player(listener, dice);
        this.printer = new Printer(tanks.size(),
                new Printer.GamePrinterSimpleImpl<Elements, Player>(tanks.reader(), player, Elements.BATTLE_GROUND.ch()));
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
        tanks.newGame(player);
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
    public Point getHero() {
        return player.getTank();
    }

    @Override
    public void tick() {
        tanks.tick();
    }

    public Player getPlayer() {
        return player;
    }
}
