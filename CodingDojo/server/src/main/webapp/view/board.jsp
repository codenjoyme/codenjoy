<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta charset="utf-8">
    <title>Game boards</title>
    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<script src="${ctx}/resources/js/google-analytics.js"></script>

<link href="${ctx}/resources/css/dojo.css" rel="stylesheet">
<script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
<script src="${ctx}/resources/js/jcanvas.js"></script>
<script src="${ctx}/resources/js/jquery.simplemodal-1.4.4.js"></script>

<script src="${ctx}/resources/js/board.js"></script>
<script src="${ctx}/resources/js/donate.js"></script>
<script src="${ctx}/resources/js/joystick.js"></script>
<script src="${ctx}/resources/js/leaderstable.js"></script>
<script src="${ctx}/resources/js/chat.js"></script>
<script src="${ctx}/resources/js/hotkeys.js"></script>
<script src="${ctx}/resources/js/advertisement.js"></script>
<script src="${ctx}/resources/js/${gameName}.js"></script>
<script>
    var game = {
        contextPath : '${ctx}/',
        allPlayersScreen : ${allPlayersScreen},
        singleBoardGame : ${singleBoardGame},
        boardSize : ${boardSize},
        gameName : '${gameName}',
        playerName : '${playerName}',
        registered : ${registered},
        code : '${code}',
        gameName : '${gameName}'
    };

    $(document).ready(function () {
        game.players = new Object();
        <c:forEach items="${players}" var="player">
        game.players["${player.name}"] = "${player.name}";
        </c:forEach>

        initBoard(game.players, game.allPlayersScreen,
                    game.singleBoardGame, game.boardSize,
                    game.gameName, game.contextPath);

        if (boardSettings.enableDonate) {
            initDonate(game.contextPath);
        }

        if (boardSettings.enableJoystick) {
            initJoystick(game.playerName, game.registered,
                    game.code, game.contextPath);
        }

        if (boardSettings.enableLeadersTable) {
            initLeadersTable(game.contextPath, game.playerName,
                    game.code);
        }

        if (boardSettings.enableChat) {
            initChat(game.playerName, game.registered,
                    game.code, game.contextPath,
                    game.gameName);
        }

        if (boardSettings.enableHotkeys) {
            initHotkeys(game.gameName, game.contextPath);
        }

        if (boardSettings.enableAdvertisement) {
            initAdvertisement();
        }
    });
</script>

</body>
    <div>
        <%@include file="canvases.jsp"%>
        <%@include file="chat.jsp"%>
        <%@include file="advertisement.jsp"%>
        <%@include file="leaderstable.jsp"%>
        <%@include file="donate.jsp"%>
    </div>
</div>
</html>