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
public class U_ implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        new U2().apply(cube);
        new U().apply(cube);
    }
}
