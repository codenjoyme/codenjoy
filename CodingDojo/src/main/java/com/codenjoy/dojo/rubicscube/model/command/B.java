package com.codenjoy.dojo.rubicscube.model.command;


import com.codenjoy.dojo.rubicscube.model.Command;
import com.codenjoy.dojo.rubicscube.model.Face;
import com.codenjoy.dojo.rubicscube.model.FaceValue;
import com.codenjoy.dojo.rubicscube.model.Line;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:23
 */
public class B implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        Line upLine0 = cube.get(Face.UP).getLine(0);
        Line leftRow0 = cube.get(Face.LEFT).getRow(0);
        Line downLine2 = cube.get(Face.DOWN).getLine(2);
        Line rightRow2 = cube.get(Face.RIGHT).getRow(2);

        cube.get(Face.UP).updateLine(0, rightRow2);
        cube.get(Face.LEFT).updateRow(0, upLine0.invert());
        cube.get(Face.DOWN).updateLine(2, leftRow0);
        cube.get(Face.RIGHT).updateRow(2, downLine2.invert());

        cube.get(Face.BACK).rotateClockwise();
    }
}
