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

// TOTO проверить может что еще осталось от старой реализации неиспользуемое
var sprites = [];

function initCanvasesGame(contextPath, players, allPlayersScreen,
                singleBoardGame, boardSize, gameName,
                enablePlayerInfo, enablePlayerInfoLevel,
                sprites, alphabet, spriteElements,
                drawBoard)
{
    loadSprites(contextPath, spriteElements);
    var plotSize = {width: 27, height: 16};
    var canvases = {};
    var infoPools = {};
    loadCanvasesData();
    var reloading = false;

    function goToHomePage() {
        window.location.href = contextPath;
    }

    function reloadCanvasesData() {
        reloading = true;

        loadPlayers(function(newPlayers) {
            var remove = [];
            var create = [];
            var playerIds = getNames(players);
            var newPlayerIds = getNames(newPlayers);
            newPlayers.forEach(function (newPlayer) {
                if ($.inArray(newPlayer.id, playerIds) == -1) {
                    create.push(newPlayer);
                }
            });
            players.forEach(function (player) {
                if ($.inArray(player.id, newPlayerIds) == -1) {
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
        buildHtml(players);
        buildCanvases(players);

        $('body').on('board-updated', function(events, data) {
            if (!reloading) {
                drawUsersCanvas(data);
            }
        });
    }

    function removeHtml(playersList) {
        playersList.forEach(function (player) {
            $('#div_' + player.id).remove();
        });
    }

    function buildHtml(playersList) {
        var templateData = [];
        playersList.forEach(function (player) {
            var id = player.id;
            var name = player.readableName;
            var levelVisible = (allPlayersScreen || !enablePlayerInfoLevel) ? 'none' : 'block';
            var playerVisible  = (!enablePlayerInfo) ? 'none' : 'block';
            templateData.push({
                name : name,
                id : id,
                levelVisible : levelVisible,
                playerVisible : playerVisible
            });
        });
        $('#players_container script').tmpl(templateData).appendTo('#players_container');
    }

    function removeCanvases(playersList) {
        playersList.forEach(function (player) {
            delete canvases[player.id];
            delete infoPools[player.id];
        });
    }

    function buildCanvases(playersList) {
        playersList.forEach(function (player) {
            canvases[player.id] = createCanvas(player.id);
            infoPools[player.id] = [];
        });
    }

    var getBoardDrawer = function(canvas, playerId, playerData, allPlayersScreen) {
        var getBoard = function() {
            return playerData.board;
        }

        var clear = function() {
            canvas.clear();
        }

        var drawLayers = function(onDrawItem) {
            var board = getBoard();

            var ctx = canvas.getCanvasContext();
            ctx.beginPath();
            ctx.rect(0, 0, plotSize.width * boardSize, plotSize.height * boardSize);
            ctx.fillStyle = "black";
            ctx.fill();

            var weatherImg = sprites[board.weatherForecast.toLowerCase()];
            if (!!weatherImg) {
                ctx.globalAlpha = 0.67;
                ctx.drawImage(weatherImg, plotSize.width * boardSize - 240, plotSize.height * boardSize - 200, 240, 200);
                ctx.globalAlpha = 1;
            }

            // draw assets graph
            var history = board.history;
            if (history && history.length > 1) {
                var graphX = 5;
                var graphY = plotSize.height * boardSize - 200;
                var graphWidth = plotSize.width * boardSize - 250;
                var graphHeight = 195;
                // background
                ctx.beginPath();
                ctx.rect(graphX, graphY, graphWidth, graphHeight);
                ctx.fillStyle = "#222";
                ctx.fill();
                // draw graph
                ctx.lineWidth = 3;
                var scaledAssetsList = history.map(function(salesResult) {
                    return salesResult.assetsAfter * 100;
                });
                var assetsListLength = scaledAssetsList.length;
                if (assetsListLength > 1) {
                    var pInterval = (graphWidth - 40) / (assetsListLength - 1);
                    var maxY = Math.max.apply(null, scaledAssetsList);
                    var minY = Math.min.apply(null, scaledAssetsList);
                    var scaleFactor = (graphHeight - 40) / (maxY - minY);
                    var yShift = graphY + graphHeight - 20;
                    var startX = graphX + 20;
                    var startY = (minY - scaledAssetsList[0]) * scaleFactor + yShift;
                    ctx.strokeStyle = "#EE0";
                    ctx.beginPath();
                    ctx.moveTo(startX, startY);
                    for (i = 1; i < assetsListLength; i++) {
                        var y = (minY - scaledAssetsList[i]) * scaleFactor + yShift;
                        ctx.lineTo(startX + i * pInterval, y);
                    }
                    ctx.stroke();
                }
            }
            // draw assets
            var assetsfont = {
                font: "24px monospace",
                fillStyle: "#AA0",
                textAlign: "left",
                shadowColor: "#222",
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowBlur: 8
            }
            canvas.drawText("Day " + board.day + "  $" + board.assets, {"x": 16, "y": 0}, assetsfont);
            // draw messages on top of everything
            var messagefont = {
                font: "13px monospace",
                fillStyle: "#DDD",
                textAlign: "left",
                shadowColor: "#222",
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowBlur: 8
            }

            if (board.isGameOver) {
                messagefont.fillStyle = board.isBankrupt ? "#FCC" : "#FFB";
                messagefont.shadowColor = board.isBankrupt ? "#411" : "#330";
            }

            var messages = board.messages.split('\n');
            for (var i = 0; i < messages.length; i++) {
                if (messages[i].startsWith("YOUR ASSETS:")) {
                    messagefont.fillStyle = "#CEE";
                }

                canvas.drawText(messages[i], {"x": 0, "y": 22.4 - i}, messagefont);
            }
        }

        var drawPlayerNames = function(font, beforeDraw) {
        }

        return {
            clear : clear,
            drawLayers : drawLayers,
            drawPlayerNames : drawPlayerNames,
            canvas : canvas,
            playerId : playerId,
            playerData : playerData,
            allPlayersScreen : allPlayersScreen
        };
    };

    function defaultDrawBoard(drawer) {
        drawer.clear();
        drawer.drawLayers();
        drawer.drawPlayerNames();
    }

    drawBoard = (!!drawBoard) ? drawBoard : defaultDrawBoard;

    function calculateTextSize(text) {
        var div = $("#width_calculator_container");
        div.html(text);
        div.css('display', 'block');
        return div[0];
    }

    function showScoreInformation(playerId, information) {
        var infoPool = infoPools[playerId];

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

        var score = $("#score_info_" + playerId);
        if (score.is(':visible')) {
            return;
        }

        var text = '<center>' + infoPool.join('<br>') + '</center>';
        infoPool.splice(0, infoPool.length);

        var canvas = $("#" + playerId);
        var size = calculateTextSize(text);
        score.css({
                position: "absolute",
                marginLeft: 0,
                marginTop: 0,
                left: canvas.position().left + canvas.width()/2 - size.clientWidth/2,
                top: canvas.position().top + canvas.height()/2 - size.clientHeight/2
            });

        score.html(text);

        score.show().delay(700).fadeOut(200, function() {
            score.hide();

            showScoreInformation(playerId, '');
        });
    }

    function resizeCanvas(canvasName) {
        var canvas = $("#" + canvasName);

        var width = plotSize.width * boardSize;
        var height = plotSize.height * boardSize;

        if (canvas[0].width != width || canvas[0].height != height) {
            canvas[0].width = width;
            canvas[0].height = height;
        }
    }

    function createCanvas(canvasName) {
        var canvas = $("#" + canvasName);

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

        return {
            drawText: drawText,
            drawPolyline: drawPolyline,
            getCanvasContext : getCanvasContext,
            clear : clear
        };
    }

    function isPlayerListEmpty(data) {
        return Object.keys(data).length == 0;
    }

    function getNames(playerList) {
        var result = [];
        playerList.forEach(function (player) {
            result.push(player.id);
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
                var player = players[i].id;
                drawUserCanvas(player, data[player], false);
            }
        }
    }

    function drawUserCanvas(playerId, data, allPlayersScreen) {
        resizeCanvas(playerId);
        var canvas = canvases[playerId];
        drawBoard(getBoardDrawer(canvas, playerId, data, allPlayersScreen));

        $("#score_" + playerId).text(data.score);

        showScoreInformation(playerId, data.info);

        if (!allPlayersScreen) {
            $("#level_" + playerId).text(data.heroesData.coordinates[playerId] .level + 1);
        }
    }

    function loadSprites(contextPath, spriteElements){
        if(!sprites)
            sprites = [];
        for(var i=0;i<spriteElements.length;i++){
            var name = spriteElements[i];
            (sprites[name] = new Image()).src = contextPath + '/resources/sprite/lemonade/' + name + '.png';
        }
    }
}
