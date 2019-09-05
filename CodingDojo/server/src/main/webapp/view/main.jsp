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
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
    <div id="settings" page="main" contextPath="${ctx}" gameName="${gameName}"></div>
    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1>Hi ${(user!=null)?user:ip}, please:</h1>
    </div>
        <div><a href="${ctx}/help">How to start</a></div>
        <div>Check game board</div>
        <div>
            <ul>
            <c:forEach items="${gameNames}" var="gameName">
                <li>
                    <div>${gameName.value}: <a class="gameView" gameName="${gameName.key}" href="${ctx}/board/game/${gameName.key}?viewOnly=true">View</a> | <a id="rejoin-${gameName.key}" href="${ctx}/board/rejoining/${gameName.key}">Join</a></div>
                </li>
            </c:forEach>
            </ul>
        </div>
</body>
</html>
