package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.*;

public class SingleA2048 implements Game {

    private Printer printer;
    private Player player;
    private A2048 game;

    public SingleA2048(A2048 game, EventListener listener) {
        this.game = game;
        this.player = new Player(listener);
        this.printer = new Printer(game.getSize(), new A2048Printer(game));
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
        return printer.toString();
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
