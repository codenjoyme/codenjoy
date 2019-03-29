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
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
    <div id="settings" page="register" contextPath="${ctx}" gameName="${gameName}" waitApprove="${wait_approve}"></div>

    <%@include file="forkMe.jsp"%>

    <div class="page-header">
        <h1 id="title">Registration</h1>
    </div>

    <form:form modelAttribute="player" action="register" method="POST">
        <form:hidden path="data"/>
        <table>
            <tr>
                <td>Email</td>
                <td>
                    <form:input path="email"/>
                </td>
                <td>
                    <form:errors path="email"/>
                </td>
            </tr>
            <tr>
                <td>Name</td>
                <td>
                    <form:input path="readableName"/>
                </td>
                <td>
                    <form:errors path="readableName" cssClass="error"/>
                </td>
            </tr>
            <tr>
                <td>Password</td>
                <td>
                    <form:password path="password"/>
                </td>
                <td>
                    <form:errors path="password"/>
                </td>
            </tr>
            <tr>
                <td>Confirm password</td>
                <td>
                    <form:password path="passwordConfirmation" />
                </td>
                <td>
                    <form:errors path="passwordConfirmation" />
                </td>
            </tr>
            <c:if test="${not adminLogin}">
                <tr>
                    <td>Your game</td>
                    <td>
                        <form:select items="${gameNames}" path="gameName"/>
                    </td>
                    <td>
                        <form:errors path="gameName" cssClass="error" />
                    </td>
                </tr>
            </c:if>
            <tr>
                <td colspan="3">
                    <c:choose>
                        <c:when test="${opened}">
                            <input type="submit" id="submit" value="Register" />
                        </c:when>
                        <c:otherwise>
                            Registration is closed, please try again later.
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>
