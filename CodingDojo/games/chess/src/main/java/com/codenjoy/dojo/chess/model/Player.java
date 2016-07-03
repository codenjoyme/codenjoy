package com.codenjoy.dojo.chess.model;

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


import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;

import java.util.LinkedList;
import java.util.List;

public class Player implements Joystick {

    private EventListener listener;
    private int maxScore;
    private int score;
    List<Figure> figures = new LinkedList<Figure>();
    private boolean isWhite;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(Events event) {
        switch (event) {
            case WIN: increaseScore(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
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

    public boolean isAlive() {
        return true; // TODO
    }

    @Override
    public void down() {
        // do nothing
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        // do nothing
    }

    @Override
    public void right() {
        // do nothing
    }

    @Override
    public void act(int... p) {
        // TODO
    }
}