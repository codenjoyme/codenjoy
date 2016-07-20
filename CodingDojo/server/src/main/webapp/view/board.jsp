<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2016 Codenjoy
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
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
    <script src="${ctx}/resources/js/board.js"></script>
    <script src="${ctx}/resources/js/canvases.js"></script>
    <script src="${ctx}/resources/js/layout.js"></script>
    <script src="${ctx}/resources/js/donate.js"></script>
    <script src="${ctx}/resources/js/joystick.js"></script>
    <script src="${ctx}/resources/js/leaderstable.js"></script>
    <script src="${ctx}/resources/js/chat.js"></script>
    <script src="${ctx}/resources/js/hotkeys.js"></script>
    <script src="${ctx}/resources/js/advertisement.js"></script>
    <!-- TODO этот скрипт должен загрузиться с игрушки -->
    <script src="${ctx}/resources/${gameName}/js/ace/src/ace.js"></script>
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

            initBoards(game.players, game.allPlayersScreen,
                    game.gameName, game.contextPath);

            initCanvases(game.players, game.allPlayersScreen,
                        game.singleBoardGame, game.boardSize,
                        game.gameName, game.enablePlayerInfo);

            if (game.enableDonate) {
                initDonate(game.contextPath);
            }

            if (game.enableJoystick) {
                initJoystick(game.playerName, game.registered,
                        game.code, game.contextPath,
                        game.enableAlways);
            }

            if (game.enableLeadersTable) {
                initLeadersTable(game.contextPath, game.playerName, game.code,
                        function(leaderboard) {
                            if (!!$("#glasses")) {
                                $(window).resize(resize);
                                resize();
                            }
                            function resize() {
                                var width = leaderboard.width();
                                var margin = 20;

                                $("#glasses").width($(window).width() - width - margin)
                                        .css({ marginLeft: margin, marginTop: margin });

                                leaderboard.width(width).css({ position: "absolute",
                                                marginLeft: 0, marginTop: margin,
                                                top: 0, left: $("#glasses").width()});
                            }
                        });
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
                $(document.body).show();
            }

            if (game.allPlayersScreen) {
                if (!!game.onBoardAllPageLoad) {
                    game.onBoardAllPageLoad();
                }
            } else {
                if (!!game.onBoardPageLoad) {
                    game.onBoardPageLoad();
                }
            }
        });
    </script>
</head>
<body style="display:none;">
    <div id="board_page">
        <%@include file="canvases.jsp"%>
        <%@include file="chat.jsp"%>
        <%@include file="advertisement.jsp"%>
        <%@include file="leaderstable.jsp"%>
        <%@include file="donate.jsp"%>
        <%@include file="widgets.jsp"%>
    </div>
</body>
</html>