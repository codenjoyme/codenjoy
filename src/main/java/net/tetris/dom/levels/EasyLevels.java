package net.tetris.dom.levels;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.FigureTypesLevel;

import static net.tetris.dom.Figure.Type.*;
import static net.tetris.dom.GlassEvent.Type.TOTAL_LINES_REMOVED;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class EasyLevels extends Levels {

    public EasyLevels() {
        super(new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        I),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 10),
                        O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 30),
                        J),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        L),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 50),
                        J, L),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        I, O, J, L),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 70),
                        S),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 90),
                        S, Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 110),
                        S, Z, T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        I, O, J, L,
                        S, Z, T));
    }
}
