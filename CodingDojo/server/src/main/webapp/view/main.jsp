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

<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="page" scope="request" value="main"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
    <div id="settings"
         page="${page}"
         contextPath="${ctx}"
         game="${game}"></div>

    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1>Hi ${(user!=null)?user:ip} !</h1>
    </div>
    <div class="main-page">
        <div>
            <sec:authorize access="!isAuthenticated()">
                <a href="${ctx}/login">Login</a>
            </sec:authorize>
        </div>
        <div><a href="${ctx}/help">How to start</a></div>
        <div>Check game board</div>
        <div>
            <ul>
            <c:forEach items="${gamesRooms.all}" var="gameRooms">
                <li>${gameRooms.game}:
                    <ul>
                    <c:forEach items="${gameRooms.rooms}" var="room">
                        <li>
                            <div>${room}:
                                <a class="gameView"
                                   room="${room}"
                                   game="${gameRooms.game}"
                                   href="${ctx}/board/room/${room}?viewOnly=true">View</a> |
                                <a id="rejoin-${room}"
                                   href="${ctx}/board/rejoining/${gameRooms.game}/room/${room}">Join</a>
                            </div>
                        </li>
                    </c:forEach>
                    </ul>
                </li>
            </c:forEach>
            </ul>
        </div>
    </div>
</body>
</html>
