/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
var currentBoardSize = null;

function initCanvases(contextPath, players, allPlayersScreen,
                singleBoardGame, boardSize, gameName,
                enablePlayerInfo, enablePlayerInfoLevel,
                sprites, drawBoard)
{
    var canvases = {};
    var infoPools = {};
    currentBoardSize = boardSize;
    var plots = {};
    var plotsUrls = {};
    loadCanvasesData();
    var reloading = false;

    function toId(email) {
        return email.replace(/[@.]/gi, "_");
    }

    function goToHomePage() {
        window.location.href = contextPath;
    }

    function reloadCanvasesData() {
        reloading = true;

        loadPlayers(function(newPlayers) {
            var remove = [];
            var create = [];
            var playerNames = getNames(players);
            var newPlayerNames = getNames(newPlayers);
            newPlayers.forEach(function (newPlayer) {
                if ($.inArray(newPlayer.name, playerNames) == -1) {
                    create.push(newPlayer);
                }
            });
            players.forEach(function (player) {
                if ($.inArray(player.name, newPlayerNames) == -1) {
                    remove.push(player);
                }
            });

            players = newPlayers;

            removeHtml(remove);
            removeCanvases(remove);

            buildHtml(create);
            buildCanvases(create);

            if (players.length == 0) {
                goToHomePage();
            }
            reloading = false;
        });
    }

    function loadCanvasesData() {
        loadData('/rest/sprites/alphabet', function(alphabet) {
            loadData('/rest/sprites/' + gameName, function(elements) {
                for (var index in elements) {
                    var char = alphabet[index];
                    var color = elements[index];
                    plots[char] = color;
                    var subFolder = (!!sprites) ? sprites + '/' : '';
                    plotsUrls[color] = contextPath + '/resources/sprite/' + gameName + '/' + subFolder + color + '.png';
                }

                buildHtml(players);
                buildCanvases(players);

                $('body').on('board-updated', function(events, data) {
                    if (!reloading) {
                        drawUsersCanvas(data);
                    }
                });
            });
        });
    }

    function removeHtml(playersList) {
        playersList.forEach(function (player) {
            $('#div_' + toId(player.name)).remove();
        });
    }

    function buildHtml(playersList) {
        var templateData = [];
        playersList.forEach(function (player) {
            var playerName = player.name;
            var id = toId(playerName);
            var name = playerName.split('@')[0];
            var visible = (allPlayersScreen || !enablePlayerInfoLevel) ? 'none' : 'block';
            templateData.push({name : name, id : id, visible : visible })
        });
        $('#players_container script').tmpl(templateData).appendTo('#players_container');
        if (!enablePlayerInfo) {
            $(".player_info").hide();
        }
    }

    function removeCanvases(playersList) {
        playersList.forEach(function (player) {
            delete canvases[player.name];
            delete infoPools[player.name];
        });
    }

    function buildCanvases(playersList) {
        playersList.forEach(function (player) {
            canvases[player.name] = createCanvas(toId(player.name));
            infoPools[player.name] = [];
        });
    }

    function decode(char) {
        return plots[char];
    }

    function plotsContains(color) {
        for (var char in plots) {
            if (plots[char] == color) {
                return true;
            }
        }
        return false;
    }

    var getNameFromEmail = function(email) {
        return email.substring(0, email.indexOf('@'));
    }

    var getBoardDrawer = function(canvas, playerName, playerData, allPlayersScreen) {
        var getBoard = function() {
            return playerData.board;
        }
        var getHeroesData = function(isAll) {
            if (isAll) {
                var result = {};
                for (var name in playerData.heroesData) {
                    result[name] = playerData.heroesData[name][name];
                }
                return result;
            } else {
                return playerData.heroesData[playerName];
            }
        }

        var drawAllLayers = function(layers, onDrawItem){
            var isDrawByOrder = true;

            var drawChar = function(plotIndex) {
                var x = 0;
                var y = boardSize - 1;
                for (var charIndex in layers[0]) {
                    for (var layerIndex in layers) {
                        var layer = layers[layerIndex];
                        var color = layer[charIndex];
                        if (!isDrawByOrder || plotIndex == color) {
                            canvas.drawPlot(decode(color), x, y);
                            if (!!onDrawItem) {
                                onDrawItem(layers, layerIndex, charIndex, x, y);
                            }
                        }
                    }
                    x++;
                    if (x == boardSize) {
                       x = 0;
                       y--;
                    }
                }
            }

            if (isDrawByOrder) {
                for (var char in plots) {
                    var plot = plots[char];
                    drawChar(char);
                }
            } else {
                drawChar();
            }
        }

        var drawBackground = function(name) {
            if (plotsContains(name)) {
                var x = boardSize / 2 - 0.5;
                canvas.drawPlot(name, x, 0);
            }
        }

        var drawBack = function() {
            drawBackground('background');
        }

        var drawFog = function() {
        }

        var clear = function() {
            canvas.clear();
        }

        var drawLayers = function(onDrawItem) {
            var board = getBoard();

            var monofont = {
                font: "13px monospace",
                fillStyle: "#444",
                textAlign: "left",
                shadowColor: "#EEE",
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowBlur: 7
            }
            canvas.drawText("TIME  " + board.time, {"x": 0, "y": 17.2}, monofont);
            canvas.drawText("FUEL  " + board.fuelmass, {"x": 0, "y": 16.4}, monofont);
            canvas.drawText("STATE " + board.state, {"x": 0, "y": 15.6}, monofont);
            canvas.drawText("XPOS " + board.x, {"x": 6, "y": 17.2}, monofont);
            canvas.drawText("YPOS " + board.y, {"x": 6, "y": 16.4}, monofont);
            canvas.drawText("HSPEED " + board.hspeed, {"x": 12, "y": 17.2}, monofont);
            canvas.drawText("VSPEED " + board.vspeed, {"x": 12, "y": 16.4}, monofont);
            if (board.hspeed >= 0.001) {
                canvas.drawText("→", {"x": 18, "y": 17.2}, monofont);
            }
            else if (board.hspeed <= -0.001) {
                canvas.drawText("←", {"x": 18, "y": 17.2}, monofont);
            }
            if (board.vspeed >= 0.001) {
                canvas.drawText("↑", {"x": 18, "y": 16.4}, monofont);
            }
            else if (board.vspeed <= -0.001) {
                canvas.drawText("↓", {"x": 18, "y": 16.4}, monofont);
            }
            canvas.drawText("LEVEL " + board.level, {"x": 0, "y": 0.4}, monofont);

            let ctx = canvas.getCanvasContext();
            // scale, move center to (300, 300), and flip vertically
            var scale = 6;
            var xshift = 300 - board.x * scale;
            var yshift = 200 + board.y * scale;
            ctx.setTransform(scale, 0, 0, -scale, xshift, yshift);
            ctx.lineWidth = 1 / scale;

            // draw relief
            var relief = board.relief;
            var reliefLen = relief.length;
            if (reliefLen > 1) {
                var ptstart = relief[0];
                ctx.strokeStyle = "#313";
                ctx.beginPath();
                ctx.moveTo(ptstart.x, ptstart.y);
                for (i = 1; i < reliefLen; i++) {
                    var pt = relief[i];
                    ctx.lineTo(pt.x, pt.y);
                }
                ctx.stroke();
            }

            // draw history for the last step
            var history = board.history;
            var historyLen = history.length;
            if (historyLen > 1) {
                var ptstart = history[0];
                ctx.strokeStyle = "#282";
                ctx.beginPath();
                ctx.moveTo(ptstart.x, ptstart.y);
                for (i = 1; i < historyLen; i++) {
                    var pt = history[i];
                    ctx.lineTo(pt.x, pt.y);
                }
                ctx.stroke();
            }

            // draw target (same transform)
            var target = board.target;
            if (target) {
                ctx.strokeStyle = "#F44";
                ctx.beginPath();
                ctx.moveTo(target.x, target.y - 8/scale);  ctx.lineTo(target.x, target.y + 8/scale);
                ctx.stroke();
                ctx.beginPath();
                ctx.moveTo(target.x - 8/scale, target.y);  ctx.lineTo(target.x + 8/scale, target.y);
                ctx.stroke();
            }

            // draw crashes
            var crashes = board.crashes;
            if (crashes && crashes.length > 0) {
                ctx.strokeStyle = "#888";
                for (i = 0; i < crashes.length; i++) {
                    var pt = crashes[i];
                    ctx.beginPath();
                    ctx.moveTo(pt.x, pt.y + 12/scale);  ctx.lineTo(pt.x, pt.y);
                    ctx.stroke();
                    ctx.beginPath();
                    ctx.moveTo(pt.x - 4/scale, pt.y + 8/scale);  ctx.lineTo(pt.x + 4/scale, pt.y + 8/scale);
                    ctx.stroke();
                }
            }

            // draw the ship
            var radian = board.angle / 180 * Math.PI;
            var sin = Math.sin(radian);
            var cos = Math.cos(radian);
            ctx.setTransform(-cos * scale, -sin * scale, sin * scale, -cos * scale, xshift + board.x * scale, yshift - board.y * scale);
            ctx.strokeStyle = "#008";
            ctx.beginPath();
            ctx.moveTo(0, 0.0);  ctx.lineTo(-1, -0.2);  ctx.lineTo(-0.7, 1.1);
            ctx.lineTo(0, 1.6);
            ctx.lineTo(0.7, 1.1);  ctx.lineTo(1, -0.2);  ctx.lineTo(0, 0.0);
            ctx.stroke();

            // draw arrow pointing to target
            if (target) {
                var deltaX = target.x - board.x;
                var deltaY = target.y - board.y;
                var distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (distance > 1) {
                    var radian = Math.atan2(deltaY, deltaX); // In radians
                    var sin = Math.sin(radian);
                    var cos = Math.cos(radian);
                    ctx.setTransform(cos, -sin, sin, cos, 300, 100);
                    ctx.lineWidth = 1;
                    ctx.strokeStyle = "#F44";
                    ctx.beginPath();
                    ctx.moveTo(-30, 0);  ctx.lineTo(0, 0);  ctx.moveTo(30, 0);
                    ctx.lineTo(0, 5);  ctx.lineTo(0, -5);  ctx.lineTo(30, 0);
                    ctx.stroke();
                }
            }

            ctx.resetTransform();

//            try {
//                drawAllLayers(board.layers, onDrawItem);
//            } catch (err) {
//                try {
//                    drawAllLayers([board], onDrawItem);
//                } catch (err) {
//                    console.log(err);
//                }
//            }
        }

        var drawPlayerNames = function(font, beforeDraw) {
        }

        return {
            clear : clear,
            drawBack : drawBack,
            drawLayers : drawLayers,
            drawPlayerNames : drawPlayerNames,
            drawFog : drawFog,
            canvas : canvas,
            playerName : playerName,
            playerData : playerData,
            allPlayersScreen : allPlayersScreen
        };
    };

    function defaultDrawBoard(drawer) {
        drawer.clear();
        drawer.drawBack();
        drawer.drawLayers();
        drawer.drawPlayerNames();
        drawer.drawFog();
    }

    drawBoard = (!!drawBoard) ? drawBoard : defaultDrawBoard;

    function calculateTextSize(text) {
        var div = $("#width_calculator_container");
        div.html(text);
        div.css('display', 'block');
        return div[0];
    }

    function showScoreInformation(playerName, information) {
        var infoPool = infoPools[playerName];

        if (information != '') {
            var arr = information.split(', ');
            for (var i in arr) {
                if (arr[i] == '0') {
                    continue;
                }
                infoPool.push(arr[i]);
            }
        }
        if (infoPool.length == 0) return;

        var score = $("#score_info_" + toId(playerName));
        if (score.is(':visible')) {
            return;
        }

        var text = '<center>' + infoPool.join('<br>') + '</center>';
        infoPool.splice(0, infoPool.length);

        var canvas = $("#" + toId(playerName));
        var size = calculateTextSize(text);
        score.css({
                position: "absolute",
                marginLeft: 0,
                marginTop: 0,
                left: canvas.position().left + canvas.width()/2 - size.clientWidth/2,
                top: canvas.position().top + canvas.height()/2 - size.clientHeight/2
            });

        score.html(text);

        score.show().delay(300).fadeOut(1600, function() {
            score.hide();

            showScoreInformation(playerName, '');
        });
    }

    function createCanvas(canvasName) {
        var canvas = $("#" + canvasName);

        var plotSize = {};
        var canvasSize = 0;
        var firstSprite = null;
        var calcSize = function(image) {
            plotSize = {width: image.width, height: image.height};
            canvasSize = canvasWidth = plotSize.width * boardSize;
            canvasHeight = plotSize.height * boardSize;
            if (canvas[0].width != canvasWidth || canvas[0].height != canvasHeight) {
                canvas[0].width = canvasWidth;
                canvas[0].height = canvasHeight;
            }
        }

        // TODO а обязательно загружать каждый раз все рисунки снова?
        var images = {};
        for (var color in plotsUrls) {
            var image = new Image();
            image.onload = function() {
                if (this == firstSprite) {
                    calcSize(this);
                }
            }
            image.src = plotsUrls[color];
            images[color] = image;
            if (!firstSprite) {
                firstSprite = image;
            }
        }

        var drawPlot = function(color, x, y) {
            var image = images[color];
            drawImage(image, x, y, 0, 0);
        }

        var drawImage = function(image, x, y, dx, dy) {
            var ctx = canvas[0].getContext("2d");
            ctx.drawImage(
                image,
                x * plotSize - (image.width - plotSize)/2 + dx,
                (boardSize - 1 - y) * plotSize - (image.height - plotSize) + dy
            );
        };

        var drawText = function(text, pt, font) {
            if (pt.x == -1 || pt.y == -1) return;

            var ctx = canvas[0].getContext("2d");
            if (!font) {
                font = {
                    font: "15px 'Verdana, sans-serif'",
                    fillStyle: "#0FF",
                    textAlign: "left",
                    shadowColor: "#000",
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowBlur: 7
                }
            }
            ctx.font = font.font;
            ctx.fillStyle =  font.fillStyle;
            ctx.textAlign = font.textAlign;
            ctx.shadowColor = font.shadowColor;
            ctx.shadowOffsetX = font.shadowOffsetX;
            ctx.shadowOffsetY = font.shadowOffsetY;
            ctx.shadowBlur = font.shadowBlur;

            var x = (pt.x + 1) * plotSize.width;
            var y = (boardSize - pt.y - 1) * plotSize.height - 5;
            if (!!font.dx) {
                x += font.dx;
            }
            if (!!font.dy) {
                y += font.dy;
            }
            for (var i = 0; i < 10; i++) {
                ctx.fillText(text, x, y);
            }
            ctx.shadowBlur = 0;
        }

        var drawPolyline = function(color, points) {
            var ctx = canvas[0].getContext("2d");
            //TODO
			ctx.strokeStyle = color;
			
			ctx.beginPath();
			ctx.moveTo(points[0].x, points[0].y);
			points.forEach(function(point){
				ctx.lineTo(point.x,point.y);
			});
			ctx.stroke();
        }

        var clear = function() {
            canvas.clearCanvas();
        }

        var getCanvasContext = function() {
            return canvas[0].getContext("2d");
        }

        var getCanvasSize = function() {
            return canvasSize;
        }

        var getPlotSize = function() {
            return plotSize;
        }

        return {
            drawImage : drawImage,
            drawPlot : drawPlot,
            drawText: drawText,
            drawPolyline: drawPolyline,
            getCanvasContext : getCanvasContext,
            clear : clear,
            getCanvasSize : getCanvasSize,
            getPlotSize : getPlotSize
        };
    }

    function isPlayerListEmpty(data) {
        return Object.keys(data).length == 0;
    }

    function getNames(playerList) {
        var result = [];
        playerList.forEach(function (player) {
            result.push(player.name);
        });
        return result;
    }

    function isPlayersListChanged(data) {
        var newPlayers = Object.keys(data);
        var oldPlayers = getNames(players);

        if (newPlayers.length != oldPlayers.length) {
            return true;
        }

        var hasNew = false;
        newPlayers.forEach(function (newPlayer) {
            if ($.inArray(newPlayer, oldPlayers) == -1) {
                hasNew = true;
            }
        });

        return hasNew;
    }

    function drawUsersCanvas(data) {
        if (data == null) {
            $("#showdata").text("There is NO data for player available!");
            return;
        }
        $("#showdata").text('');

        if (isPlayerListEmpty(data)) {
            goToHomePage();
            return;
        }

        if (allPlayersScreen && isPlayersListChanged(data)) {
            reloadCanvasesData();
            return;
        }

        if (allPlayersScreen) {
            for (var player in data) {
                drawUserCanvas(player, data[player], true);
            }
        } else {
            for (var i in players) {
                var player = players[i].name;
                drawUserCanvas(player, data[player], false);
            }
        }
    }

    function drawUserCanvas(playerName, data, allPlayersScreen) {
        if (currentBoardSize != data.boardSize) {    // TODO так себе решение... Почему у разных юзеров передается размер добры а не всем сразу?
            reloadCanvasesData();
        }

        var canvas = canvases[playerName];
        canvas.boardSize = boardSize;
        drawBoard(getBoardDrawer(canvas, playerName, data, allPlayersScreen));

        $("#score_" + toId(playerName)).text(data.score);

        showScoreInformation(playerName, data.info);

        if (!allPlayersScreen) {
            $("#level_" + toId(playerName)).text(data.heroesData[playerName][playerName].level + 1);
        }
    }

}