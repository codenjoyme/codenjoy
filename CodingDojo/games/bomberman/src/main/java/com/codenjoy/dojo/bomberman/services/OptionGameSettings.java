package com.codenjoy.dojo.bomberman.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class OptionGameSettings implements GameSettings {

    private final Parameter<Integer> bombPower;
    private final Parameter<Integer> bombsCount;
    private final Parameter<Integer> destroyWallCount;
    private final Parameter<Integer> boardSize;
    private final Parameter<Integer> meatChoppersCount;

    private final Parameter<Integer> perkDropRatio;
    private final Parameter<Integer> perkBombBlastRadiusInc;
    private final Parameter<Integer> timeoutBombBlastRadiusInc;

    private final Dice dice;

    public OptionGameSettings(Settings settings, Dice dice) {
        bombsCount = settings.addEditBox("Bombs count").type(Integer.class).def(1);
        bombPower = settings.addEditBox("Bomb power").type(Integer.class).def(DefaultGameSettings.BOMB_POWER);
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(DefaultGameSettings.BOARD_SIZE);
        destroyWallCount = settings.addEditBox("Destroy wall count").type(Integer.class).def(boardSize.getValue() * boardSize.getValue() / 10);
        meatChoppersCount = settings.addEditBox("Meat choppers count").type(Integer.class).def(DefaultGameSettings.MEAT_CHOPPERS_COUNT);
        this.dice = dice;

        // perks. Set value to 0 = perk is disabled.
        perkDropRatio = settings.addEditBox("Perks drop ratio in %").type(Integer.class).def(20); // 20%
        perkBombBlastRadiusInc = settings.addEditBox("Bomb blast radius increase").type(Integer.class).def(20);
        timeoutBombBlastRadiusInc = settings.addEditBox("Bomb blast radius increase effect timeout").type(Integer.class).def(5);
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

            @Override
            public int perksDropRate() {
                return perkDropRatio.getValue();
            }
        };
    }

    @Override
    public Walls getWalls(Bomberman board) {
        OriginalWalls originalWalls = new OriginalWalls(boardSize);
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, board, meatChoppersCount, dice);

        EatSpaceWalls eatWalls = new EatSpaceWalls(meatChoppers, board, destroyWallCount, dice);
        return eatWalls;
    }

    @Override
    public Hero getBomberman(Level level) {
        PerksSettingsWrapper.clear();
        PerksSettingsWrapper.setDropRatio(perkDropRatio.getValue());

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE,
                perkBombBlastRadiusInc.getValue(), timeoutBombBlastRadiusInc.getValue());

        return new Hero(level, dice);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return boardSize;
    }


}
