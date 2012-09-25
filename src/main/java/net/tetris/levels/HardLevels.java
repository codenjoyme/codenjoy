package net.tetris.levels;

import static net.tetris.dom.Figure.Type.*;
import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.FigureTypesLevel;
import net.tetris.services.PlayerFigures;
import static net.tetris.dom.GlassEvent.Type.LINES_REMOVED;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class HardLevels extends Levels {

    public HardLevels () {
        super(new FigureTypesLevel(new GlassEvent<>(LINES_REMOVED, 4),
                        I),

                new FigureTypesLevel(new GlassEvent<>(LINES_REMOVED, 4),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(LINES_REMOVED, 4),
                        I, O, J, L),

                new FigureTypesLevel(new GlassEvent<>(LINES_REMOVED, 4),
                        I, O, J, L,
                        S, Z, T));
    }
}
