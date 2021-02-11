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
var currentBoardSize = null;

function initCanvasesText(contextPath, players, allPlayersScreen,
                multiplayerType, boardSize, gameName,
                enablePlayerInfo, enablePlayerInfoLevel, drawBoard)
{
    var canvases = {};
    var infoPools = {};
    currentBoardSize = boardSize;
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
            var playerIds = getIds(players);
            var newPlayerIds = getIds(newPlayers);
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
        if (!!game.canvasCursor) {
            $('#players_container canvas').css('cursor', game.canvasCursor);
        }
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

    var getBoardDrawer = function(canvas, playerId, playerData) {
        var data = playerData.board;

        var clear = function() {
            canvas.resizeHeight(data.history.length + 1);
            canvas.clear();
        }

        var drawLines = function() {
            for (var index in data.history) {
                var value = data.history[index];
                if (!value.valid) color = '#900';
                if (value.valid) color = '#090';

                canvas.drawText(value.question, {x:7, y:1 + parseInt(index)}, color);
            }

            if (!!data.nextQuestion) {
                canvas.drawText(data.nextQuestion, {x:7, y:1 + parseInt(data.history.length)}, '#099');
            }
        }

        return {
            clear : clear,
            drawLines : drawLines,
            canvas : canvas,
            drawText: canvas.drawText,
            playerId : playerId,
            playerData : playerData
        };
    }

    function defaultDrawBoard(drawer) {
        drawer.clear();
        drawer.drawLines();
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

    function createCanvas(canvasName) {
        var plotSize = 20;
        var canvas = $("#" + canvasName);
        canvas[0].width = plotSize * 15 ;
        canvas[0].height = plotSize * 15;

        var drawPlot = function(color, x, y) {
            var plot = $("#" + color)[0];
            canvas.drawImage({
                source:plot,
                x:x * plotSize + plotSize / 2,
                y:(boardSize - y) * plotSize - plotSize / 2
            });
        };

        var drawText = function(text, pt, color) {
            if (!text) {
                console.warn("Text to draw is undefined or empty");
            }
            canvas.drawText({
                fillStyle: color,
                strokeStyle: '#000',
                strokeWidth: 0,
                x: (pt.x) * plotSize, y: (pt.y) * plotSize,
                fontSize: 16,
                fontFamily: 'Verdana, sans-serif',
                text: text
            });
        }

        var clear = function() {
            canvas.clearCanvas();
        }

        var resizeHeight = function(current) {
            if (canvas[0].height < current * plotSize) {
                canvas[0].height = canvas[0].height * 1.5;
            }
        }

        return {
            drawPlot : drawPlot,
            drawText: drawText,
            clear : clear,
            resizeHeight : resizeHeight
        };
    }

    function isPlayerListEmpty(data) {
        return Object.keys(data).length == 0;
    }

    function getIds(playerList) {
        var result = [];
        playerList.forEach(function (player) {
            result.push(player.id);
        });
        return result;
    }

    function isPlayersListChanged(data) {
        var newPlayers = Object.keys(data);
        var oldPlayers = getIds(players);

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
            $.each(data, drawUserCanvas);
        } else {
            for (var i in players) {
                var player = players[i].id;
                drawUserCanvas(player, data[player]);
            }
        }
    }

    function drawUserCanvas(playerId, data) {
        if (!canvases[playerId]) {
            reloadCanvasesData();
        }

       var canvas = canvases[playerId];
       canvas.boardSize = boardSize;
       drawBoard(getBoardDrawer(canvas, playerId, data));

        $("#score_" + playerId).text(data.score);

        showScoreInformation(playerId, data.info);

        if (!allPlayersScreen) {
            $("#level_" + playerId).text(data.heroesData.coordinates[playerId].level + 1);
        }
    }
}
