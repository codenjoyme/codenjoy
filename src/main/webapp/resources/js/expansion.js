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
if (gameName == 'Expansion Training') {
    game.sprites = 'robot';
} else if (gameName == 'Expansion Contest') {
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

// ========================== leaderboard page ==========================

var initHelpLink = function() {
    var pageName = gameName.split(' ').join('-').toLowerCase();
    $('#help-link').attr('href', '/codenjoy-contest/resources/expansion/landing-' + pageName + '.html')
}
var initAdditionalLink = function() {
    if (game.onlyLeaderBoard) {
        $('#additional-link').attr('href', '/codenjoy-contest/resources/user/expansion-servers.zip')
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

game.playerDrawer = function (canvas, playerName, gameName,
        board, heroesData, defaultPlayerDrawer)
{
    fonts = {};
    fonts.userName = {};
    fonts.userName.dx = -15;
    fonts.userName.dy = -45;
    fonts.userName.font = "22px 'verdana'";
    fonts.userName.fillStyle = "#0FF";
    fonts.userName.textAlign = "left";
    fonts.userName.shadowColor = "#000";
    fonts.userName.shadowOffsetX = 0;
    fonts.userName.shadowOffsetY = 0;
    fonts.userName.shadowBlur = 5;

    var GREEN = 0;
    var RED = 1;
    var BLUE = 2;
    var YELLOW = 3;

    fonts.forces = {};
    fonts.forces.dx = 24;
    fonts.forces.dy = 35;
    fonts.forces.font = "23px 'verdana'";
    fonts.forces.fillStyles = {};
    fonts.forces.fillStyles[GREEN] = "#115e34";
    fonts.forces.fillStyles[RED] = "#681111";
    fonts.forces.fillStyles[BLUE] = "#306177";
    fonts.forces.fillStyles[YELLOW] = "#7f6c1b";
    fonts.forces.shadowStyles = {};
    fonts.forces.shadowStyles[GREEN] = "#64d89b";
    fonts.forces.shadowStyles[RED] = "#d85e5b";
    fonts.forces.shadowStyles[BLUE] = "#6edff9";
    fonts.forces.shadowStyles[YELLOW] = "#f9ec91";
    fonts.forces.textAlign = "center";
    fonts.forces.shadowOffsetX = 0;
    fonts.forces.shadowOffsetY = 0;
    fonts.forces.shadowBlur = 0;

    var changeColor = function(color) {
        if (color == GREEN) return YELLOW;
        if (color == YELLOW) return GREEN;
        if (color == RED) return BLUE;
        return RED;
    }

    var getColor = function(char) {
        if (char == 'P') return BLUE;
        if (char == 'Q') return RED;
        if (char == 'S') return YELLOW;
        if (char == 'R') return GREEN;
    }

    defaultPlayerDrawer(canvas, playerName, gameName, board, heroesData, fonts);

    var forces = board.forces;
    var size = canvas.boardSize;

    var parseCount = function(code) {
        if (code == '-=') return 0;
        return parseInt(code, 36);
    }

    var drawForces = function(){
        var layer2 = board.layers[1];
        for (y = 0; y < size; y++) {
            for (x = 0; x < size; x++) {
                var l = y * size + x;
                var sub = forces.substring(l*2, (l + 1)*2);
                var count = parseCount(sub);

                if (count > 0) {
                    var color = getColor(layer2.substring(l, l + 1));
                    fonts.forces.fillStyle = fonts.forces.fillStyles[color];
                    fonts.forces.shadowColor = fonts.forces.shadowStyles[color];
                    canvas.drawText(count, {'x':x - 1, 'y':size - 1 - y}, fonts.forces);
                }
            }
        }
    }

    drawForces();
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