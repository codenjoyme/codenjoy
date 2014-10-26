package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.*;

public class SingleRubicsCube implements Game { // TODO потести меня

    private Printer printer;
    private Player player;
    private RubicsCube rubicsCube;

    public SingleRubicsCube(EventListener listener) {
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
        return rubicsCube.isGameOver();
    }

    @Override
    public void newGame() {
        rubicsCube = new RubicsCube(new RandomCommand(new RandomDice()));
        rubicsCube.newGame(player);
        printer = new Printer(rubicsCube.getSize(),
                new Printer.GamePrinterImpl<Elements, Player>(rubicsCube.reader(), player, Elements.NONE.ch()));
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        rubicsCube.remove(player);
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
        rubicsCube.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
