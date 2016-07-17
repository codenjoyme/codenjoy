package com.codenjoy.dojo.tetris.model;

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
