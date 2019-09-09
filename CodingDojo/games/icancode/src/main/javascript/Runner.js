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

// you can get this code after registration on the server with your email
var url = "http://codenjoy.com/codenjoy-contest/board/player/5q0uczbju6no468bg6w0?code=1725193577353578184&gameName=icancode";

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
   return result;
};

var processBoard = function(boardString) {
    var boardJson = eval(boardString);
    var board = new Board(boardJson);
    if (!!printBoardOnTextArea) {
        printBoardOnTextArea(board.toString());
    }

    var logMessage = board + "\n\n";
    var answer = new YourSolver(board).whatToDo().toString();
    logMessage += "Answer: " + answer + "\n";
    logMessage += "---------------------------------------------------------------------------------------------------------\n";
    
    log(logMessage);

    return answer;
};

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
        var answer = processBoard(message);
        ws.send(answer);
    });
}

connect();

var elements = [];
var elementsTypes = [];
var elementsByChar = {};
var elementsByType = {};

var el = function(char, type, direction) {
    var result = {
        char: char,
        type: type,
        direction: direction
    };

    elementsByChar[char] = result;

    if (!elementsByType[type]) {
        elementsByType[type] = [];
    }

    elementsByType[type].push(result);
    elements.push(result);

    if (elementsTypes.indexOf(type) == -1) {
        elementsTypes.push(type);
    }

    return result;
}

var D = function(index, dx, dy, name){

    var change = function(pt) {
        return pt(changeX(pt.getX()), changeY(pt.getY()));
    };

    var changeX = function(x) {
        return x + dx;
    };

    var changeY = function(y) {
        return y - dy;
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
            case Direction.LEFT : return Direction.UP;
            case Direction.UP : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.DOWN;
            case Direction.DOWN : return Direction.LEFT;
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
    UP   : D(2, 0, 1, 'up'),                // you can move
    DOWN : D(3, 0, -1, 'down'),
    LEFT : D(0, -1, 0, 'left'),
    RIGHT : D(1, 1, 0, 'right'),
    JUMP : D(4, 0, 0, 'act(1)'),            // jump
    PULL : D(5, 0, 0, 'act(2)'),            // pull box
    FIRE : D(6, 0, 0, 'act(3)'),            // fire
    DIE  : D(7, 0, 0, 'act(0)'),            // die
    STOP : D(8, 0, 0, '')                   // stay
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.JUMP, Direction.PULL, Direction.FIRE, Direction.DIE, Direction.STOP];
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

var Element = {
    EMPTY: el('-', 'NONE'),
    FLOOR: el('.', 'NONE'),

    ANGLE_IN_LEFT: el('╔', 'WALL'),
    WALL_FRONT: el('═', 'WALL'),
    ANGLE_IN_RIGHT: el('┐', 'WALL'),
    WALL_RIGHT: el('│', 'WALL'),
    ANGLE_BACK_RIGHT: el('┘', 'WALL'),
    WALL_BACK: el('─', 'WALL'),
    ANGLE_BACK_LEFT: el('└', 'WALL'),
    WALL_LEFT: el('║', 'WALL'),
    WALL_BACK_ANGLE_LEFT: el('┌', 'WALL'),
    WALL_BACK_ANGLE_RIGHT: el('╗', 'WALL'),
    ANGLE_OUT_RIGHT: el('╝', 'WALL'),
    ANGLE_OUT_LEFT: el('╚', 'WALL'),
    SPACE: el(' ', 'WALL'),

    LASER_MACHINE_CHARGING_LEFT: el('˂', 'LASER_MACHINE', Direction.LEFT),
    LASER_MACHINE_CHARGING_RIGHT: el('˃', 'LASER_MACHINE', Direction.RIGHT),
    LASER_MACHINE_CHARGING_UP: el('˄', 'LASER_MACHINE', Direction.UP),
    LASER_MACHINE_CHARGING_DOWN: el('˅', 'LASER_MACHINE', Direction.DOWN),

    LASER_MACHINE_READY_LEFT: el('◄', 'LASER_MACHINE_READY', Direction.LEFT),
    LASER_MACHINE_READY_RIGHT: el('►', 'LASER_MACHINE_READY', Direction.RIGHT),
    LASER_MACHINE_READY_UP: el('▲', 'LASER_MACHINE_READY', Direction.UP),
    LASER_MACHINE_READY_DOWN: el('▼', 'LASER_MACHINE_READY', Direction.DOWN),

    START: el('S', 'START'),
    EXIT: el('E', 'EXIT'),
    HOLE: el('O', 'HOLE'),
    BOX: el('B', 'BOX'),
    ZOMBIE_START: el('Z', 'ZOMBIE_START'),
    GOLD: el('$', 'GOLD'),

    ROBOT: el('☺', 'MY_ROBOT'),
    ROBOT_FALLING: el('o', 'MY_ROBOT'),
    ROBOT_FLYING: el('*', 'MY_ROBOT'),
    ROBOT_LASER: el('☻', 'MY_ROBOT'),

    ROBOT_OTHER: el('X', 'OTHER_ROBOT'),
    ROBOT_OTHER_FALLING: el('x', 'OTHER_ROBOT'),
    ROBOT_OTHER_FLYING: el('^', 'OTHER_ROBOT'),
    ROBOT_OTHER_LASER: el('&', 'OTHER_ROBOT'),

    LASER_LEFT: el('←', 'LASER_LEFT', Direction.LEFT),
    LASER_RIGHT: el('→', 'LASER_RIGHT', Direction.RIGHT),
    LASER_UP: el('↑', 'LASER_UP', Direction.UP),
    LASER_DOWN: el('↓', 'LASER_DOWN', Direction.DOWN),

    FEMALE_ZOMBIE: el('♀', 'ZOMBIE'),
    MALE_ZOMBIE: el('♂', 'ZOMBIE'),
    ZOMBIE_DIE: el('✝', 'ZOMBIE_DIE'),

    getElements: function () {
        return elements.slice(0);
    },

    getElement: function (char) {
        var el = elementsByChar[char];
        if (!el) {
            throw "Element not found for: " + char;
        }
        return el;
    },

    getElementsTypes: function () {
        var elements = [];
        elementsTypes.forEach(function(e) {
            if (Array.isArray(e)) {
                elements = elements.concat(e);
            } else {
                elements.push(e);
            }
        });

        var result = [];
        elements.forEach(function(e) {
            if (result.indexOf(e) < 0) {
                result.push(e);
            }
        });

        return result;
    },

    getElementsOfType: function (type) {
        return elementsByType[type];
    },

    isWall: function(element) {
        return element.type == 'WALL';
    }
};

var Point = function (x, y, direction) {
    return {
        x: x,
        y: y,
        direction: direction,

        equals: function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString: function () {
            return '[' + x + ',' + y + (!!direction ? (',' + direction) : '') + ']';
        },

        isBad: function (boardSize) {
            return x >= boardSize || y >= boardSize || x < 0 || y < 0;
        },

        getX: function () {
            return x;
        },

        getY: function () {
            return y;
        },

        move: function (dx, dy) {
            x += dx;
            y += dy;
        }
    }
};

var pt = function (x, y) {
    return new Point(x, y);
};

var LAYER1 = 0;
var LAYER2 = 1;
var LAYER3 = 2;

var LengthToXY = function (boardSize) {
    var inversion = function (y) {
        return boardSize - 1 - y;
    }

    return {
        getXY: function (length) {
            if (length == -1) {
                return null;
            }
            return new Point(length % boardSize, Math.trunc(length / boardSize));
        },

        getLength: function (x, y) {
            return inversion(y) * boardSize + x;
        }
    };
};

var Board = function (board) {    
    var layersString = board.layers;
    var scannerOffset = board.offset;
    var heroPosition = board.heroPosition;
    var levelFinished = board.levelFinished;
    var size = Math.sqrt(layersString[LAYER1].length);
    var xyl = new LengthToXY(size);

    var parseLayer = function (layer) {
        var map = [];
        for (var x = 0; x < size; x++) {
            map[x] = [];
            for (var y = 0; y < size; y++) {
                map[x][y] = Element.getElement(layer.charAt(xyl.getLength(x, y)))
            }
        }
        return map;
    }

    var layers = [];
    for (var index in layersString) {
        layers.push(parseLayer(layersString[index]));
    }

    // TODO to add List<Elements> getNear(int numLayer, int x, int y) method

    var isAt = function (layer, x, y, elements) {
        if (!Array.isArray(elements)) {
            var arr = [];
            arr.push(elements);
            elements = arr;
        }

        if (pt(x, y).isBad(size) || getAt(layer, x, y) == null) {
            return false;
        }

        var found = false;
        for (var e in elements) {
            var element = elements[e];
            found |= (getAt(layer, x, y).char == element.char);
        }
        return found;
    };

    var getAt = function (layer, x, y) {
        return layers[layer][x][y];
    };

    var removeAllElements = function(array, element) {
        var index;
        while ((index = array.indexOf(element)) !== -1) {
            array.splice(index, 1);
        }
        return array;
    }

    var getWholeBoard = function() {
        var result = [];
        for (var x = 0; x < size; x++) {
            var arr = [];
            result.push(arr);
            for (var y = 0; y < size; y++) {
                var cell = [];
                cell.push(getAt(LAYER1, x, y).type);
                cell.push(getAt(LAYER2, x, y).type);
                cell.push(getAt(LAYER3, x, y).type);
                removeAllElements(cell, 'NONE');
                if (cell.length == 0) {
                    cell.push('NONE');
                }

                arr.push(cell);
            }
        }
        return result;
    }

    var get = function (layer, elements) {
        if (!Array.isArray(elements)) {
            var arr = [];
            arr.push(elements);
            elements = arr;
        }
        var result = [];
        for (var x = 0; x < size; x++) {
            for (var y = 0; y < size; y++) {
                for (var e in elements) {
                    var element = elements[e];
                    if (isAt(layer, x, y, element)) {
                        result.push(element.direction ? new Point(x, y, element.direction.toString()) : new Point(x, y));
                    }
                }
            }
        }
        return result;
    };

    var isAnyOfAt = function (x, y, layer, elements) {
        for (var index in elements) {
            var element = elements[index];
            if (isAt(layer, x, y, element)) {
                return true;
            }
        }
        return false;
    };

    var isNear = function (layer, x, y, element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return isAt(layer, x + 1, y, element) || isAt(layer, x - 1, y, element)
            || isAt(layer, x, y + 1, element) || isAt(layer, x, y - 1, element);
    };

    var isBarrierAt = function (x, y) {
        if (!barriersMap) {
            getBarriers();
        }
        return barriersMap[x][y];
    };

    var isWallAt = function (x, y) {
        return getAt(LAYER1, x, y).type == 'WALL';
    };

    var countNear = function (layer, x, y, element) {
        if (pt(x, y).isBad(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(layer, x + 1, y, element)) count++;
        if (isAt(layer, x - 1, y, element)) count++;
        if (isAt(layer, x, y - 1, element)) count++;
        if (isAt(layer, x, y + 1, element)) count++;
        return count;
    };

    var getOtherHeroes = function () {
        var elements = [Element.ROBOT_OTHER, Element.ROBOT_OTHER_FALLING, Element.ROBOT_OTHER_LASER];
        return get(LAYER2, elements)
            .concat(get(LAYER3, Element.ROBOT_OTHER_FLYING));
    };

    var getLaserMachines = function () {
        var elements = [Element.LASER_MACHINE_CHARGING_LEFT, Element.LASER_MACHINE_CHARGING_RIGHT,
            Element.LASER_MACHINE_CHARGING_UP, Element.LASER_MACHINE_CHARGING_DOWN,
            Element.LASER_MACHINE_READY_LEFT, Element.LASER_MACHINE_READY_RIGHT,
            Element.LASER_MACHINE_READY_UP, Element.LASER_MACHINE_READY_DOWN];
        return get(LAYER1, elements);
    };

    var getLasers = function () {
        var elements = [Element.LASER_LEFT, Element.LASER_RIGHT,
            Element.LASER_UP, Element.LASER_DOWN];
        return get(LAYER2, elements);
    };

    var getWalls = function () {
        var elements = [Element.ANGLE_IN_LEFT, Element.WALL_FRONT,
            Element.ANGLE_IN_RIGHT, Element.WALL_RIGHT,
            Element.ANGLE_BACK_RIGHT, Element.WALL_BACK,
            Element.ANGLE_BACK_LEFT, Element.WALL_LEFT,
            Element.WALL_BACK_ANGLE_LEFT, Element.WALL_BACK_ANGLE_RIGHT,
            Element.ANGLE_OUT_RIGHT, Element.ANGLE_OUT_LEFT,
            Element.SPACE];
        return get(LAYER1, elements);
    };

    var getBoxes = function () {
        return get(LAYER2, Element.BOX);
    };

    var getStarts = function () {
        return get(LAYER1, Element.START);
    };

    var getZombieStart = function () {
        return get(LAYER1, Element.ZOMBIE_START);
    };

    var getExits = function () {
        return get(LAYER1, Element.EXIT);
    };

    var getGold = function () {
        return get(LAYER1, Element.GOLD);
    };

    var getZombies = function () {
        var elements = [Element.FEMALE_ZOMBIE, Element.MALE_ZOMBIE,
                    Element.ZOMBIE_DIE];
        return get(LAYER2, elements);
    };

    var getHoles = function () {
        return get(LAYER1, Element.HOLE);
    };

    var isMeAlive = function () {
        return layersString[LAYER2].indexOf(Element.ROBOT_LASER.char) == -1 &&
            layersString[LAYER2].indexOf(Element.ROBOT_FALLING.char) == -1;
    };

    var barriers = null;
    var barriersMap = null;
    var getBarriers = function () {
        if (!!barriers) {
            return barriers;
        }

        barriers = [];
        barriersMap = Array(size);
        for (var x = 0; x < size; x++) {
            barriersMap[x] = new Array(size);
            for (var y = 0; y < size; y++) {
                var element1 = getAt(LAYER1, x, y);
                var element2 = getAt(LAYER2, x, y);

                barriersMap[x][y] = (
                    element1.type == 'WALL' ||
                    element1 == Element.HOLE ||
                    element2 == Element.BOX ||
                    !!element1.direction
                );

                if (barriersMap[x][y]) {
                    barriers.push(pt(x, y));
                }
            }
        }
        return barriers;
    };

    var getFromArray = function(x, y, array, def) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            return def;
        }
        return array[x][y];
    }

    var isBarrier = function(x, y) {
        return getFromArray(x, y, barriersMap, true);
    }

    var boardAsString = function (layer) {
        var result = "";
        for (var i = 0; i <= size - 1; i++) {
            result += layersString[layer].substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    var getMe = function() {
        return pt(heroPosition.x, heroPosition.y);
    }

    // thanks http://jsfiddle.net/queryj/g109jvxd/
    String.format = function () {
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

    var setCharAt = function(str, index, replacement) {
        return str.substr(0, index) + replacement + str.substr(index + replacement.length);
    }

    var maskOverlay = function(source, mask) {
        var result = source;
        for (var i = 0; i < result.length; ++i) {
            var el = Element.getElement(mask[i]);
            if (Element.isWall(el)) {
                result = setCharAt(result, i, el.char);
            }
        }

        return result.toString();
    }

    var toString = function () {
        var temp = '0123456789012345678901234567890';

        var result = '';

        var layer1 = boardAsString(LAYER1).split('\n').slice(0, -1);
        var layer2 = boardAsString(LAYER2).split('\n').slice(0, -1);
        var layer3 = boardAsString(LAYER3).split('\n').slice(0, -1);

        var numbers = temp.substring(0, layer1.length);
        var space = ''.padStart(layer1.length - 5);
        var numbersLine = numbers + '   ' + numbers + '   ' + numbers;
        var firstPart = ' Layer1 ' + space + ' Layer2' + space + ' Layer3' + '\n  ' + numbersLine;

        for (var i = 0; i < layer1.length; ++i) {
            var ii = size - 1 - i;
            var index = (ii < 10 ? ' ' : '') + ii;
            result += index + layer1[i] +
                    ' ' + index + maskOverlay(layer2[i], layer1[i]) +
                    ' ' + index + maskOverlay(layer3[i], layer1[i]);

            switch (i) {
                case 0:
                    result += ' Robots: ' + getMe() + ',' + printArray(getOtherHeroes());
                    break;
                case 1:
                    result += ' Gold: ' + printArray(getGold());
                    break;
                case 2:
                    result += ' Starts: ' + printArray(getStarts());
                    break;
                case 3:
                    result += ' Exits: ' + printArray(getExits());
                    break;
                case 4:
                    result += ' Boxes: ' + printArray(getBoxes());
                    break;
                case 5:
                    result += ' Holes: ' + printArray(getHoles());
                    break;
                case 6:
                    result += ' LaserMachine: ' + printArray(getLaserMachines());
                    break;
                case 7:
                    result += ' Lasers: ' + printArray(getLasers());
                    break;
                case 8:
                    result += ' Zombies: ' + printArray(getZombies());
                    break;
            }

            if (i != layer1.length - 1) {
                result += '\n';
            }
        }

        return firstPart + '\n' + result + '\n  ' + numbersLine;
    };

    return {
        size: function () {
            return size;
        },
        getMe: getMe, 
        isLevelFinished: function() {
            return levelFinished;
        },
        getOtherHeroes: getOtherHeroes, 
        getLaserMachines: getLaserMachines,
        getLasers: getLasers,
        getWalls: getWalls,
        getBoxes: getBoxes,
        getGold: getGold,
        getStarts: getStarts,
        getZombies: getZombies,
        getZombieStart: getZombieStart,
        getExits: getExits,
        getHoles: getHoles,
        isMeAlive: isMeAlive, 
        isAt: isAt, 
        getAt: getAt, 
        get: get, 
        toString: toString,
        layer1: function () {
            return boardAsString(LAYER1)
        },
        layer2: function () {
            return boardAsString(LAYER2)
        },
        layer3: function () {
            return boardAsString(LAYER3)
        },
        getWholeBoard: getWholeBoard,
        getBarriers: getBarriers,        
        isAnyOfAt: isAnyOfAt,
        isNear: isNear, 
        isBarrierAt: isBarrierAt, 
        countNear: countNear, 
        getScannerOffset: function () {
            return pt(scannerOffset.x, scannerOffset.y);
        }
    };
};

var random = function (n) {
    return Math.floor(Math.random() * n);
};

var Command = {

    /**
     * Says to Hero do nothing
     */
    doNothing : function() {
        return Direction.STOP.toString();
    },

    /**
     * Reset current level
     */
    die : function() {
        return Direction.DIE.toString();
    },

    /**
     * Says to Hero jump to direction
     */
    jump : function(direction) {
        return Direction.JUMP.toString() + "," + direction.toString();
    },

    /**
     * Says to Hero pull box on this direction
     */
    pull : function(direction) {
        return Direction.PULL.toString() + "," + direction.toString();
    },

    /**
     * Says to Hero fire on this direction
     */
    fire : function(direction) {
        return Direction.FIRE.toString() + "," + direction.toString();
    },

    /**
     * Says to Hero jump in place
     */
    jump : function() {
        return Direction.JUMP.toString();
    },

    /**
     * Says to Hero go to direction
     */
    go : function(direction) {
        return Direction.valueOf(direction.toString()).toString();
    },

    /**
     * Says to Hero goes to start point
     */
    reset : function() {
        return Direction.DIE.toString();
    }

}

var direction;

var YourSolver = function(board){

    return {
        /**
         * @return next robot action
         */
        whatToDo : function() {
            var hero = board.getMe();

            // TODO your code here

            return Command.go(Direction.RIGHT);
        }
    };
};

