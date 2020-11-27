package com.codenjoy.dojo.tetris.model.levels.gamelevel;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.*;
import com.codenjoy.dojo.tetris.model.levels.random.EquiprobableRandomizer;

public class FigureTypesLevel implements GameLevel {

    protected FigureQueue figuresQueue;
    private GlassEvent event;
    private Type[] figureTypesToOpen;

    public FigureTypesLevel(Dice dice, FigureQueue figuresQueue, GlassEvent event, Type... figureTypesToOpen) {
        this.figuresQueue = figuresQueue;
        this.event = event;
        this.figureTypesToOpen = figureTypesToOpen;

        figuresQueue.setRandomizer(() -> new EquiprobableRandomizer(dice));
    }

    @Override
    public boolean accept(GlassEvent event) {
        return this.event.equals(event);
    }

    @Override
    public void apply() {
        figuresQueue.open(figureTypesToOpen);
    }

    @Override
    public String nextLevelCriteria() {
        switch (event.getType()) {
            case LINES_REMOVED : return String.format("Remove %s lines together", event.getValue());
            case TOTAL_LINES_REMOVED : return String.format("Remove %s lines", event.getValue());
            case WITHOUT_OVERFLOWN_LINES_REMOVED : return String.format("Remove %s lines without overflown", event.getValue());
        }
        return NullGameLevel.THIS_IS_LAST_LEVEL;
    }

    @Override
    public FigureQueue queue() {
        return figuresQueue;
    }

    @Override
    public int openCount() {
        return figureTypesToOpen.length;
    }
}
