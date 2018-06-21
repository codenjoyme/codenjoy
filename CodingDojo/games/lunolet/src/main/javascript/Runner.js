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
var util = require('util');

// to use for local server
//var hostIP = 'epruryaw0576';
var hostIP = '127.0.0.1';
var port = 8080;

// this is your email
var userName = 'your@email.com';
// you can get this code after registration on the server with your email
// http://servernameorip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890
var code = '1889919902398150091';

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

var server = 'ws://' + hostIP + ':' + port + '/codenjoy-contest/ws';
var WSocket = require('ws');
var wsurl = server + '?user=' + userName + '&code=' + code;
//log(wsurl);
var ws = new WSocket(wsurl);

ws.on('open', function() {
    log('Opened');
});

ws.on('close', function() {
    log('Closed');
});

ws.on('message', function(message) {
    var pattern = new RegExp(/^board=(.*)$/);
    var parameters = message.match(pattern);
    var boardString = parameters[1];
    var answer = processBoard(boardString);
    ws.send(answer);
});

log('Web socket client running at ' + server);

// Board elements
var Element = {
    GOOD_APPLE : '☺',            // an apple to eat
    BAD_APPLE : '☻',             // a poisoned apple to avoid
    WALL : '☼',                  // a wall to avoid
    NONE : ' ',                  // empty space

    /// this is the Snake
    SNAKE_HEAD_LEFT  : '◄',      // four variants for the snake head
    SNAKE_HEAD_RIGHT : '►',
    SNAKE_HEAD_UP    : '▲',
    SNAKE_HEAD_DOWN  : '▼',
};

Element.snakeHeads = function() {
   return [Element.SNAKE_HEAD_LEFT, Element.SNAKE_HEAD_RIGHT, Element.SNAKE_HEAD_UP, Element.SNAKE_HEAD_DOWN];
};

var D = function(index, dx, dy, name){

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
    UP    : D(2,  0,  1, 'UP'),                 // you can move
    DOWN  : D(3,  0, -1, 'DOWN'),
    LEFT  : D(0, -1,  0, 'LEFT'),
    RIGHT : D(1,  1,  0, 'RIGHT')
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT];
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
            var y = inversionY(Math.ceil(length / boardSize));
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
        return Math.sqrt(board.length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    var getSnakeHead = function() {
        var result = findAllOf(Element.snakeHeads());
        return result[0];
    };

    var isAt = function(x, y, element) {
       if (pt(x, y).isOutOf(size)) {
           return false;
       }
       return getAt(x, y) == element;
    };

    var getAt = function(x, y) {
		if (pt(x, y).isOutOf(size)) {
           return Element.WALL;
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

    var getApples = function() {
       return findAll(Element.GOOD_APPLE);
    };

    var findAll = function(element) {
       var result = [];
       for (var i = 0; i < size*size; i++) {
           var point = xyl.getXY(i);
           if (isAt(point.getX(), point.getY(), element)) {
               result.push(point);
           }
       }
       return result;
   };

    var findAllOf = function(elements) {
       var result = [];
       for (var i = 0; i < size*size; i++) {
           var point = xyl.getXY(i);
           if (isAnyOfAt(point.getX(), point.getY(), elements)) {
               result.push(point);
           }
       }
       return result;
   };

   var getWalls = function() {
       return findAll(Element.WALL);
   };

   var isAnyOfAt = function(x, y, elements) {
       for (var index in elements) {
           var element = elements[index];
           if (isAt(x, y,element)) {
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
        isAt : isAt,
        boardAsString : boardAsString,
        findAll : findAll,
        getSnakeHead : getSnakeHead,
        getWalls : getWalls,
        isAnyOfAt : isAnyOfAt,
        isNear : isNear,
        countNear : countNear,
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
         * @return next action
         */
        get : function() {

        	//TODO: Code your logic here and return direction
            return Direction.UP;
        }
    };
};

