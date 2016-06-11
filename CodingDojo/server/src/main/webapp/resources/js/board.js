var currentBoardSize = null;

function initBoard(players, allPlayersScreen, singleBoardGame, boardSize, gameName, contextPath){
    var canvases = new Object();
    var infoPools = new Object();
    currentBoardSize = boardSize;

    function toId(email) {
        return email.replace(/[@.]/gi, "_");
    }

    for (var i in players) {
        var player = players[i];
        canvases[player] = createCanvas(toId(player));
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
        return plots[color];
    }

    function drawBoardForPlayer(playerName, gameName, board, coordinates) {
        var drawLayer = function(layer){
            var x = 0;
            var y = boardSize - 1;
            $.each(layer, function (index, color) {
                canvases[playerName].drawPlot(decode(gameName, color), x, y);
                x++;
                if (x == boardSize) {
                   x = 0;
                   y--;
                }
            });
        }

        try {
            var json = $.parseJSON(board);
            $.each(json.layers, function(index, layer) {
                drawLayer(layer);
            });
        } catch (err) {
            drawLayer(board);
        }

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

        var score = $("#score_info_" + toId(playerName));
        if (score.is(':visible')) {
            return;
        }

        var text = '<center>' + infoPool.join('<br><br>') + '</center>';
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
        var plotSize = $('#systemCanvas img')[0].width;
        var canvas = $("#" + canvasName);
        var canvasSize = plotSize * boardSize;
        if (canvas[0].width != canvasSize || canvas[0].height != canvasSize) {
            canvas[0].width = canvasSize;
            canvas[0].height = canvasSize;
        }

        var drawPlot = function(color, x, y) {
            var plot = $("#" + color)[0];
            var img = new Image();
            var ctx = canvas[0].getContext("2d");
            img.onload = function() {
                ctx.drawImage(
                    img,
                    x * plotSize - (plot.width - plotSize)/2,
                    (boardSize - 1 - y) * plotSize - (plot.height - plotSize)
                );
            }
            img.src = plot.src;
        };

        var drawPlayerName = function(email, pt) {
            var name = email.substring(0, email.indexOf('@'));

            if (pt.x == -1 || pt.y == -1) return;

            var ctx = canvas[0].getContext("2d");
            ctx.font = "15px 'Verdana, sans-serif'";
            ctx.fillStyle = "#0FF";
            ctx.textAlign = "left";
            ctx.shadowColor = "#000";
            ctx.shadowOffsetX = 0;
            ctx.shadowOffsetY = 0;
            ctx.shadowBlur = 7;
            ctx.fillText(name, (pt.x + 1) * plotSize, (boardSize - pt.y - 1) * plotSize - 5);
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

    function isPlayerListEmpty(data) {
        return Object.keys(data).length == 0;
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

        if (allPlayersScreen && isPlayersListChanged(data) || isPlayerListEmpty(data)) {
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

        drawBoardForPlayer(playerName, data.gameName, data.board, $.parseJSON(data.coordinates));
        $("#score_" + toId(playerName)).text(data.score);
        showScoreInformation(playerName, data.info);
        if (!allPlayersScreen) {
            $("#level_" + toId(playerName)).text(data.level);
        }
    }

    $('body').bind("board-updated", function(events, data) {
        drawUsersCanvas(data);
    });

    function updatePlayersInfo() {
        currentCommand = null; // for joystick.js
        $.ajax({ url:constructUrl(),
                error:function(data) {
                    $('body').css('background-color', 'bisque');
                },
                success:function (data) {
                    $('body').css('background-color', 'white');

                    // TODO:1 Вот тут надо вообще другим запросом чат брать из другого скрина, чтобы тут им и не пахло
                    if (chatLog == null) { // uses for chat.js
                        chatLog = data['#CHAT'].messages;
                    }
                    delete data['#CHAT'];

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