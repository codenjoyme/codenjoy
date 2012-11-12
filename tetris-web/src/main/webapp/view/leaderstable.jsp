<script>
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

    function drawLeaderTable(data) {
        if (data == null) {
            $("#table-logs-body").empty();
            return;
        }

        data = sortByScore(data);

        var tbody = '';
        var count = 0;
        $.each(data, function (playerName, playerData) {
            count++;
            tbody +=
             '<tr>' +
                 '<td>' + count + '</td>' +
                 '<td>' + playerName + '</td>' +
                 '<td class="center">' + playerData.score + '</td>' +
                 '<td class="center">' + playerData.level + '</td>' +
             '</tr>'
        });

        $("#table-logs-body").empty().append(tbody);
    }

    $(document).ready(function () {
        (function updateLeaderBoard() {
            setTimeout(function() {
                if (typeof allPlayersData == 'undefined' || !allPlayersData) {
                    $.ajax({ url:"/screen?allPlayersScreen=true",
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
        })();
    });
</script>

<style type="text/css"><!--
    #table-logs th.center, #table-logs td.center  {
        text-align : center;
    }

    #table-logs {
        font-size:25px;
    }
--></style>

<table id="table-logs" class="table table-striped">
    <thead>
        <th width="5%">#</th>
        <th width="50%">Player</th>
        <th width="20%" class="center">Score</th>
        <th width="15%" class="center">Level</th>
    </thead>
    <tbody id="table-logs-body">
    </tbody>
</table>