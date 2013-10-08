package com.apofig.command;

import com.apofig.Command;
import com.apofig.Face;
import com.apofig.FaceValue;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 6:15
 */
public class B_ implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        new B2().apply(cube);
        new B().apply(cube);
    }
}
