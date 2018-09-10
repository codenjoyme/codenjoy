package com.codenjoy.dojo.rubicscube.model;

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

import static com.codenjoy.dojo.services.PointImpl.pt;

public class LengthToXY {
    public LengthToXY(int size) {
        this.size = size;
    }

    private int size;

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return pt(length % size + 1, length / size + 1);
    }

    public int getLength(int x, int y) {
        return (size - y - 1)* size + x;
    }

    public int getSize() {
        return size;
    }
}
