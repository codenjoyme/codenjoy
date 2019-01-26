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

    var getBoardDrawer = function(canvas, playerName, playerData) {
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
            playerName : playerName,
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

        score.show().delay(700).fadeOut(200, function() {
            score.hide();

            showScoreInformation(playerName, '');
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
            $.each(data, drawUserCanvas);
        } else {
            for (var i in players) {
                var player = players[i].name;
                drawUserCanvas(player, data[player]);
            }
        }
    }

    function drawUserCanvas(playerName, data) {
        if (!canvases[playerName]) {
            reloadCanvasesData();
        }

       var canvas = canvases[playerName];
       canvas.boardSize = boardSize;
       drawBoard(getBoardDrawer(canvas, playerName, data));

        $("#score_" + toId(playerName)).text(data.score);

        showScoreInformation(playerName, data.info);

        if (!allPlayersScreen) {
            $("#level_" + toId(playerName)).text(data.heroesData.coordinates[playerName].level + 1);
        }
    }
}
