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
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class MeatChoppers extends WallsDecorator implements Walls {

    private static final boolean WITH_MEATCHOPPERS = true;
    private Parameter<Integer> count;
    private Field board;
    private Dice dice;

    public MeatChoppers(Walls walls, Field board, Parameter<Integer> count, Dice dice) {
        super(walls);
        this.board = board;
        this.dice = dice;
        this.count = count;
    }


    public void regenerate() {     // TODO потестить
        if (count.getValue() < 0) {
            count.update(0);
        }

        int count = walls.subList(MeatChopper.class).size();

        int c = 0;
        int maxc = 100;
        while (count < this.count.getValue() && c < maxc) {
            int x = dice.next(board.size());
            int y = dice.next(board.size());

            // TODO это капец как долго выполняется, убрать нафиг митчомеров из Walls и сам Walls рассформировать!
            if (!board.isBarrier(x, y, WITH_MEATCHOPPERS) && !board.getBombermans().contains(pt(x, y))) {
                walls.add(new MeatChopper(x, y));
                count++;
            }

            c++;
        }

        if (c == maxc) {
//            throw new IllegalStateException("Dead loop at MeatChoppers.regenerate!"); // TODO тут часто вылетает :(
        }
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку + сделать через Template Method
        regenerate();

        List<MeatChopper> meatChoppers = walls.subList(MeatChopper.class);
        for (MeatChopper meatChopper : meatChoppers) {
            Direction direction = meatChopper.getDirection();
            if (direction != null && dice.next(5) > 0) {
                int x = direction.changeX(meatChopper.getX());
                int y = direction.changeY(meatChopper.getY());
                if (!walls.itsMe(x, y)) {
                    meatChopper.move(x, y);
                    continue;
                } else {
                    // do nothing
                }
            }
            meatChopper.setDirection(tryToMove(meatChopper));
        }
    }

    private Direction tryToMove(Point pt) {
        int count = 0;
        int x = pt.getX();
        int y = pt.getY();
        Direction direction = null;
        do {
            int n = 4;
            int move = dice.next(n);
            direction = Direction.valueOf(move);

            x = direction.changeX(pt.getX());
            y = direction.changeY(pt.getY());

        } while ((walls.itsMe(x, y) || isOutOfBorder(x, y)) && count++ < 10);

        if (count < 10) {
            pt.move(x, y);
            return direction;
        }
        return null;
    }

    private boolean isOutOfBorder(int x, int y) {
        return x >= board.size() || y >= board.size() || x < 0 || y < 0;
    }

}
