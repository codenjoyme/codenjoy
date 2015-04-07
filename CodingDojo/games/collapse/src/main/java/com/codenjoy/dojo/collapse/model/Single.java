package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Settings;

public class Single implements Game { // TODO потести меня
    private final EventListener listener;
    private final Settings settings;

    private Printer printer;
    private Player player;
    private Collapse game;

    public Single(Collapse game, EventListener listener, Settings settings, PrinterFactory factory) {
        this.listener = listener;
        this.settings = settings;
        this.game = game;
        this.player = new Player(listener);
        printer = factory.getPrinter(game.reader(), player);
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
        game.remove(player);
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
