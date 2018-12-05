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
    <title>Game boards</title>
    <link href="${ctx}/resources/css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/dojo.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">

    <script src="${ctx}/resources/js/google-analytics.js"></script>

    <script src="${ctx}/resources/js/jquery/jquery-3.1.0.min.js"></script>
    <script src="${ctx}/resources/js/jquery/jquery.tmpl.js"></script>
    <script src="${ctx}/resources/js/jquery/jquery.mousewheel-3.1.13.min.js"></script>
    <script src="${ctx}/resources/js/jcanvas/jcanvas.min.js"></script>
    <script src="${ctx}/resources/js/jquery/jquery.simplemodal-1.4.4.js"></script>

    <script src="${ctx}/resources/js/settings.js"></script>
    <script src="${ctx}/resources/js/ajax-loader.js"></script>
    <script src="${ctx}/resources/js/board-data-ws.js"></script>
    <script src="${ctx}/resources/js/board-scroll.js"></script>
    <script src="${ctx}/resources/js/canvases.js"></script>
    <script src="${ctx}/resources/js/canvases-text.js"></script>
    <script src="${ctx}/resources/js/canvases-${gameName}.js"></script>
    <script src="${ctx}/resources/js/layout.js"></script>
    <script src="${ctx}/resources/js/donate.js"></script>
    <script src="${ctx}/resources/js/joystick.js"></script>
    <script src="${ctx}/resources/js/leaderstable.js"></script>
    <script src="${ctx}/resources/js/hotkeys.js"></script>
    <script src="${ctx}/resources/js/advertisement.js"></script>
    <script src="${ctx}/resources/js/board.js"></script>
    <script src="${ctx}/resources/js/${gameName}.js"></script>
</head>
<body style="display:none;">
    <div id="settings" contextPath="${ctx}" gameName="${gameName}" playerName="${playerName}" code="${code}" allPlayersScreen="${allPlayersScreen}"></div>

    <%@include file="forkMe.jsp"%>

    <div id="board_page">
        <%@include file="canvases.jsp"%>
        <%@include file="leaderstable.jsp"%>
        <%@include file="info.jsp"%>
        <%@include file="advertisement.jsp"%>
        <%@include file="donate.jsp"%>
        <%@include file="widgets.jsp"%>
    </div>
</body>
</html>
