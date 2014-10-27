package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.*;

public class SingleSudoku implements Game { // TODO потести меня

    private Printer printer;
    private Player player;
    private Sudoku game;

    public SingleSudoku(EventListener listener) {
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
        LevelBuilder builder = new LevelBuilder(40, new RandomDice());
        builder.build();
        Level level = new LevelImpl(builder.getBoard(), builder.getMask());
        game = new Sudoku(level);
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
