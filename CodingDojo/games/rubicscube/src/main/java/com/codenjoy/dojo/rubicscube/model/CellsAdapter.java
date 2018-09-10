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

import java.util.LinkedList;
import java.util.List;
import static com.codenjoy.dojo.services.PointImpl.*;

public class CellsAdapter {
    private Cube cube;

    public CellsAdapter(Cube cube) {
        this.cube = cube;
    }

    public List<Cell> getCells() {
        List<Cell> result = new LinkedList<Cell>();

        fillAt(result, Face.LEFT,  0, 6);
        fillAt(result, Face.FRONT, 3, 6);
        fillAt(result, Face.RIGHT, 6, 6);
        fillAt(result, Face.BACK,  9, 6);
        fillAt(result, Face.UP,    3, 9);
        fillAt(result, Face.DOWN,  3, 3);

        return result;
    }

    private void fillAt(List<Cell> result, Face face, int dx, int dy) {
        FaceValue faceValue = cube.face(face);
        for (int x = 0; x < 3; x++) {
            Line row = faceValue.getRow(x);
            for (int y = 0; y < 3; y++) {
                Point pt = pt(x + dx, y + dy);
                result.add(new Cell(pt, row.get(2 - y)));
            }
        }
    }
}
