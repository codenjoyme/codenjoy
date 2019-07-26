package com.codenjoy.dojo.crossyroad.model;

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


import com.codenjoy.dojo.services.*;

public class Car extends PointImpl implements Tickable, State<Elements, Player> {

    private Direction direction;

    public Car(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Car(Point point, Direction direction) {
        super(point);
        this.direction = direction;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (direction == Direction.RIGHT){
        return Elements.CARLEFTTORIGHT;}
        else return Elements.CARRIGHTTOLEFT;
    }

    @Override
    public void tick() {
        if(direction == Direction.LEFT) {
            move(x - 1, y);
        }else
        if(direction == Direction.RIGHT) {
            move(x + 1, y);
        }else
            move(x - 1, y);

    }

    public void down (){
        move(x , y-1);
    }

    public Direction getDirection() {
        return direction;
    }
}
