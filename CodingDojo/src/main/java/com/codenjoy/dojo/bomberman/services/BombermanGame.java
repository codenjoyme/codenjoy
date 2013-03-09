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
    public static final int MEAT_CHOPPERS_COUNT = 4;
    public static final int BOMB_POWER = 1;
    public static final int BOMBS_COUNT = 1;

    @Override
    public PlayerScores getPlayerScores(int minScore) {
        return new BombermanPlayerScores(minScore);
    }

    @Override
    public Game newGame(EventListener listener) {
        return createNewGame(listener);
    }

    BombermanBoardArapter createNewGame(EventListener listener) {
        Level level = new Level() {
            @Override
            public int bombsCount() {
                return BOMBS_COUNT;
            }

            @Override
            public int bombsPower() {
                return BOMB_POWER;
            }
        };

        OriginalWalls walls1 = new OriginalWalls(BOARD_SIZE);
        DestroyWalls walls2 = new DestroyWalls(walls1, BOARD_SIZE, new RandomDice());
        MeatChoppers walls3 = new MeatChoppers(walls2, BOARD_SIZE, MEAT_CHOPPERS_COUNT, new RandomDice());

        BoardEvented board = new BoardEvented(walls3, level, BOARD_SIZE, listener);

        return new BombermanBoardArapter(board);
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
