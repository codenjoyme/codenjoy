<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Codenjoy</title>
    <script src="${ctx}/resources/js/google-analytics.js"></script>

    <link href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
    <script src="${ctx}/resources/js/jquery-1.7.2.js"></script>
    <script src="${ctx}/resources/js/hotkeys.js"></script>
    <script>
        $(document).ready(function () {
            initHotkeys('${gameName}', '${ctx}/');
        });
    </script>
</head>
<body>
    <div class="page-header">
        <h1>Hi ${(user!=null)?user:ip}, please:</h1>
    </div>
    <ol>
        <li><a href="${ctx}/help">How to start</a></li>
        <c:if test="${!registered}">
        <li><a href="${ctx}/register">Register/Login</a></li>
        </c:if>
        <c:if test="${registered}">
            <li><a href="${ctx}/register?remove_me&code=${code}">Unregister</a></li>
        </c:if>
        <li>Check game board</li>
        <c:forEach items="${gameNames}" var="gameName">
            - <a href="${ctx}/board?gameName=${gameName}">${gameName}</a></br>
        </c:forEach>
        <li>Active players</li>
        <c:forEach items="${statistics}" var="data">
            - <a href="${ctx}/board/${data.name}">${data.name}</a> ${data.score}</br>
        </c:forEach>
        </ol>
    </div>
<body>
</html>