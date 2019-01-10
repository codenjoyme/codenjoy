import { getNextSnakeMove } from './bot';
import { getBoardAsString } from './utils';

var URL = "http://codenjoy.com/codenjoy-contest/board/player/vreshch@gmail.com?code=16021902521521292064";
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
    var answer = getNextSnakeMove(board);
    var boardString = getBoardAsString(board);

    var logMessage = boardString + "\n\n";
    logMessage += "Answer: " + answer + "\n";
    logMessage += "-----------------------------------\n";

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
