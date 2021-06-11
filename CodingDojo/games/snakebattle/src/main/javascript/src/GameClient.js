/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
const WebSocket = require('ws');
const Board = require("./Board");
const BoardStringParser = require("./BoardStringParser");

class GameClient {
    webSocketUrl;
    incomingMessagePattern = new RegExp(/^board=(.*)$/);
    /**
     * @type {Solver}
     */
    solver;

    /**
     * @type {BoardStringParser}
     */
    boardStringParser;

    /**
     *
     * @param {string} webServerUrl
     * @param {Solver} solver
     */
    constructor(webServerUrl, solver) {
        this.solver = solver;

        this.webSocketUrl = webServerUrl
            .replace("http", "ws")
            .replace("board/player/", "ws?user=")
            .replace("?code=", "&code=");

        this.boardStringParser = new BoardStringParser();
    }

    connect() {
        const self = this;
        const webSocket = new WebSocket(this.webSocketUrl);
        console.log('Opening...');

        webSocket.on('open', function () {
            console.log('Web socket client opened ' + self.webSocketUrl);
        });

        webSocket.on('close', function () {
            console.log('Web socket client closed');

            setTimeout(self.connect, 5000);
        });

        webSocket.on('message', function (message) {
            let boardString = message.match(self.incomingMessagePattern)[1];

            const cells = self.boardStringParser.parse(boardString);
            const board = new Board(cells);
            const answer = self.solver.Decide(board);
            
            let logMessage = self.boardStringFormatForConsole(boardString) + "\n";
            logMessage+= `Answer: ${answer}\n`;
            logMessage+= "-----------------------------------\n";
            console.log(logMessage);

            webSocket.send(answer);
        });
    }

    /**
     * @param {string} boardString
     */
    boardStringFormatForConsole(boardString) {
        const boardSize = Math.sqrt(boardString.length);

        let result = "";
        for (let i = 0; i < boardSize; i++) {
            result += boardString.substring(i * boardSize, (i + 1) * boardSize);
            result += "\n";
        }
        
        return result;
    }
}

module.exports = GameClient;