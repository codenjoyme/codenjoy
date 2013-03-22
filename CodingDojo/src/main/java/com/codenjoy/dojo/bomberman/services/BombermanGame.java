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
        Board board = new Board(settings, listener);
        board.newGame();
        return board;
    }

    @Override
    public int getBoardSize() {
        return settings.getBoardSize();
    }
}
