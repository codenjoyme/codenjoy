function initLeadersTable(contextPath){

    function leaderboardStyle() {
        var width = $("#leaderboard").width();
        var margin = 20;
        $("#glasses").width($(window).width() - width - margin).css({ marginLeft: margin, marginTop: margin });

        $("#leaderboard").width(width).css({ position: "absolute",
                        marginLeft: 0, marginTop: margin,
                        top: 0, left: $("#glasses").width()});
    }

    function sortByScore(data) {
        var vals = new Array();

        for (i in data) {
            vals.push([i, data[i]])
        }
        vals = vals.sort(function(a, b) {
            return b[1].score - a[1].score
        });

        var result = new Object();

        for (i in vals) {
            result[vals[i][0]] = vals[i][1];
        }

        return result;
    }

    function makeMePositive(data) {
        var vals = new Array();

        var min = 0;
        for (i in data) {
           if (min > data[i].score) {
                min = data[i].score;
           }
        }

        var max = min;
        for (i in data) {
           if (max < data[i].score) {
                max = data[i].score;
           }
        }

        for (i in data) {
            data[i].score = Math.round(100*(data[i].score-min)/(-min+max));
        }

        return data;
    }

    function drawLeaderTable(data) {
        if (data == null) {
            $("#table-logs-body").empty();
            return;
        }

        data = sortByScore(data);
        // data = makeMePositive(data);

        var tbody = '';
        var count = 0;
        $.each(data, function (playerName, playerData) {
            if (playerName == 'chatLog') {
                return;
            }

            count++;
            tbody +=
             '<tr>' +
                 '<td>' + count + '</td>' +
                 '<td>' + playerName + '</td>' +
                 '<td class="center">' + playerData.score + '</td>' +
                 '<td class="center">' + playerData.maxLength + '</td>' +
                 '<td class="center">' + playerData.level + '</td>' +
             '</tr>'
        });

        $("#table-logs-body").empty().append(tbody);
        $("#leaderboard").trigger($.Event('resize'));
    }

    function updateLeaderBoard() {
        setTimeout(function() {
            if (typeof allPlayersData == 'undefined' || !allPlayersData) {
                $.ajax({ url:contextPath + "screen?allPlayersScreen=true",
                    success:drawLeaderTable,
                    data:{},
                    dataType:"json",
                    complete:updateLeaderBoard,
                    cache:false,
                    timeout:30000});
            } else {
                drawLeaderTable(allPlayersData);
                allPlayersData = null;
                updateLeaderBoard();
            }
        }, 1000);
    }

    if (!!$("#glasses")) {
        $(window).resize(leaderboardStyle);
        leaderboardStyle();
    }

    updateLeaderBoard();
};