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


var Api = function(WSocket, configuration, Direction, Element, Point, Board, Solver, logger) {

    var url = configuration.connectionString;
    var reconnectionTimeout = configuration.reConnectionTimeout;

    var ws;

    var solver = new Solver(Direction, Element, logger);

    function connect() {
        url = url.replace("http", "ws");
        url = url.replace("board/player/", "ws?user=");
        url = url.replace("?code=", "&code=");

        ws = new WSocket(url);

        logger.log('Opening...');

        ws.on('open', function () {
            logger.log('Web socket client opened ' + url);
        });

        ws.on('close', function () {
            logger.log('Web socket client closed');

            setTimeout(function () {
                logger.log('Try to reconnect...');
                connect();
            }, reconnectionTimeout);
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
        logger.logBoard(board);

        if(!solver) {
            logger.log('recreate Solver');
            solver =  new Solver(Direction, Element, logger);
        }
        try{
            var command = solver.get(board);
        }catch(ex){
           logger.log("error: " + ex)
        }
        logger.logCommand(command);
        var answer = command ? command.toString(): " ";
        return answer;
    };

    return {
        start: connect
    };
};

if(module) module.exports = Api;
