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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codenjoy.dojo.bomberman.model.Field.FOR_HERO;

public class EatSpaceWalls extends WallsDecorator implements Walls { // TODO протестить класс

    public static final int MAX = 1000;

    private Parameter<Integer> count;
    private Dice dice;

    public EatSpaceWalls(Walls walls, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.count = count;
        this.dice = dice;
    }

    private int freeSpaces() {
        return  (field.size()* field.size() - 1) // TODO -1 это один бомбер, а если их несколько?
                - walls.listSubtypes(Wall.class).size();
    }

    @Override
    public void tact() {
        regenerate();
    }

    public void regenerate() {
        if (count.getValue() < 0) {
            count.update(0);
        }

        List<DestroyWall> destroy = walls.listSubtypes(DestroyWall.class);
        int need = this.count.getValue() - destroy.size();
        if (need > freeSpaces()) {  // TODO и это потестить
            count.update(count.getValue() - (need - freeSpaces()) - 50); // 50 это место под бомберов
        }

        int count = destroy.size();
        if (count > this.count.getValue()) { // TODO и удаление лишних
            for (int i = 0; i < (count - this.count.getValue()); i++) {
                walls.destroy(destroy.remove(0));
            }
            return;
        }

        int iteration = 0;
        Set<Point> checked = new HashSet<>();
        while (count < this.count.getValue() && iteration++ < MAX) {  // TODO и это
            Point pt = PointImpl.random(dice, field.size());

            if (checked.contains(pt) || field.isBarrier(pt, !FOR_HERO)) {
                checked.add(pt);
                continue;
            }

            walls.add(new DestroyWall(pt));
            count++;
        }

        if (iteration >= MAX) {
            System.out.println("Dead loop at EatSpaceWalls.generate!");
        }
    }
}
