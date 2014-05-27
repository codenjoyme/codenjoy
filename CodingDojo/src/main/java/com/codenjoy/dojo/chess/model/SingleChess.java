package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.model.figures.Korol;
import com.codenjoy.dojo.services.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class SingleChess implements Game {

    private Printer printer;
    private Player player;
    private Chess game;

    public SingleChess(Chess sample, EventListener listener) {
        this.game = sample;
        this.player = new Player(listener);
        this.printer = new Printer(sample.getSize(), new ChessPrinter(sample, player));
    }

    @Override
    public Joystick getJoystick() {
        return player;
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
        game.newGame(player);
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
        for (Figure figure : player.getFigures()) {
            if (figure instanceof Korol) {
                return figure;
            }
        }
        return pt(-1, -1);
    }

    @Override
    public void tick() {
        game.tick();
    }

    public Player getPlayer() {
        return player;
    }

}
