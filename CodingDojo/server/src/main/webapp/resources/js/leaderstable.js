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
function initLeadersTable(contextPath, playerName, code, onSetup, onDrawItem, onParseValue){

    var leaderboard = $("#leaderboard");
    leaderboard.show();

    function getFirstValue(data) {
        return data[Object.keys(data)[0]];
    }

    function sortByScore(data) {
        var vals = new Array();

        for (i in data) {
            var score = data[i];
            if (!!onParseValue) {
                score = onParseValue(score);
            }
            vals.push([i, score])
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
            var link = contextPath + 'board/player/' + email + ((!!code)?('?code=' + code):"");
            tbody += onDrawItem(count, you, link, name, score);

        });

        $("#table-logs-body").empty().append(tbody);
        leaderboard.trigger($.Event('resize'));
    }

    function isEmpty(map) {
       for (var key in map) {
          if (map.hasOwnProperty(key)) {
             return false;
          }
       }
       return true;
    }

    $('body').bind("board-updated", function(event, data) {
        if (!isEmpty(data)) {
            drawLeaderTable(data);
        }
    });

    if (!!onSetup) {
        onSetup(leaderboard);
    }
};
