package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.08.13
 * Time: 19:52
 */
public class SingleLoderunner implements Game {

    private Printer printer;
    private Player player;
    private Loderunner loderunner;
    private LazyJoystick joystick;
    private Ticker ticker;

    public SingleLoderunner(Loderunner loderunner, Ticker ticker, EventListener listener) {
        this.loderunner = loderunner;
        this.ticker = ticker;
        this.player = new Player(listener);
        this.joystick = new LazyJoystick();
        this.printer = new Printer(loderunner, player);
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
        return !player.hero.isAlive();
    }

    @Override
    public void newGame() {
        loderunner.newGame(player);
        joystick.setJoystick(player.getHero());
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        loderunner.remove(player);
    }

    @Override
    public void clearScore() {
        player.clearScore();
    }

    @Override
    public void tick() {
        if (ticker.collectTicks()) return;

        loderunner.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
