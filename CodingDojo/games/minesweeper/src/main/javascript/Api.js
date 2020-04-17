/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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


var Api = function(WSocket, Configuration, Direction, Element, Point, Board, Solver) {

    var url = Configuration().connectionString;
    var isNeedToLog = Configuration().isAdditionalLoggingEnabled;
    var connectionTimeout = Configuration().connectionTimeout;

    var log = function (string) {
        if (isNeedToLog) {
            console.log(string);
            if (additionalLogging) {
                printLogOnTextArea(string);
            }
        }
    };

    var additionalLogging = false;
    try{
        if (!!printBoardOnTextArea){
            additionalLogging = true;
        }
    } catch (e) {
        log('No additional browser logging');
    }

    var ws;

    var solver = new Solver(Direction, Element);

    function connect() {
        url = url.replace("http", "ws");
        url = url.replace("board/player/", "ws?user=");
        url = url.replace("?code=", "&code=");

        ws = new WSocket(url);

        log('Opening...');

        ws.on('open', function () {
            log('Web socket client opened ' + url);
        });

        ws.on('close', function () {
            log('Web socket client closed');

            setTimeout(function () {
                log('Try to reconnect...');
                connect();
            }, connectionTimeout);
        });

        ws.on('message', function (message) {
            var pattern = new RegExp(/^board=(.*)$/);
            var parameters = message.match(pattern);
            var boardString = parameters[1];
            var answer = processBoard(boardString);
            ws.send(answer);
        });
    }

    var processBoard = function (boardString) {
        var board = new Board(boardString, Element, Point);
        if (additionalLogging) {
            printBoardOnTextArea(board.toString());
        }

        var logMessage = board + "\n\n";

        if(!solver) {
            logMessage('recreate Solver');
            solver =  new Solver(Direction, Element);
        }
        var command = solver.get(board);
        var answer = command ? command.toString(): " ";
        logMessage += "Answer: " + answer + "\n";
        logMessage += "-----------------------------------\n";

        log(logMessage);

        return answer;
    };

    return {
        start: connect
    };
};

if(module) module.exports = Api;
