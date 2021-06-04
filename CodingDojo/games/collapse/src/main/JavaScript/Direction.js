/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

var actX = 0;
var actY = 0;

var D = function (index, dx, dy, name, isAction = false) {


    var changeX = function (x) {
        return x + dx;
    };

    var changeY = function (y) {
        return y - dy;
    };

    var inverted = function () {
        switch (this) {
            case Direction.UP: return Direction.DOWN;
            case Direction.DOWN: return Direction.UP;
            case Direction.LEFT: return Direction.RIGHT;
            case Direction.RIGHT: return Direction.LEFT;
            default: return Direction.STOP;
        }
    };

    var toString = function () {
        return 'act(' + actX + ', ' + actY + '),' + name;
    };

    var setAct = function(x, y) {
       actX = x;
       actY = y;
       return self;
    }

    var self = {
        changeX: changeX,

        changeY: changeY,

        inverted: inverted,

        toString: toString,

        getIndex: function () {
            return index;
        },

        isAction: isAction,

        ACT: setAct
    };

    return self;
};

var act = function(x,y) {
       actX = x;
       actY = y;
    return Direction;
}

var Direction = {
    ACT: act,                              // set coordinates
    UP: D(2, 0, -1, 'up'),                 // you can move
    DOWN: D(3, 0, 1, 'down'),
    LEFT: D(0, -1, 0, 'left'),
    RIGHT: D(1, 1, 0, 'right'),
    STOP: D(-1, 0, 0, '', true)                 // do Nothing
};

Direction.values = function () {
    return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.STOP];
};

Direction.valueOf = function (index) {
    var directions = Direction.values();
    for (var i in directions) {
        var direction = directions[i];
        if (direction.getIndex() == index) {
            return direction;
        }
    }
    return Direction.STOP;
};


if (module) module.exports = Direction;
