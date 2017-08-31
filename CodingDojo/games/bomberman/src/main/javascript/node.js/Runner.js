/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

//var hostIp = '127.0.0.1';
var hostIp = 'tetrisj.jvmhost.net';

var userName = 'apofig';
var protocol = 'WS';

var processBoard = function(boardString) {
    var board = new Board(boardString);
    log("Board: " + board);

    var answer = new DirectionSolver(board).get().toString();
    log("Answer: " + answer);
    log("-----------------------------------");

    return answer;
};

if (protocol == 'HTTP') {
    var http = require('http');
    var url = require('url');

    http.createServer(function (request, response) {
        var parameters = url.parse(request.url, true).query;
        var boardString = parameters.board;

        var answer = processBoard(boardString);

        response.writeHead(200, {'Content-Type': 'text/plain'});
        response.end(answer);
    }).listen(8888, hostIp);

    log('Server running at http://' + hostIp + ':8888/');
} else {
    var port = 8080;
    if (hostIp == 'tetrisj.jvmhost.net') {
        port = 12270;
    }
    var server = 'ws://' + hostIp + ':' + port + '/codenjoy-contest/ws';
    var WebSocket = require('ws');
    var ws = new WebSocket(server + '?user=' + userName);

    ws.on('open', function() {
        log('Opened');
    });

    ws.on('close', function() {
        log('Closed');
    });

    ws.on('message', function(message) {
        log('received: %s', message);

        var pattern = new RegExp(/^board=(.*)$/);
        var parameters = message.match(pattern);
        var boardString = parameters[1];

        var answer = processBoard(boardString);

        ws.send(answer);
    });

    log('Web socket client running at ' + server);
}

var Element = {
    /// This is your Bomberman
    BOMBERMAN : '☺',             // this is what he usually looks like
    BOMB_BOMBERMAN : '☻',        // this is if he is sitting on own bomb
    DEAD_BOMBERMAN : 'Ѡ',        // oops, your Bomberman is dead (don't worry, he will appear somewhere in next move)
                                 // you're getting -200 for each death

    /// this is other players Bombermans
    OTHER_BOMBERMAN : '♥',       // this is what other Bombermans looks like
    OTHER_BOMB_BOMBERMAN : '♠',  // this is if player just set the bomb
    OTHER_DEAD_BOMBERMAN : '♣',  // enemy corpse (it will disappear shortly, right on the next move)
                                 // if you've done it you'll get +1000

    /// the bombs
    BOMB_TIMER_5 : '5',          // after bomberman set the bomb, the timer starts (5 tacts)
    BOMB_TIMER_4 : '4',          // this will blow up after 4 tacts
    BOMB_TIMER_3 : '3',          // this after 3
    BOMB_TIMER_2 : '2',          // two
    BOMB_TIMER_1 : '1',          // one
    BOOM : '҉',                  // Boom! this is what is bomb does, everything that is destroyable got destroyed

    /// walls
    WALL : '☼',                  // indestructible wall - it will not fall from bomb
    DESTROY_WALL : '#',          // this wall could be blowed up
    DESTROYED_WALL : 'H',        // this is how broken wall looks like, it will dissapear on next move
                                 // if it's you did it - you'll get +10 points.

    /// meatchoppers
    MEAT_CHOPPER : '&',          // this guys runs over the board randomly and gets in the way all the time
                                 // if it will touch bomberman - it will die
                                 // you'd better kill this piece of ... meat, you'll get +100 point for it
    DEAD_MEAT_CHOPPER : 'x',     // this is chopper corpse

    /// a void
    NONE : ' '                  // this is the only place where you can move your Bomberman
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
    UP : D(2, 0, -1, 'up'),        // направления движения бомбермена
    DOWN : D(3, 0, 1, 'down'),
    LEFT : D(0, -1, 0, 'left'),
    RIGHT : D(1, 1, 0, 'right'),
    ACT : D(4, 0, 0, 'act'),       // поставить бомбу
    STOP : D(5, 0, 0, '')         // стоять на месте
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.ACT, Direction.STOP];
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

        isBad : function(boardSize) {
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
    return {
        getXY : function(length) {
            if (length == -1) {
                return null;
            }
            return new Point(length % boardSize, Math.ceil(length / boardSize));
        },

        getLength : function(x, y) {
            return y*boardSize + x;
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

    var getBomberman = function() {
        var result = [];
        result = result.concat(findAll(Element.BOMBERMAN));
        result = result.concat(findAll(Element.BOMB_BOMBERMAN));
        result = result.concat(findAll(Element.DEAD_BOMBERMAN));
        return result[0];
    };

    var getOtherBombermans = function() {
        var result = [];
        result = result.concat(findAll(Element.OTHER_BOMBERMAN));
        result = result.concat(findAll(Element.OTHER_BOMB_BOMBERMAN));
        result = result.concat(findAll(Element.OTHER_DEAD_BOMBERMAN));
        return result;
    };

    var isMyBombermanDead = function() {
        return board.indexOf(Element.DEAD_BOMBERMAN) != -1;
    };

    var isAt = function(x, y, element) {
       if (pt(x, y).isBad(size)) {
           return false;
       }
       return getAt(x, y) == element;
    };

    var getAt = function(x, y) {
        return board.charAt(xyl.getLength(x, y));
    };

    var boardAsString = function() {
        var result = "";
        for (var i = 0; i <= size - 1; i++) {
            result += board.substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    var getBarriers = function() {
        var all = getMeatChoppers();
        all = all.concat(getWalls());
        all = all.concat(getBombs());
        all = all.concat(getDestroyWalls());
        all = all.concat(getOtherBombermans());
        all = all.concat(getFutureBlasts());
        return removeDuplicates(all);
    };

    var toString = function() {
        return util.format("Board:\n%s\n" +
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

    var getMeatChoppers = function() {
       return findAll(Element.MEAT_CHOPPER);
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

   var getWalls = function() {
       return findAll(Element.WALL);
   };

   var getDestroyWalls = function() {
       return findAll(Element.DESTROY_WALL);
   };

   var getBombs = function() {
       var result = [];
       result = result.concat(findAll(Element.BOMB_TIMER_1));
       result = result.concat(findAll(Element.BOMB_TIMER_2));
       result = result.concat(findAll(Element.BOMB_TIMER_3));
       result = result.concat(findAll(Element.BOMB_TIMER_4));
       result = result.concat(findAll(Element.BOMB_TIMER_5));
       result = result.concat(findAll(Element.BOMB_BOMBERMAN));
       return result;
   };

   var getBlasts = function() {
       return findAll(Element.BOOM);
   };

   var getFutureBlasts = function() {
       var result = [];
       var bombs = getBombs();
       bombs = bombs.concat(findAll(Element.OTHER_BOMB_BOMBERMAN));
       bombs = bombs.concat(findAll(Element.BOMB_BOMBERMAN));

       for (var index in bombs) {
           var bomb = bombs[index];
           result.push(bomb);
           result.push(new Point(bomb.getX() - 1, bomb.getY()));
           result.push(new Point(bomb.getX() + 1, bomb.getY()));
           result.push(new Point(bomb.getX()    , bomb.getY() - 1));
           result.push(new Point(bomb.getX()    , bomb.getY() + 1));
       }
       var copy = result.slice();
       for (var index in copy) {
           var blast = copy[index];
           if (blast.isBad(size) || contains(getWalls(), blast)) {
               result.splice(index, 1);
           }
       }
       return removeDuplicates(result);
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
       if (pt(x, y).isBad(size)) {
           return false;
       }
       return isAt(x + 1, y, element) || isAt(x - 1, y, element) || isAt(x, y + 1, element) || isAt(x, y - 1, element);
   };

   var isBarrierAt = function(x, y) {
       return contains(getBarriers(), pt(x, y));
   };

   var countNear = function(x, y, element) {
       if (pt(x, y).isBad(size)) {
           return 0;
       }
       var count = 0;
       if (isAt(x - 1, y    , element)) count ++;
       if (isAt(x + 1, y    , element)) count ++;
       if (isAt(x    , y - 1, element)) count ++;
       if (isAt(x    , y + 1, element)) count ++;
       return count;
   };

   return {
        size : boardSize,
        getBomberman : getBomberman,
        getOtherBombermans : getOtherBombermans,
        isMyBombermanDead : isMyBombermanDead,
        isAt : isAt,
        boardAsString : boardAsString,
        getBarriers : getBarriers,
        toString : toString,
        getMeatChoppers : getMeatChoppers,
        findAll : findAll,
        getWalls : getWalls,
        getDestroyWalls : getDestroyWalls,
        getBombs : getBombs,
        getBlasts : getBlasts,
        getFutureBlasts : getFutureBlasts,
        isAnyOfAt : isAnyOfAt,
        isNear : isNear,
        isBarrierAt : isBarrierAt,
        countNear : countNear,
        getAt : getAt
   };
};

var random = function(n){
    return Math.floor(Math.random()*n);
};

var direction;

var DirectionSolver = function(board){

    var tryToMove = function(x, y) {
        var count = 0;
        var result = null;
        var again = false;
        var newX = x;
        var newY = y;
        do {
            var count1 = 0;
            do {
                result = Direction.valueOf(random(4));
            } while (count1++ < 10 && (result.inverted() == direction && board.countNear(x, y, Element.NONE) > 1));

            newX = result.changeX(x);
            newY = result.changeY(y);

            var bombAtWay = bomb != null && bomb.equals(pt(newX, newY));
            var barrierAtWay = board.isBarrierAt(newX, newY);
            var meatChopperNearWay = board.isNear(newX, newY, Element.MEAT_CHOPPER);
//            var deadEndAtWay = board.countNear(newX, newY, Element.NONE) == 0;   // TODO продолжить но с тестами
//            if (deadEndAtWay) {
//                bomb = null;
//            }

            again = bombAtWay || barrierAtWay || meatChopperNearWay;
        } while (count++ < 20 && again);

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    };

    function mergeCommands(bomb, direction) {
        return "" + ((bomb != null) ? Direction.ACT + "," : "") + ((direction != null) ? direction : "");
    }

    return {
        get : function() {
            var bomberman = board.getBomberman();

            var nearDestroyWall = board.isNear(bomberman.getX(), bomberman.getY(), Element.DESTROY_WALL);
            var bombNotDropped = !board.isAt(bomberman.getX(), bomberman.getY(), Element.BOMB_BOMBERMAN);

            bomb = null;
            if (nearDestroyWall && bombNotDropped) {
                bomb = new Point(bomberman.getX(), bomberman.getY());
            }

            direction = tryToMove(bomberman.getX(), bomberman.getY(), bomb);

            return mergeCommands(bomb, direction);
        }

//        /**
//         * @return next bot action
//         */
//        get : function(board) {
//            return Direction.ACT;
//        }
    };
};

