package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:41 PM
 */
public class DefaultGameSettings implements GameSettings {

    public static final int MEAT_CHOPPERS_COUNT = 4;
    public static final int BOMB_POWER = 1;
    public static final int BOMBS_COUNT = 1;
    public static final int BOARD_SIZE = 15;

    @Override
    public Level getLevel() {
        return new Level() {
            @Override
            public int bombsCount() {
                return BOMBS_COUNT;
            }

            @Override
            public int bombsPower() {
                return BOMB_POWER;
            }
        };
    }

    @Override
    public Walls getWalls() {
        OriginalWalls originalWalls = new OriginalWalls(BOARD_SIZE);
        DestroyWalls destroyWalls = new DestroyWalls(originalWalls, BOARD_SIZE, new RandomDice());
        MeatChoppers meatChoppers = new MeatChoppers(destroyWalls, BOARD_SIZE, MEAT_CHOPPERS_COUNT, new RandomDice());
        return meatChoppers;
    }

    @Override
    public Bomberman getBomberman(Level level) {
        return new MyBomberman(level);
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
