/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

 // Отступ от нижней границы в поинтах(10 полос на дороге по вертикали + 2 полосы ограждений)
var MAX_Y_SIZE = 12;



var util = require('util');


var Direction = {
    UP          : D(1,  0, 1, 'UP'),         // move up
    DOWN        : D(2,  0, -1, 'DOWN'),       // move down
    STOP        : D(3,  0,  0, ''),           // stay
    LEFT        : D(4,  -1,  0, 'LEFT'),
    RIGHT       : D(5,  1,  0, 'RIGHT'),
};

Direction.values = function () {
    return [Direction.UP, Direction.DOWN, Direction.STOP];
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

var Point = function (x, y) {
    return {
        equals: function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString: function () {
            return '[' + x + ',' + y + ']';
        },

        isOutOf: function (boardSize) {
            return x >= boardSize || y > MAX_Y_SIZE || x < 0 || y < 0;
        },

        getX: function () {
            return x;
        },

        getY: function () {
            return y;
        },
        
        moveTo : function(direction) {
            return pt(direction.changeX(x), direction.changeY(y));
        }
    }
};

var Board = function (board) {
    var contains = function (a, obj) {
        var i = a.length;
        while (i--) {
            if (a[i].equals(obj)) {
                return true;
            }
        }
        return false;
    };

    var sort = function (all) {
        return all.sort(function (pt1, pt2) {
            return pt1.getY() * 1000 + pt1.getX() - pt2.getY() * 1000 + pt2.getX();
        });
    }

    var removeDuplicates = function (all) {
        var result = [];
        for (var index in all) {
            var point = all[index];
            if (!contains(result, point)) {
                result.push(point);
            }
        }
        return sort(result);
    };

    var boardSize = function () {
        return Math.sqrt(board.length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    var getMe = function () {
        var result = [];
        result = result.concat(findAll(Elements.BIKE));
        result = result.concat(findAll(Elements.BIKE_AT_ACCELERATOR));
        result = result.concat(findAll(Elements.BIKE_AT_INHIBITOR));
        result = result.concat(findAll(Elements.BIKE_AT_KILLED_BIKE));
        result = result.concat(findAll(Elements.BIKE_AT_LINE_CHANGER_DOWN));
        result = result.concat(findAll(Elements.BIKE_AT_LINE_CHANGER_UP));
        result = result.concat(findAll(Elements.BIKE_AT_SPRINGBOARD_LEFT));
        result = result.concat(findAll(Elements.BIKE_AT_SPRINGBOARD_LEFT_DOWN));
        result = result.concat(findAll(Elements.BIKE_AT_SPRINGBOARD_RIGHT));
        result = result.concat(findAll(Elements.BIKE_AT_SPRINGBOARD_RIGHT_DOWN));
        result = result.concat(findAll(Elements.BIKE_FALLEN));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_ACCELERATOR));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_FENCE));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_INHIBITOR));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_LINE_CHANGER_DOWN));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_LINE_CHANGER_UP));
        result = result.concat(findAll(Elements.BIKE_FALLEN_AT_OBSTACLE));
        result = result.concat(findAll(Elements.BIKE_IN_FLIGHT_FROM_SPRINGBOARD));
        return result[0];
    };

    var getOtherHeroes = function () {
        var result = [];
        result = result.concat(findAll(Elements.OTHER_BIKE));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_ACCELERATOR));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_INHIBITOR));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_KILLED_BIKE));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_LINE_CHANGER_DOWN));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_LINE_CHANGER_UP));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_SPRINGBOARD_LEFT));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_SPRINGBOARD_LEFT_DOWN));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_SPRINGBOARD_RIGHT));
        result = result.concat(findAll(Elements.OTHER_BIKE_AT_SPRINGBOARD_RIGHT_DOWN));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_ACCELERATOR));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_FENCE));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_INHIBITOR));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_DOWN));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_UP));
        result = result.concat(findAll(Elements.OTHER_BIKE_FALLEN_AT_OBSTACLE));
        result = result.concat(findAll(Elements.OTHER_BIKE_IN_FLIGHT_FROM_SPRINGBOARD));
        return result;
    };

    var getAccelerators = function () {
        var result = [];
        result = result.concat(findAll(Elements.ACCELERATOR));
        return result;
    };

    var getFences = function () {
        var result = [];
        result = result.concat(findAll(Elements.FENCE));
        return result;
    };

    var getInhibitors = function () {
        var result = [];
        result = result.concat(findAll(Elements.INHIBITOR));
        return result;
    };

    var getLineUpChangers = function () {
        var result = [];
        result = result.concat(findAll(Elements.LINE_CHANGER_UP));
        return result;
    };

    var getLineDownChangers = function () {
        var result = [];
        result = result.concat(findAll(Elements.LINE_CHANGER_DOWN));
        return result;
    };

    var getObstacles = function () {
        var result = [];
        result = result.concat(findAll(Elements.OBSTACLE));
        return result;
    };

    var getSpringboardDarkElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_LEFT));
        return result;
    };

    var getSpringboardLightElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_RIGHT));
        return result;
    };

    var getSpringboardLeftDownElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_LEFT_DOWN));
        return result;
    };

    var getSpringboardRightDownElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_RIGHT_DOWN));
        return result;
    };

    var getSpringboardLeftUpElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_LEFT_UP));
        return result;
    };

    var getSpringboardRightUpElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_RIGHT_UP));
        return result;
    };

    var getSpringboardTopElements = function () {
        var result = [];
        result = result.concat(findAll(Elements.SPRINGBOARD_TOP));
        return result;
    };

    var isGameOver = function () {
        return board.indexOf(Elements.BIKE_FALLEN) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_ACCELERATOR) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_FENCE) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_INHIBITOR) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_LINE_CHANGER_DOWN) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_LINE_CHANGER_UP) != -1
            || board.indexOf(Elements.BIKE_FALLEN_AT_OBSTACLE) != -1;
    };
    
    var isAt = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y) == element;
    };

    var isAtMany = function (x, y, elements) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        var elpt = getAt(x, y);
        for(var el of elements){
            if(elpt==el){
                return true;
            }
        }
        return false;
    };

    var checkNearMeManyMoves = function (directions, elements) {
        var point = getMe();
        if (point == null) {
            return false;
        }
        for(var direction of directions){
            point = direction.change(point)
        }
        return isAtMany(point.getX(), point.getY(), elements);
    }

    var checkNearMe = function (direction, elements) {
        var me = getMe();
        if (me == null) {
            return false;
        }
        var atDirection = direction.change(me);
        return isAtMany(atDirection.getX(), atDirection.getY(), elements);
    }

    var isOutOfField = function(x, y){
        return x<0 || x>size || y<0 || y>MAX_Y_SIZE;
    }

    var isOutOfFieldRelativeToMe = function (direction) {
        var me = getMe();
        if (me == null) {
            return false;
        }
        var atDirection = direction.change(me);
        return isOutOfField(atDirection.getX(), atDirection.getY());
    }   

    var getAt = function (x, y) {
        if (pt(x, y).isOutOf(size)) {
            return Elements.FENCE;
        }
        return board.charAt(xyl.getLength(x, y));
    };

    var boardAsString = function () {
        var result = "";
        for (var i = 0; i < size; i++) {
            result += board.substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    var getBarriers = function () {
        var all = getFences();
        all = all.concat(getOtherHeroes());
        all = all.concat(getObstacles());
        return removeDuplicates(all);
    };

    var findAll = function (element) {
        var result = [];
        for (var i = 0; i < size * size; i++) {
            var point = xyl.getXY(i);
            if (isAt(point.getX(), point.getY(), element)) {
                result.push(point);
            }
        }
        return sort(result);
    };

    var isAnyOfAt = function (x, y, elements) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        for (var index in elements) {
            var element = elements[index];
            if (isAt(x, y, element)) {
                return true;
            }
        }
        return false;
    };

    var isBarrierAt = function (x, y) {
        if (pt(x, y).isOutOf(size)) {
            return true;
        }
        return contains(getBarriers(), pt(x, y));
    };

    var hasOtherBikeAt = function (x, y) {
        return isAnyOfAt(x, y,
            [Elements.OTHER_BIKE, 
                Elements.OTHER_BIKE_AT_ACCELERATOR, 
                Elements.OTHER_BIKE_AT_INHIBITOR, 
                Elements.OTHER_BIKE_AT_KILLED_BIKE, 
                Elements.OTHER_BIKE_AT_LINE_CHANGER_DOWN, 
                Elements.OTHER_BIKE_AT_LINE_CHANGER_UP, 
                Elements.OTHER_BIKE_AT_SPRINGBOARD_LEFT, 
                Elements.OTHER_BIKE_AT_SPRINGBOARD_LEFT_DOWN, 
                Elements.OTHER_BIKE_AT_SPRINGBOARD_RIGHT, 
                Elements.OTHER_BIKE_AT_SPRINGBOARD_RIGHT_DOWN,
                Elements.OTHER_BIKE_FALLEN,
                Elements.OTHER_BIKE_FALLEN_AT_ACCELERATOR,
                Elements.OTHER_BIKE_FALLEN_AT_FENCE,
                Elements.OTHER_BIKE_FALLEN_AT_INHIBITOR,
                Elements.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_DOWN,
                Elements.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_UP,
                Elements.OTHER_BIKE_FALLEN_AT_OBSTACLE,
                Elements.OTHER_BIKE_IN_FLIGHT_FROM_SPRINGBOARD]);
    };

    var hasFenceAt = function (x, y) {
        if (pt(x, y).isOutOf(size)) {
            return true;
        }
        return isAt(x, y, Elements.FENCE);
    };

    var hasInhibitorAt = function (x, y) {
        return isAt(x, y, Elements.INHIBITOR);
    };

    var hasAcceleratorAt = function (x, y) {
        return isAt(x, y, Elements.ACCELERATOR);
    };

    var hasObstacleAt = function (x, y) {
        return isAt(x, y, Elements.OBSTACLE);
    };

    var hasUpLineChangerAt = function (x, y) {
        return isAt(x, y, Elements.LINE_CHANGER_UP);
    };

    var hasDownLineChangerAt = function (x, y) {
        return isAt(x, y, Elements.LINE_CHANGER_DOWN);
    };

    var hasSpringboardDarkElementAt = function (x, y) {
        return isAt(x, y, Elements.SPRINGBOARD_RIGHT);
    };

    var hasSpringboardLightElementAt = function (x, y) {
        return isAt(x, y, Elements.SPRINGBOARD_LEFT);
    };

    var hasSpringboardLeftDownElementAt = function (x, y) {
        return isAt(x, y, Elements.SPRINGBOARD_LEFT_DOWN);
    };

    var hasSpringboardRightDownElementAt = function (x, y) {
        return isAt(x, y, Elements.SPRINGBOARD_RIGHT_DOWN);
    };

    var hasSpringboardTopElementAt = function (x, y) {
        return isAt(x, y, Elements.SPRINGBOARD_TOP);
    };

    var toString = function () {
        return util.format("Board:\n%s\n" +
            "Me at: %s\n" +
            "Enemy bikes at: %s\n" +
            "Accelerators at: %s\n" +
            "Fences at: %s\n" +
            "Inhibitors at: %s\n" +
            "Line Up Changers at: %s\n" +
            "Line Down Changers at: %s\n" +
            "Obstacles at: %s\n" +
            "Springboard Dark Elements at: %s\n" +
            "Springboard Light Elements at: %s\n" +
            "Springboard Left Down Elements at: %s\n" +
            "Springboard Right Down Elements at: %s\n" +
            "Springboard Left Up Elements at: %s\n" +
            "Springboard Right Up Elements at: %s\n" +
            "Springboard Top Elements at: %s\n",
            boardAsString(),
            getMe(),
            printArray(getOtherHeroes()),
            printArray(getAccelerators()),
            printArray(getFences()),
            printArray(getInhibitors()),
            printArray(getLineUpChangers()),
            printArray(getLineDownChangers()),
            printArray(getObstacles()),
            printArray(getSpringboardDarkElements()),
            printArray(getSpringboardLightElements()),
            printArray(getSpringboardLeftDownElements()),
            printArray(getSpringboardRightDownElements()),
            printArray(getSpringboardLeftUpElements()),
            printArray(getSpringboardRightUpElements()),
            printArray(getSpringboardTopElements())
        );
    };

    var isNear = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(x + 1, y, element) ||
            isAt(x - 1, y, element) ||
            isAt(x, y + 1, element) ||
            isAt(x, y - 1, element);
    };

    var countNear = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(x - 1, y, element)) count++;
        if (isAt(x + 1, y, element)) count++;
        if (isAt(x, y - 1, element)) count++;
        if (isAt(x, y + 1, element)) count++;
        return count;
    };

    return {
        size: boardSize,
        getMe: getMe,
        getOtherHeroes: getOtherHeroes,
        isGameOver: isGameOver,
        isAt: isAt,
        isAtMany: isAtMany,
        getAt: getAt,
        boardAsString: boardAsString,
        getBarriers: getBarriers,
        toString: toString,
        findAll: findAll,
        getAccelerators: getAccelerators,
        getFences: getFences,
        getInhibitors: getInhibitors,
        getLineUpChangers: getLineUpChangers,
        getLineDownChangers: getLineDownChangers,
        getObstacles: getObstacles,
        getSpringboardDarkElements: getSpringboardDarkElements,
        getSpringboardLightElements: getSpringboardLightElements,
        getSpringboardLeftDownElements: getSpringboardLeftDownElements,
        getSpringboardRightDownElements: getSpringboardRightDownElements,
        getSpringboardLeftUpElements: getSpringboardLeftUpElements,
        getSpringboardRightUpElements: getSpringboardRightUpElements,
        getSpringboardTopElements: getSpringboardTopElements,
        checkNearMe: checkNearMe,
        checkNearMeManyMoves: checkNearMeManyMoves,
        isOutOfFieldRelativeToMe: isOutOfFieldRelativeToMe,
        hasFenceAt: hasFenceAt,
        hasInhibitorAt: hasInhibitorAt,
        hasAcceleratorAt: hasAcceleratorAt,
        hasObstacleAt: hasObstacleAt,
        hasUpLineChangerAt: hasUpLineChangerAt,
        hasDownLineChangerAt: hasDownLineChangerAt,
        hasSpringboardDarkElementAt: hasSpringboardDarkElementAt,
        hasSpringboardLightElementAt: hasSpringboardLightElementAt,
        hasSpringboardLeftDownElementAt: hasSpringboardLeftDownElementAt,
        hasSpringboardRightDownElementAt: hasSpringboardRightDownElementAt,
        hasSpringboardTopElementAt: hasSpringboardTopElementAt,
        hasOtherBikeAt: hasOtherBikeAt,
        isAnyOfAt: isAnyOfAt,
        isNear: isNear,
        isBarrierAt: isBarrierAt,
        countNear: countNear
    };
};
