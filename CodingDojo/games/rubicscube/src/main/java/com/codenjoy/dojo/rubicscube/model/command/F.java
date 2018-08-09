package com.codenjoy.dojo.rubicscube.model.command;

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


import com.codenjoy.dojo.rubicscube.model.Command;
import com.codenjoy.dojo.rubicscube.model.Face;
import com.codenjoy.dojo.rubicscube.model.FaceValue;
import com.codenjoy.dojo.rubicscube.model.Line;

import java.util.Map;

public class F implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        Line upLine2 = cube.get(Face.UP).getLine(2);
        Line downLine0 = cube.get(Face.DOWN).getLine(0);
        Line leftRow2 = cube.get(Face.LEFT).getRow(2);
        Line rightRow0 = cube.get(Face.RIGHT).getRow(0);

        cube.get(Face.UP).updateLine(2, leftRow2.invert());
        cube.get(Face.LEFT).updateRow(2, downLine0);
        cube.get(Face.DOWN).updateLine(0, rightRow0.invert());
        cube.get(Face.RIGHT).updateRow(0, upLine2);

        cube.get(Face.FRONT).rotateClockwise();
    }
}
