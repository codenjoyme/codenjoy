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


import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class Tetris implements Field {

    protected FigureQueue queue;
    private int size;
    private Player player;

    public Tetris(FigureQueue queue, int size) {
        this.queue = queue;
        this.size = size;
        takeFigure();
    }

    public Figure takeFigure() {
        return queue.next();
    }

    public void tick() {
        player.getHero().tick();
    }

    @Override
    public List<Type> getFutureFigures() {
        return queue.getFutureFigures();
    }

    @Override
    public BoardReader reader() {
        return null; // do nothing because board has JSON format
    }

    @Override
    public void newGame(Player player) {
        this.player = player;
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    @Override
    public int size() {
        return size;
    }

    public Player getPlayer() {
        return player;
    }

    void setPlots(List<Plot> plots) {
        Glass glass = player.getHero().getGlass();
        Collections.sort(plots, Comparator.comparingInt(PointImpl::getY));
        for (Plot plot : plots) {
            Type type = Type.valueOf(String.valueOf(plot.getColor().ch()));
            Figure figure = new FigureImpl(0, 0, type, "#");
            glass.drop(figure, plot.getX(), plot.getY());
        }
    }
}