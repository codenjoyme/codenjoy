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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <link href="${ctx}/resources/favicon.ico" rel="icon">
    <link href="${ctx}/resources/css/all.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">

    <c:choose>
        <c:when test="${debug}" >
            <script src="${ctx}/resources/js/all.js"></script>
            <script src="${ctx}/resources/js/chat.js"></script>
        </c:when>
        <c:otherwise>
            <script src="${ctx}/resources/js/all.min.js"></script>
        </c:otherwise>
    </c:choose>
</head>

    <header class="header-bar">
        <div class="header-container">
            <div class="default elements">
                <a href="#" title="Home" class="default logo-link">
                    <div class="default logo-div"></div>
                </a>
                <nav class="default navigation-bar-elements">
                    <a class="default header-buttons" href="/events">Events</a>
                    <a aria-current="page" class="default header-buttons active" href="${ctx}/">Games</a>
                    <a class="default header-buttons" href="/organizer">Organizer</a>
                    <a class="default header-buttons" href="/manuals">Manuals</a>
                    <a class="default header-buttons" href="/about-us">About Us</a>
                </nav>
            </div>
            <div class="default logout-outline">
                <!-- TODO to remove games from here -->
                <c:if test="${!justBoard && page != 'register' && gameOnly != 'icancode' && gameOnly != 'expansion'}" >
                    <sec:authorize access="isAuthenticated()">
                        <body>
                            <a href="${ctx}/process_logout" class="default logout-button">Logout</a>
                        </body>
                    </sec:authorize>
                    <sec:authorize access="!isAuthenticated()">
                        <body>
                            <a href="${ctx}/login?game=${gameOnly}" class="default logout-button">Login</a>
                        </body>
                    </sec:authorize>
                </c:if>
            </div>
        </div>
    </header>
