package com.codenjoy.dojo.rubicscube.model.command;

import com.codenjoy.dojo.rubicscube.model.Command;
import com.codenjoy.dojo.rubicscube.model.Face;
import com.codenjoy.dojo.rubicscube.model.FaceValue;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 6:11
 */
public class F2 implements Command {
    @Override
    public void apply(Map<Face, FaceValue> cube) {
        new F().apply(cube);
        new F().apply(cube);
    }
}
