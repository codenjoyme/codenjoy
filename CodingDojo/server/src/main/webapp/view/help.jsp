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
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="page" scope="request" value="help"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy help</title>

    <link href="${ctx}/resources/css/all.min.css" rel="stylesheet">
    <link href="${ctx}/resources/css/custom.css" rel="stylesheet">
    <jsp:include page="common-inclusion.jsp" />
</head>
<body>
<div id="settings"
     page="${page}"
     contextPath="${ctx}"></div>

<%@include file="forkMe.jsp"%>

<div class="page-header">
    <h1>Help</h1>
</div>
<h3>Environment setup and registration</h3>
<div class="main-page">
    <ol>
        <li>
            Download <a href="https://github.com/codenjoyme/codenjoy-clients.git">clients</a>
            for selected language.
        </li>
        <li>
            Setup project according to instruction in README.md for
            the selected programming language.
        </li>
        <li>
            [Optional] For Java
            <a href="${ctx}/resources/user/engine-libs.zip">these dependencies</a>
            may be useful if there is no internet.
            Unzip content and run setup.bat or setup.sh inside.
        </li>
        <li>Read game instructions:
            <ul>
                <c:forEach items="${games}" var="item">
                    <li>
                        <a href="${ctx}/help?game=${item}">${item}</a>
                    </li>
                </c:forEach>
            </ul>
        </li>
        <li>
            Please <a href="${ctx}/register">register</a>
            or <a href="${ctx}/login">login</a>.
        </li>
        <li>
            Enter your name/password and codenjoy!
        </li>
    </ol>
</div>
</body>
</html>