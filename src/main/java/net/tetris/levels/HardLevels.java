package net.tetris.levels;

import static net.tetris.dom.Figure.Type.*;
import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.FigureTypesLevel;
import net.tetris.services.PlayerFigures;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class HardLevels extends Levels {

    public HardLevels (PlayerFigures queue) {
        super(new FigureTypesLevel(queue,
                        new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                        I),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                        I, O),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                        I, O, J, L),

                new FigureTypesLevel(queue,
                        new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                        I, O, J, L,
                        S, Z, T));
    }
}
