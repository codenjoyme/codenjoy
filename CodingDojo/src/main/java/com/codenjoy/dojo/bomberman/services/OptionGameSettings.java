package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:38
 */
public class OptionGameSettings implements GameSettings {

    private final Parameter<Integer> bombPower;
    private final Parameter<Integer> bombsCount;
    private final Parameter<Integer> destroyWallCount;
    private final Parameter<Integer> boardSize;
    private final Parameter<Integer> meatChoppersCount;

    public OptionGameSettings(Settings settings) {
        bombsCount = settings.addEditBox("Bombs count").type(Integer.class).def(1);
        bombPower = settings.addEditBox("Bomb power").type(Integer.class).def(DefaultGameSettings.BOMB_POWER);
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(DefaultGameSettings.BOARD_SIZE);
        destroyWallCount = settings.addEditBox("Destroy wall count").type(Integer.class).def(boardSize.getValue()*boardSize.getValue()/10);
        meatChoppersCount = settings.addEditBox("Meat choppers count").type(Integer.class).def(DefaultGameSettings.MEAT_CHOPPERS_COUNT);
    }

    @Override
    public Level getLevel() {
        return new Level() {
            @Override
            public int bombsCount() {
                return bombsCount.getValue();
            }

            @Override
            public int bombsPower() {
                return bombPower.getValue();
            }
        };
    }

    @Override
    public Walls getWalls(Board board) {
        OriginalWalls originalWalls = new OriginalWalls(boardSize);
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, board, meatChoppersCount, new RandomDice());

        EatSpaceWalls eatWalls = new EatSpaceWalls(meatChoppers, board, destroyWallCount, new RandomDice());
        return eatWalls;
    }

    @Override
    public Bomberman getBomberman(Level level) {
        return new MyBomberman(level, new RandomDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }
}
