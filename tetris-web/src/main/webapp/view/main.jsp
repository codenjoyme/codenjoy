<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Tetris coding dojo</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="page-header">
        <h1>Hi ${(user!=null)?user:ip}, please:</h1>
    </div>
    <ol>
        <li><a href="/help">Read help</a></li>
        <c:choose>
            <c:when test="${user==null}">
                <li><a href="/register">Register</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/board/${user}">Check your board</a></li>
            </c:otherwise>
        </c:choose>
        <li><a href="/board">Check leader board</a></li>
        <c:if test="${user!=null}">
            <li><a href="/register?remove_me">Unregister</a></li>
        </c:if>
    </ol>
<body>
</html>