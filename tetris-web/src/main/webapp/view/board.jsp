<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Glass board</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<script src="/resources/jquery-1.7.2.js"></script>
<script src="/resources/jcanvas.min.js"></script>
<script>
    var canvases = new Object();
    var players = new Object();

    function constructUrl() {
        if (allPlayersScreen) {
            return "/screen?allPlayersScreen=true"
        }
        var url = "/screen?";
        for (var player in players) {
            if (players.hasOwnProperty(player)) {
                url += player + "=" + player + "&";
            }
        }
        return url;
    }

    function drawGlassForPlayer(playerName, plots) {
        canvases[playerName].clear();
        $.each(plots, function (index, plot) {
            for (var color in plot) {
                x = plot[color][0];
                y = plot[color][1];
                canvases[playerName].drawPlot(color, x, y);
//                $('#showdata').append("<p>" + color + " x:" + x + " y:" + y + "</p>");
            }
        })

    }

    function Canvas(canvasName) {
        const plotSize = 24;
        const glassHeight = 20;
        this.playerName = canvasName;

        Canvas.prototype.drawPlot = function (color, x, y) {
            $("#" + this.playerName).drawImage({
                source:$("#" + color)[0],
                x:x * plotSize + plotSize / 2,
                y:(glassHeight - y) * plotSize - plotSize / 2
            });
        };

        Canvas.prototype.clear = function () {
            $("#" + this.playerName).clearCanvas();
        }
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

    $(document).ready(function () {
        <c:forEach items="${players}" var="player">
        canvases["${player.name}"] = new Canvas("${player.name}");
        players["${player.name}"] = "${player.name}";
        </c:forEach>
        allPlayersScreen = ${allPlayersScreen};
        (function updatePlayersInfo() {
            $.ajax({ url:constructUrl(), success:function (data) {
                if (data == null) {
                    $("#showdata").text("There is NO data for player available!");
                    return;
                }
                $("#showdata").text('');

                if (allPlayersScreen && isPlayersListChanged(data)) {
                    window.location.reload();
                    return;
                }
                if (allPlayersScreen) { // uses for leaderstable.jsp
                    allPlayersData = data;
                }
                $.each(data, function (playerName, value) {
                    $.each(value, function (key, data) {
                        if (key == "plots") {
                            drawGlassForPlayer(playerName, data);
                        } else if (key == "score") {
                            $("#score_" + playerName).text(data);
                        }
                        if (!allPlayersScreen) {
                            if (key == "linesRemoved") {
                                $("#lines_removed_" + playerName).text(data);
                            } else if (key == "nextLevelIngoingCriteria") {
                                $("#next_level_" + playerName).text(data);
                            } else if (key == "level") {
                                $("#level_" + playerName).text(data);
                            }
                        }
                    });
                });
            },
                data:players,
                dataType:"json", cache:false, complete:updatePlayersInfo, timeout:30000 });
        })();

        /*
         $("input").click(function () {
         query();
         })
         */
    });
</script>

    <div id="showdata"></div>
    <div>
        <div id="glasses">
            <c:forEach items="${players}" var="player">
                <div id="div_${player.name}" style="float: left">
                    <table>
                        <tr>
                            <td>
                                <span class="label label-info big">${player.name}</span> :
                                <span class="label label-info big" id="score_${player.name}"></span>
                            </td>
                        </tr>
                        <c:if test="${!allPlayersScreen}">
                            <tr>
                                <td>
                                    <span class="label small">Level</span> :
                                    <span class="label small" id="level_${player.name}"></span>
                                    <span class="label small">Lines removed</span> :
                                    <span class="label small" id="lines_removed_${player.name}"></span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="label small">For next level</span> :
                                    <span class="label small" id="next_level_${player.name}"></span>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td>
                                <canvas id="${player.name}" width="240" height="480" style="border:1px solid">
                                    <!-- each pixel is 24x24-->
                                    Your browser does not support the canvas element.
                                </canvas>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>

            <div id="systemCanvas" style="display: none">
                <canvas id="_system" width="168" height="24"> <!-- 7 figures x 24px-->
                    Your browser does not support the canvas element.
                </canvas>
                <img src="/resources/blue.png" id="blue">
                <img src="/resources/cyan.png" id="cyan">
                <img src="/resources/green.png" id="green">
                <img src="/resources/orange.png" id="orange">
                <img src="/resources/purple.png" id="purple">
                <img src="/resources/red.png" id="red">
                <img src="/resources/yellow.png" id="yellow">
            </div>
        </div>
        <div id="leaderboard">
            <script>
                $(document).ready(function () {
                    function leaderboardStyle() {
                        var width = 450;
                        var margin = 20;
                        $("#glasses").width($(window).width() - width - margin).css({ marginLeft: margin, marginTop: margin });

                        $("#leaderboard").width(width).css({ position: "absolute",
                                        marginLeft: 0, marginTop: margin,
                                        top: 0, left: $("#glasses").width()});
                    }

                    $(window).resize(leaderboardStyle);
                    leaderboardStyle();
                });
            </script>
            <%@include file="leaderstable.jsp"%>
        </div>
    </div>
</body>
</html>