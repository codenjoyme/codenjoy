package com.apofig;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:41
 */
public class RubicsCube {

    private Map<Face, FaceValue> cube = new HashMap<Face, FaceValue>();

    public RubicsCube() {
        cube.put(Face.BACK, new FaceValue(Color.RED));
        cube.put(Face.DOWN, new FaceValue(Color.YELLOW));
        cube.put(Face.UP, new FaceValue(Color.WHITE));
        cube.put(Face.RIGHT, new FaceValue(Color.BLUE));
        cube.put(Face.LEFT, new FaceValue(Color.GREEN));
        cube.put(Face.FRONT, new FaceValue(Color.ORANGE));
    }

    public String getFace(Face name) {
        return cube.get(name).toString();
    }
}
