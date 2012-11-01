<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    var canvases = new Object();
    var players = new Object();
    var currentRequest;

    function constructUrl() {
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

    function replay(obj, timestamp) {
        $.ajax({ url:'${pageContext.request.contextPath}' + '/replay?timestamp=' + timestamp,
            success:function (data) {
                drawReplay();
            },
            error:function (xhr, ajaxOptions, thrownError) {
                $("#error").text("Error on getting game progress. status:" + xhr.status + " error: " + thrownError);
            },
            dataType:"text", cache:false, timeout:300000 });

        function drawReplay() {
            canvases["${requestScope["logged.user"]}"] = new Canvas("${requestScope["logged.user"]}");
            players["${requestScope["logged.user"]}"] = "${requestScope["logged.user"]}";
            if (currentRequest) {
                currentRequest.abort();
            }
            (function poll() {
                currentRequest = $.ajax({ url:constructUrl(),
                    success:function (data) {
                        if (data == null) {
                            $("#showdata").text("There is NO data for player available!");
                            return;
                        }
                        $.each(data, function (playerName, value) {
                            $.each(value, function (key, data) {
                                if (key == "plots") {
                                    drawGlassForPlayer(playerName, data);
                                } else if (key == "score") {
                                    $("#score_" + playerName).text(data);
                                }
                                if (key == "linesRemoved") {
                                    $("#lines_removed_" + playerName).text(data);
                                } else if (key == "nextLevelIngoingCriteria") {
                                    $("#next_level_" + playerName).text(data);
                                } else if (key == "level") {
                                    $("#level_" + playerName).text(data);
                                }
                            });
                        });
                    },
                    data:players,
                    dataType:"json", cache:false,
                    complete:function (obj, status) {
                        if (status != "abort") {
                            poll();
                        }
                    }, timeout:30000 });
            })();
        }
    }
</script>
</body>

<div id="showdata"></div>
<div class="row-fluid">
    <div class="span10">
        <c:set var="playerName" value="<%=request.getAttribute("logged.user")%>"/>
        <div id="div_${requestScope["logged.user"]}" style="float: left">
            <table>
                <tr>
                    <td>
                        <span class="label label-info big">${playerName}</span> :
                        <span class="label label-info big" id="score_${playerName}"></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="label small">Level</span> :
                        <span class="label small" id="level_${playerName}"></span>
                        <span class="label small">Lines removed</span> :
                        <span class="label small" id="lines_removed_${playerName}"></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="label small">For next level</span> :
                        <span class="label small" id="next_level_${playerName}"></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <canvas id="${playerName}" width="240" height="480" style="border:1px solid">
                            <!-- each pixel is 24x24-->
                            Your browser does not support the canvas element.
                        </canvas>
                    </td>
                </tr>
            </table>
        </div>

        <div id="systemCanvas" style="display: none">
            <canvas id="_system" width="168" height="24"> <!-- 7 figures x 24px-->
                Your browser does not support the canvas element.
            </canvas>
            <img src="${pageContext.request.contextPath}/resources/blue.png" id="blue">
            <img src="${pageContext.request.contextPath}/resources/cyan.png" id="cyan">
            <img src="${pageContext.request.contextPath}/resources/green.png" id="green">
            <img src="${pageContext.request.contextPath}/resources/orange.png" id="orange">
            <img src="${pageContext.request.contextPath}/resources/purple.png" id="purple">
            <img src="${pageContext.request.contextPath}/resources/red.png" id="red">
            <img src="${pageContext.request.contextPath}/resources/yellow.png" id="yellow">
        </div>
    </div>

</div>
