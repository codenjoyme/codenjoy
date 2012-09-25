package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.GameLevel;
import net.tetris.dom.GlassEvent;
import net.tetris.services.randomizer.EquiprobableRandomizer;
import net.tetris.services.randomizer.Randomizer;

public class FigureTypesLevel implements GameLevel {
    private PlayerFigures figuresQueue;
    private GlassEvent event;
    private Figure.Type[] figureTypesToOpen;

    public FigureTypesLevel(GlassEvent event, Figure.Type... figureTypesToOpen) {
        this(event, new EquiprobableRandomizer(), figureTypesToOpen);
    }

    public FigureTypesLevel(GlassEvent event, Randomizer randomizer, Figure.Type... figureTypesToOpen) {
        this.event = event;
        this.figureTypesToOpen = figureTypesToOpen;
        figuresQueue = new PlayerFigures(randomizer);
        this.figuresQueue.setRandomizer(randomizer);
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
        return event.getNextLevelIngoingCriteria();
    }

    @Override
    public FigureQueue getFigureQueue() {
        return figuresQueue;
    }
}
