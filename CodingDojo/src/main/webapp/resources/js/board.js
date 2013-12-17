var currentBoardSize = null;

function initBoard(players, allPlayersScreen, boardSize, gameType, contextPath){
    var canvases = new Object();
    var infoPools = new Object();
    currentBoardSize = boardSize;

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

    var plots = {
        'bomberman':function(color) {
            switch (color) {
               case 'A' : return 'bomberman';
               case 'B' : return 'bomb_bomberman';
               case 'C' : return 'dead_bomberman';
               case 'D' : return 'boom';
               case 'E' : return 'bomb_five';
               case 'F' : return 'bomb_four';
               case 'G' : return 'bomb_three';
               case 'H' : return 'bomb_two';
               case 'I' : return 'bomb_one';
               case 'J' : return 'wall';
               case 'K' : return 'destroy_wall';
               case 'L' : return 'destroyed_wall';
               case 'M' : return 'meat_chopper';
               case 'N' : return 'dead_meat_chopper';
               case 'O' : return 'empty';
               case 'P' : return 'other_bomberman';
               case 'Q' : return 'other_bomb_bomberman';
               case 'R' : return 'other_dead_bomberman';
            }
        },

        'snake':function(color) {
             switch (color) {
                case 'A' : return 'bad_apple';
                case 'B' : return 'good_apple';
                case 'C' : return 'break';
                case 'D' : return 'head_down';
                case 'E' : return 'head_left';
                case 'F' : return 'head_right';
                case 'G' : return 'head_up';
                case 'H' : return 'tail_end_down';
                case 'I' : return 'tail_end_left';
                case 'J' : return 'tail_end_up';
                case 'K' : return 'tail_end_right';
                case 'L' : return 'tail_horizontal';
                case 'M' : return 'tail_vertical';
                case 'N' : return 'tail_left_down';
                case 'O' : return 'tail_left_up';
                case 'P' : return 'tail_right_down';
                case 'Q' : return 'tail_right_up';
                case 'R' : return 'space';
             }
        },

        'minesweeper':function(color) {
             switch (color) {
                case 'A' : return 'bang';
                case 'B' : return 'here_is_bomb';
                case 'C' : return 'detector';
                case 'D' : return 'flag';
                case 'E' : return 'hidden';
                case 'F' : return 'one_mine';
                case 'G' : return 'two_mines';
                case 'H' : return 'three_mines';
                case 'I' : return 'four_mines';
                case 'J' : return 'five_mines';
                case 'K' : return 'six_mines';
                case 'L' : return 'seven_mines';
                case 'M' : return 'eight_mines';
                case 'N' : return 'border';
                case 'O' : return 'no_mine';
                case 'P' : return 'destroyed_bomb';
             }
        },

        'battlecity':function(color) {
            switch (color) {
                case 'A' : return 'battle_ground';
                case 'B' : return 'battle_wall';
                case 'C' : return 'dead_tank';
                case 'D' : return 'construction';

                case 'E' : return 'construction_destroyed_down';
                case 'F' : return 'construction_destroyed_up';
                case 'G' : return 'construction_destroyed_left';
                case 'H' : return 'construction_destroyed_right';

                case 'I' : return 'construction_destroyed_down_twice';
                case 'J' : return 'construction_destroyed_up_twice';
                case 'K' : return 'construction_destroyed_left_twice';
                case 'L' : return 'construction_destroyed_right_twice';

                case 'M' : return 'construction_destroyed_left_right';
                case 'N' : return 'construction_destroyed_up_down';

                case 'O' : return 'construction_destroyed_up_left';
                case 'P' : return 'construction_destroyed_right_up';
                case 'Q' : return 'construction_destroyed_down_left';
                case 'R' : return 'construction_destroyed_down_right';

                case 'S' : return 'battle_ground';

                case 'T' : return 'bullet';

                case 'U' : return 'tank_up';
                case 'V' : return 'tank_right';
                case 'W' : return 'tank_down';
                case 'X' : return 'tank_left';

                case 'Y' : return 'other_tank_up';
                case 'Z' : return 'other_tank_right';
                case '0' : return 'other_tank_down';
                case '1' : return 'other_tank_left';

                case '2' : return 'ai_tank_up';
                case '3' : return 'ai_tank_right';
                case '4' : return 'ai_tank_down';
                case '5' : return 'ai_tank_left';

            }
        },

        'loderunner':function(color) {
            switch (color) {
                case 'A' : return 'none';
                case 'B' : return 'brick';

                case 'C' : return 'pit_fill_1';
                case 'D' : return 'pit_fill_2';
                case 'E' : return 'pit_fill_3';
                case 'F' : return 'pit_fill_4';
                case 'G' : return 'undestroyable_wall';

                case 'H' : return 'drill_space';
                case 'I' : return 'drill_pit';

                case 'J' : return 'enemy_ladder';
                case 'K' : return 'enemy_left';
                case 'L' : return 'enemy_right';
                case 'M' : return 'enemy_pipe_left';
                case 'N' : return 'enemy_pipe_right';
                case 'O' : return 'enemy_pit';

                case 'P' : return 'gold';

                case 'Q' : return 'hero_die';
                case 'R' : return 'hero_drill_left';
                case 'S' : return 'hero_drill_right';
                case 'T' : return 'hero_ladder';
                case 'U' : return 'hero_left';
                case 'V' : return 'hero_right';
                case 'W' : return 'hero_pipe_left';
                case 'X' : return 'hero_pipe_right';

                case 'Y' : return 'ladder';
                case 'Z' : return 'pipe';
            }
        }
    }

    function decode(color) {
        return plots[gameType](color);
    }

    function drawBoardForPlayer(playerName, board) {
        canvases[playerName].clear();
        var x = 0;
        var y = boardSize - 1;
        $.each(board, function (index, color) {
            canvases[playerName].drawPlot(decode(color), x, y);
            x++;
            if (x == boardSize) {
               x = 0;
               y--;
            }
       })
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

        var clear = function() {
            canvas.clearCanvas();
        }

        return {
            drawPlot : drawPlot,
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

        drawBoardForPlayer(playerName, data.board);
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