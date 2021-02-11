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
var util = require('util');
var WSocket = require('ws');

var log = function(string) {
    console.log(string);
    if (!!printBoardOnTextArea) {
        printLogOnTextArea(string);
    }
};

var printArray = function (array) {
    var result = [];
    for (var index in array) {
        var element = array[index];
        result.push(element.toString());
    }
    return "[" + result + "]";
};

var processBoard = function(boardString) {
    var board = new Board(boardString);
    if (!!printBoardOnTextArea) {
        printBoardOnTextArea(board.boardAsString());
    }

    var logMessage = board + "\n\n";
    var answer = new DirectionSolver(board).get().toString();
    logMessage += "Answer: " + answer + "\n";
    logMessage += "-----------------------------------\n";

    log(logMessage);

    return answer;
};

// you can get this code after registration on the server with your email
var url = "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789";

url = url.replace("http", "ws");
url = url.replace("board/player/", "ws?user=");
url = url.replace("?code=", "&code=");

var ws;

function connect() {
    ws = new WSocket(url);
    log('Opening...');

    ws.on('open', function() {
        log('Web socket client opened ' + url);
    });

    ws.on('close', function() {
        log('Web socket client closed');

        setTimeout(function() {
            connect();
        }, 5000);
    });

    ws.on('message', function(message) {
        var pattern = new RegExp(/^board=(.*)$/);
        var parameters = message.match(pattern);
        var boardString = parameters[1];
        var answer = processBoard(boardString);
        ws.send(answer);
    });
}

connect();

var Elements = {
    
    NONE: ' ',
    BATTLE_WALL: '☼',
    BANG: 'Ѡ',

    WALL: '╬',

    WALL_DESTROYED_DOWN: '╩',
    WALL_DESTROYED_UP: '╦',
    WALL_DESTROYED_LEFT: '╠',
    WALL_DESTROYED_RIGHT: '╣',

    WALL_DESTROYED_DOWN_TWICE: '╨',
    WALL_DESTROYED_UP_TWICE: '╥',
    WALL_DESTROYED_LEFT_TWICE: '╞',
    WALL_DESTROYED_RIGHT_TWICE: '╡',

    WALL_DESTROYED_LEFT_RIGHT: '│',
    WALL_DESTROYED_UP_DOWN: '─',

    WALL_DESTROYED_UP_LEFT: '┌',
    WALL_DESTROYED_RIGHT_UP: '┐',
    WALL_DESTROYED_DOWN_LEFT: '└',
    WALL_DESTROYED_DOWN_RIGHT: '┘',

    WALL_DESTROYED: ' ',

    BULLET: '•',

    TANK_UP: '▲',
    TANK_RIGHT: '►',
    TANK_DOWN: '▼',
    TANK_LEFT: '◄',

    OTHER_TANK_UP: '˄',
    OTHER_TANK_RIGHT: '˃',
    OTHER_TANK_DOWN: '˅',
    OTHER_TANK_LEFT: '˂',

    AI_TANK_UP: '?',
    AI_TANK_RIGHT: '»',
    AI_TANK_DOWN: '¿',
    AI_TANK_LEFT: '«'
    
};

var D = function(index, dx, dy, name){

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

    var toString = function() {
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

var Direction = {
    UP:   D(2, 0, 1, 'up'),                 // you can move
    DOWN: D(3, 0, -1, 'down'),
    LEFT: D(0, -1, 0, 'left'),
    RIGHT:D(1, 1, 0, 'right'),
    ACT:  D(4, 0, 0, 'act'),               // fire
    STOP: D(5, 0, 0, '')                   // stay
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.DRILL_LEFT, Direction.DRILL_RIGHT, Direction.STOP];
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

var Point = function (x, y) {
    return {
        equals : function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString : function() {
            return '[' + x + ',' + y + ']';
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

        moveTo : function(direction) {
            return pt(direction.changeX(x), direction.changeY(y));
        }
    }
};

var pt = function(x, y) {
    return new Point(x, y);
};

var LengthToXY = function(boardSize) {
    function inversionY(y) {
        return boardSize - 1 - y;
    }

    function inversionX(x) {
        return x;
    }

    return {
        getXY : function(length) {
            if (length == -1) {
                return null;
            }
            var x = inversionX(length % boardSize);
            var y = inversionY(Math.trunc(length / boardSize));
            return new Point(x, y);
        },

        getLength : function(x, y) {
            var xx = inversionX(x);
            var yy = inversionY(y);
            return yy*boardSize + xx;
        }
    };
};

var Board = function(board){
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
        result = result.concat(findAll(Elements.TANK_UP));
        result = result.concat(findAll(Elements.TANK_DOWN));
        result = result.concat(findAll(Elements.TANK_LEFT));
        result = result.concat(findAll(Elements.TANK_RIGHT));
        if (result.lenght == 0) {
            return null;
        }
        return result[0];
    };

    var getEnemies = function() {
        var result = [];
        result = result.concat(findAll(Elements.AI_TANK_UP));
        result = result.concat(findAll(Elements.AI_TANK_DOWN));
        result = result.concat(findAll(Elements.AI_TANK_LEFT));
        result = result.concat(findAll(Elements.AI_TANK_RIGHT));
        result = result.concat(findAll(Elements.OTHER_TANK_UP));
        result = result.concat(findAll(Elements.OTHER_TANK_DOWN));
        result = result.concat(findAll(Elements.OTHER_TANK_LEFT));
        result = result.concat(findAll(Elements.OTHER_TANK_RIGHT));
        return result;
    };

    var getBullets = function() {
        var result = [];
        result = result.concat(findAll(Elements.BULLET));
        return result;
    }

    var isGameOver = function() {
        return getMe() == null;
    };

    var isBulletAt = function(x, y) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }

        return getAt(x, y) == Elements.BULLET;
    }

    var isAt = function(x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y) == element;
    };

    var getAt = function(x, y) {
        if (pt(x, y).isOutOf(size)) {
            return Elements.BATTLE_WALL;
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
        result = result.concat(findAll(Elements.BATTLE_WALL));
        result = result.concat(findAll(Elements.WALL));
        result = result.concat(findAll(Elements.WALL_DESTROYED_DOWN));
        result = result.concat(findAll(Elements.WALL_DESTROYED_UP));
        result = result.concat(findAll(Elements.WALL_DESTROYED_LEFT));
        result = result.concat(findAll(Elements.WALL_DESTROYED_RIGHT));
        result = result.concat(findAll(Elements.WALL_DESTROYED_DOWN_TWICE));
        result = result.concat(findAll(Elements.WALL_DESTROYED_UP_TWICE));
        result = result.concat(findAll(Elements.WALL_DESTROYED_LEFT_TWICE));
        result = result.concat(findAll(Elements.WALL_DESTROYED_RIGHT_TWICE));
        result = result.concat(findAll(Elements.WALL_DESTROYED_LEFT_RIGHT));
        result = result.concat(findAll(Elements.WALL_DESTROYED_UP_DOWN));
        result = result.concat(findAll(Elements.WALL_DESTROYED_UP_LEFT));
        result = result.concat(findAll(Elements.WALL_DESTROYED_RIGHT_UP));
        result = result.concat(findAll(Elements.WALL_DESTROYED_DOWN_LEFT));
        result = result.concat(findAll(Elements.WALL_DESTROYED_DOWN_RIGHT));
        return sort(result);
    };

    var toString = function() {
        return util.format("Board:\n%s\n" +
            "My tank at: %s\n" +
            "Enemies at: %s\n" +
            "Bulets at: %s\n",
                boardAsString(),
                getMe(),
                getEnemies(),
                getBullets()
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
        if (pt(x, y).isOutOf(size)) {
            return true;
        }

        return contains(getBarriers(), pt(x, y));
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

var random = function(n){
    return Math.floor(Math.random()*n);
};

var direction;

var DirectionSolver = function(board){

    return {
        /**
         * @return next hero action
         */
        get : function() {
            var tank = board.getMe();

            // TODO your code here

            return "ACT";
        }
    };
};

