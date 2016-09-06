/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
function runProgram(program, robot) {
    program(robot);
}

// ========================== game setup ==========================

if (typeof game == 'undefined') {
    game = {};
    game.demo = true;
    game.code = 123;
    game.playerName = 'user@gmail.com';
    initLayout = function(game, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

var gameName = localStorage.getItem('gameName');
if (gameName == 'iCanCode') {
    game.enableBefunge = false;
    game.sprites = 'robot';
} else {
    game.enableBefunge = true;
    game.sprites = 'ekids';
}
game.enableDonate = false;
game.enableJoystick = false;
game.enableAlways = true;
game.enablePlayerInfo = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableInfo = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;

// ========================== leaderboard page ==========================

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        ['js/game/loader/boardAllPageLoad.js'],
        function() {
            boardAllPageLoad();
        });
}

// ========================== user page ==========================

var controller;

game.onBoardPageLoad = function() {
    initLayout(game.gameName, 'board.html', game.contextPath,
        null,
        [
//            'js/mousewheel/jquery.mousewheel.min.js',
//            'js/jquery/jquery-ui.js',
//            'js/scroll/jquery.mCustomScrollbar.js',
//            'bootstrap/js/bootstrap.js',
//
//            'js/game/console.js',
//            'js/game/buttons.js',
//
//            'js/game/point.js',
//            'js/game/direction.js',
//            'js/game/elements.js',
//            'js/game/board.js',
//            'js/game/level-info.js',
//            'js/game/autocomplete.js',
//            'js/game/editor.js',
//            'js/game/runnerJs.js',
//            'js/game/runnerBefunge.js',
//            'js/game/progressbar.js',
//            'js/game/levelProgress.js',
//
//            'js/game/robot.js',
//            'js/game/socket.js',
//            'js/game/controller.js',
//
//            'js/game/befunge.js',
//            'js/game/loader/boardPageLoad.js',
//
//            'js/ace/src/ace.js',
//            'js/ace/src/ext-language_tools.js',

        ], function() {
            if (this.hasOwnProperty('boardPageLoad')) {
                boardPageLoad();
            }
        });
}

if (game.demo) {
    game.onBoardPageLoad();
}