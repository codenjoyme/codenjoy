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
    game.playerId = 'userId';
    game.readableName = 'Stiven Pupkin';
    initLayout = function(game, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

var gameName = localStorage.getItem('gameType'); // check KEYS constants in register.js

if (window.location.href.includes("controlsOnly=true")) {
    game.drawCanvases = false;
    game.enableHeader = false;
    game.enableFooter = false;
    gameName = 'JavaScript';
} else {
    game.enableHeader = true;
    game.enableFooter = true;
}

if (gameName == 'JavaScript') {
    game.enableBefunge = false;
    game.sprites = 'robot';
} else if (gameName == 'eKids') {
    game.enableBefunge = true;
    game.sprites = 'ekids';
} else if (gameName == 'Befunge') {
    game.enableBefunge = true;
    game.sprites = 'robot';
} else {
    gameName = 'Contest';
    game.enableBefunge = false;
    game.sprites = 'robot';
    game.onlyLeaderBoard = true;
}

game.isDrawByOrder = (game.sprites == 'ekids');
game.enableDonate = false;
game.enableJoystick = false;
game.enablePlayerInfo = false;
game.enablePlayerInfoLevel = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableInfo = false;
game.enableHotkeys = true;
game.enableForkMe = false;
game.enableAdvertisement = false;
game.showBody = false;
game.debug = false;

// ========================== leaderboard page ==========================

var initHelpLink = function() {
    if (gameName == 'eKids') {
        $('#help-link').hide();
        return; // TODO написать нормально мануал и убрать это
    }
    var pageName = gameName.split(' ').join('-').toLowerCase();
    $('#help-link').attr('href', '/codenjoy-contest/resources/icancode/landing-' + pageName + '.html')
}
var initAdditionalLink = function() {
    if (game.onlyLeaderBoard) {
        $('#additional-link').attr('href', '/codenjoy-contest/resources/user/icancode-servers.zip')
        $('#additional-link').text('Get client')
    }
}

game.onBoardAllPageLoad = function(showProgress) {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        [],
        function() {
            boardAllPageLoad(!!showProgress);
            initHelpLink();
            initAdditionalLink();
        });
}

game.drawBoard = function(drawer) {
    drawer.clear();
    drawer.drawBack();
    drawer.drawLayers();

    var fonts = {};
    fonts.userName = {};
    fonts.userName.dx = -15;
    fonts.userName.dy = -45;
    fonts.userName.font = "20px 'Verdana, sans-serif'";
    fonts.userName.fillStyle = "#0FF";
    fonts.userName.textAlign = "left";
    fonts.userName.shadowColor = "#000";
    fonts.userName.shadowOffsetX = 0;
    fonts.userName.shadowOffsetY = 0;
    fonts.userName.shadowBlur = 5;
    drawer.drawPlayerNames(fonts.userName);

    drawer.drawFog();
}

// ========================== user page ==========================

var controller;

if (game.onlyLeaderBoard) {
    game.onBoardPageLoad = function() {
        var showProgress = true;
        game.onBoardAllPageLoad(showProgress);
    }
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