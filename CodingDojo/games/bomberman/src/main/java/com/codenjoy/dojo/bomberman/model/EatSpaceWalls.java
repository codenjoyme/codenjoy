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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 21.04.13
 * Time: 0:17
 */
public class EatSpaceWalls extends WallsDecorator implements Walls { // TODO протестить класс

    private static final boolean WITH_MEAT_CHOPPERS = true;
    private int size;
    private Dice dice;
    private Level board;

    public EatSpaceWalls(Walls walls, Level board, List<Point> points, Dice dice) {
        super(walls);
        for (Point pt : points) {
            add(new DestroyWall(pt.getX(), pt.getY()));
        }
        this.size = points.size();
        this.dice = dice;
        this.board = board;
    }

    @Override
    public void tick() {
        super.tick();    // TODO протестить эту строчку
        regenerate();
    }

    private void regenerate() {
        List<DestroyWall> destroyWalls = walls.subList(DestroyWall.class);
        int count = destroyWalls.size();
        if (count > size) { // TODO и удаление лишних
            for (int i = 0; i < (count - size); i++) {
                DestroyWall meatChopper = destroyWalls.remove(0);
                walls.destroy(meatChopper.getX(), meatChopper.getY());
            }
            return;
        } else {
            int c = 0;
            int maxc = 10000;
            while (count < size && c < maxc) {  // TODO и это
                int x = dice.next(board.getSize().getValue());
                int y = dice.next(board.getSize().getValue());

                if (!board.isBarrier(x, y, WITH_MEAT_CHOPPERS)) {
                    walls.add(new DestroyWall(x, y));
                    count++;
                }
                c++;
            }
            if (c == maxc) {
                throw new RuntimeException("Dead loop at Deatroyed Wall generate!");
            }
        }
    }
}
