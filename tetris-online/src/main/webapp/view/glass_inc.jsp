<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="loggedUser" value='${requestScope["logged.user"]}'/>

<script>
    var canvases = new Object();
    var players = new Object();
    var currentRequest;
    var currentReplayId;

    function constructUrl() {
        return '${pageContext.request.contextPath}/screen?replayId='+currentReplayId;
    }

    function drawGlassForPlayer(playerName, plots) {
        canvases['${loggedUser}'].clear();
        $.each(plots, function (index, plot) {
            for (var color in plot) {
                x = plot[color][0];
                y = plot[color][1];
                canvases['${loggedUser}'].drawPlot(color, x, y);
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

    function cancelCurrentReplay() {
        if (currentReplayId) {
            $.ajax({url:'${pageContext.request.contextPath}' + '/cancel?replayId='+currentReplayId});
        }
        if (currentRequest) {
            currentRequest.abort();
        }
    }

    function replay(obj, timestamp, playerName) {
        $.ajax({ url:'${pageContext.request.contextPath}' + '/replay?timestamp=' + timestamp + '&player='+playerName,
            success:function (data) {
                cancelCurrentReplay();
                currentReplayId = data;
                drawReplay();
            },
            error:function (xhr, ajaxOptions, thrownError) {
                $("#error").text("Error on getting game progress. status:" + xhr.status + " error: " + thrownError);
            },
            dataType:"text", cache:false, timeout:300000 });

        function drawReplay() {
            canvases["${loggedUser}"] = new Canvas("${loggedUser}");
            players["${loggedUser}"] = "${loggedUser}";
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
                                    $("#score").text(data);
                                }
                                if (key == "linesRemoved") {
                                    $("#lines_removed").text(data);
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
<c:set var="playerName" value="<%=request.getAttribute("logged.user")%>"/>
<div id="div_${loggedUser}" style="float: left">
    <table>
        <tr>
            <td>
                <span class="label label-info big">Score</span> :
                <span class="label label-info big" id="score"></span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="label small">Lines removed</span> :
                <span class="label small" id="lines_removed"></span>
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



