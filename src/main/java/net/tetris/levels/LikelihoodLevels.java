package net.tetris.levels;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.FigureTypesLevel;
import net.tetris.services.randomizer.LikelihoodRandomizer;
import net.tetris.services.PlayerFigures;
import net.tetris.services.randomizer.Randomizer;

import static net.tetris.dom.Figure.Type.*;
import static net.tetris.dom.GlassEvent.Type.TOTAL_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class LikelihoodLevels extends Levels {

    public LikelihoodLevels() {
        super(new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        I),
                // + O
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        lastFigureLikelihood(15),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        lastFigureLikelihood(50),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        lastFigureLikelihood(100),
                        I, O),

                // + J
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        lastFigureLikelihood(15),
                        I, O, J),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        lastFigureLikelihood(50),
                        I, O, J),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        lastFigureLikelihood(100),
                        I, O, J),

                // + L
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 140),
                        lastFigureLikelihood(15),
                        I, O, J, L),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 160),
                        lastFigureLikelihood(50),
                        I, O, J, L),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 180),
                        lastFigureLikelihood(100),
                        I, O, J, L),

                // + S
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 200),
                        lastFigureLikelihood(15),
                        I, O, J, L, S),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 220),
                        lastFigureLikelihood(50),
                        I, O, J, L, S),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 240),
                        lastFigureLikelihood(100),
                        I, O, J, L, S),

                // + Z
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 260),
                        lastFigureLikelihood(15),
                        I, O, J, L, S, Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 280),
                        lastFigureLikelihood(50),
                        I, O, J, L, S, Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 300),
                        lastFigureLikelihood(100),
                        I, O, J, L, S, Z),

                // + T
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 320),
                        lastFigureLikelihood(15),
                        I, O, J, L, S, Z, T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 340),
                        lastFigureLikelihood(50),
                        I, O, J, L, S, Z, T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 360),
                        lastFigureLikelihood(100),
                        I, O, J, L, S, Z, T));
    }

    private static Randomizer lastFigureLikelihood(int i) {
        return new LikelihoodRandomizer(i);
    }
}
