package net.tetris.dom.levels;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import static net.tetris.dom.Figure.Type.*;
import net.tetris.services.FigureTypesLevel;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class AllFigureLevels extends Levels {

    public AllFigureLevels() {
        super(new FigureTypesLevel(new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4),
                I, O, J, L, S, Z, T));
    }
}
