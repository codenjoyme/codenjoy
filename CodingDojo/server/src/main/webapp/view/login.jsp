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
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="page" scope="request" value="register"/>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Codenjoy | Sign In</title>
    <link href="${ctx}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/fonts/font-awesome-4.6.3/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="${ctx}/resources/css/registration-style.css" rel="stylesheet"/>
    <jsp:include page="common-inclusion.jsp" />
    <script src="${ctx}/resources/css/bootstrap/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
    <div id="settings" page="${page}" contextPath="${ctx}" game="${game}" waitApprove="${wait_approve}"></div>

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
                            <li class="title icancode-title inline"><a id="additional-link" href="#"></a></li>
                            <li class="title icancode-title inline"><a id="help-link" href="#"></a></li>
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
        <form:form class="form-registr" id="form" action="${ctx}/process_login" method="POST">
            <h2 class="form-title">Sign in</h2>
            <div class="inputs">
                <c:if test="${param.failed == true}">
                    <div id="error-message" class="error-message">
                        <spring:message key="login.credentials.invalid" />
                    </div>
                </c:if>
                <c:if test="${param.closed}">
                    <div id="error-message" class="error-message">
                        <spring:message key="registration.room.closed" />
                    </div>
                </c:if>
                <c:if test="${!opened}">
                    <div id="error-message" class="error-message">
                        <spring:message key="registration.closed" />
                    </div>
                </c:if>
                <c:if test="${opened || adminLogin}">
                    <div id="email" class="field not-valid">
                        <input type="email" placeholder="Email address (valid)" name="email"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="password" class="field not-valid">
                        <input type="password" placeholder="Password"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="data" hidden>
                        <input type="text" name="data"/>
                    </div>
                    <div id="password-md5" hidden>
                        <input type="hidden" name="password"/>
                    </div>
                    <c:if test="${!adminLogin}">
                        <div id="game" class="field valid" hidden>
                            <select name="game">
                                <c:forEach items="${games}" var="item" >
                                    <option value="${item}">${item}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div id="gameType" class="field valid" hidden>
                            <select placeholder="Select your game" name="game">
                                <!--option value="Type1">Type1</option-->
                                <!--option value="Type2">Type2</option-->
                                <!--option value="Type3">Type3</option-->
                            </select>
                        </div>
                    </c:if>
                    <button class="btn-submit" id="submit-button" type="button">Sign in</button>
                    <a href="${ctx}/register">
                        <button class="btn-submit" id="register-button" type="button">Sign Up</button>
                    </a>
                </c:if>
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
