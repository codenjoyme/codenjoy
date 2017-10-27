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
// ========================== for debugging ==========================

function compileProgram(code) {
    try {
        eval(code);
        return program;
    } catch (e) {
        throw e;
    }
}

function runProgram(program, robot) {
    try {
        program(robot);
    } catch (e) {
        throw e;
    }
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
if (gameName == 'iCanCode Training') {
    game.enableBefunge = false;
    game.sprites = 'robot';
} else if (gameName == 'eKids') {
    game.enableBefunge = true;
    game.sprites = 'ekids';
} else { // if (gameName == 'iCanCode Contest') { by default
    game.enableBefunge = false;
    game.sprites = 'robot';
    game.onlyLeaderBoard = true;
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
game.debug = false;

game.heroInfo = {};
game.heroInfo.dx = -15;
game.heroInfo.dy = -45;
game.heroInfo.font = "20px 'Verdana, sans-serif'";
game.heroInfo.fillStyle = "#0FF";
game.heroInfo.textAlign = "left";
game.heroInfo.shadowColor = "#000";
game.heroInfo.shadowOffsetX = 0;
game.heroInfo.shadowOffsetY = 0;
game.heroInfo.shadowBlur = 5;

// ========================== leaderboard page ==========================

var initHelpLink = function() {
    var pageName = gameName.split(' ').join('-').toLowerCase();
    $('#help-link').attr('href', '/codenjoy-contest/resources/icancode/landing-' + pageName + '.html')
}
var initAdditionalLink = function() {
    if (game.onlyLeaderBoard) {
        $('#additional-link').attr('href', '/codenjoy-contest/resources/user/icancode-servers.zip')
        $('#additional-link').text('Get client')
    }
}

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        ['js/game/loader/boardAllPageLoad.js'],
        function() {
            boardAllPageLoad();
            initHelpLink();
            initAdditionalLink();
        });
}

// ========================== user page ==========================

var controller;

if (game.onlyLeaderBoard) {
    game.onBoardPageLoad = game.onBoardAllPageLoad;
} else {
    game.onBoardPageLoad = function() {
        initLayout(game.gameName, 'board.html', game.contextPath,
            null,
            [],
            function() {
                if (this.hasOwnProperty('boardPageLoad')) {
                    boardPageLoad();
                    initHelpLink();
                    initAdditionalLink();
                }
            });
    }
}

if (game.demo) {
    game.onBoardPageLoad();
}