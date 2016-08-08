/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
/**
 * Created by Mikhail_Udalyi on 08.08.2016.
 */

var D = function(index, dx, dy, name){

    var changeX = function(x) {
        return x + dx;
    };

    var changeY = function(y) {
        return y + dy;
    };

    var inverted = function() {
        switch (this) {
            case Direction.UP : return Direction.DOWN;
            case Direction.DOWN : return Direction.UP;
            case Direction.LEFT : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.LEFT;
            default : return Direction.STOP;
        }
    };

    var getName = function() {
        return name.toUpperCase();
    };

    var getIndex = function() {
        return index;
    }

    return {
        changeX : changeX,
        changeY : changeY,
        inverted : inverted,
        name : getName,
        getIndex : getIndex
    };
};

var Direction = {
    UP : D(2, 0, -1, 'UP'),
    DOWN : D(3, 0, 1, 'DOWN'),
    LEFT : D(0, -1, 0, 'LEFT'),
    RIGHT : D(1, 1, 0, 'RIGHT'),
    ACT : D(4, 0, 0, 'ACT'),
    STOP : D(5, 0, 0, ''),

    get : function(direction) {
        if (typeof direction == 'string') {
            direction = direction.toUpperCase();
            for (var name in Direction) {
                if (direction == Direction[name].name()) {
                    return Direction[name];
                }
            }
            return null;
        } else {
            return direction;
        }
    }
};

Direction.values = function() {
    return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.ACT, Direction.STOP];
};

Direction.valueOf = function(index) {
    var directions = Direction.values();
    for (var i in directions) {
        var direction = directions[i];
        if (direction.getIndex() == index) {
            return direction;
        }
    }
    return Direction.STOP;
};