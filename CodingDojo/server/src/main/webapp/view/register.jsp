<!DOCTYPE html>
<!--
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
-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Codenjoy | Sign Up</title>
    <link href="${ctx}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/fonts/font-awesome-4.6.3/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/css/registration-style.css" rel="stylesheet"/>
    <jsp:include page="common-inclusion.jsp" />
    <script src="${ctx}/resources/css/bootstrap/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
<div id="settings" page="register" contextPath="${ctx}" gameName="${gameName}" waitApprove="${wait_approve}"></div>
<div class="header-container">
    <div class="container-fluid">
        <header class="header">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="logo inline" href="#" title="Home"><img src="${ctx}/resources/img/logo.png"></a>
                <span class="title dojo-title">Coding DOJO&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <c:if test="${activeProfiles.contains('icancode')}">
                    <!-- TODO to remove from here -->
                    <a class="logo inline" href="#" title="Home"><img src="${ctx}/resources/img/robot-logo.png"></a>
                    <span class="title icancode-title">ICanCode</span>
                </c:if>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <nav class="nav pull-right text-right">
                    <ul class="nav-list">
                        <li class="title icancode-title inline"><a id="additional-link" target="_blank" href="#"></a></li>
                        <li class="title icancode-title inline"><a id="help-link" target="_blank" href="#"></a></li>
                        <sec:authorize access="isAuthenticated()">
                            <li class="logo title inline"><img src="${ctx}/resources/img/profile.png"></li>
                        </sec:authorize>
                    </ul>
                </nav>
            </div>
        </header>
    </div>
</div>
<div class="container-fluid">
    <form:form class="form-registr" id="form" modelAttribute="player" action="register" method="POST">
        <h2 class="form-title">Sign up</h2>
        <div class="inputs">
            <div id="readableName" class="field not-valid" not-empty>
                <form:errors path="readableName" cssClass="error-message" />
                <form:input type="text" placeholder="FirstName LastName" path="readableName"/>
                <span class="icon fa"></span>
            </div>
            <div id="email" class="field not-valid">
                <form:errors path="email" cssClass="error-message" />
                <form:input type="email" placeholder="Email address (valid)" path="email"/>
                <span class="icon fa"></span>
            </div>
            <div id="password" class="field not-valid">
                <form:errors path="password" cssClass="error-message" />
                <form:input type="password" placeholder="Password" path="password"/>
                <span class="icon fa"></span>
            </div>
            <div id="passwordConfirmation" class="field not-valid">
                <form:errors path="passwordConfirmation" cssClass="error-message" />
                <form:input type="password" placeholder="Confirm password" path="passwordConfirmation"/>
                <span class="icon fa"></span>
            </div>
            <div id="data" hidden>
                <form:input type="text" path="data"/>
            </div>
            <!-- add attribute 'not-empty' to the div if you want to enable validation -->
            <div id="data1" class="field not-valid" not-empty hidden>
                <input type="text" placeholder="Country / City"/>
                <span class="icon fa"></span>
            </div>
            <div id="data2" class="field not-valid" not-empty hidden>
                <!-- also you can change input placeholder="..." -->
                <input type="text" placeholder="Primary skill / Tech level"/>
                <span class="icon fa"></span>
            </div>
            <div id="data3" class="field not-valid" not-empty hidden>
                <input type="text" placeholder="Company / Position"/>
                <span class="icon fa"></span>
            </div>
            <div id="data4" class="field not-valid" not-empty hidden>
                <input type="text" placeholder="Experience"/>
                <span class="icon fa"></span>
            </div>
            <c:if test="${not adminLogin}">
                <div id="gameName" class="field valid" hidden>
                    <form:select id="gameNameSelect" items="${gameNames}" path="gameName"/>
                    <form:errors path="gameName" cssClass="error" />
                </div>

                <div id="gameType" class="field valid" hidden>
                    <select placeholder="Select your game" name="game">
                        <!--option value="Type1">Type1</option-->
                        <!--option value="Type2">Type2</option-->
                        <!--option value="Type3">Type3</option-->
                    </select>
                </div>
            </c:if>
            <button class="btn-submit" id="submit-button" type="button">Sign up</button>
        </div>
    </form:form>
</div>
<footer class="footer">
    <div class="container-fluid">
        <nav class="footer-nav">
            <ul class="footer-list">
                <li class="footer-item inline"><a href="http://codenjoy.com" target="blank">About DOJO</a></li>
                <li class="footer-item inline"><a href="https://github.com/codenjoyme/codenjoy" target="blank">Codenjoy on GitHub</a></li>
                <li class="footer-item inline"><a href="mailto:codenjoyme@gmail.com" target="blank">Ask me anything</a></li>
            </ul>
        </nav>
    </div>
</footer>
</body>
</html>
