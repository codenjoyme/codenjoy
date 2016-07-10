package net.tetris;

import net.tetris.dom.Levels;
import net.tetris.dom.NullGameLevel;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class TestUtils {

    public static Levels emptyLevels() {
        return new Levels(new NullGameLevel());
    }

}
