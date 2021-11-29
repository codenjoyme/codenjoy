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
<c:set var="page" scope="request" value="register"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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
    <div id="settings" page="${page}" contextPath="${ctx}" waitApprove="${wait_approve}"></div>

    <div class="container-fluid">
        <form:form class="form-registr" id="form" modelAttribute="player" action="register" method="POST">
            <h2 class="form-title">Sign up</h2>
            <div class="inputs">
                <c:if test="${!opened}">
                    <div id="error-message" class="error-message">
                        <spring:message key="registration.closed" />
                    </div>
                </c:if>
                <c:if test="${opened}">
                    <div id="fullName" class="field not-valid" not-empty>
                        <form:errors path="fullName" cssClass="error-message" />
                        <form:input type="text" placeholder="FirstName LastName" path="fullName"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="readableName" class="field not-valid" not-empty>
                        <form:errors path="readableName" cssClass="error-message" />
                        <form:input type="text" placeholder="User name" path="readableName"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="email" class="field not-valid">
                        <form:errors path="email" cssClass="error-message" />
                        <form:input type="email" placeholder="Email address (valid)" path="email"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="password" class="field not-valid">
                        <form:errors path="password" cssClass="error-message" />
                        <input type="password" placeholder="Password"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="passwordConfirmation" class="field not-valid">
                        <form:errors path="passwordConfirmation" cssClass="error-message" />
                        <input type="password" placeholder="Confirm password"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="data" hidden>
                        <form:input type="text" path="data"/>
                    </div>
                    <div id="password-md5" hidden>
                        <form:input type="hidden" path="password"/>
                    </div>
                    <div id="passwordConfirmation-md5" hidden>
                        <form:input type="hidden" path="passwordConfirmation"/>
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
                    <div id="gitHubUsername" class="field not-valid">
                        <form:errors path="gitHubUsername" cssClass="error-message" />
                        <form:input type="gitHubUsername" placeholder="Github Username" path="gitHubUsername"/>
                        <span class="icon fa"></span>
                    </div>
                    <div id="slackEmail" class="field not-valid">
                        <form:errors path="slackEmail" cssClass="error-message" />
                        <form:input type="slackEmail" placeholder="Slack email(optional)" path="slackEmail"/>
                        <span class="icon fa"></span>
                    </div>
                    <button class="btn-submit" id="submit-button" type="button">Sign up</button>
                </c:if>
            </div>
        </form:form>
    </div>
        <footer class="default page-footer">
            <div class="default footer-container">
                <div class="default footer-elements">
                    <div class="default copyrights">
                        <span class="default copyrights-lines">© 2021 EPAM Systems inc. All rights reserved.</span>
                        <span class="default copyrights-lines">
                        Powered by
                        <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNTQiIGhlaWdodD0iMTkiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgZmlsbD0iI0ZGRiI+CiAgICA8cGF0aCBkPSJNMTAuMTg4IDguNTA1djMuNDZjMCAuODg3LjQzMiAxLjMxNyAxLjMyMyAxLjMxN2guMzg3Yy44OTEgMCAxLjMyMy0uNDMgMS4zMjMtMS4zMTdWMTAuMjdoMi4wMzR2MS42MjJjMCAyLjIyMy0xLjEyNSAzLjM3LTMuMzg0IDMuMzdoLS40MzJjLTIuMjMyIDAtMy4zODQtMS4xMi0zLjM4NC0zLjEwMVYzLjY2NmMwLTIuNDgzIDEuMTI1LTMuNjMgMy4zODQtMy42M2guNDMyYzIuMjMyIDAgMy4zODQgMS4xMiAzLjM4NCAzLjM3djUuMDgxaC01LjA1OHYuMDE4aC0uMDA5em0wLTIuMDM0aDIuOTk3VjMuMzE2YzAtLjg4Ny0uNDMyLTEuMzE3LTEuMzIzLTEuMzE3aC0uMzMzYy0uODkxIDAtMS4zMjMuNDMtMS4zMjMgMS4zMTd2My4xNTVoLS4wMTh6TTE2LjY0MS4yMTVoMi4xMDZ2Ljk2Yy41MDQtLjUzIDEuMzY4LTEuMTIxIDIuNTY1LTEuMTIxIDEuOCAwIDIuNjgyIDEuMTkyIDIuNjgyIDIuOTR2OS4zMTFjMCAxLjc2Ni0uODkxIDIuOTQtMi42ODIgMi45NC0xLjIyNCAwLTIuMDYxLS41NzQtMi41NjUtMS4wNzZ2NC44MDRoLTIuMTA2Vi4yMTV6bTIuMTA2IDEyLjI3OGMuNTc2LjUwMiAxLjI1MS43NjIgMS45NDQuNzYyLjc2NSAwIDEuMjI0LS4zNTggMS4yMjQtMS4yMTlWMy4yNTNjMC0uODYtLjQzMi0xLjIxOS0xLjIyNC0xLjIxOS0uNjkzIDAtMS4zNjguMjg3LTEuOTQ0Ljc2MnY5LjY5N3pNMjUuMjcyIDkuOTEyYzAtMS44MzcuNjAzLTIuNTggMi4yMDUtMy4xMjhsMi44MjYtLjk1OVYzLjMxNmMwLS44ODctLjQzMi0xLjM2Mi0xLjMyMy0xLjM2MmgtLjMzM2MtLjg5MSAwLTEuMzIzLjQ3NS0xLjMyMyAxLjM2MnYyLjA5N0gyNS4yOVYzLjM4OGMwLTIuMjIzIDEuMTI1LTMuMzcgMy4zODQtMy4zN2guNDA1YzIuMjMyIDAgMy4zODQgMS4xMiAzLjM4NCAzLjM3djExLjY3OGgtMi4xMDZ2LS45MzNjLS41NzYuNTQ3LTEuMzY4IDEuMTAzLTIuNTY1IDEuMTAzLTEuNjgzIDAtMi40NjYtLjk2LTIuNDY2LTIuNjI2VjkuOTEyaC0uMDU0em01LjA0IDIuNjUzVjcuNjcybC0xLjkxNy43MTdjLS43NjUuMzEzLTEuMDM1LjY5LTEuMDM1IDEuNTV2Mi4yNjhjMCAuNzE3LjM2IDEuMDc1IDEuMDM1IDEuMDc1LjYzOS0uMDI3IDEuMjE1LS4yNDIgMS45MTctLjcxN3oiLz4KICAgIDxwYXRoIGQ9Ik00MC45MDUgMTUuMDc1aC0yLjEwNlYzLjExYzAtLjc0NC0uMzYtMS4wNzYtMS4wMDgtMS4wNzYtLjU3NiAwLTEuMTk3LjIxNi0xLjk0NC43NDR2MTIuMjk3aC0yLjEwNlYuMjE1aDIuMTA2di45NkMzNi40NS42IDM3LjI2LjA1MyAzOC40MTIuMDUzYzEuMDggMCAxLjguNDMgMi4xNzggMS4yMTkuNzQ3LS42NDYgMS42MjktMS4yMiAyLjg1My0xLjIyIDEuNjI5IDAgMi40NjYgMS4wMDUgMi40NjYgMi42Mjd2MTIuNDEyaC0yLjEwNlYzLjExYzAtLjc0NC0uMzYtMS4wNzYtMS4wMDgtMS4wNzYtLjU3NiAwLTEuMTk3LjIxNi0xLjk0NC43NDR2MTIuMjk3aC4wNTR6Ii8+CiAgICA8cGF0aCBmaWxsPSIjMDBCNUQxIiBkPSJNNS4zOTEgMS41NzdWNC4zMkwxLjg2MyA3LjY2M2wzLjUyOCAzLjI5OHYyLjc5NkwwIDguNjkzVjYuNjg2ek01NCA2LjY4NnYyLjAwN2wtNS4zOTEgNS4wNjR2LTIuNzk2bDMuNTI4LTMuMjk4LTMuNTI4LTMuMzYxVjEuNTc3eiIvPgo8L3N2Zz4=" alt="EPAM Systems" class="default epm-logo">
                        </span>
                    </div>
                    <ul data-aid="footer" class="default buttons-list">
                        <li class="button-specification">
                            <a class="default button-link" href="/events">Events</a>
                        </li>
                        <li class="button-specification">
                            <a aria-current="page" class="default button-link" href="/games">Games</a>
                        </li>
                        <li class="button-specification">
                            <a class="default button-link" href="/organizer">Organizer</a>
                        </li>
                        <li class="button-specification">
                            <a class="default button-link" href="/manuals">Manuals</a>
                        </li>
                        <li class="button-specification">
                            <a class="default button-link" href="/about-us">About Us</a>
                        </li>
                    </ul>
                </div>
            </div>
        </footer>
</body>
</html>
