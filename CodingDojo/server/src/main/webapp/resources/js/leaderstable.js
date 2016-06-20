function initLeadersTable(contextPath, playerName, code, onSetup, onDrawItem){

    var leaderboard = $("#leaderboard");
    leaderboard.show();

    function getFirstValue(data) {
        return data[Object.keys(data)[0]];
    }

    function sortByScore(data) {
        var vals = new Array();

        for (i in data) {
            vals.push([i, data[i]])
        }
        vals = vals.sort(function(a, b) {
            return b[1] - a[1];
        });

        var result = new Object();

        for (i in vals) {
            result[vals[i][0]] = vals[i][1];
        }

        return result;
    }

    function drawLeaderTable(data) {
        if (data == null) {
            $("#table-logs-body").empty();
            return;
        }
        data = getFirstValue(data);
        data = data.scores;
        if (data == null) {
            $("#table-logs-body").empty();
            return;
        }
        data = $.parseJSON(data);

        data = sortByScore(data);

        if (!onDrawItem) {
            onDrawItem = function(count, you, link, name, score) {
                return '<tr>' +
                        '<td>' + count + '</td>' +
                        '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                        '<td class="center">' + score + '</td>' +
                    '</tr>';
            }
        }

        var tbody = '';
        var count = 0;
        $.each(data, function (email, score) {
            var name = email.substring(0, email.indexOf('@'));
            if (name == 'chatLog') {
                return;
            }

            var you = (name == playerName)?"=> ":"";

            count++;
            var link = contextPath + 'board/' + email + ((!!code)?('?code=' + code):"");
            tbody += onDrawItem(count, you, link, name, score);

        });

        $("#table-logs-body").empty().append(tbody);
        leaderboard.trigger($.Event('resize'));
    }

    $('body').bind("board-updated", function(event, data) {
        drawLeaderTable(data);
    });

    if (!!onSetup) {
        onSetup(leaderboard);
    }
};
