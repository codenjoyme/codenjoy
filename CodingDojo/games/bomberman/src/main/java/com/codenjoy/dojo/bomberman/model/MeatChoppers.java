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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Field.FOR_HERO;

public class MeatChoppers extends WallsDecorator implements Walls {

    public static final int MAX = 1000;

    private Parameter<Integer> count;
    private Dice dice;

    public MeatChoppers(Walls walls, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.dice = dice;
        this.count = count;
    }

    public void regenerate() {     // TODO потестить
        if (count.getValue() < 0) {
            count.update(0);
        }

        int count = walls.subList(MeatChopper.class).size();

        int iteration = 0;
        while (count < this.count.getValue() && iteration++ < MAX) {
            Point pt = PointImpl.random(dice, field.size());

            // TODO это капец как долго выполняется, убрать нафиг митчомеров из Walls и сам Walls рассформировать!
            if (field.isBarrier(pt, !FOR_HERO)) {
                continue;
            }

            walls.add(new MeatChopper(pt));
            count++;
        }

        if (iteration >= MAX) {
            System.out.println("Dead loop at MeatChopper.regenerate!"); // TODO тут часто вылетает :(
        }
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку + сделать через Template Method
        regenerate();

        List<MeatChopper> choppers = walls.subList(MeatChopper.class);
        for (MeatChopper chopper : choppers) {
            Direction direction = chopper.getDirection();
            if (direction != null && dice.next(5) > 0) {
                Point to = direction.change(chopper);
                if (!walls.itsMe(to)) {
                    chopper.move(to);
                    continue;
                } else {
                    // do nothing
                }
            }
            chopper.setDirection(tryToMove(chopper));
        }
    }

    private Direction tryToMove(Point from) {
        int iteration = 0;
        Point to;
        Direction direction;
        do {
            int n = 4;
            int move = dice.next(n);
            direction = Direction.valueOf(move);

            to = direction.change(from);
        } while ((walls.itsMe(to) || to.isOutOf(field.size())) && iteration++ < MAX);

        if (iteration >= MAX) {
            return null;
        }

        from.move(to);
        return direction;
    }
}
