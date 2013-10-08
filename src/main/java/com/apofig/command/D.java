package com.apofig.command;

import com.apofig.Command;
import com.apofig.Face;
import com.apofig.FaceValue;
import com.apofig.Line;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:23
 */
public class D implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        Line frontLine0 = cube.get(Face.FRONT).getLine(2);
        Line rightLine0 = cube.get(Face.RIGHT).getLine(2);
        Line backLine0 = cube.get(Face.BACK).getLine(2);
        Line leftLine0 = cube.get(Face.LEFT).getLine(2);

        cube.get(Face.FRONT).updateLine(2, leftLine0);
        cube.get(Face.RIGHT).updateLine(2, frontLine0);
        cube.get(Face.BACK).updateLine(2, rightLine0);
        cube.get(Face.LEFT).updateLine(2, backLine0);

        cube.get(Face.DOWN).rotateClockwise();
    }
}
