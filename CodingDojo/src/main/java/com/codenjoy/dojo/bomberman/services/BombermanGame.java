package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.*;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:18 PM
 */
public class BombermanGame implements GameType {

    private DefaultGameSettings settings;

    public BombermanGame() {
        settings = new DefaultGameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new BombermanPlayerScores(score);
    }

    @Override
    public Game newGame(EventListener listener) {
        Game game = new SingleBoard(new Board(settings, listener));
        game.newGame();
        return game;
    }

    @Override
    public int getBoardSize() {
        return settings.getBoardSize();
    }

    @Override
    public String gameName() {
        return "bomberman";
    }

    @Override
    public Object[] getPlots() {
        return PlotColor.values();
    }
}
