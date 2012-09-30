package net.tetris.services;

import net.tetris.dom.*;
import net.tetris.services.randomizer.EquiprobableRandomizer;
import net.tetris.services.randomizer.Randomizer;
import net.tetris.services.randomizer.RandomizerFetcher;

public class FigureTypesLevel implements GameLevel {
    private PlayerFigures figuresQueue;
    private GlassEvent event;
    private Figure.Type[] figureTypesToOpen;
    private Randomizer randomizer;

    public FigureTypesLevel(GlassEvent event, Figure.Type... figureTypesToOpen) {
        this(event, new EquiprobableRandomizer(), figureTypesToOpen);
    }

    public FigureTypesLevel(GlassEvent event, Randomizer randomizer, Figure.Type... figureTypesToOpen) {
        this.event = event;
        this.figureTypesToOpen = figureTypesToOpen;
        figuresQueue = new PlayerFigures();

        this.randomizer = randomizer;
        this.figuresQueue.setRandomizerFetcher(new RandomizerFetcher() {
            @Override
            public Randomizer get() {
                return FigureTypesLevel.this.randomizer;
            }
        });
    }

    @Override
    public boolean accept(GlassEvent event) {
        return this.event.equals(event);
    }

    @Override
    public void apply() {
        figuresQueue.openFigures(figureTypesToOpen);
    }

    @Override
    public String getNextLevelIngoingCriteria() {
        switch (event.getType()) {
            case LINES_REMOVED : return String.format("Remove %s lines together", event.getValue());
            case TOTAL_LINES_REMOVED : return String.format("Total removes %s lines", event.getValue());
        }
        return NullGameLevel.THIS_IS_LAST_LEVEL;
    }

    @Override
    public FigureQueue getFigureQueue() {
        return figuresQueue;
    }

    @Override
    public int getFigureTypesToOpenCount() {
        return figureTypesToOpen.length;
    }
}
