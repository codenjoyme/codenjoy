package com.codenjoy.dojo.bomberman.model;

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
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 11:59 PM
 */
public class BoomEngineOriginal implements BoomEngine {

    private Hero bomberman;

    public BoomEngineOriginal(Hero bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends PointImpl> barriers, int boardSize, PointImpl source, int radius) {
        List<Blast> blasts = new LinkedList<Blast>();

        add(barriers, boardSize, blasts, source.getX(), source.getY());

        for (int dx = 1; dx <= radius; dx++) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dx = -1; dx >= -radius; dx--) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dy = 1; dy <= radius; dy++) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dy = -1; dy >= -radius; dy--) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        return blasts;
    }

    private boolean add(List<? extends PointImpl> barriers, int boardSize, List<Blast> blasts, int x, int y) {
        PointImpl pt = new PointImpl(x, y);

        if (!isOnBoard(pt, boardSize)) {
            return false;
        }

        if (barriers.contains(pt)) {
            if (!barriers.get(barriers.indexOf(pt)).getClass().equals(Wall.class)) {    // TODO немного жвачка
                blasts.add(new Blast(x, y, bomberman));
            }
            return false;
        }

        blasts.add(new Blast(x, y, bomberman));
        return true;
    }

    private boolean isOnBoard(PointImpl pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
