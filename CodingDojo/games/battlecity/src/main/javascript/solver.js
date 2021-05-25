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
