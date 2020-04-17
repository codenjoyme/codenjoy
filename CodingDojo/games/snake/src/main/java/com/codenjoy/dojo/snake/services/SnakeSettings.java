package com.codenjoy.dojo.snake.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class SnakeSettings {

    private Parameter<Boolean> maxScoreMode;
    private Parameter<Integer> gameOverPenalty;
    private Parameter<Integer> startSnakeLength;
    private Parameter<Integer> eatStonePenalty;
    private Parameter<Integer> eatStoneDecrease;
    private Parameter<Integer> boardSize;

    public SnakeSettings(Settings settings) {
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(15);
        maxScoreMode = settings.addCheckBox("Max score mode").type(Boolean.class).def(false);
        gameOverPenalty = settings.addEditBox("Game over penalty").type(Integer.class).def(0);
        startSnakeLength = settings.addEditBox("Start snake length").type(Integer.class).def(2);
        eatStonePenalty = settings.addEditBox("Eat stone penalty").type(Integer.class).def(0);
        eatStoneDecrease = settings.addEditBox("Eat stone decrease").type(Integer.class).def(10);
    }

    public Parameter<Integer> eatStoneDecrease() {
        return eatStoneDecrease;
    }

    public Parameter<Integer> startSnakeLength() {
        return startSnakeLength;
    }

    public Parameter<Integer> eatStonePenalty() {
        return eatStonePenalty;
    }

    public Parameter<Integer> gameOverPenalty() {
        return gameOverPenalty;
    }

    public Parameter<Integer> boardSize() {
        return boardSize;
    }

    public Parameter<Boolean> maxScoreMode() {
        return maxScoreMode;
    }
}
