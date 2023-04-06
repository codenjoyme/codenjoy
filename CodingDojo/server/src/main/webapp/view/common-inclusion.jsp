<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2012 - 2022 Codenjoy
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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <link href="${ctx}/resources/favicon.ico" rel="icon">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <link href="${resourceUrl}" rel="stylesheet">
    <c:if test="${!empty gameOnly}">
        <!-- TODO для игр, где нет этого файла будет ошибка в консоли и эксепшен в стектрейсе -->
        <link href="${ctx}/resources/${gameOnly}/css/custom.css" rel="stylesheet">
    </c:if>

    <c:choose>
        <c:when test="${debug}" >
            <script src="${ctx}/resources/js/all.js"></script>
        </c:when>
        <c:otherwise>
            <script src="${ctx}/resources/js/all.min.js"></script>
        </c:otherwise>
    </c:choose>
</head>

<c:if test="${!justBoard && page != 'register'}" >
    <sec:authorize access="isAuthenticated()">
        <body>
            <a href="${ctx}/process_logout" class="logout-link">Logout</a>
        </body>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <body>
            <a href="${ctx}/login?game=${gameOnly}" class="logout-link">Login</a>
        </body>
    </sec:authorize>
</c:if>
