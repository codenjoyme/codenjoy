var Point = module.exports = function (x, y, direction, element) {
    return {
        x: x,
        y: y,
        direction: direction,
        element: element,

        equals : function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString : function() {
            return '[' + x + ',' + y + (!!direction ? (',' + direction) : '') + ']';
        },

        isOutOf : function(boardSize) {
            return x >= boardSize || y >= boardSize || x < 0 || y < 0;
        },

        getX : function() {
            return x;
        },

        getY : function() {
            return y;
        },

        getElement: function(){
            return element
        },

        moveTo : function(direction) {
            return pt(direction.changeX(x), direction.changeY(y));
        },

        move: function(dx, dy) {
            x += dx;
            y += dy;
        },

        shiftLeft : function (delta = 1) {
            return new Point(x - delta, y);
        },

        shiftRight : function (delta = 1) {
            return new Point(x + delta, y);
        },

        shiftTop : function (delta = 1) {
            return new Point(x, y + delta);
        },

        shiftBottom : function (delta = 1) {
            return new Point(x, y - delta);
        },
    }
};

var pt = function(x, y) {
    return new Point(x, y);
};