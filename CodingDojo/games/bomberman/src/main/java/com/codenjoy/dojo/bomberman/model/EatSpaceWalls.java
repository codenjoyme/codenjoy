package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Field.FOR_HERO;

public class EatSpaceWalls extends WallsDecorator implements Walls { // TODO протестить класс

    private Field board;
    private Parameter<Integer> count;
    private Dice dice;

    public EatSpaceWalls(Walls walls, Field board, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.board = board;
        this.count = count;
        this.dice = dice;
    }

    private int freeSpaces() {
        return  (board.size()*board.size() - 1) // TODO -1 это один бомбер, а если их несколько?
                - walls.subList(Wall.class).size();
    }

    @Override
    public void tick() {
        super.tick();    // TODO протестить эту строчку

        regenerate();
    }

    private void regenerate() {
        if (count.getValue() < 0) {
            count.update(0);
        }

        List<DestroyWall> destroyWalls = walls.subList(DestroyWall.class);
        int needToCreate = this.count.getValue() - destroyWalls.size();
        if (needToCreate > freeSpaces()) {  // TODO и это потестить
            count.update(count.getValue() - (needToCreate - freeSpaces()) - 50); // 50 это место под бомберов
        }

        int count = destroyWalls.size();
        if (count > this.count.getValue()) { // TODO и удаление лишних
            for (int i = 0; i < (count - this.count.getValue()); i++) {
                DestroyWall meatChopper = destroyWalls.remove(0);
                walls.destroy(meatChopper);
            }
            return;
        }

        int c = 0;
        int maxc = 1000;
        while (count < this.count.getValue() && c < maxc) {  // TODO и это
            Point pt = PointImpl.random(dice, board.size());

            if (!board.isBarrier(pt, !FOR_HERO)) {
                walls.add(new DestroyWall(pt));
                count++;
            }

            c++;
        }

        if (c == maxc) {
            throw new RuntimeException("Dead loop at EatSpaceWalls.generate!");
        }
    }
}
