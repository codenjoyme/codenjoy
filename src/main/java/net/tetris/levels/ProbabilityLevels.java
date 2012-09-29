package net.tetris.levels;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.FigureTypesLevel;
import net.tetris.services.randomizer.ProbabilityRandomizer;
import net.tetris.services.randomizer.Randomizer;

import static net.tetris.dom.Figure.Type.*;
import static net.tetris.dom.GlassEvent.Type.TOTAL_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityLevels extends Levels {

    public ProbabilityLevels() {
        super(new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        I),
                // + O
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        lastFigureProbability(15),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        lastFigureProbability(50),
                        I, O),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        lastFigureProbability(100),
                        I, O),

                // + J
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        lastFigureProbability(15),
                        I, O, J),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        lastFigureProbability(50),
                        I, O, J),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        lastFigureProbability(100),
                        I, O, J),

                // + L
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 140),
                        lastFigureProbability(15),
                        I, O, J, L),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 160),
                        lastFigureProbability(50),
                        I, O, J, L),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 180),
                        lastFigureProbability(100),
                        I, O, J, L),

                // + S
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 200),
                        lastFigureProbability(15),
                        I, O, J, L, S),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 220),
                        lastFigureProbability(50),
                        I, O, J, L, S),


                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 240),
                        lastFigureProbability(100),
                        I, O, J, L, S),

                // + Z
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 260),
                        lastFigureProbability(15),
                        I, O, J, L, S, Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 280),
                        lastFigureProbability(50),
                        I, O, J, L, S, Z),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 300),
                        lastFigureProbability(100),
                        I, O, J, L, S, Z),

                // + T
                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 320),
                        lastFigureProbability(15),
                        I, O, J, L, S, Z, T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 340),
                        lastFigureProbability(50),
                        I, O, J, L, S, Z, T),

                new FigureTypesLevel(new GlassEvent<>(TOTAL_LINES_REMOVED, 360),
                        lastFigureProbability(100),
                        I, O, J, L, S, Z, T));
    }

    private static Randomizer lastFigureProbability(int i) {
        return new ProbabilityRandomizer(i);
    }
}
