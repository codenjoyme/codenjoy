/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
var BattlecityBoard = module.exports = function(board){

    var Games = require('./../../games.js');
    var Direction = Games.require('./direction.js');
    var Point = require('./../../point.js');
    var util = require('util');
    var Stuff = require('./../../stuff.js');
    var Element = Games.require('./elements.js');
    var LengthToXY = require('./../../lxy.js');

    var contains  = function(a, obj) {
        var i = a.length;
        while (i--) {
            if (a[i].equals(obj)) {
                return true;
            }
        }
        return false;
    };

    var sort = function(all) {
        return all.sort(function(pt1, pt2) {
            return (pt1.getY()*1000 + pt1.getX()) -
                (pt2.getY()*1000 + pt2.getX());
        });
    }

    var removeDuplicates = function(all) {
        var result = [];
        for (var index in all) {
            var point = all[index];
            if (!contains(result, point)) {
                result.push(point);
            }
        }
        return sort(result);
    };

    var boardSize = function() {
        return Math.sqrt(board.length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    var getMe = function() {
        var result = [];
        result = result.concat(findAll(Element.TANK_UP));
        result = result.concat(findAll(Element.TANK_DOWN));
        result = result.concat(findAll(Element.TANK_LEFT));
        result = result.concat(findAll(Element.TANK_RIGHT));
        if (result.lenght == 0) {
            return null;
        }
        return result[0];
    };

    var getEnemies = function() {
        var result = [];
        result = result.concat(findAll(Element.AI_TANK_UP));
        result = result.concat(findAll(Element.AI_TANK_DOWN));
        result = result.concat(findAll(Element.AI_TANK_LEFT));
        result = result.concat(findAll(Element.AI_TANK_RIGHT));
        result = result.concat(findAll(Element.OTHER_TANK_UP));
        result = result.concat(findAll(Element.OTHER_TANK_DOWN));
        result = result.concat(findAll(Element.OTHER_TANK_LEFT));
        result = result.concat(findAll(Element.OTHER_TANK_RIGHT));
        return result;
    };

    var getBullets = function() {
        var result = [];
        result = result.concat(findAll(Element.BULLET));
        return result;
    }

    var isGameOver = function() {
        return getMe() == null;
    };

    var isBulletAt = function(x, y) {
        if (new Point(x, y).isOutOf(size)) {
            return false;
        }

        return getAt(x, y) == Element.BULLET;
    }

    var isAt = function(x, y, element) {
        if (new Point(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y) == element;
    };

    var getAt = function(x, y) {
        if (new Point(x, y).isOutOf(size)) {
            return Element.BATTLE_WALL;
        }
        return board.charAt(xyl.getLength(x, y));
    };

    var boardAsString = function() {
        var result = "";
        for (var i = 0; i < size; i++) {
            result += board.substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    var getBarriers = function() {
        var result = [];
        result = result.concat(findAll(Element.BATTLE_WALL));
        result = result.concat(findAll(Element.WALL));
        result = result.concat(findAll(Element.WALL_DESTROYED_DOWN));
        result = result.concat(findAll(Element.WALL_DESTROYED_UP));
        result = result.concat(findAll(Element.WALL_DESTROYED_LEFT));
        result = result.concat(findAll(Element.WALL_DESTROYED_RIGHT));
        result = result.concat(findAll(Element.WALL_DESTROYED_DOWN_TWICE));
        result = result.concat(findAll(Element.WALL_DESTROYED_UP_TWICE));
        result = result.concat(findAll(Element.WALL_DESTROYED_LEFT_TWICE));
        result = result.concat(findAll(Element.WALL_DESTROYED_RIGHT_TWICE));
        result = result.concat(findAll(Element.WALL_DESTROYED_LEFT_RIGHT));
        result = result.concat(findAll(Element.WALL_DESTROYED_UP_DOWN));
        result = result.concat(findAll(Element.WALL_DESTROYED_UP_LEFT));
        result = result.concat(findAll(Element.WALL_DESTROYED_RIGHT_UP));
        result = result.concat(findAll(Element.WALL_DESTROYED_DOWN_LEFT));
        result = result.concat(findAll(Element.WALL_DESTROYED_DOWN_RIGHT));
        return sort(result);
    };

    var toString = function() {
        return util.format("Board:\n%s\n" +
            "My tank at: %s\n" +
            "Enemies at: %s\n" +
            "Bulets at: %s\n",
            boardAsString(),
            getMe(),
            Stuff.printArray(getEnemies()),
            Stuff.printArray(getBullets())
        );
    };

    var findAll = function(element) {
        var result = [];
        for (var i = 0; i < size*size; i++) {
            var point = xyl.getXY(i);
            if (isAt(point.getX(), point.getY(), element)) {
                result.push(point);
            }
        }
        return sort(result);
    };

    var isAnyOfAt = function(x, y, elements) {
        if (new Point(x, y).isOutOf(size)) {
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

    // TODO применить этот подход в других js клиентах
    var getNear = function(x, y) {
        var result = [];
        for (var dx = -1; dx <= 1; dx++) {
            for (var dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                result.push(getAt(x + dx, y + dy));
            }
        }
        return result;
    };

    var isNear = function(x, y, element) {
        return getNear(x, y).includes(element);
    };

    var isBarrierAt = function(x, y) {
        if (new Point(x, y).isOutOf(size)) {
            return true;
        }

        return contains(getBarriers(), new Point(x, y));
    };

    var countNear = function(x, y, element) {
        return getNear(x, y)
            .filter(function(value) { return value === element })
            .length;
    };

    return {
        size : boardSize,
        getMe : getMe,
        getEnemies : getEnemies,
        getBullets : getBullets,
        isGameOver : isGameOver,
        isAt : isAt,
        boardAsString : boardAsString,
        toString : toString,
        findAll : findAll,
        isAnyOfAt : isAnyOfAt,
        getNear : getNear,
        isNear : isNear,
        countNear : countNear,
        isBarrierAt : isBarrierAt,
        getBarriers : getBarriers,
        getAt : getAt
    };
};
