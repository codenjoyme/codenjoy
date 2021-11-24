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

var Point;

var pt = function (x, y) {
    return new Point(x, y);
};

var LengthToXY = function (boardSize) {
    function inversionY(y) {
        return boardSize - 1 - y;
    }

    function inversionX(x) {
        return x;
    }

    return {
        getXY: function (length) {
            if (length == -1) {
                return null;
            }
            var x = inversionX(length % boardSize);
            var y = inversionY(Math.trunc(length / boardSize));
            return new Point(x, y);
        },
        getXYExtended: function (length, element) {
            if (length == -1) {
                return null;
            }
            var x = inversionX(length % boardSize);
            var y = inversionY(Math.trunc(length / boardSize));
            return new Point(x, y, element);
        },


        getLength: function (x, y) {
            var xx = inversionX(x);
            var yy = inversionY(y);
            return yy * boardSize + xx;
        }
    };
};

var Board = function (board, Element, pointClass) {
    Point = pointClass;

    var contains = function (a, obj) {
        var i = a.length;
        while (i--) {
            if (a[i].equals(obj)) {
                return true;
            }
        }
        return false;
    };

    var isAt = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        for(var i = 2; i < arguments.length ; i++)
            if(getAt(x,y) == arguments[i]) return true;
         return false;
    };

    var getAt = function (x, y) {
        if (pt(x, y).isOutOf(size)) {
            return Element.BORDER;
        }
        return board.charAt(xyl.getLength(x, y));
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

    var boardSize = function () {
        return Math.sqrt(board.length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);


    var isBarrierAt = function (x, y) {
        return isAt(x, y, Element.BORDER);
    };

    var getMe = function () {
        return findAll(Element.BANG, Element.DETECTOR)[0];
    };

    var isGameOver = function () {
        return findAll(Element.BANG).length != 0;
    };

    var findAll = function (element) {
        var result = [];
        for (var i = 0; i < size * size; i++) {
            var point = xyl.getXY(i);
            var elements = [point.getX(), point.getY()];
            elements.push.apply(elements, arguments);
            if (isAt.apply(null, elements)) {
                result.push(point);
            }
        }
        return result;
    };

    var findAllExtended = function () {
        var result = [];
        for (var i = 0; i < size * size; i++) {
            var point = xyl.getXYExtended(i, board.charAt(i));
            result.push(point);
        }
        return result;
    };


    var getBorders = function () {
        return findAll(Element.BORDER);
    };

    var toString= function() {
        var result = "";
        for (var i = 0; i < size; i++) {
            result += board.substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    return {
        size: boardSize, // public int size();

        get: findAll,  // public List<Point> get(Element... elements);
        getAllExtended: findAllExtended,
        isAt: isAt,    // public boolean isAt(int x, int y, Element ... elements);
        getAt: getAt,  // public Element getAt(int x, int y);
        getBarriers: getBorders, // public List<Point> getBarriers();
        getWalls: getBorders, // public List<Point> getWalls();
        isNear: isNear, //public boolean isNear(int x, int y, Element element);
        getMe: getMe,   // public Point getMe();
        isGameOver: isGameOver, // public boolean isGameOver();
        isBarrierAt: isBarrierAt, // public boolean isBarrierAt(int x, int y);


        toString: toString
    }
};

if(module) module.exports = Board;
