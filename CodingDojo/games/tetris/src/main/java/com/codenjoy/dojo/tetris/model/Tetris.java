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


import com.codenjoy.dojo.services.Point;
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
    private Levels levels;

    public Tetris(Levels levels, FigureQueue queue, int size) {
        this.levels = levels;
        this.queue = queue;
        this.size = size;
        take();
    }

    public Figure take() {
        return queue.next();
    }

    public void tick() {
        player.getHero().tick();
    }

    @Override
    public void clearScore() {
        levels.clearScore();
        newGame(player);
    }

    @Override
    public List<Type> getFuture() {
        return queue.future();
    }

    @Override
    public Levels getLevels() {
        return levels;
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

    public static void setPlots(Glass glass, List<Plot> plots) {
        Collections.sort(plots, Comparator.comparingInt(Point::getY));
        for (Plot plot : plots) {
            Type type = Type.valueOf(String.valueOf(plot.getColor().ch()));
            Figure figure = new FigureImpl(0, 0, type, "#");
            glass.drop(figure, plot.getX(), plot.getY());
        }
    }
}