var _init = function(index, dx, dy, name, isAction){

    var withAct = function(){
        if (!! isAction) {
            return _init(index, dx, dy, name, !!isAction);
        }
        return _init(index, 0, 0, name + ',act', true);
    };

    var changeX = function(x) {
        return x + dx;
    };

    var changeY = function(y) {
        return y + dy;
    };

    var change = function(point) {
        return point.moveTo(this);
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

    var clockwise = function() {
        switch (this) {
            case Direction.UP : return Direction.LEFT;
            case Direction.LEFT : return Direction.DOWN;
            case Direction.DOWN : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.UP;
            default : return Direction.STOP;
        }
    };

    var contrClockwise = function() {
        switch (this) {
            case Direction.UP : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.DOWN;
            case Direction.DOWN : return Direction.LEFT;
            case Direction.LEFT : return Direction.UP;
            default : return Direction.STOP;
        }
    };

    var mirrorTopBottom = function() {
        switch (this) {
            case Direction.UP : return Direction.LEFT;
            case Direction.RIGHT : return Direction.DOWN;
            case Direction.DOWN : return Direction.RIGHT;
            case Direction.LEFT : return Direction.UP;
            default : return Direction.STOP;
        }
    };

    var mirrorBottomTop = function() {
        switch (this) {
            case Direction.UP : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.UP;
            case Direction.DOWN : return Direction.LEFT;
            case Direction.LEFT : return Direction.DOWN;
            default : return Direction.STOP;
        }
    };

    var toString = function() {
        return name;
    };

    var getIndex = function() {
        return index;
    }

    return {
        changeX : changeX,
        changeY : changeY,
        change : change,
        inverted : inverted,
        clockwise : clockwise,
        contrClockwise : contrClockwise,
        mirrorTopBottom : mirrorTopBottom,
        mirrorBottomTop : mirrorBottomTop,
        toString : toString,
        getIndex : getIndex,
        withAct: withAct,
        isAction: isAction
    };
};

var Direction = module.exports = {
    STOP : _init(0, 0, 0, ''),

    _init : _init

    // others will be added later
};

Direction.values = function() {
    var result = [];
    for (var key in Direstion) {
        result.push(Direstion[key]);
    }
    return result;
};

Direction.valueOf = function(indexOrName) {
    var directions = Direction.values();
    for (var i in directions) {
        var direction = directions[i];
        if (direction.getIndex() == indexOrName || direction.toString() == indexOrName) {
            return direction;
        }
    }
    return Direction.STOP;
};

Direction.where = function(from, to) {
    var dx = to.x - from.x;
    var dy = to.y - from.y;

    return Direction.values().find(d => (d.changeX(0) == dx) && (d.changeY(0) == dy));
}