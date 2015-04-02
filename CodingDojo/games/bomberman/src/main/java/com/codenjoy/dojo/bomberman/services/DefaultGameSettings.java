package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:41 PM
 */
public class DefaultGameSettings implements GameSettings {

    public static final int MEAT_CHOPPERS_COUNT = 10;
    public static final int BOMB_POWER = 3;
    public static final int BOMBS_COUNT = 1;
    public static final int BOARD_SIZE = 33;
    public static final int DESTROY_WALL_COUNT = BOARD_SIZE * BOARD_SIZE / 10;

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
    public Walls getWalls(Bomberman board) {
        OriginalWalls originalWalls = new OriginalWalls(v(BOARD_SIZE));
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, board, v(MEAT_CHOPPERS_COUNT), new RandomDice());
        EatSpaceWalls eatWalls = new EatSpaceWalls(meatChoppers, board, v(DESTROY_WALL_COUNT), new RandomDice());
        return eatWalls;
    }

    @Override
    public Hero getBomberman(Level level) {
        return new HeroImpl(level, new RandomDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(BOARD_SIZE);
    }
}
