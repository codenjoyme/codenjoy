package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private Glass glass;

    private boolean dropRequested;
    private Figure figure;

    @Override
    public void init(Field field) {
        glass = new GlassImpl(field.size(), field.size());
        this.field = field;
    }

    @Override
    public void down() {
        dropRequested = true;
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        moveHorizontallyIfAccepted(x - 1 < figure.getLeft() ? figure.getLeft() : x - 1);
    }

    @Override
    public void right() {
        moveHorizontallyIfAccepted(x + 1 > field.size() - figure.getRight() ? field.size() - figure.getRight() : x + 1);
    }

    protected void moveHorizontallyIfAccepted(int tmpX) {
        if (glass.accept(figure, tmpX, y)) {
            x = tmpX;
        }
    }

    @Override
    public void act(int... p) {
        if (p.length == 0) {
            act(1);
        } else {
            act(p[0]);
        }
    }

    public void act(int times) {
        Figure clonedFigure = figure.getCopy();

        figure.rotate(times);
        if (!glass.accept(figure, x, y)) {
            figure = clonedFigure;
        }
        glass.figureAt(figure, x, y);
    }

    private boolean theFirstStep() {
        return figure == null;
    }
    
    @Override
    public void tick() {
        if (theFirstStep()) {
            Figure figure = field.takeFigure();
            setFigure(figure);
            showCurrentFigure();
            return;
        }

        if (!glass.accept(figure, x, y)) {
            glass.empty();
            figure = null;
            tick();
            return;
        }

        if (dropRequested) {
            dropRequested = false;
            glass.drop(figure, x, y);
            figure = null;
            tick();
            return;
        }
        if (!glass.accept(figure, x, y - 1)) {
            glass.drop(figure, x, y);
            figure = null;
            tick();
            return;
        }
        y--;
        showCurrentFigure();
    }

    public boolean isAlive() {
        return true;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (figure == null) {
            return Elements.NONE;
        }

        return figure.getType().getColor();
    }

    public List<Plot> getCurrentFigurePlots() {
        return glass.getCurrentFigurePlots();
    }

    public List<Plot> getDroppedPlots() {
        return glass.getDroppedPlots();
    }

    public Glass getGlass() {
        return glass;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        x = field.size() / 2 - 1;
        y = initialYPosition();
    }

    private int initialYPosition() {
        return field.size() - figure.getTop();
    }

    public void showCurrentFigure() {
        glass.figureAt(figure, x, y);
    }

    public Type getCurrentFigureType() {
        if (figure == null) {
            return null;
        }
        return figure.getType();
    }

    public Point getCurrentFigurePoint() {
        if (figure == null) {
            return null;
        }
        return this.copy();
    }

    public List<Character> getFutureFigures() {
        return field.getFutureFigures()
                .stream()
                .map(f -> f.getColor().ch())
                .collect(toList());
    }

    public int boardSize() {
        return field.size();
    }
}
