package com.codenjoy.dojo.tetris.model;

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

import com.codenjoy.dojo.tetris.model.levels.random.Randomizer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class NullFigureQueue implements FigureQueue {

    public static final FigureQueue INSTANCE = new NullFigureQueue();

    @Override
    public Figure next() {
        return null;
    }

    @Override
    public List<Type> future() {
        return Arrays.asList();
    }

    @Override
    public void clear() {
        // do nothing
    }

    @Override
    public void setRandomizer(Supplier<Randomizer> get) {
        // do nothing
    }

    @Override
    public void open(Type[] figureTypesToOpen) {
        // do nothing
    }
}
