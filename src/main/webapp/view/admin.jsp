<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Admin page</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>
    <div class="page-header">
        <h1>Admin page</h1>
    </div>
    <c:choose>
        <c:when test="${paused}">
            The dojo was suspended. <a href="/admin31415?resume">Resume game</a>.
        </c:when>
        <c:otherwise>
            The dojo is active. <a href="/admin31415?pause">Pause game</a>.
        </c:otherwise>
    </c:choose></br>
    </br>
    Go to <a href="/">main page</a>.
</body>
</html>
