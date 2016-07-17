package com.codenjoy.dojo.tetris.model;

import static com.codenjoy.dojo.tetris.model.Figure.Type.I;
import static com.codenjoy.dojo.tetris.model.Figure.Type.O;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.LINES_REMOVED;

/**
 * User: oleksandr.baglai
 * Date: 11/18/12
 * Time: 6:37 PM
 */
public class MockLevels extends Levels {

    public static final int LINES_REMOVED_FOR_NEXT_LEVEL = 4;

    public MockLevels(PlayerFigures queue) {
        super(new FigureTypesLevel(queue,
                new GlassEvent<>(LINES_REMOVED, LINES_REMOVED_FOR_NEXT_LEVEL),
                        I),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(LINES_REMOVED, LINES_REMOVED_FOR_NEXT_LEVEL),
                        O),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(LINES_REMOVED, LINES_REMOVED_FOR_NEXT_LEVEL),
                        I, O));
    }
}
