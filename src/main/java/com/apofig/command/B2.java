package com.apofig.command;

import com.apofig.Command;
import com.apofig.Face;
import com.apofig.FaceValue;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 7:45
 */
public class B2 implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        new B().apply(cube);
        new B().apply(cube);
    }
}
