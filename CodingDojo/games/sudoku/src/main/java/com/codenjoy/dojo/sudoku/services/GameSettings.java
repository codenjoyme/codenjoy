package com.codenjoy.dojo.sudoku.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.model.level.Level;
import com.codenjoy.dojo.sudoku.model.level.Levels;

public final class GameSettings extends SettingsImpl {

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> failPenalty;
    private final Parameter<Integer> successScore;
    private final Parameter<Integer> loosePenalty;
    private final Parameter<Integer> levelsCount;

    public GameSettings() {
        winScore = addEditBox("Win score").type(Integer.class).def(1000);
        failPenalty = addEditBox("Fail penalty").type(Integer.class).def(10);
        loosePenalty = addEditBox("Loose penalty").type(Integer.class).def(500);
        successScore = addEditBox("Success score").type(Integer.class).def(10);

        levelsCount = addEditBox("Levels count").type(Integer.class).def(0);
        Levels.setup(this);
    }

    public int levelsCount() {
        return levelsCount.getValue();
    }

    public GameSettings addLevel(int index, Level level) {
        levelsCount.update(index);

        String prefix = levelPrefix(index);
        addEditBox(prefix).multiline().type(String.class).def(level.all());
        return this;
    }

    public String levelMap(int index) {
        String prefix = levelPrefix(index);
        return addEditBox(prefix).type(String.class).getValue();
    }

    private String levelPrefix(int index) {
        return "Level" + index + "";
    }

    public int winScore() {
        return winScore.getValue();
    }

    public int failPenalty() {
        return failPenalty.getValue();
    }

    public int successScore() {
        return successScore.getValue();
    }

    public int loosePenalty() {
        return loosePenalty.getValue();
    }

    // setters for testing

    public GameSettings levelsCount(int value) {
        levelsCount.update(value);
        return this;
    }
}
