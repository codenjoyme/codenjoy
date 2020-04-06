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
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Game boards</title>

    <link href="${ctx}/resources/css/all.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />

    <script src="${ctx}/resources/js/canvases-${gameNameOnly}.js"></script>
    <script src="${ctx}/resources/js/${gameNameOnly}.js"></script>
</head>
<body style="display:none;">
    <div id="settings" page="board" contextPath="${ctx}" gameName="${gameName}" playerId="${playerId}" readableName="${readableName}" code="${code}" allPlayersScreen="${allPlayersScreen}"></div>

    <%@include file="forkMe.jsp"%>

    <div id="board_page">
        <div id="donate" style="display:none;">
            <input type="button" id="want-donate" value="Помочь проекту..."/>
        </div>
        <%@include file="canvases.jsp"%>
        <%@include file="leaderstable.jsp"%>
        <%@include file="advertisement.jsp"%>
        <%@include file="donate.jsp"%>
        <%@include file="widgets.jsp"%>
    </div>
</body>
</html>
