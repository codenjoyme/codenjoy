<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>jQuery demo</title>
</head>
<body>
<a href="http://jquery.com/">jQuery</a>
<script src="/resources/jquery-1.7.2.js"></script>
<script src="/resources/jcanvas.min.js"></script>
<script>
    var canvases = new Object();
    var players = new Object();

    function query() {
        $.ajax({ url:"/resources/testdata.txt", success:function (data) {
            $.each(data, function (playerName, value) {
                drawGlassForPlayer(playerName, value);
            });


            /*
             $.each(data, function(){

             })
             */
        }, dataType:"json", cache:false, timeout:30000 });
    }
    function drawGlassForPlayer(playerName, plots) {
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
        this.playerName = canvasName;

        Canvas.prototype.drawPlot = function (color, x, y) {
            $("#" + this.playerName).drawImage({
                source: $("#"+color)[0],
                x: x * plotSize,
                y: y * plotSize
            });
        }
    }

    $(document).ready(function () {
        <c:forEach items="${players}" var="player">
        canvases["${player.name}"] = new Canvas("${player.name}");
        players["${player.name}"] = "${player.name}";
        </c:forEach>
        (function poll() {
            $.ajax({ url:"/screen", success:function (data) {
                $.each(data, function (playerName, value) {
                    drawGlassForPlayer(playerName, value);
                })
            },
                data:players,
                dataType:"json", complete:poll, timeout:30000 });
        })();

        /*
         $("input").click(function () {
         query();
         })
         */
    });
</script>
</body>
<input type="button" value="ajax"/>

<div id="showdata"></div>
<c:forEach items="${players}" var="player">
    <canvas id="${player.name}" width="240" height="480" style="border:1px solid"> <!-- each pixel is 24x24-->
        Your browser does not support the canvas element.
    </canvas>
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

</html>