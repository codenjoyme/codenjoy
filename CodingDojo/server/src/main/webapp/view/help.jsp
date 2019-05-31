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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy help</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
    <div id="settings" page="help" contextPath="${ctx}"></div>
    <%@include file="forkMe.jsp"%>
    <div class="page-header">
        <h1>Help</h1>
    </div>
    <h3>Environment setup and registration</h3>
    <ol>
        <li>Download client templates for your game
            <select id="games">
            <option value="">(select your game)</option>
            <c:forEach items="${gameNames}" var="gameName">
                <option value="${gameName}">${gameName}</option>
            </c:forEach>
            </select>
        <li>Setup project according to instruction in README.txt for your developing language</li>
        <ul>
            <li>For Java, please <a href="${ctx}/resources/user/engine-libs.zip">download zip</a> and install Engine library before (run setup.bat inside)</li>
        </ul>
        <li>Read game instructions: <c:forEach items="${gameNames}" var="gameName"><a href="${ctx}/help?gameName=${gameName}">${gameName}</a>&nbsp;&nbsp;</c:forEach></li>
        <li>Open <a href="${ctx}/register">registration page</a></li>
        <li>Enter your name/password and codenjoy!</li>
    </ol>
</body>
</html>
