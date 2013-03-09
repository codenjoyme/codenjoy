package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.*;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 7:18 PM
 */
public class BombermanGame implements GameType {

    public static final int BOARD_SIZE = 15;

    @Override
    public PlayerScores getPlayerScores(int minScore) {
        return new BombermanPlayerScores(minScore);
    }

    @Override
    public Game newGame(EventListener listener) {
        return new BombermanBoardArapter(createNewGame(listener));
    }

    BoardEvented createNewGame(EventListener listener) {
        Level level = new Level() {
            @Override
            public int bombsCount() {
                return 2;
            }

            @Override
            public int bombsPower() {
                return 1;
            }
        };

        OriginalWalls walls1 = new OriginalWalls(BOARD_SIZE);
        DestroyWalls walls2 = new DestroyWalls(walls1, BOARD_SIZE, new RandomDice());
        MeatChoppers walls3 = new MeatChoppers(walls2, BOARD_SIZE, 1, new RandomDice());

        return new BoardEvented(walls3, level, BOARD_SIZE, listener);
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
