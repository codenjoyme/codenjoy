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

var Solver = require('./solver.js');
var Games = require('./games.js');
var WSocket = require('ws');
var Board = Games.require('./board.js');

var browser = (browser !== undefined);

var log = function(string) {
    console.log(string);
    if (browser) {
        printLogOnTextArea(string);
    }
};

var processBoard = function(boardString) {
    var board = new Board(boardString);
    if (browser) {
        printBoardOnTextArea(board.boardAsString());
    }

    var logMessage = board + "\n\n";
    var answer = Solver.get(board).toString();
    logMessage += "Answer: " + answer + "\n";
    logMessage += "-----------------------------------\n";
    
    log(logMessage);

    return answer;
};

var parseBoard = function(message) {
    var pattern = new RegExp(/^board=(.*)$/);
    var parameters = message.match(pattern);
    var board = parameters[1];
    return board;
}

function getWSUrl(url) {
    return url.replace("http", "ws")
              .replace("board/player/", "ws?user=")
              .replace("?code=", "&code=");
}

function getUrl() {
    if (typeof process != 'undefined' && typeof process.argv != 'undefined') {
        var env = process.argv.slice(2);
        if (env != '') {
            var url = env[0];
            console.log('Got url from Environment: ' + url);
            return url;
        }
    }
    var url = Solver.url;
    console.log('Got url from Solver: ' + url);
    return url;
}

function connect() {
    var url = getWSUrl(getUrl());
    var socket = new WSocket(url);
    log('Opening...');

    socket.on('open', function() {
        log('Web socket client opened ' + url);
    });

    socket.on('close', function() {
        log('Web socket client closed');

        if (!browser) {
            setTimeout(function() {
                connect();
            }, 5000);
        }
    });

    socket.on('message', function(message) {
        var board = parseBoard(message);
        var answer = processBoard(board);
        socket.send(answer);
    });

    return socket;
}

if (!browser) {
    connect();
}





