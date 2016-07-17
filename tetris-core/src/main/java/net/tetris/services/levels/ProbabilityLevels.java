package net.tetris.services.levels;

import com.codenjoy.dojo.tetris.model.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.PlayerFigures;
import net.tetris.services.ProbabilityFigureTypesLevel;
import net.tetris.services.randomizer.ProbabilityRandomizer;
import net.tetris.services.randomizer.Randomizer;

import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.TOTAL_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityLevels extends Levels {

    public ProbabilityLevels(PlayerFigures queue) {
        super(new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        lastFigureProbability(100),
                        O),
                // + I
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        lastFigureProbability(15),
                        O, I),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        lastFigureProbability(50),
                        O, I),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        lastFigureProbability(100),
                        O, I),

                // + J
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        lastFigureProbability(15),
                        O, I, J),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        lastFigureProbability(50),
                        O, I, J),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        lastFigureProbability(100),
                        O, I, J),

                // + L
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 140),
                        lastFigureProbability(15),
                        O, I, J, L),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 160),
                        lastFigureProbability(50),
                        O, I, J, L),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 180),
                        lastFigureProbability(100),
                        O, I, J, L),

                // + S
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 200),
                        lastFigureProbability(15),
                        O, I, J, L, S),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 220),
                        lastFigureProbability(50),
                        O, I, J, L, S),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 240),
                        lastFigureProbability(100),
                        O, I, J, L, S),

                // + Z
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 260),
                        lastFigureProbability(15),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 280),
                        lastFigureProbability(50),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 300),
                        lastFigureProbability(100),
                        O, I, J, L, S, Z),

                // + T
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 320),
                        lastFigureProbability(15),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 340),
                        lastFigureProbability(50),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 360),
                        lastFigureProbability(100),
                        O, I, J, L, S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, getTotalRemovedLines());
    }

    private static Randomizer lastFigureProbability(int i) {
        return new ProbabilityRandomizer(i);
    }
}
