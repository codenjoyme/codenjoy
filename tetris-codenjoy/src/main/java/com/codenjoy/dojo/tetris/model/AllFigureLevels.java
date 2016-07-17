package com.codenjoy.dojo.tetris.model;


import static com.codenjoy.dojo.tetris.model.Figure.Type.*;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class AllFigureLevels extends Levels {

    public AllFigureLevels(PlayerFigures queue) {
        super(new FigureTypesLevel(queue,
                new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                O, I, J, L, S, Z, T));
    }
}
