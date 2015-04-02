package com.codenjoy.dojo.rubicscube.model.command;


import com.codenjoy.dojo.rubicscube.model.Command;
import com.codenjoy.dojo.rubicscube.model.Face;
import com.codenjoy.dojo.rubicscube.model.FaceValue;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 7:45
 */
public class D2 implements Command {

    @Override
    public void apply(Map<Face, FaceValue> cube) {
        new D().apply(cube);
        new D().apply(cube);
    }
}
