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
public class L implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        Line upRow0 = cube.get(Face.UP).getRow(0);
        Line frontRow0 = cube.get(Face.FRONT).getRow(0);
        Line downRow0 = cube.get(Face.DOWN).getRow(0);
        Line backRow2 = cube.get(Face.BACK).getRow(2);

        cube.get(Face.UP).updateRow(0, backRow2.invert());
        cube.get(Face.FRONT).updateRow(0, upRow0);
        cube.get(Face.DOWN).updateRow(0, frontRow0);
        cube.get(Face.BACK).updateRow(2, downRow0.invert());

        cube.get(Face.LEFT).rotateClockwise();
    }
}
