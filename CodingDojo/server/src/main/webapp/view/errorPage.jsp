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
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <title>Codenjoy</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
    <div id="settings" page="main" contextPath="${ctx}" gameName="${gameName}"></div>
    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1>WTF! Something wrong...</h1>
    </div>
        <div>Your ticket number is: ${ticketNumber}</div></br>
        <div>${message}</div></br>
        <div><a href="${ctx}">Go to main page</a><div></br>
        <div>${stacktrace}</div></br>
    </div>
</body>
</html>
