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

var log = function (string) {
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

var processBoard = function (boardString) {
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
var url = "http://codenjoy.com:80/codenjoy-contest/board/player/your@email.com?code=12345678901234567890";

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

var Element = {
      NONE: ' ',        // например это пустое место, куда можно перейти герою
      WALL: '0',        // а это стенка, через которую я хочу чтобы проходить нельзя было
      START_FLOOR: '#', // место старта змей
      OTHER: '?',       // TODO а это что за чудо?

      APPLE: '0',
      STONE: '?',
      FLYING_PILL: '©',
      FURY_PILL: '®',
      GOLD: '$',

      // игрок
      HEAD_DOWN: 'Ў',
      HEAD_LEFT: '<',
      HEAD_RIGHT: '>',
      HEAD_UP: '^',
      HEAD_DEAD: 'O',
      HEAD_EVIL: '¦',
      HEAD_FLY: '¦',
      HEAD_SLEEP: '&',

      TAIL_END_DOWN: 'L',
      TAIL_END_LEFT: 'L',
      TAIL_END_UP: 'г',
      TAIL_END_RIGHT: '¬',
      TAIL_INACTIVE: '~',

      BODY_HORIZONTAL: '=',
      BODY_VERTICAL: '¦',
      BODY_LEFT_DOWN: '¬',
      BODY_LEFT_UP: '-',
      BODY_RIGHT_DOWN: 'г',
      BODY_RIGHT_UP: 'L',

      // противник
      ENEMY_HEAD_DOWN: '?',
      ENEMY_HEAD_LEFT: '<',
      ENEMY_HEAD_RIGHT: '>',
      ENEMY_HEAD_UP: '?',
      ENEMY_HEAD_DEAD: 'O',
      ENEMY_HEAD_EVIL: '¦',
      ENEMY_HEAD_FLY: '¦',
      ENEMY_HEAD_SLEEP: 'o',

      ENEMY_TAIL_END_DOWN: '¤',
      ENEMY_TAIL_END_LEFT: '?',
      ENEMY_TAIL_END_UP: '?',
      ENEMY_TAIL_END_RIGHT: 'o',
      ENEMY_TAIL_INACTIVE: '*',

      ENEMY_BODY_HORIZONTAL: '-',
      ENEMY_BODY_VERTICAL: '¦',
      ENEMY_BODY_LEFT_DOWN: '¬',
      ENEMY_BODY_LEFT_UP: '-',
      ENEMY_BODY_RIGHT_DOWN: '-',
      ENEMY_BODY_RIGHT_UP: 'L'
};

var D = function (index, dx, dy, name) {

    var changeX = function (x) {
        return x + dx;
    };

    var changeY = function (y) {
        return y - dy;
    };

    var inverted = function () {
        switch (this) {
            case Direction.UP :
                return Direction.DOWN;
            case Direction.DOWN :
                return Direction.UP;
            case Direction.LEFT :
                return Direction.RIGHT;
            case Direction.RIGHT :
                return Direction.LEFT;
            default :
                return Direction.STOP;
        }
    };

    var toString = function () {
        return name;
    };

    return {
        changeX: changeX,

        changeY: changeY,

        inverted: inverted,

        toString: toString,

        getIndex: function () {
            return index;
        }
    };
};

var Direction = {
    UP: D(2, 0, 1, 'up'),                 // you can move
    DOWN: D(3, 0, -1, 'down'),
    LEFT: D(0, -1, 0, 'left'),
    RIGHT: D(1, 1, 0, 'right'),
    ACT: D(4, 0, 0, 'act'),                // drop bomb
    STOP: D(5, 0, 0, '')                   // stay
};

Direction.values = function () {
    return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.ACT, Direction.STOP];
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
            return x >= boardSize || y >= boardSize || x < 0 || y < 0;
        },

        getX: function () {
            return x;
        },

        getY: function () {
            return y;
        }
    }
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
            var y = inversionY(Math.ceil(length / boardSize));
            return new Point(x, y);
        },

        getLength: function (x, y) {
            var xx = inversionX(x);
            var yy = inversionY(y);
            return yy * boardSize + xx;
        }
    };
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

    var removeDuplicates = function (all) {
        var result = [];
        for (var index in all) {
            var point = all[index];
            if (!contains(result, point)) {
                result.push(point);
            }
        }
        return result;
    };

    var boardSize = function () {
        return Math.sqrt(board.length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getBomberman = function () {
        var result = [];
        result = result.concat(findAll(Element.BOMBERMAN));
        result = result.concat(findAll(Element.BOMB_BOMBERMAN));
        result = result.concat(findAll(Element.DEAD_BOMBERMAN));
        return result[0];
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getOtherBombermans = function () {
        var result = [];
        result = result.concat(findAll(Element.OTHER_BOMBERMAN));
        result = result.concat(findAll(Element.OTHER_BOMB_BOMBERMAN));
        result = result.concat(findAll(Element.OTHER_DEAD_BOMBERMAN));
        return result;
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var isMyBombermanDead = function () {
        return board.indexOf(Element.DEAD_BOMBERMAN) != -1;
    };

    var isAt = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y) == element;
    };

    var getAt = function (x, y) {
        if (pt(x, y).isOutOf(size)) {
            return Element.WALL;
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

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getBarriers = function () {
        var all = getMeatChoppers();
        all = all.concat(getWalls());
        all = all.concat(getBombs());
        all = all.concat(getDestroyWalls());
        all = all.concat(getOtherBombermans());
        all = all.concat(getFutureBlasts());
        return removeDuplicates(all);
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var toString = function () {
        return util.format("%s\n" +
            "Bomberman at: %s\n" +
            "Other bombermans at: %s\n" +
            "Meat choppers at: %s\n" +
            "Destroy walls at: %s\n" +
            "Bombs at: %s\n" +
            "Blasts: %s\n" +
            "Expected blasts at: %s",
            boardAsString(),
            getBomberman(),
            printArray(getOtherBombermans()),
            printArray(getMeatChoppers()),
            printArray(getDestroyWalls()),
            printArray(getBombs()),
            printArray(getBlasts()),
            printArray(getFutureBlasts()));
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getMeatChoppers = function () {
        return findAll(Element.MEAT_CHOPPER);
    };

    var findAll = function (element) {
        var result = [];
        for (var i = 0; i < size * size; i++) {
            var point = xyl.getXY(i);
            if (isAt(point.getX(), point.getY(), element)) {
                result.push(point);
            }
        }
        return result;
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getWalls = function () {
        return findAll(Element.WALL);
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getDestroyWalls = function () {
        return findAll(Element.DESTROYABLE_WALL);
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getBombs = function () {
        var result = [];
        result = result.concat(findAll(Element.BOMB_TIMER_1));
        result = result.concat(findAll(Element.BOMB_TIMER_2));
        result = result.concat(findAll(Element.BOMB_TIMER_3));
        result = result.concat(findAll(Element.BOMB_TIMER_4));
        result = result.concat(findAll(Element.BOMB_TIMER_5));
        result = result.concat(findAll(Element.BOMB_BOMBERMAN));
        result = result.concat(findAll(Element.OTHER_BOMB_BOMBERMAN));
        return result;
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getBlasts = function () {
        return findAll(Element.BOOM);
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    var getFutureBlasts = function () {
        var bombs = getBombs();
        var result = [];
        for (var index in bombs) {
            var bomb = bombs[index];
            result.push(bomb);
            result.push(new Point(bomb.getX() - 1, bomb.getY())); // TODO to remove duplicate
            result.push(new Point(bomb.getX() + 1, bomb.getY()));
            result.push(new Point(bomb.getX(), bomb.getY() - 1));
            result.push(new Point(bomb.getX(), bomb.getY() + 1));
        }
        var result2 = [];
        for (var index in result) {
            var blast = result[index];
            if (blast.isOutOf(size) || contains(getWalls(), blast)) {
                continue;
            }
            result2.push(blast);
        }
        return removeDuplicates(result2);
    };

    var isAnyOfAt = function (x, y, elements) {
        for (var index in elements) {
            var element = elements[index];
            if (isAt(x, y, element)) {
                return true;
            }
        }
        return false;
    };

    var isNear = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(x + 1, y, element) || // TODO to remove duplicate
            isAt(x - 1, y, element) ||
            isAt(x, y + 1, element) ||
            isAt(x, y - 1, element);
    };

    var isBarrierAt = function (x, y) {
        return contains(getBarriers(), pt(x, y));
    };

    var countNear = function (x, y, element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(x - 1, y, element)) count++; // TODO to remove duplicate
        if (isAt(x + 1, y, element)) count++;
        if (isAt(x, y - 1, element)) count++;
        if (isAt(x, y + 1, element)) count++;
        return count;
    };

    // TODO:BATTLE исправить метод на аналогичный для snakebattle
    return {
        size: boardSize,
        getBomberman: getBomberman,
        getOtherBombermans: getOtherBombermans,
        isMyBombermanDead: isMyBombermanDead,
        isAt: isAt,
        boardAsString: boardAsString,
        getBarriers: getBarriers,
        toString: toString,
        getMeatChoppers: getMeatChoppers,
        findAll: findAll,
        getWalls: getWalls,
        getDestroyWalls: getDestroyWalls,
        getBombs: getBombs,
        getBlasts: getBlasts,
        getFutureBlasts: getFutureBlasts,
        isAnyOfAt: isAnyOfAt,
        isNear: isNear,
        isBarrierAt: isBarrierAt,
        countNear: countNear,
        getAt: getAt
    };
};

var random = function (n) {
    return Math.floor(Math.random() * n);
};

var direction;

var DirectionSolver = function (board) {

    return {
        /**
         * @return next hero action
         */
        get: function () {
            var bomberman = board.getBomberman();

            // TODO your code here

            return Direction.ACT;
        }
    };
};
