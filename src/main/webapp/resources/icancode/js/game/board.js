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

// ========================== board ==========================

var LengthToXY = function(boardSize) {
    return {
        getXY : function(length) {
            if (length == -1) {
                return null;
            }
            return new Point(length % boardSize, Math.trunc(length / boardSize));
        },

        getLength : function(x, y) {
            return y*boardSize + x;
        }
    };
};

var LAYER1 = 0;
var LAYER2 = 1;

var Board = function(boardString){
    var board = eval(boardString);
    var layers = board.layers;

    var contains  = function(a, obj) {
        var i = a.length;
        while (i--) {
            if (a[i].equals(obj)) {
                return true;
            }
        }
        return false;
    };

    var removeDuplicates = function(all) {
        var result = [];
        for (var index in all) {
            var point = all[index];
            if (!contains(result, point)) {
                result.push(point);
            }
        }
        return result;
    };

    var boardSize = function() {
        return Math.sqrt(layers[LAYER1].length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    var isAt = function(x, y, layer, element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return getAt(x, y, layer).char == element.char;
    };

    var getAt = function(x, y, layer) {
        return Element.getElement(layers[layer].charAt(xyl.getLength(x, y)));
    };

    var findAll = function(element, layer) {
        var result = [];
        for (var i = 0; i < size*size; i++) {
            var point = xyl.getXY(i);
            if (isAt(point.getX(), point.getY(), layer, element)) {
                result.push(point);
            }
        }
        return result;
    };

    var isAnyOfAt = function(x, y, layer, elements) {
        for (var index in elements) {
            var element = elements[index];
            if (isAt(x, y, layer, element)) {
                return true;
            }
        }
        return false;
    };

    var isNear = function(x, y, layer, element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return isAt(x + 1, y, layer, element) || isAt(x - 1, y, layer, element)
            || isAt(x, y + 1, layer, element) || isAt(x, y - 1, layer, element);
    };

    var isBarrierAt = function(x, y) {
        return contains(getBarriers(), pt(x, y));
    };

    var countNear = function(x, y, layer, element) {
        if (pt(x, y).isBad(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(x - 1, y    , layer, element)) count ++;
        if (isAt(x + 1, y    , layer, element)) count ++;
        if (isAt(x    , y - 1, layer, element)) count ++;
        if (isAt(x    , y + 1, layer, element)) count ++;
        return count;
    };

    var getHero = function() {
        var result = [];
        result = result.concat(findAll(Element.ROBOT, LAYER2));
        result = result.concat(findAll(Element.ROBOT_FALLING, LAYER2));
        result = result.concat(findAll(Element.ROBOT_FLYING, LAYER2));
        result = result.concat(findAll(Element.ROBOT_LASER, LAYER2));
        return result[0];
    };

    var getOtherHeroes = function() {
        var result = [];
        result = result.concat(findAll(Element.ROBOT_OTHER, LAYER2));
        return result;
    };

    var getLaserMachines = function() {
        var result = [];
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_LEFT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_RIGHT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_UP, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_DOWN, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_LEFT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_RIGHT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_UP, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_DOWN, LAYER1));
        return result;
    };

    var getLasers = function() {
        var result = [];
        result = result.concat(findAll(Element.LASER_LEFT, LAYER2));
        result = result.concat(findAll(Element.LASER_RIGHT, LAYER2));
        result = result.concat(findAll(Element.LASER_UP, LAYER2));
        result = result.concat(findAll(Element.LASER_DOWN, LAYER2));
        return result;
    };

    var getWalls = function() {
        var result = [];
        result = result.concat(findAll(Element.ANGLE_IN_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_FRONT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_IN_RIGHT, LAYER1));
        result = result.concat(findAll(Element.WALL_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_BACK_RIGHT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK, LAYER1));
        result = result.concat(findAll(Element.ANGLE_BACK_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK_ANGLE_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK_ANGLE_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_OUT_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_OUT_LEFT, LAYER1));
        result = result.concat(findAll(Element.SPACE, LAYER1));
        return result;
    };

    var getBoxes = function() {
        var result = [];
        result = result.concat(findAll(Element.BOX, LAYER1));
        return result;
    };

    var getGold = function() {
        var result = [];
        result = result.concat(findAll(Element.GOLD, LAYER1));
        return result;
    };

    var getStart = function() {
        var result = [];
        result = result.concat(findAll(Element.START, LAYER1));
        return result;
    };

    var getExit = function() {
        var result = [];
        result = result.concat(findAll(Element.EXIT, LAYER1));
        return result;
    };

    var getGold = function() {
        var result = [];
        result = result.concat(findAll(Element.GOLD, LAYER1));
        return result;
    };

    var getHoles = function() {
        var result = [];
        result = result.concat(findAll(Element.HOLE, LAYER1));
        return result;
    };

    var isMyRobotAlive = function() {
        return layers[LAYER2].indexOf(Element.ROBOT_LASER.char) == -1 &&
            layers[LAYER2].indexOf(Element.ROBOT_FALLING.char) == -1;
    };

    var barriers = null; // TODO optimize this method
    var getBarriers = function() {
        if (!!barriers) {
            return barriers;
        }
        var all = getWalls();
        all = all.concat(getLaserMachines());
        all = all.concat(getBoxes());
        barriers = removeDuplicates(all);
        return barriers;
    };

    var getShortestWay = function(from, to) {
        var mask = Array(size);
        for (var x = 0; x < size; x++) {
            mask[x] = new Array(size);
            for (var y = 0; y < size; y++) {
                mask[x][y] = (isBarrierAt(x, y)) ? -1 : 0;
            }
        }

        var current = 1;
        mask[from.getX()][from.getY()] = current;

        var isOutOf = function(x, y) {
            return (x < 0 || y < 0 || x >= size || y >= size);
        }

        var comeRound = function(x, y, onElement) {
            var dd = [[-1, 0], [1, 0], [0, -1], [0, 1]];
            for (var i in dd) {
                var dx = dd[i][0];
                var dy = dd[i][1];

                var xx = x + dx;
                var yy = y + dy;
                if (isOutOf(xx, yy)) continue;

                var stop = !onElement(xx, yy);

                if (stop) return;
            }
        }

        var done = false;
        while (!done) {
            for (var x = 0; x < size; x++) {
                for (var y = 0; y < size; y++) {
                    if (mask[x][y] != current) continue;

                    comeRound(x, y, function(xx, yy) {
                        if (mask[xx][yy] == 0) {
                            mask[xx][yy] = current + 1;
                            if (xx == to.getX() && yy == to.getY()) {
                                done = true;
                            }
                        }
                        return true;
                    });
                }
            }
            current++;
        }
        var point = to;
        done = false;
        current = mask[point.getX()][point.getY()];
        var path = [];
        path.push(point);
        while (!done) {
            comeRound(point.getX(), point.getY(), function(xx, yy) {
                if (mask[xx][yy] == current - 1) {
                    point = pt(xx, yy);
                    current--;

                    path.push(point);

                    if (current == 1) {
                        done = true;
                    }
                    return false;
                }
                return true;
            });
        }

        return path.reverse();
    }

    var boardAsString = function(layer) {
        var result = "";
        for (var i = 0; i <= size - 1; i++) {
            result += layers[layer].substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    // thanks http://jsfiddle.net/queryj/g109jvxd/
    String.format = function() {
        // The string containing the format items (e.g. "{0}")
        // will and always has to be the first argument.
        var theString = arguments[0];

        // start with the second argument (i = 1)
        for (var i = 1; i < arguments.length; i++) {
            // "gm" = RegEx options for Global search (more than one instance)
            // and for Multiline search
            var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            theString = theString.replace(regEx, arguments[i]);
        }
    }

    var toString = function() {
        return String.format(
            "Board layer 1:\n{0}\n" +
            "Board layer 2:\n{1}\n" +
            "Robot at: {2}\n" +
            "Other robots at: {3}\n" +
            "LaserMachine at: {4}" +
            "Laser at: {5}" +
            boardAsString(LAYER1),
            boardAsString(LAYER2),
            getHero(),
            printArray(getOtherHeroes()),
            printArray(getLaserMachines()),
            printArray(getLasers())
        );
    };

    return {
        size : function() {
            return size;
        },
        getHero : getHero,
        getOtherHeroes : getOtherHeroes,
        getLaserMachines : getLaserMachines,
        getLasers : getLasers,
        getWalls : getWalls,
        getBoxes : getBoxes,
        getGold : getGold,
        getStart : getStart,
        getExit : getExit,
        getHoles : getHoles,
        isMyRobotAlive : isMyRobotAlive,
        isAt : isAt,
        getAt : getAt,
        toString : toString,
        layer1 : function() {
            return boardAsString(LAYER1)
        },
        layer2 : function() {
            return boardAsString(LAYER2)
        },
        getBarriers : getBarriers,
        findAll : findAll,
        isAnyOfAt : isAnyOfAt,
        isNear : isNear,
        isBarrierAt : isBarrierAt,
        countNear : countNear,
        getShortestWay : getShortestWay
    };
};

var random = function(n){
    return Math.floor(Math.random()*n);
};