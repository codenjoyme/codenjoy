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
    <title>Codenjoy login</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <link href="${ctx}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/bootstrap/bootstrap.min.js" rel="script">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
<div id="settings" page="register" contextPath="${ctx}" gameName="${gameName}" waitApprove="${wait_approve}"></div>

<%@include file="forkMe.jsp"%>

<div class="page-header">
    <h1 id="title">Login</h1>
</div>

<c:if test="${param.error != null}">
    <div class="alert alert-danger">
        Invalid email or password
    </div>
</c:if>

<c:if test="${param.logout != null}">
    <div class="alert alert-info" >
        You have been successfully logged out
    </div>
</c:if>

<form action="${ctx}/process_login" method="POST">
    <table>
        <tr>
            <td>Email</td>
            <td>
                <input name="email"/>
            </td>
        </tr>
        <tr>
            <td>Password</td>
            <td>
                <input name="password" type="password"/>
            </td>
        </tr>
        <c:if test="${not adminLogin}">
            <tr>
                <td>Your game</td>
                <td>
                    <select name="gameName">
                        <c:forEach items="${gameNames}" var="g" >
                            <option value="${g}">${g}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </c:if>
        <tr>
            <td colspan="3">
                <input type="submit" id="submit" value="Login" />
            </td>
            <td colspan="3">
                <c:choose>
                    <c:when test="${opened and not adminLogin}">
                        <a href="${ctx}/register">Not registered yet</a>
                    </c:when>
                    <c:otherwise>
                        Registration is closed, please try again later.
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
