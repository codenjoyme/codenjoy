package com.codenjoy.dojo.chess.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.model.figures.Korol;
import com.codenjoy.dojo.chess.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import java.util.LinkedList;
import java.util.List;

public class Player extends GamePlayer<Korol, Field>{

    List<Figure> figures = new LinkedList<Figure>();

    private boolean isWhite;

    public Player(EventListener listener) {
        super(listener);
    }


    public void initFigures(Field field) {
        List<Figure> figures = field.getFigures(false);
        isWhite = figures.get(0).hasPlayer();
        if (isWhite) {
            figures = field.getFigures(true);
        }
        for (Figure figure : figures) {
            figure.setPlayer(this);
            figure.init(field);
        }
    }

    public List<Figure> getFigures() {
        return figures;
    }

    @Override
    public Korol getHero() {
        return null; // TODO implement me
    }

    @Override
    public void newHero(Field field) {
        // TODO implement me
    }

    public boolean isAlive() {
        return true; // TODO implement me
    }

}
