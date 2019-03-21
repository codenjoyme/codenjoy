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

// TODO to use new way of get ws link
// this is your player id
var playerId = '3edq63tw0bq4w4iem7nb';
// you can get this code after registration on the server
// http://servernameorip:8080/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890
var code = '1889919902398150091';

var processBoard = function(boardString) {
    var board = new Board(boardString);
        if (!!printBoardOnTextArea) {
        printBoardOnTextArea(boardString);
    }

    drawTelemetry(board);

    var logMessage = board.getLogString() + "\n\n";
    var answer = new DirectionSolver(board).get().toString();
    logMessage += "Answer: " + answer + "\n";
    logMessage += "-----------------------------------\n";
	
    log(logMessage);

    return answer;
};

var server = 'ws://' + hostIP + ':' + port + '/codenjoy-contest/ws';
var WSocket = require('ws');
var wsurl = server + '?user=' + playerId + '&code=' + code;
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

var canvas;
var ctx;

var drawText = function(text, pt) {
    ctx.fillText(text, pt.x, pt.y);
}

function drawTelemetry(board) {
    if (!canvas) {
        canvas = document.getElementById("mycanvas");
        ctx = canvas.getContext("2d");
    }

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.font = "bold 13px monospace";
    ctx.fillStyle = "#444";

    drawText("TIME  " + board.getTime(), {"x": 50, "y": 30});
    drawText("FUEL  " + board.getFuelMass(), {"x": 50, "y": 45});
    drawText("STATE " + board.getState(), {"x": 50, "y": 60});
    drawText("XPOS " + board.getX(), {"x": 200, "y": 30});
    drawText("YPOS " + board.getY(), {"x": 200, "y": 45});
    drawText("HSPEED " + board.getHSpeed(), {"x": 350, "y": 30});
    drawText("VSPEED " + board.getVSpeed(), {"x": 350, "y": 45});
    if (board.getHSpeed() >= 0.001) {
        drawText("→", {"x": 500, "y": 30});
    }
    else if (board.getHSpeed() <= -0.001) {
        drawText("←", {"x": 500, "y": 30});
    }
    if (board.getVSpeed() >= 0.001) {
        drawText("↑", {"x": 500, "y": 45});
    }
    else if (board.getVSpeed() <= -0.001) {
        drawText("↓", {"x": 500, "y": 45});
    }

    // scale, move center to (300, 200), and flip vertically
    var scale = 6;
    var xshift = 300 - board.getX() * scale;
    var yshift = 200 + board.getY() * scale;
    ctx.setTransform(scale, 0, 0, -scale, xshift, yshift);
    ctx.lineWidth = 1 / scale;

    // draw relief
    var relief = board.getRelief();
    var reliefLen = relief.length;
    if (reliefLen > 1) {
        var ptstart = relief[0];
        ctx.strokeStyle = "#313";
        ctx.beginPath();
        ctx.moveTo(ptstart.x, ptstart.y);
        for (i = 1; i < reliefLen; i++) {
            var pt = relief[i];
            ctx.lineTo(pt.x, pt.y);
        }
        ctx.stroke();
    }

    // draw history for the last step
    var history = board.getHistory();
    var historyLen = history.length;
    if (historyLen > 1) {
        var ptstart = history[0];
        ctx.strokeStyle = "#282";
        ctx.beginPath();
        ctx.moveTo(ptstart.x, ptstart.y);
        for (i = 1; i < historyLen; i++) {
            var pt = history[i];
            ctx.lineTo(pt.x, pt.y);
        }
        ctx.stroke();
    }

    // draw target (same transform)
    var target = board.getTarget();
    if (target) {
        ctx.strokeStyle = "#F44";
        ctx.beginPath();
        ctx.moveTo(target.x, target.y - 8/scale);  ctx.lineTo(target.x, target.y + 8/scale);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(target.x - 8/scale, target.y);  ctx.lineTo(target.x + 8/scale, target.y);
        ctx.stroke();
    }

    // draw the ship
    var radian = board.getAngle() / 180 * Math.PI;
    var sin = Math.sin(radian);
    var cos = Math.cos(radian);
    ctx.setTransform(-cos * scale, -sin * scale, sin * scale, -cos * scale, xshift + board.getX() * scale, yshift - board.getY() * scale);
    ctx.strokeStyle = "#008";
    ctx.beginPath();
    ctx.moveTo(0, 0.0);  ctx.lineTo(-1, -0.2);  ctx.lineTo(-0.7, 1.1);
    ctx.lineTo(0, 1.6);
    ctx.lineTo(0.7, 1.1);  ctx.lineTo(1, -0.2);  ctx.lineTo(0, 0.0);
    ctx.stroke();

    // draw arrow pointing to target
    if (target) {
        var deltaX = target.x - board.getX();
        var deltaY = target.y - board.getY();
        var distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 1) {
            var radian = Math.atan2(deltaY, deltaX); // In radians
            var sin = Math.sin(radian);
            var cos = Math.cos(radian);
            ctx.setTransform(cos, -sin, sin, cos, 300, 100);
            ctx.lineWidth = 1;
            ctx.strokeStyle = "#F44";
            ctx.beginPath();
            ctx.moveTo(-30, 0);  ctx.lineTo(0, 0);  ctx.moveTo(30, 0);
            ctx.lineTo(0, 5);  ctx.lineTo(0, -5);  ctx.lineTo(30, 0);
            ctx.stroke();
        }
    }

    ctx.resetTransform();
}

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

var Board = function(board) {
    var boardObj = JSON.parse(board);

    var getState = function() {
        return boardObj.state;
    };
    var getFuelMass = function() {
        return parseFloat(boardObj.fuelmass);
    };
    var getTime = function() {
        return parseFloat(boardObj.time);
    };
    var getX = function() {
        return parseFloat(boardObj.x);
    };
    var getY = function() {
        return parseFloat(boardObj.y);
    };
    var getHSpeed = function() {
        return parseFloat(boardObj.hspeed);
    };
    var getVSpeed = function() {
        return parseFloat(boardObj.vspeed);
    };
    var getAngle = function() {
        return parseFloat(boardObj.angle);
    };
    var getRelief = function() {
        return boardObj.relief;
    };
    var getHistory = function() {
        return boardObj.history;
    };
    var getTarget = function() {
        return boardObj.target;
    };

    var getLogString = function() {
        return getState() + " y:" + getY();
    };

    return {
        getState : getState,
        getFuelMass : getFuelMass,
        getTime : getTime,
        getX : getX,
        getY : getY,
        getHSpeed : getHSpeed,
        getVSpeed : getVSpeed,
        getAngle : getAngle,
        getRelief : getRelief,
        getHistory : getHistory,
        getTarget : getTarget,
        getLogString : getLogString
   };
};

var random = function(n){
    return Math.floor(Math.random()*n);
};

var direction;

var DirectionSolver = function(board) {

    return {
        /**
         * @return next action
         */
        get : function() {
            var target = board.getTarget();

            if (board.getState() == "START") {
                return "message('go 0, 0.2, 1')";
            }

            if (board.getY() < target.y + 4.0 || board.getVSpeed() < -1.5) {
                return Direction.UP;
            }
            else if (board.getX() < target.x && board.getHSpeed() < 3.0) {
                return Direction.RIGHT;
            }
            else if (board.getX() > target.x && board.getHSpeed() > -3.0) {
                return Direction.LEFT;
            }
            else {
                return Direction.DOWN;
            }

            //TODO: Code your logic here and return direction
            return Direction.UP;
        }
    };
};

