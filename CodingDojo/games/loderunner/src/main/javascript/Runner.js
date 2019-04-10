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

// TODO test me

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

    var logMessage = /*board +*/ "\n";
    var answer = new DirectionSolver(board).get().toString();
    logMessage += "Answer: " + answer + "\n";
    logMessage += "-----------------------------------\n";

    log(logMessage);

    return answer;
};

// you can get this code after registration on the server with your email
var url = "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890";

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
    /// a void
    NONE : ' ',

    /// walls
    BRICK : '#',
    PIT_FILL_1 : '1',
    PIT_FILL_2 : '2',
    PIT_FILL_3 : '3',
    PIT_FILL_4 : '4',
    UNDESTROYABLE_WALL : '☼',

    DRILL_PIT : '*',

    // this is enemy
    ENEMY_LADDER : 'Q',
    ENEMY_LEFT : '«',
    ENEMY_RIGHT : '»',
    ENEMY_PIPE_LEFT : '<',
    ENEMY_PIPE_RIGHT : '>',
    ENEMY_PIT : 'X',

    /// gold ;)
    GOLD : '$',

    /// this is you
    HERO_DIE : 'Ѡ',
    HERO_DRILL_LEFT : 'Я',
    HERO_DRILL_RIGHT : 'R',
    HERO_LADDER : 'Y',
    HERO_LEFT : '◄',
    HERO_RIGHT : '►',
    HERO_FALL_LEFT : ']',
    HERO_FALL_RIGHT : '[',
    HERO_PIPE_LEFT : '{',
    HERO_PIPE_RIGHT : '}',

    /// this is other players
    OTHER_HERO_DIE : 'Z',
    OTHER_HERO_LEFT : ')',
    OTHER_HERO_RIGHT : ' : ',
    OTHER_HERO_LADDER : 'U',
    OTHER_HERO_PIPE_LEFT : 'Э',
    OTHER_HERO_PIPE_RIGHT : 'Є',

    /// ladder and pipe - you can walk
    LADDER : 'H',
    PIPE : '~'
};

var D = function(index, dx, dy, name) {

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
            case Direction.DRILL_LEFT : return Direction.DRILL_RIGHT;
            case Direction.DRILL_RIGHT : return Direction.DRILL_LEFT;
            default : return Direction.STOP;
        }
    };

    var toString = function() {
        return name;
    };

    return {
        changeX : changeX,
        changeY : changeY,
        inverted : inverted,
        toString : toString,

        getIndex : function() {
            return index;
        }
    };
};

var Direction = {
    UP          : D(2,  0, -1, 'UP'),         // move up
    DOWN        : D(3,  0,  1, 'DOWN'),       // move down
    LEFT        : D(0, -1,  0, 'LEFT'),
    RIGHT       : D(1,  1,  0, 'RIGHT'),
    DRILL_LEFT  : D(4,  0,  0, 'ACT,LEFT'),   // drill ground and move left
    DRILL_RIGHT : D(5,  0,  0, 'ACT,RIGHT'),  // drill ground and move right
    STOP        : D(6,  0,  0, ''),           // stay
    DIE         : D(8,  0,  0, "ACT(0)")      // suicide
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.DRILL_LEFT, Direction.DRILL_RIGHT, Direction.STOP, Direction.DIE];
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

var Board = function(board) {
    var contains = function(a, obj) {
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
            return pt1.getY()*1000 + pt1.getX() - pt2.getY()*1000 + pt2.getX();
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
        result = result.concat(findAll(Elements.HERO_DIE));
        result = result.concat(findAll(Elements.HERO_DRILL_LEFT));
        result = result.concat(findAll(Elements.HERO_DRILL_RIGHT));
        result = result.concat(findAll(Elements.HERO_FALL_RIGHT));
        result = result.concat(findAll(Elements.HERO_FALL_LEFT));
        result = result.concat(findAll(Elements.HERO_LADDER));
        result = result.concat(findAll(Elements.HERO_LEFT));
        result = result.concat(findAll(Elements.HERO_RIGHT));
        result = result.concat(findAll(Elements.HERO_PIPE_LEFT));
        result = result.concat(findAll(Elements.HERO_PIPE_RIGHT));
        return result[0];
    };

    var getOtherHeroes = function() {
        var result = [];
        result = result.concat(findAll(Elements.OTHER_HERO_LEFT));
        result = result.concat(findAll(Elements.OTHER_HERO_RIGHT));
        result = result.concat(findAll(Elements.OTHER_HERO_LADDER));
        result = result.concat(findAll(Elements.OTHER_HERO_PIPE_LEFT));
        result = result.concat(findAll(Elements.OTHER_HERO_PIPE_RIGHT));
        return result;
    };

    var getEnemies = function() {
        var result = [];
        result = result.concat(findAll(Elements.ENEMY_LADDER));
        result = result.concat(findAll(Elements.ENEMY_LADDER));
        result = result.concat(findAll(Elements.ENEMY_LEFT));
        result = result.concat(findAll(Elements.ENEMY_PIPE_LEFT));
        result = result.concat(findAll(Elements.ENEMY_PIPE_RIGHT));
        result = result.concat(findAll(Elements.ENEMY_RIGHT));
        result = result.concat(findAll(Elements.ENEMY_PIT));
        return result;
    };

    var getGold = function() {
        return findAll(Elements.GOLD);
    };

    var getWalls = function() {
        var result = [];
        result = result.concat(findAll(Elements.BRICK));
        result = result.concat(findAll(Elements.UNDESTROYABLE_WALL));
        return result;
    };

    var getLadders = function() {
        var result = [];
        result = result.concat(findAll(Elements.LADDER));
        result = result.concat(findAll(Elements.HERO_LADDER));
        result = result.concat(findAll(Elements.ENEMY_LADDER));
        return result;
    };

    var getPipes = function() {
        var result = [];
        result = result.concat(findAll(Elements.PIPE));
        result = result.concat(findAll(Elements.HERO_PIPE_LEFT));
        result = result.concat(findAll(Elements.HERO_PIPE_RIGHT));
        result = result.concat(findAll(Elements.OTHER_HERO_PIPE_LEFT));
        result = result.concat(findAll(Elements.OTHER_HERO_PIPE_RIGHT));
        return result;
    };

    var isGameOver = function() {
        return board.indexOf(Elements.HERO_DIE) != -1;
    };

    var isAt = function(x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y) == element;
    };

    var getAt = function(x, y) {
        if (pt(x, y).isOutOf(size)) {
            return Elements.UNDESTROYABLE_WALL;
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
        var all = getWalls();
        all = all.concat(getEnemies());
        all = all.concat(getOtherHeroes());
        all = all.concat(getWalls());
        return removeDuplicates(all);
    };

    var toString = function() {
        return util.format("Board:\n%s\n" +
            "Me at: %s\n" +
            "Other heroes at: %s\n" +
            "Enemies at: %s\n" +
            "Gold at: %s\n",
                boardAsString(),
                getMe(),
                printArray(getOtherHeroes()),
                printArray(getEnemies()),
                printArray(getGold())
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

    var isNear = function(x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(x + 1, y, element) || // TODO to remove duplicate
            isAt(x - 1, y, element) ||
            isAt(x, y + 1, element) ||
            isAt(x, y - 1, element);
    };

    var isBarrierAt = function(x, y) {
        if (pt(x, y).isOutOf(size)) {
            return true;
        }
        return contains(getBarriers(), pt(x, y));
    };

    var hasEnemyAt = function(x, y) {
        return isAnyOfAt(x, y,
            [Elements.ENEMY_LADDER, Elements.ENEMY_LEFT, Elements.ENEMY_PIPE_LEFT, Elements.ENEMY_PIPE_RIGHT, Elements.ENEMY_PIT, Elements.ENEMY_RIGHT]);
    };

    var hasOtherHeroAt = function(x, y) {
        return isAnyOfAt(x, y,
            [Elements.OTHER_HERO_LEFT, Elements.OTHER_HERO_RIGHT, Elements.OTHER_HERO_LADDER, Elements.OTHER_HERO_PIPE_LEFT, Elements.OTHER_HERO_PIPE_RIGHT]);
    };

    var hasWallAt = function(x, y) {
        if (pt(x, y).isOutOf(size)) {
            return true;
        }
        return isAnyOfAt(x, y, [Elements.BRICK, Elements.UNDESTROYABLE_WALL]);
    };

    var hasLadderAt = function(x, y) {
        return isAnyOfAt(x, y, [Elements.LADDER, Elements.HERO_LADDER, Elements.ENEMY_LADDER]);
    };

    var hasGoldAt = function(x, y) {
        return isAt(x, y, Elements.GOLD);
    };

    var hasPipeAt = function(x, y) {
        return isAnyOfAt(x, y,
            [Elements.PIPE, Elements.HERO_PIPE_LEFT, Elements.HERO_PIPE_RIGHT, Elements.OTHER_HERO_PIPE_LEFT, Elements.OTHER_HERO_PIPE_RIGHT]);
    };

    var countNear = function(x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(x - 1, y    , element)) count ++; // TODO to remove duplicate
        if (isAt(x + 1, y    , element)) count ++;
        if (isAt(x    , y - 1, element)) count ++;
        if (isAt(x    , y + 1, element)) count ++;
        return count;
    };

    return {
        size : boardSize,
        getMe : getMe,
        getOtherHeroes : getOtherHeroes,
        isGameOver : isGameOver,
        isAt : isAt,
        getAt : getAt,
        boardAsString : boardAsString,
        getBarriers : getBarriers,
        toString : toString,
        findAll : findAll,
        getWalls : getWalls,
        getLadders : getLadders,
        getPipes : getPipes,
        getGold : getGold,
        isAnyOfAt : isAnyOfAt,
        isNear : isNear,
        isBarrierAt : isBarrierAt,
        hasEnemyAt : hasEnemyAt,
        hasOtherHeroAt : hasOtherHeroAt,
        hasWallAt : hasWallAt,
        hasLadderAt : hasLadderAt,
        hasGoldAt : hasGoldAt,
        hasPipeAt : hasPipeAt,
        countNear : countNear
    };
};

var random = function(n) {
    return Math.floor(Math.random()*n);
};

var direction;

var DirectionSolver = function(board) {
    return {
        /**
         * @return next hero action
         */
        get : function() {
            var me = board.getMe();
            //console.log(me.getX(), me.getY());

            // TODO your code here
            var dir = Direction.values()[random(6)];  // STUB get any random direction except Direction.DIE

            //return Direction.DIE;  // for suicide
            return dir;
        }
    };
};
