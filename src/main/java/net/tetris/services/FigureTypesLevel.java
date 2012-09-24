package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GameLevel;
import net.tetris.dom.GlassEvent;

public class FigureTypesLevel implements GameLevel {
    private PlayerFigures figuresQueue;
    private GlassEvent event;
    private Figure.Type[] figureTypesToOpen;

    public FigureTypesLevel(PlayerFigures figuresQueue, GlassEvent event, Figure.Type... figureTypesToOpen) {
        this.figuresQueue = figuresQueue;
        this.event = event;
        this.figureTypesToOpen = figureTypesToOpen;
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
}
