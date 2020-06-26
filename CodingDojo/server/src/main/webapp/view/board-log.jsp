<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2018 Codenjoy
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
    <title>Game board</title>
    <link href="${ctx}/resources/css/all-board-only.min.css" rel="stylesheet">

    <style>
        canvas {
            width: unset!important;
            cursor: unset!important;
        }
        .player_info {
            color: white!important;
        }
        .player_info h2 {
            line-height: 11px;
            margin: 0px;
        }
        .label-value {
            font-size: 14px;
            font-weight: 100;
        }
        .player-canvas {
            margin: 15px;
        }
    </style>

    <script src="${ctx}/resources/js/canvases-${gameNameOnly}.js"></script>
    <script src="${ctx}/resources/js/all.js"></script>
</head>
<body style="display:none;">
    <div id="settings" page="boardLog" contextPath="${ctx}" gameName="${gameName}" playerId="${playerId}" readableName="${readableName}"></div>

    <div id="board_page">
        <%@include file="canvases.jsp"%>
    </div>
</body>
</html>
