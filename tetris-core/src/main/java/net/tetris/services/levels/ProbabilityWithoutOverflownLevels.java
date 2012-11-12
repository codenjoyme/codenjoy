package net.tetris.services.levels;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.PlayerFigures;
import net.tetris.services.ProbabilityFigureTypesLevel;
import net.tetris.services.randomizer.ProbabilityRandomizer;
import net.tetris.services.randomizer.Randomizer;

import static net.tetris.dom.Figure.Type.*;
import static net.tetris.dom.GlassEvent.Type.WITHOUT_OVERFLOWN_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityWithoutOverflownLevels extends Levels {

    public static final int LINES_REMOVED_FOR_NEXT_LEVEL = 20;
    private static final GlassEvent.Type сriteria = WITHOUT_OVERFLOWN_LINES_REMOVED;

    public ProbabilityWithoutOverflownLevels(PlayerFigures queue) {
        super(new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, 0),
                        lastFigureProbability(100),
                        I),
                // + O
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O),

                // + J
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O, J),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O, J),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O, J),

                // + L
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O, J, L),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O, J, L),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O, J, L),

                // + S
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O, J, L, S),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O, J, L, S),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O, J, L, S),

                // + Z
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O, J, L, S, Z),

                // + T
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(15),
                        I, O, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(50),
                        I, O, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(сriteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(100),
                        I, O, J, L, S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.WITHOUT_OVERFLOWN_LINES_REMOVED,
                getRemovedLinesWithoutOverflown());
    }

    private static Randomizer lastFigureProbability(int i) {
        return new ProbabilityRandomizer(i);
    }
}
