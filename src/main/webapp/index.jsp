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

                $('#showdata').append("<p>" + color + " x:" + x + " y:" + y + "</p>");
            }
        })

    }

    function Canvas() {
        const plotSize = 24;
        const images = ["blue", "cyan", "green", "orange", "purple", "red", "yellow"];
        this.canvasName = undefined;

        this.prototype.drawPlot = function (color, x, y) {
            $(this.canvasName).drawImage({
                source: $(color),

            });
        }
    }

    $(document).ready(function () {
        /*
         (function poll(){
         $.ajax({ url: "/resources/testdata.txt", success: function(data){
         $('#showdata').html("<p>" + data + "</p>");

         }, dataType: "json", complete: poll, timeout: 30000 });
         })();
         */
        $("input").click(function () {
            query();
        })
    });
</script>
</body>
<input type="button" value="ajax"/>

<div id="showdata"></div>
<!-- TODO: Iterate over players. Each player has its own canvas-->
<canvas id="Player1" width="240" height="480" style="border:1px solid"> <!-- each pixel is 24x24-->
    Your browser does not support the canvas element.
</canvas>

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