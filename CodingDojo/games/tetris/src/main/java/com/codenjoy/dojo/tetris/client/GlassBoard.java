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

package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.model.Elements;

import java.util.List;

public class GlassBoard extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) {
        return size() - 1 - y;
    }

    public boolean isFree(int x, int y) {
        return isAt(x, y, Elements.NONE);
    }

    public List<Point> getFigures() {
        return get(
                Elements.BLUE,
                Elements.CYAN,
                Elements.ORANGE,
                Elements.YELLOW,
                Elements.GREEN,
                Elements.PURPLE,
                Elements.RED
        );
    }

    public List<Point> getFreeSpace() {
        return get(Elements.NONE);
    }
}
