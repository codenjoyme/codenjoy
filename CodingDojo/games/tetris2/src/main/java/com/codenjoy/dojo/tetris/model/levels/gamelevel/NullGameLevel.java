package com.codenjoy.dojo.tetris.model.levels.gamelevel;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.GameLevel;
import com.codenjoy.dojo.tetris.model.GlassEvent;

public class NullGameLevel implements GameLevel {

    public static final String THIS_IS_LAST_LEVEL = "This is last level";
    public static final boolean NO_ACCEPT = false;

    @Override
    public boolean accept(GlassEvent event) {
        return NO_ACCEPT;
    }

    @Override
    public void apply() {
    }

    @Override
    public String nextLevelCriteria() {
        return THIS_IS_LAST_LEVEL;
    }

    @Override
    public FigureQueue queue() {
        return null;
    }

    @Override
    public int openCount() {
        return 0;
    }
}
