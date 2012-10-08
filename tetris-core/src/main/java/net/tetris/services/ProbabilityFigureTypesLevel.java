package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GlassEvent;
import net.tetris.services.randomizer.Randomizer;
import net.tetris.services.randomizer.RandomizerFetcher;

public class ProbabilityFigureTypesLevel extends FigureTypesLevel {
    private Randomizer randomizer;

    public ProbabilityFigureTypesLevel(PlayerFigures figuresQueue,
                                       GlassEvent event,
                                       Randomizer randomizer,
                                       Figure.Type... figureTypesToOpen)
    {
        super(figuresQueue, event, figureTypesToOpen);
        this.randomizer = randomizer;

        figuresQueue.setRandomizerFetcher(new RandomizerFetcher() {
            @Override
            public Randomizer get() {
                return ProbabilityFigureTypesLevel.this.randomizer;
            }
        });
    }
}
