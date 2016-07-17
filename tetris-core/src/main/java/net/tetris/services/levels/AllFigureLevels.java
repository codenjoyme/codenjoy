package net.tetris.services.levels;

import com.codenjoy.dojo.tetris.model.GlassEvent;
import net.tetris.dom.Levels;
import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import net.tetris.services.FigureTypesLevel;
import net.tetris.services.PlayerFigures;

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
