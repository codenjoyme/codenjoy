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
var hostIP = 'epruryaw0576.moscow.epam.com';
var port = 43022;

// this is your email
var userName = 'borcwdgwta7o6a406kvh';
// you can get this code after registration on the server with your email
// http://servernameorip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890
var code = '1471240509319024989';

var processBoard = function(boardString) {
    var board = new Board(boardString);
	if (!!printBoardOnTextArea) {
        printBoardOnTextArea(JSON.stringify(board.getParsedBoard(), null, 4));
    }

    var logMessage = board.getLogString() + "\n\n";
    var answer = new LemonadeSolver(board).get().toString();
    logMessage += "Your request to the server: " + answer + "\n";
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


var getCommand = function(amountOfGlassesToMake, signsToMake, priceCents){
    return 'message(\'go '+amountOfGlassesToMake+','+signsToMake+','+priceCents+'\')';
};

var resetCall = function(){
	return 'message(\'go reset\')';
}

var Board = function(board) {
    var boardObj = JSON.parse(board);

    var getAssets = function() {
        return boardObj.assets;
    };
    var getWeatherForecast = function() {
        return boardObj.weatherForecast;
    };
    var isBankrupt = function() {
        return boardObj.isBankrupt;
    };
	var getMessages = function() {
        return boardObj.messages;
    };
    var getDay = function() {
        return parseInt(boardObj.day);
    };
    var getLemonadePrice = function() {
        return parseFloat(boardObj.lemonadePrice);
    };
    var getHistory = function() {
        return boardObj.history;
    };
	var getLogString = function() {
        return "Your assets: $" + getAssets() 
			+ "\nLemonsville weather report: " + getWeatherForecast() 
			+ "\nOn day " + getDay() 
			+ ", the cost of lemonade is: $" + getLemonadePrice();
    };
	var getParsedBoard = function() {
        return boardObj;
    };

    return {
		getAssets : getAssets,
        getWeatherForecast : getWeatherForecast,
        isBankrupt : isBankrupt,
        getMessages : getMessages,
        getDay : getDay,
		getLemonadePrice : getLemonadePrice,
		getHistory : getHistory,
		getLogString : getLogString,
		getParsedBoard : getParsedBoard
   };
};

var random = function(n){
    return Math.floor(Math.random()*n);
};

var LemonadeSolver = function(board) {

    return {
        /**
         * @return next action
         */
        get : function() {
            if (board.isBankrupt() == "true") {
                return resetCall();
            }

            //TODO: Code your logic here and return direction
            return getCommand(2,0,15);
        }
    };
};

