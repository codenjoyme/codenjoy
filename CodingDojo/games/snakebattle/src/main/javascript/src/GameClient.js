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