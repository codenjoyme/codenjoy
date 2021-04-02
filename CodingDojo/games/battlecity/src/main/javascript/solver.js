/**
 * Choose game
 */
var Games = require('./games.js');
Games.init('battlecity');

var Point = require('./point.js');
var Direction = Games.require('./direction.js');
var Element = Games.require('./elements.js');
var Board = Games.require('./board.js');
var Stuff = require('./stuff.js');

var Solver = module.exports = {

    /**
     * paste here board page url from browser (board page) after registration
     */
    url : 'http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000',

    /**
     * @return next hero action
     */
    get : function(board) {
        // TODO your code here

        return Direction.ACT;
    }
};