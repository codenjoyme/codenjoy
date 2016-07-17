package net.tetris;

import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.NullGameLevel;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class TestUtils {

    public static Levels emptyLevels() {
        return new Levels(new NullGameLevel());
    }

}
