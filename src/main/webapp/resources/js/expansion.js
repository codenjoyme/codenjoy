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
    loadArrowImages();
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        ['js/game/loader/boardAllPageLoad.js'],
        function() {
            boardAllPageLoad();
            loadStuff();
        });
}

// ========================== user page ==========================

var controller;

if (game.onlyLeaderBoard) {
    game.onBoardPageLoad = game.onBoardAllPageLoad;
} else {
    game.onBoardPageLoad = function() {
        loadArrowImages();
        initLayout(game.gameName, 'board.html', game.contextPath,
            null,
            [],
            function() {
                if (this.hasOwnProperty('boardPageLoad')) {
                    boardPageLoad();
                    loadStuff();
                }
            });
    }
}

// ========================== board draw logic ==========================

var loadStuff = function() {
    initHelpLink();
    initAdditionalLink();
}

var sprites = {};
var directions = ['up', 'right_up', 'right', 'right_down', 'down', 'left_down', 'left', 'left_up'];
var loadImage = function(name) {
    var url = game.contextPath + 'resources/sprite/' + game.gameName + '/' + game.sprites + '/' + name + '.png';
    var image = new Image();
    image.onload = function() {
        // do nothing
    }
    image.src = url;
    return image;
}

var loadArrowImages = function() {
    for (var force = 0; force < 4; force++) {
    	sprites[force] = {};
    	for (var i in directions) {
    		var direction = directions[i];
            var image = loadImage('force' + (force + 1) + '_' + direction);
    		sprites[force][direction] = image;
    	}
    }

    for (var force = 0; force <= 4; force++) {
        sprites['base' + force] = loadImage('base' + force);
    }
    // TODO эти спрайты уже загружались ранее в canvases.js надо их оттуда сюда прокинуть
    sprites['gold'] = loadImage('gold');
    sprites['floor'] = loadImage('floor');
}

var previousBoard = {};
game.drawBoard = function(drawer) {
    // so we see past tick on board with current arrows
    var playerName = drawer.playerName;
    var allPlayersScreen = drawer.allPlayersScreen;
    var board = previousBoard[playerName];
    previousBoard[playerName] = drawer.playerData.board;
    drawer.playerData.board = board;
    if (!board) {
        return;
    }

    var canvas = drawer.canvas;
    var heroesData = drawer.playerData.heroesData;
    var forces = board.forces;
    var size = canvas.boardSize;

    drawer.clear();
    drawer.drawBack();

    var BLUE = 0;
    var RED = 1;
    var GREEN = 2;
    var YELLOW = 3;
    var WHITE = 10;

    var fonts = {};
    fonts.forces = {};
    fonts.forces.dxForce = 24;
    fonts.forces.dyForce = 35;
    fonts.forces.dxBase = 21;
    fonts.forces.dyBase = 27;
    fonts.forces.font = "23px 'verdana'";
    fonts.forces.fillStyles = {};
    fonts.forces.fillStyles[GREEN] = "#115e34";
    fonts.forces.fillStyles[RED] = "#681111";
    fonts.forces.fillStyles[BLUE] = "#306177";
    fonts.forces.fillStyles[YELLOW] = "#7f6c1b";
    fonts.forces.fillStyles[WHITE] = "#FFFFFF";
    fonts.forces.shadowStyles = {};
    fonts.forces.shadowStyles[GREEN] = "#64d89b";
    fonts.forces.shadowStyles[RED] = "#d85e5b";
    fonts.forces.shadowStyles[BLUE] = "#6edff9";
    fonts.forces.shadowStyles[YELLOW] = "#f9ec91";
    fonts.forces.fillStyles[WHITE] = "#FFFFFF";
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

    // TODO завязываться на буквы не очень ок, потому что любое пермещение в Elevemnts
    // приведет к поломке UI, надо привязываться к названиям спрайтов
    var parseColor = function(char) {
        if (char == 'P' || char == 'X') return BLUE;
        if (char == 'Q' || char == 'Y') return RED;
        if (char == 'R' || char == 'Z') return GREEN;
        if (char == 'S' || char == 'a') return YELLOW;
        return -1;
    }

    var isBase = function(char) {
        return (char == 'X') || (char == 'Y') || (char == 'Z') || (char == 'a');
    }

    var isForce = function(char) {
        return (char == 'P') || (char == 'Q') || (char == 'R') || (char == 'S');
    }

    var isGold = function(char) {
        return (char == 'W');
    }

    var parseCount = function(code) {
        if (code == '-=#') return 0;
        return parseInt(code, 36);
    }

    var length = function(x, y) {
        return (size - 1 - y)*size + x;
    }

    var getColor = function(x, y, layerNumber) {
        if (typeof layerNumber == 'undefined') layerNumber = 1;

        var layer2 = board.layers[layerNumber];
        var l = length(x, y);
        var color = parseColor(layer2.substring(l, l + 1));
        return color;
    }

    var COUNT_NUMBERS = 3;
    var getCount = function(x, y) {
        var l = length(x, y);
        var sub = forces.substring(l*COUNT_NUMBERS, (l + 1)*COUNT_NUMBERS);
        var count = parseCount(sub);
        return count;
    }

    var drawForces = function(x, y, afterBase, afterForce){
        var count = getCount(x, y);
        if (count == 0) return;

        if (afterBase) {
            fonts.forces.dx = fonts.forces.dxBase;
            fonts.forces.dy = fonts.forces.dyBase;
        } else {
            fonts.forces.dx = fonts.forces.dxForce;
            fonts.forces.dy = fonts.forces.dyForce;
        }
        var color = getColor(x, y);
        if (color != -1) {
            if (afterBase) {
                fonts.forces.fillStyle = fonts.forces.fillStyles[WHITE];
                fonts.forces.shadowColor = fonts.forces.shadowStyles[WHITE];
            } else {
                fonts.forces.fillStyle = fonts.forces.fillStyles[color];
                fonts.forces.shadowColor = fonts.forces.shadowStyles[color];
            }
            canvas.drawText(count, {'x':x - 1, 'y':y}, fonts.forces);
        }
    }

    try {
        drawer.drawLayers(function(layers, layerIndex, charIndex, x, y) {
            try {
                var hasGold = isGold(layers[0][charIndex]);
                var hasForce = isForce(layers[1][charIndex]);
                var hasBase = isBase(layers[0][charIndex]);

                var afterGold = (layerIndex == 0 && hasGold);
                var afterForce = (layerIndex == 1 && hasForce);
                var afterBase = (layerIndex == 0 && hasBase);
                if (afterBase) {
                    if (getCount(x, y) == 0) {
                        canvas.drawImage(sprites['base0'], x, y, 0, 0);
                    } else {
                        var colorForces = getColor(x, y, 1);
                        var colorBase = getColor(x, y, 0);
                        if (colorForces != colorBase) {
                            canvas.drawImage(sprites['base' + (colorForces + 1)], x, y, 0, 0);
                        }
                    }
                }

                if (afterGold) {
                    if (!hasForce) {
                        canvas.drawImage(sprites['floor'], x, y, 0, 0);
                    }
                    canvas.drawImage(sprites['gold'], x, y, 0, 0);
                    if (hasForce) {
                        drawForces(x, y, afterBase, afterForce);
                    }
                } else if (afterForce || afterBase) {
                    if (!hasGold) {
                        drawForces(x, y, afterBase, afterForce);
                    }
                }
            } catch (err) {
                console.log(err);
            }
        });
    } catch (err) {
        console.log(err);
    }

    try {
        var h = canvas.getPlotSize()/2;
        var drawArrow = function(color, direction, x, y) {
            direction = direction.toLowerCase();
            switch (direction) {
                case 'right':      canvas.drawImage(sprites[color]['right'], x, y, h, 0); break;
                case 'up':         canvas.drawImage(sprites[color]['up'], x, y, 0, -h); break;
                case 'down':       canvas.drawImage(sprites[color]['down'], x, y, 0, h); break;
                case 'left':       canvas.drawImage(sprites[color]['left'], x, y, -h, 0); break;
                case 'right_up':   canvas.drawImage(sprites[color]['right_up'], x, y, h, -h); break;
                case 'right_down': canvas.drawImage(sprites[color]['right_down'], x, y, h, h); break;
                case 'left_up':    canvas.drawImage(sprites[color]['left_up'], x, y, -h, -h); break;
                case 'left_down':  canvas.drawImage(sprites[color]['left_down'], x, y, -h, h); break;
                default: break;
            }
        }
        for (var name in heroesData[playerName]) {
            var additionalData = heroesData[playerName][name].additionalData;
            var lastAction = additionalData.lastAction;
            if (!lastAction) continue;
            var movements = lastAction.movements;
            var increase = lastAction.increase;

            for (var i in movements) {
                var movement = movements[i];
                var pt = movement.region;
                var direction = movement.direction;

                var color = getColor(pt.x, pt.y);
                if (color != -1) {
                    drawArrow(color, direction, pt.x, pt.y);
                }
            }
        }
    } catch (err) {
        console.log(err);
    }

    if (!board.inLobby) {
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
        drawer.drawPlayerNames(fonts.userName);
    }

    if (board.round != -1) {
        fonts.round = {};
        fonts.round.dx = 20;
        fonts.round.dy = 0;
        fonts.round.font = "50px 'verdana'";
        fonts.round.fillStyle = "#F0F";
        fonts.round.textAlign = "center";
        fonts.round.shadowColor = "#000";
        fonts.round.shadowOffsetX = 0;
        fonts.round.shadowOffsetY = 0;
        fonts.round.shadowBlur = 5;
        var text = board.rounds - board.round;
        canvas.drawText(text, {'x':9, 'y':18}, fonts.round);
    }

    if (!!allPlayersScreen) {
        fonts.userBoard = {};
        fonts.userBoard.dy = 45;
        fonts.userBoard.dx = -40;
        fonts.userBoard.font = "40px 'verdana'";
        fonts.userBoard.fillStyle = "#FFF";
        fonts.userBoard.textAlign = "left";
        fonts.userBoard.shadowColor = "#000";
        fonts.userBoard.shadowOffsetX = 0;
        fonts.userBoard.shadowOffsetY = 0;
        fonts.userBoard.shadowBlur = 5;
        var text = playerName;
        canvas.drawText(text, {'x':12, 'y':18}, fonts.userBoard);
    }

    drawer.drawFog();
}

// ========================== demo stuff ==========================

if (game.demo) {
    game.onBoardPageLoad();
}