package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Settings;

public class SingleCollapse implements Game { // TODO потести меня
    private final EventListener listener;
    private final Settings settings;

    private Printer printer;
    private Player player;
    private Collapse game;

    public SingleCollapse(EventListener listener, Settings settings) {
        this.listener = listener;
        this.settings = settings;
        this.player = new Player(listener);
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
        Integer size = settings.getParameter("Field size").type(Integer.class).getValue();
        LevelBuilder builder = new LevelBuilder(new RandomDice(), size);
        Level level = new LevelImpl(builder.getBoard());
        game = new Collapse(level);
        game.newGame(player);
        printer = Printer.getSimpleFor(game.reader(), player, Elements.NONE);
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
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
