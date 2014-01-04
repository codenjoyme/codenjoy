var currentBoardSize = null;

function initBoard(players, allPlayersScreen, boardSize, gameName, contextPath){
    var canvases = new Object();
    var infoPools = new Object();
    currentBoardSize = boardSize;
    var singleBoardGame = Object.keys(players).length == 1 && !allPlayersScreen;

    for (var i in players) {
        var player = players[i];
        canvases[player] = createCanvas(player);
        infoPools[player] = [];
    }

    function constructUrl() {
        var url = contextPath + "screen?";

        var playersPresent = !!Object.keys(players)[0];
        if (!playersPresent) {
            allPlayersScreen = true;
        }

        var users = ((!allPlayersScreen && playersPresent) ? ("&" + players[Object.keys(players)[0]]) : "");
        return url + "allPlayersScreen=" + allPlayersScreen + users;
    }

    function decode(gameName, color) {
        return plots[gameName][color];
    }

    function drawBoardForPlayer(playerName, gameName, board, coordinates) {
        canvases[playerName].clear();
        var x = 0;
        var y = boardSize - 1;
        $.each(board, function (index, color) {
            canvases[playerName].drawPlot(decode(gameName, color), x, y);
            x++;
            if (x == boardSize) {
               x = 0;
               y--;
            }
        });

        if (singleBoardGame) {
            $.each(coordinates, function(name, pt) {
                canvases[playerName].drawPlayerName(name, pt);
            });
        }
    }

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

        var score = $("#score_info_" + playerName);
        if (score.is(':visible')) {
            return;
        }

        var text = '<center>' + infoPool.join('<br><br>') + '</center>';
        infoPool.splice(0, infoPool.length);

        var canvas = $("#" + playerName);
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
        var plotSize = 30;
        var canvas = $("#" + canvasName);

        var drawPlot = function(color, x, y) {
            var plot = $("#" + color)[0];
            if (plotSize != plot.width) {
                plotSize = plot.width;
                canvas[0].width = plotSize * boardSize;
                canvas[0].height = plotSize * boardSize;
            }
            canvas.drawImage({
                source:plot,
                x:x * plotSize + plotSize / 2,
                y:(boardSize - y) * plotSize - plotSize / 2
            });
        };

        var drawPlayerName = function(name, pt) {
            canvas.drawText({
                fillStyle: '#0ff',
                strokeStyle: '#000',
                strokeWidth: 3,
                x: (pt.x + 2) * plotSize, y: (boardSize - pt.y - 1) * plotSize,
                fontSize: 10,
                fontFamily: 'Verdana, sans-serif',
                text: name
            });
        }

        var clear = function() {
            canvas.clearCanvas();
        }

        return {
            drawPlot : drawPlot,
            drawPlayerName: drawPlayerName,
            clear : clear
        };
    }

    function isPlayersListChanged(data) {
        var newPlayers = Object.keys(data);
        var oldPlayers = Object.keys(players);

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

        if (allPlayersScreen && isPlayersListChanged(data)) {
            window.location.reload();
            return;
        }

        if (allPlayersScreen) {
            $.each(data, drawUserCanvas);
        } else {
            for (var i in players) {
                var player = players[i];
                drawUserCanvas(player, data[player]);
            }
        }
    }

    function drawUserCanvas(playerName, data) {
        if (currentBoardSize != data.boardSize) {    // TODO так себе решение... Почему у разных юзеров передается размер добры а не всем сразу?
            window.location.reload();
        }
        if (chatLog == null) { // uses for chat.js
            chatLog = data.chatLog;
        }

        drawBoardForPlayer(playerName, data.gameName, data.board, $.parseJSON(data.coordinates));
        $("#score_" + playerName).text(data.score);
        showScoreInformation(playerName, data.info);
        if (!allPlayersScreen) {
            $("#level_" + playerName).text(data.level);
        }
    }

    $('body').bind("board-updated", function(events, data) {
        drawUsersCanvas(data);
    });

    function updatePlayersInfo() {
        currentCommand = null; // for joystick.js
        $.ajax({ url:constructUrl(),
                success:function (data) {

                    if (!!gameName) {  // TODO вот потому что dojo transport не делает подобной фильтрации - ее приходится делать тут.
                        var filtered = {};
                        for (var key in data) {
                            if (data[key].gameName == gameName) {
                                filtered[key] = data[key];
                            }
                        }

                        data = filtered;

                    }

                    $('body').trigger("board-updated", data);
                },
                dataType:"json",
                cache:false,
                complete:updatePlayersInfo,
                timeout:30000
            });
    }

    updatePlayersInfo();
}