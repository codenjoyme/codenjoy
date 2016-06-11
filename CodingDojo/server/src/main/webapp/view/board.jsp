<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta charset="utf-8">
    <title>Game boards</title>
    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${ctx}/resources/css/dojo.css" rel="stylesheet">

    <script src="${ctx}/resources/js/google-analytics.js"></script>

    <script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
    <script src="${ctx}/resources/js/jcanvas.js"></script>
    <script src="${ctx}/resources/js/jquery.simplemodal-1.4.4.js"></script>

    <script src="${ctx}/resources/js/settings.js"></script>
    <script src="${ctx}/resources/js/canvases.js"></script>
    <script src="${ctx}/resources/js/layout.js"></script>
    <script src="${ctx}/resources/js/donate.js"></script>
    <script src="${ctx}/resources/js/joystick.js"></script>
    <script src="${ctx}/resources/js/leaderstable.js"></script>
    <script src="${ctx}/resources/js/chat.js"></script>
    <script src="${ctx}/resources/js/hotkeys.js"></script>
    <script src="${ctx}/resources/js/advertisement.js"></script>
    <script src="${ctx}/resources/js/${gameName}.js"></script>

    <script>
        game.contextPath = '${ctx}/';
        game.allPlayersScreen = ${allPlayersScreen};
        game.singleBoardGame = ${singleBoardGame};
        game.boardSize = ${boardSize};
        game.gameName = '${gameName}';
        game.playerName = '${playerName}';
        game.registered = ${registered};
        game.code = '${code}';
        game.gameName = '${gameName}';

        $(document).ready(function () {
            game.players = new Object();
            <c:forEach items="${players}" var="player">
            game.players["${player.name}"] = "${player.name}";
            </c:forEach>

            initCanvases(game.players, game.allPlayersScreen,
                        game.singleBoardGame, game.boardSize,
                        game.gameName, game.contextPath,
                        game.enablePlayerInfo);

            if (game.enableDonate) {
                initDonate(game.contextPath);
            }

            if (game.enableJoystick) {
                initJoystick(game.playerName, game.registered,
                        game.code, game.contextPath,
                        game.enableAlways);
            }

            if (game.enableLeadersTable) {
                initLeadersTable(game.contextPath, game.playerName,
                        game.code);
            }

            if (game.enableChat) {
                initChat(game.playerName, game.registered,
                        game.code, game.contextPath,
                        game.gameName);
            }

            if (game.enableHotkeys) {
                initHotkeys(game.gameName, game.contextPath);
            }

            if (game.enableAdvertisement) {
                initAdvertisement();
            }

            if (game.showBody) {
                $("#board_page").show();
            }
        });
    </script>
</head>
<body>
    <div id="board_page" style="display:none;">
        <%@include file="canvases.jsp"%>
        <%@include file="chat.jsp"%>
        <%@include file="advertisement.jsp"%>
        <%@include file="leaderstable.jsp"%>
        <%@include file="donate.jsp"%>
        <%@include file="widgets.jsp"%>
    </div>
</body>
</html>