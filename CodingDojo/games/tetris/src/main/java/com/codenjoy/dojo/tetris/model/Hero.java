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

    private boolean drop;
    private Figure figure;
    private Levels levels;

    @Override
    public void init(Field field) {
        levels = field.getLevels();
        glass = new GlassImpl(field.size(), field.size(),
                () -> levels.getCurrentLevelNumber() + 1);
        this.field = field;
    }

    @Override
    public void down() {
        drop = true;
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        if (isFirstStep()) {
            return;
        }
        moveHorizontallyIfAccepted(x - 1 < figure.left() ? figure.left() : x - 1);
    }

    @Override
    public void right() {
        if (isFirstStep()) {
            return;
        }
        moveHorizontallyIfAccepted(x + 1 > field.size() - figure.right() ? field.size() - figure.right() : x + 1);
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
        } else if (p.length == 1) {
            act(p[0]);
        } else if (p.length == 2) {
            glass.empty();
        }
    }

    public void act(int times) {
        if (isFirstStep()) {
            return;
        }
        Figure clonedFigure = figure.copy();

        figure.rotate(times);
        if (!glass.accept(figure, x, y)) {
            figure = clonedFigure;
        }
        glass.figureAt(figure, x, y);
    }

    private boolean isFirstStep() {
        return figure == null;
    }
    
    @Override
    public void tick() {
        if (levelCompleted()) {
            return;
        }

        if (processFigure()) {
            takeNewFigure();
        }

        drop = false;
    }

    private boolean processFigure() {
        if (isFirstStep()) {
            return true;
        } else if (!glass.accept(figure, x, y)) {
            glass.empty();
            return true;
        } else if (drop) {
            glass.drop(figure, x, y);
            return true;
        } else if (!glass.accept(figure, x, y - 1)) {
            glass.drop(figure, x, y);
            return true;
        } else {
            y--;
            if (!showCurrentFigure()) {
                glass.empty();
                return true;
            }
            return false;
        }
    }

    protected void takeNewFigure() {
        Figure figure = field.take();
        setFigure(figure);
        showCurrentFigure();
    }

    public boolean levelCompleted() {
        return levels.levelCompleted();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (figure == null) {
            return Elements.NONE;
        }

        return figure.type().getColor();
    }

    public List<Plot> currentFigure() {
        return glass.currentFigure();
    }

    public List<Plot> dropped() {
        return glass.dropped();
    }

    public Glass glass() {
        return glass;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        move(field.size() / 2 - 1 + figure.left(), initialYPosition());
    }

    private int initialYPosition() {
        return (field.size() - 1) + figure.bottom() - 1;
    }

    public boolean showCurrentFigure() {
        boolean accept = glass.accept(figure, x, y);
        if (accept) {
            glass.figureAt(figure, x, y);
        }
        return accept;
    }

    public Type currentFigureType() {
        if (figure == null) {
            return null;
        }
        return figure.type();
    }

    public Point currentFigurePoint() {
        if (figure == null) {
            return null;
        }
        return this.copy();
    }

    public List<Character> future() {
        return field.getFuture()
                .stream()
                .map(f -> f.getColor().ch())
                .collect(toList());
    }

    public int boardSize() {
        return field.size();
    }

    public int level() {
        return levels.getCurrentLevelNumber();
    }

    public GlassEventListener levelsListener() {
        return levels;
    }
}
