package com.codenjoy.dojo.snake.model.artifacts;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Hero;

public class EateablePoint extends PointImpl implements Affectable {

    private Runnable doItOnEat;

    public EateablePoint(Point pt) {
        super(pt);
    }

    public EateablePoint(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Hero snake) {
        if (doItOnEat != null) {
            doItOnEat.run();
        }
    }

    public void onEat(Runnable runnable) {
        this.doItOnEat = runnable;
    }
}
