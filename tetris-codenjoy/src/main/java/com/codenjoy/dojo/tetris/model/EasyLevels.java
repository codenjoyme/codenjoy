package com.codenjoy.dojo.tetris.model;

import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.TOTAL_LINES_REMOVED;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class EasyLevels extends Levels {

    public EasyLevels(PlayerFigures queue) {
        super(new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        O),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 10),
                        I),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        O, I),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 30),
                        J),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        L),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 50),
                        J, L),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        O, I, J, L),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 70),
                        S),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        Z),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 90),
                        S, Z),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        T),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 110),
                        S, Z, T),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        O, I, J, L,
                        S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, getTotalRemovedLines());
    }
}
