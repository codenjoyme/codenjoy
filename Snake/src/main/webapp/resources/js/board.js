var allPlayersData = null;

function initBoard(players, allPlayersScreen, boardSize){
   var canvases = new Object();

   for (var i in players) {
       var player = players[i];
       canvases[player] = new Canvas(player);
   }

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
       const glassHeight = boardSize;
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

   (function poll() {
       $.ajax({ url:constructUrl(), success:function (data) {
           if (data == null) {
               $("#showdata").text("There is NO data for player available!");
               return;
           }
           if (allPlayersScreen && Object.keys(data).length != Object.keys(players).length) {
               window.location.reload();
               return;
           }
           $.each(data, function (playerName, value) {
               $.each(value, function (key, data) {
                   if (key == "plots") {
                       drawGlassForPlayer(playerName, data);
                   } else if (key == "score") {
                       $("#score_" + playerName).text(data);
                   }
                   if (!allPlayersScreen) {
                       if (key == "level") {
                           $("#level_" + playerName).text(data);
                       }
                   }
               });
           });
       },
           data:players,
           dataType:"json", cache:false, complete:poll, timeout:30000 });
   })();
}