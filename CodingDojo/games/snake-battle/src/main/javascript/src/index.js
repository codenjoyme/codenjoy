/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import { getNextSnakeMove } from './bot';
import { getBoardAsString } from './utils';

var URL = process.env.GAME_URL || '';
var url = URL.replace("http", "ws").replace("board/player/", "ws?user=").replace("?code=", "&code=");

var socket = new WebSocket(url);

socket.addEventListener('open', function (event) {
    console.log('Open');
});

socket.addEventListener('close', function (event) {
    console.log('Closed');
});

socket.addEventListener('message', function (event) {
    var pattern = new RegExp(/^board=(.*)$/);
    var message = event.data;
    var parameters = message.match(pattern);
    var board = parameters[1];
    var answer = processBoard(board);
    socket.send(answer);
});

function processBoard(board) {
    var programLogs = "";
    function logger(message) {
        programLogs += message + "\n"
    }
    var answer = getNextSnakeMove(board, logger);
    var boardString = getBoardAsString(board);

    var logMessage = boardString + "\n\n";
    if (programLogs) {
        logMessage += "-----------------------------------\n";
        logMessage += programLogs;
    }
    logMessage += "-----------------------------------\n";
    logMessage += "Answer: " + answer + "\n";

    printBoard(boardString);
    printLog(logMessage);
    return answer;
}

function printBoard(text) {
    var textarea = document.getElementById("board");
    if (!textarea) {
        return;
    }
    var size = text.split('\n')[0].length;
    textarea.cols = size;
    textarea.rows = size + 1;
    textarea.value = text;
}

function printLog(text) {
    var textarea = document.getElementById("log-area");
    var addToEnd = document.getElementById("add-to-end");
    if (!textarea || !addToEnd) {
        return;
    }
    if (addToEnd.checked) {
        textarea.value = textarea.value + "\n" + text;
    } else {
        textarea.value = text + "\n" + textarea.value;
    }
}
