package com.codenjoy.dojo.services.nullobj;

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


import com.codenjoy.dojo.services.PlayerScores;

public final class NullPlayerScores implements PlayerScores {

    public static final PlayerScores INSTANCE = new NullPlayerScores();

    private NullPlayerScores() {
        // do nothing
    }

    @Override
    public Integer getScore() {
        return 0;
    }

    @Override
    public int clear() {
        return 0;
    }

    @Override
    public void event(Object event) {
        // do nothing
    }

    @Override
    public void update(Object score) {
        // do nothing
    }
}
