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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy registration</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">

    <script src="${ctx}/resources/js/all.min.js"></script>
</head>
<body>
    <div id="settings" page="register" contextPath="${ctx}" gameName="${gameName}" waitApprove="${wait_approve}"></div>

    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1 id="title">Registration</h1>
    </div>

    <form:form commandName="player" action="register" method="POST">
        <form:hidden path="data"/>
        <table>
            <tr>
                <td>Email<form:errors path="name"/></td>
            </tr>
            <tr>
                <td>
                    <form:input path="name"/>
                    <span class="error">
                        <c:if test="${bad_pass}">Already registered</c:if>
                        <c:if test="${wait_approve}">Please check your email</c:if>
                    </span>
                </td>
            </tr>
            <tr>
                <td>Name</td>
            </tr>

            <tr>
                <td><form:input path="readableName"/></td>
            </tr>
            <tr>
                <td>Password<form:errors path="password"/></td>
            </tr>
            <tr>
                <td><form:password path="password"/><c:if test="${bad_pass}">Bad password</c:if></td>
            </tr>
            <tr>
                <td>Your game</td>
            </tr>
            <tr>
                <td><form:select items="${gameNames}" path="gameName"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <c:choose>
                        <c:when test="${opened}">
                            <input type="submit" id="submit" value="Register" />
                        </c:when>
                        <c:otherwise>
                            Registration was closed, please try again tomorrow.
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>
