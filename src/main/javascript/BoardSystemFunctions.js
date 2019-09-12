
var D = function (index, dx, dy, name) {

    var changeX = function (x) {
        return x + dx;
    };

    var changeY = function (y) {
        return y + dy;
    };

    var change = function(point) {
        return point.moveTo(this);
    };

    var inverted = function () {
        switch (this) {
            case Direction.UP: return Direction.DOWN;
            case Direction.DOWN: return Direction.UP;
            default: return Direction.STOP;
        }
    };

    var toString = function () {
        return name;
    };

    return {
        changeX : changeX,
        changeY : changeY,
        change : change,
        inverted : inverted,
        toString : toString,

        getIndex : function() {
            return index;
        }
    };
};

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

        getLength: function (x, y) {
            var xx = inversionX(x);
            var yy = inversionY(y);
            return yy * boardSize + xx;
        }
    };
};